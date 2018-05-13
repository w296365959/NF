package com.sscf.investment.push;

import BEC.*;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import com.sscf.investment.R;
import com.sscf.investment.main.manager.AppConfigRequestManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.sdk.net.ProtoManager;
import com.sscf.investment.sdk.utils.*;
import com.dengtacj.component.entity.RemindedMessageItem;
import com.sscf.investment.setting.DengtaSettingPref;
import com.sscf.investment.setting.manager.RemindRequestManager;
import com.sscf.investment.utils.*;
import com.taobao.accs.ChannelService;
import com.umeng.message.PushAgent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by davidwei on 2015/9/2.
 *
 * push管理
 */
public final class PushManager implements DataSourceProxy.IRequestCallback, Handler.Callback {

    public static final String TAG = PushManager.class.getSimpleName();

    private static final int MSG_RESET_COUNT = 1;
    private static final int MAX_SOUND_COUNT = 3;
    private int mSoundCount = 0;

    /**
     * 单线程运行
     */
    public final Executor mExecutor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(100));

    // 为后台数据做保护，去除重复的msg
    private HashMap<String, Long> mMsgIds;
    private boolean mInited;
    private final File mMsgFile;
    private ArrayList<PushSwitchInfo> mPushSwitchList;
    private File mPushSwitchFile;

    private static final int ID_IMPORTANT_NEWS_START = 10000;
    private static final int ID_ANNOUNCEMENT_START = 20000;
    private static final int ID_RESEARCH_START = 30000;

    private static final int MAX_IMPORTANT_NEWS = 3;
    private static final int MAX_ANNOUNCEMENT = 3;
    private static final int MAX_RESEARCH = 3;

    private static final int API_RECONNECT_MAX_COUNT = 5;

    private int mImportantNewsId = 0;
    private int mAnnouncementId = 0;
    private int mResearchId = 0;

    private static int mNotificationId = 0;

    private final Handler mHandler;

    private Context context;

    public PushManager(final Context context) {
        DtLog.d(TAG, "PushManager constructor");

        this.context = context;

        mMsgIds = new HashMap<String, Long>();
        mMsgFile = FileUtil.getPushMsgIdFile(context);
        mHandler = new Handler(Looper.getMainLooper(), this);
    }

    public void start() {
        DtLog.d(TAG, "start");

        try {
            PushAgent pushAgent = PushAgent.getInstance(context);
            pushAgent.setPushCheck(true);
            UmengPushReceiver umengPushReceiver = new UmengPushReceiver();
            pushAgent.setMessageHandler(umengPushReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPushSwitch() {
        mPushSwitchFile = FileUtil.getPushSwitchFile(DengtaApplication.getApplication());
        mPushSwitchList = (ArrayList<PushSwitchInfo>) FileUtil.getObjectFromFile(mPushSwitchFile);
        if (mPushSwitchList != null) {
            DtLog.d(TAG, "initial switch list size = " + mPushSwitchList.size());
        }
    }

    public void checkPushSwitchUpdate() {
        DtLog.d(TAG, "checkPushSwitchUpdate");
        AppConfigRequestManager.getPushSwitchRequest(this);
    }

    public void dealWithCustomMessage(final Context context, final String msg, final String from, boolean playSound) {
        DtLog.d(TAG, "dealWithCustomMessage: msg = " + msg);
        new DealPushTask().executeOnExecutor(mExecutor, msg, from, playSound, context);
    }

    public boolean isPushEnabled(final int pushType) {
        if (mPushSwitchList == null) {
            initPushSwitch();
        }

        if (mPushSwitchList == null || mPushSwitchList.size() == 0) {
            // 如果没有初始化，默认全部打开
            if (pushType == E_CONFIG_PUSH_TYPE.E_CONFIG_PUSH_HUAWEI) {
                return true;
            } else if(pushType == E_CONFIG_PUSH_TYPE.E_CONFIG_PUSH_UMENG) {
                return true;
            }
            return true;
        }

        for (PushSwitchInfo pushSwitchInfo : mPushSwitchList) {
            int switchType = pushSwitchInfo.getISwitchType();
            if (pushType == switchType) {
                return pushSwitchInfo.getISwitchState() == 1;
            }
        }

        return true;
    }

    /**
     * 打开关闭umeng push
     * @param enabled
     */
    public void enableUmengPushService(final boolean enabled) {
        DtLog.d(TAG, "enableUmengPushService enabled=" + enabled);
        try {
            DengtaApplication application = DengtaApplication.getApplication();
            final ComponentName cn = new ComponentName(application.getPackageName(), ChannelService.class.getName());
            final PackageManager pm = application.getPackageManager();

            if (!enabled) {
                pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
            } else {
                pm.setComponentEnabledSetting(cn, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
            }
        } catch (Exception e) {
            e.printStackTrace();
            DtLog.w(TAG, "enableUmengPushService Exception=" + e.getMessage());
        }
    }

    public final class DealPushTask extends AsyncTask<Object, Object, RemindedMessageItem> {
        private String mFrom;
        private boolean mPlaySound;
        private Context mContext;

        private boolean removeRepeatedMsg(final String msgId) {
            // 已经是单线程了，不需要做同步
            if (!mInited) {
                Object msgIds = FileUtil.getObjectFromFile(mMsgFile);
                if (msgIds != null && msgIds instanceof HashMap) {
                    mMsgIds = (HashMap<String, Long>) msgIds;
                }
                mInited = true;
            }

            if (mMsgIds.containsKey(msgId)) {
                return true;
            }

            final long now = System.currentTimeMillis();
            Iterator<Map.Entry<String, Long>> it = mMsgIds.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Long> entry = it.next();
                long value = entry.getValue();
                if (now - value > 2 * 24 * 3600 * 1000L) {
                    it.remove();
                }
            }

            mMsgIds.put(msgId, now);

            FileUtil.saveObjectToFile(mMsgIds, mMsgFile);
            return false;
        }

        @Override
        protected RemindedMessageItem doInBackground(Object... params) {
            final byte[] response = Base64Utils.decode((String) params[0]);
            mFrom = (String) params[1];
            mPlaySound = (boolean) params[2];
            mContext = (Context) params[3];

            final PushData pushData = new PushData();
            ProtoManager.decode(pushData, response);

            DtLog.d(TAG, "dealWithCustomMessage pushData.stPushType.ePushDataType : " + pushData.stPushType.ePushDataType);
            final RemindedMessageItem item = RemindRequestManager.decodePushData(pushData);

            final String msgId = pushData.stPushType.sMsgId;
            if (removeRepeatedMsg(msgId)) {
                return null;
            }

            if (item == null) {
                return null;
            }

            report(item, msgId, mFrom);

            DengtaApplication dengtaApplication = DengtaApplication.getApplication();

            // 保存未读消息的id
            dengtaApplication.getRemindDataManager().addUnreadRemind(item.id);
            final boolean isLogined = dengtaApplication.getAccountManager().isLogined();

            switch (item.messageType) {
                case RemindedMessageItem.TYPE_SEC:
                    // 股价提醒
                    StatisticsUtil.reportAction(StatisticsConst.PUSH_SEC_REMIND);
                    if (!isLogined) {
                        return null;
                    }
                    break;
                case RemindedMessageItem.TYPE_ANNOUNCEMENT:
                    // 公告
                    StatisticsUtil.reportAction(StatisticsConst.PUSH_ANNOUNCEMENT_REMIND);
                    if (!isLogined) {
                        return null;
                    }
                case RemindedMessageItem.TYPE_RESEARCH:
                    // 研报
                    StatisticsUtil.reportAction(StatisticsConst.PUSH_RESEARCH_REMIND);
                    if (!isLogined) {
                        return null;
                    }
                    break;
                case RemindedMessageItem.TYPE_IMPORTANT_NEWS:
                    // 要闻
                    StatisticsUtil.reportAction(StatisticsConst.PUSH_IMPORTANT_NEWS_REMIND);
                    if (!DengtaSettingPref.isPushImportantNewsEnabled()) {
                        // 推送开关关闭的时候，直接忽略
                        return null;
                    }
                    break;
                case RemindedMessageItem.TYPE_ACTIVITY:
                    // 活动
                    StatisticsUtil.reportAction(StatisticsConst.PUSH_ACTIVITY_REMIND);
                    if (!isLogined) {
                        return null;
                    }
                    break;
                case RemindedMessageItem.TYPE_NEW_SHARE:
                    // 新股
                    StatisticsUtil.reportAction(StatisticsConst.PUSH_NEW_SHARE_REMIND);
                    if (!DengtaSettingPref.isPushNewSharesEnabled()) {
                        // 推送开关关闭的时候，直接忽略
                        return null;
                    }
                    break;
                case RemindedMessageItem.TYPE_DAILY_REPORT:
                    StatisticsUtil.reportAction(StatisticsConst.PUSH_DAILY_REPORT_REMIND);
                    // 自选日报
                    break;
                case RemindedMessageItem.TYPE_INVESTMENT_ADVISER_EVENTS:
                    break;
                case RemindedMessageItem.TYPE_INTERACT_MESSAGE:
                    if (!isLogined) {
                        return null;
                    }
                    break;
                case RemindedMessageItem.TYPE_VALUEADDED_SERVICES:
                    if (!isLogined) {
                        return null;
                    }
                    break;
                default:
                    return null;
            }

            return item;
        }

        @Override
        protected void onPostExecute(final RemindedMessageItem item) {
            if (item != null) {
                final Context context = mContext;

                boolean playSound = false;
                if (mPlaySound) {
                    if (!mHandler.hasMessages(MSG_RESET_COUNT)) {
                        mHandler.sendEmptyMessageDelayed(MSG_RESET_COUNT, 60 * 1000L);
                    }
                    playSound = mSoundCount < MAX_SOUND_COUNT;
                    mSoundCount++;
                }

                showNotificationCustom(context, PackageUtil.getAppName(context), item.content, PendingIntent.getBroadcast(context,
                        (int) System.currentTimeMillis(), getRemindClickIntent(context, item, mFrom), 0), getId(item), playSound);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_RESET_COUNT:
                mSoundCount = 0;
                break;
            default:
                break;
        }
        return true;
    }

    private int getId(final RemindedMessageItem item) {
        int id = 0;
        switch (item.messageType) {
            case RemindedMessageItem.TYPE_ANNOUNCEMENT:
                id = mAnnouncementId + ID_ANNOUNCEMENT_START;
                if (++mAnnouncementId >= MAX_ANNOUNCEMENT) {
                    mAnnouncementId = 0;
                }
                break;
            case RemindedMessageItem.TYPE_RESEARCH:
                id = mResearchId + ID_RESEARCH_START;
                if (++mResearchId >= MAX_RESEARCH) {
                    mResearchId = 0;
                }
                break;
            case RemindedMessageItem.TYPE_IMPORTANT_NEWS:
                id = mImportantNewsId + ID_IMPORTANT_NEWS_START;
                if (++mImportantNewsId >= MAX_IMPORTANT_NEWS) {
                    mImportantNewsId = 0;
                }
                break;
            case RemindedMessageItem.TYPE_SEC:
            case RemindedMessageItem.TYPE_ACTIVITY:
            case RemindedMessageItem.TYPE_NEW_SHARE:
            case RemindedMessageItem.TYPE_DAILY_REPORT:
            default:
                id = mNotificationId++;
                break;
        }
        return id;
    }

    public void clickNotification(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        DtLog.d(TAG, "clickNotification msg=" + msg);

        final byte[] response = Base64Utils.decode(msg);
        final PushData pushData = new PushData();
        ProtoManager.decode(pushData, response);
        final RemindedMessageItem item = RemindRequestManager.decodePushData(pushData);
        if (item != null) {
            new PushClickReceiver().handleRemindClick(item, DengtaConst.PUSH_FROM_HUAWEI);
        }
    }

    private static Intent getRemindClickIntent(final Context context, final RemindedMessageItem item, final String from) {
        final Intent intent = new Intent();
        intent.setClass(context, PushClickReceiver.class);
        intent.putExtra(DengtaConst.EXTRA_PUSH_REMIND, item);
        intent.putExtra(DengtaConst.EXTRA_FROM, from);
        return intent;
    }

    private static void showNotificationCustom(final Context context, String title, String content, PendingIntent intent, int id, boolean sound) {
        DtLog.d(TAG, "showNotificationCustom intent : " + intent);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        RemoteViews remoteViews;
        final String MANUFACTURER = Build.MANUFACTURER;
        if ("huawei".equalsIgnoreCase(MANUFACTURER)) {
            remoteViews = createHuaweiRemotesViews(context, title, content);
        } else if ("smartisan".equalsIgnoreCase(MANUFACTURER)) {
            remoteViews = createSmartisanRemotesViews(context, title, content);
        } else {
            remoteViews = createRemotesViews(context, title, content);
        }

        builder.setContent(remoteViews)
                .setTicker(content) // 设置收到通知后通知栏会一闪而过的消息
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setContentIntent(intent)
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setSmallIcon(R.drawable.app_icon_small);
        if (sound) {
            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        }
        final Notification notification = builder.build();

        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
        notification.contentView = remoteViews;

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            notificationManager.notify(id, notification);
            DtLog.d(TAG, "showNotificationCustom notify success");
        } catch (Exception e) {
            // Lenovo A680 可能会crash
            e.printStackTrace();
        }
    }

    private static RemoteViews createRemotesViews(final Context context, String title, String content) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_remote_views);
        remoteViews.setTextViewText(R.id.notification_title, title);
        remoteViews.setTextViewText(R.id.notification_text, content);
        remoteViews.setLong(R.id.notification_time, "setTime", System.currentTimeMillis());
        ViewUtils.setNotificationStyle(context, remoteViews, R.id.notification_title, R.id.notification_text, R.id.notification_time);
        return remoteViews;
    }

    private static RemoteViews createSmartisanRemotesViews(final Context context, String title, String content) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_remote_views);
        remoteViews.setTextViewText(R.id.notification_title, title);
        remoteViews.setTextViewText(R.id.notification_text, content);
        remoteViews.setLong(R.id.notification_time, "setTime", System.currentTimeMillis());
        // 适配smartisan
        remoteViews.setInt(R.id.notification_layout, "setBackgroundColor", context.getResources().getColor(R.color.main_bg_color));
        ViewUtils.setNotificationStyle(context, remoteViews, R.id.notification_title, R.id.notification_text, R.id.notification_time);
        return remoteViews;
    }

    private static RemoteViews createHuaweiRemotesViews(final Context context, String title, String content) {
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_remote_views_huawei);
        remoteViews.setTextViewText(R.id.notification_title, title);
        remoteViews.setTextViewText(R.id.notification_text, content);
        ViewUtils.setNotificationStyle(context, remoteViews, R.id.notification_title, R.id.notification_text, 0);
        return remoteViews;
    }

    private static void showNotificationOld(final Context context, String title, String content, PendingIntent intent, int id) {
        final Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setTicker(title)
                .setContentText(content)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon))//自定义图标作为大图
                .setSmallIcon(R.drawable.app_icon)
                .setContentIntent(intent)
                .getNotification();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    private static void showNotification(final Context context, String title, String content, PendingIntent intent, int id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(title)
                .setContentTitle(title)//设置通知栏标题
                .setContentText(content)
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setContentIntent(intent)
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(R.drawable.app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));
        }

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public void report(final RemindedMessageItem item, final String id, final String from) {
        final StringBuilder key = new StringBuilder();
        key.append(from).append('_');
        switch (item.messageType) {
            case RemindedMessageItem.TYPE_SEC:
                // 股价提醒
                key.append(StatisticsConst.PUSH_SEC_REMIND);
                break;
            case RemindedMessageItem.TYPE_ANNOUNCEMENT:
                // 公告
                key.append(StatisticsConst.PUSH_ANNOUNCEMENT_REMIND);
                break;
            case RemindedMessageItem.TYPE_RESEARCH:
                // 研报
                key.append(StatisticsConst.PUSH_RESEARCH_REMIND);
                break;
            case RemindedMessageItem.TYPE_IMPORTANT_NEWS:
                // 要问
                key.append(StatisticsConst.PUSH_IMPORTANT_NEWS_REMIND);
                break;
            case RemindedMessageItem.TYPE_ACTIVITY:
                // 活动
                key.append(StatisticsConst.PUSH_ACTIVITY_REMIND);
                break;
            case RemindedMessageItem.TYPE_NEW_SHARE:
                // 新股
                key.append(StatisticsConst.PUSH_NEW_SHARE_REMIND);
                break;
            case RemindedMessageItem.TYPE_DAILY_REPORT:
                // 自选日报
                key.append(StatisticsConst.PUSH_DAILY_REPORT_REMIND);
                break;
            case RemindedMessageItem.TYPE_INTERACT_MESSAGE:
                // 互动消息
                key.append(StatisticsConst.PUSH_INTERACT_MESSAGE_REMIND);
                break;
            case RemindedMessageItem.TYPE_INVESTMENT_ADVISER_EVENTS:
                // 投顾动态
                key.append(StatisticsConst.PUSH_INVESTMENT_ADVISER_EVENTS_REMIND);
                break;
            default:
                break;
        }
        key.append(':').append(id);

        report(key.toString(), this);
    }

    public static void report(final String data, final DataSourceProxy.IRequestCallback observer) {
        final BeaconStat req = new BeaconStat();
        final ArrayList<BeaconStatData> statDatas = new ArrayList<BeaconStatData>(1);
        BeaconStatData statData = new BeaconStatData();
        statData.setITime(System.currentTimeMillis() / 1000);
        statData.setEType(BEACON_STAT_TYPE.E_BST_PUSH);
        statData.setSData(data);
        statDatas.add(statData);
        req.vBeaconStatData = statDatas;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();

        DataEngine.getInstance().request(EntityObject.ET_STATISTIC, req, observer);
    }

    @Override
    public void callback(boolean success, EntityObject data) {
        if (!success) {
            DtLog.d(TAG, "callback failed");
            return;
        }

        switch (data.getEntityType()) {
            case EntityObject.ET_GET_PUSH_SWITCH:
                final ArrayList<ConfigDetail> configs = ((GetConfigRsp) data.getEntity()).getVList();
                for (ConfigDetail config : configs) {
                    if (config.iType == E_CONFIG_TYPE.E_CONFIG_PUSH_SWITCH) {
                        final PushSwitchList pushSwitchList = new PushSwitchList();

                        if (ProtoManager.decode(pushSwitchList, config.vData)) {
                            ArrayList<PushSwitchInfo> switchList = pushSwitchList.getVSwitchList();
                            if (switchList != null) {
                                DtLog.d(TAG, "callback size = " + switchList.size());
                                for (PushSwitchInfo info : switchList) {
                                    DtLog.d(TAG, "PushSwitchInfo info type & state=" + info.getISwitchType() + " & " + info.getISwitchState());
                                }
                                mPushSwitchList = switchList;

                                FileUtil.saveObjectToFile(switchList, mPushSwitchFile);
                            }
                        }
                        break;
                    }
                }
                break;
            case EntityObject.ET_STATISTIC:
                break;
            default:
                break;
        }
    }
}
