package com.sscf.investment.social;

import android.content.Context;
import android.text.TextUtils;

import com.dengtacj.component.event.DeleteCommentEvent;
import com.dengtacj.component.event.DeleteFeedEvent;
import com.dengtacj.component.event.DoLikeByWebEvent;
import com.dengtacj.component.event.GetFeedResultEvent;
import com.dengtacj.component.event.PostCommentResultEvent;
import com.dengtacj.component.event.PostFeedResultEvent;
import com.dengtacj.component.managers.IFeedRequestManager;
import com.sscf.investment.common.entity.CommentDraftEntity;
import com.sscf.investment.common.entity.FeedDraftEntity;
import com.sscf.investment.common.entity.FeedLikeEntity;
import com.sscf.investment.db.DBHelper;
import com.sscf.investment.detail.FeedOperationDialog;
import com.sscf.investment.sdk.net.DataEngine;
import com.sscf.investment.sdk.net.DataSourceProxy;
import com.sscf.investment.main.manager.AccountManager;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.net.EntityObject;
import com.sscf.investment.setting.entity.AccountInfoEntity;
import com.sscf.investment.sdk.utils.DtLog;
import com.dengtacj.thoth.MapProtoLite;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import BEC.AccountTicket;
import BEC.AccuPointTaskType;
import BEC.DelCommentReq;
import BEC.DelFeedReq;
import BEC.E_SET_FEED_LIST_TYPE;
import BEC.GetFeedReq;
import BEC.GetFeedRsp;
import BEC.GetInvestListReq;
import BEC.GetInvestRecommendReq;
import BEC.LikeReq;
import BEC.PoCommentReq;
import BEC.PoCommentRsp;
import BEC.PoFeedReq;
import BEC.PoFeedRsp;
import BEC.ReplyCommentInfo;
import BEC.SecCodeName;
import static BEC.E_FEED_TYPE.E_FT_STOCK_REVIEW;

/**
 * Created by liqf on 2016/9/30.
 */
public class FeedRequestManager implements DataSourceProxy.IRequestCallback, IFeedRequestManager {
    private static final String TAG = FeedRequestManager.class.getSimpleName();

    public void getFeed(String feedId) {
        GetFeedReq req = new GetFeedReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        req.setStUserInfo(accountManager.getUserInfo());
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);
        req.setSFeedId(feedId);
        DataEngine.getInstance().request(EntityObject.ET_GET_FEED, req, this);
    }

    public void postFeed(String comment, String dtSecCode, String secName, DataSourceProxy.IRequestCallback callback) {
        PoFeedReq req = new PoFeedReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        req.setStUserInfo(accountManager.getUserInfo());
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);
        req.setSContent(comment.trim());
        ArrayList<SecCodeName> secCodeNames = new ArrayList<>();
        secCodeNames.add(new SecCodeName(dtSecCode, secName));
        req.setVRelateSec(secCodeNames);
        req.setEType(E_FT_STOCK_REVIEW);

        DataEngine.getInstance().request(EntityObject.ET_POST_FEED, req, callback, dtSecCode);
    }

    public void postComment(String comment, String feedId, int feedType, String commentNickName, ReplyCommentInfo replyCommentInfo, DataSourceProxy.IRequestCallback callback) {
        PoCommentReq req = new PoCommentReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        req.setStUserInfo(accountManager.getUserInfo());
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);
        req.setSContent(comment.trim());
        req.setSFeedId(feedId);
        req.setEFeedType(feedType);
        req.setSCommentNickName(commentNickName);
        req.setStReplyComment(replyCommentInfo);

        DataEngine.getInstance().request(EntityObject.ET_POST_COMMENT, req, callback, feedId);
    }

    public void deleteFeed(String feedId, String dtSecCode, DataSourceProxy.IRequestCallback callback, boolean fromStock) {
        DelFeedReq req = new DelFeedReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        req.setStUserInfo(accountManager.getUserInfo());
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);
        req.setSFeedId(feedId);
        req.setSDtSecCode(dtSecCode);
        if(fromStock) {
            req.setESetFeedListType(E_SET_FEED_LIST_TYPE.E_SFLT_SECCODE);
        } else {
            req.setESetFeedListType(E_SET_FEED_LIST_TYPE.E_SFLT_ACCOUNT);
        }
        DataEngine.getInstance().request(EntityObject.ET_DELETE_FEED, req, callback, feedId);
    }

    public void deleteComment(String feedId, String commentId, DataSourceProxy.IRequestCallback callback) {
        DelCommentReq req = new DelCommentReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        req.setStUserInfo(accountManager.getUserInfo());
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);
        req.setSFeedId(feedId);
        req.setSCommentId(commentId);
        DataEngine.getInstance().request(EntityObject.ET_DELETE_COMMENT, req, callback, feedId);
    }

    public void doLike(String feedId, int feedType, boolean isAddd, DataSourceProxy.IRequestCallback callback) {
        LikeReq req = new LikeReq();
        AccountManager accountManager = DengtaApplication.getApplication().getAccountManager();
        AccountInfoEntity accountInfo = accountManager.getAccountInfo();
        req.setStUserInfo(accountManager.getUserInfo());
        AccountTicket accountTicket = new AccountTicket();
        accountTicket.setVtTicket(accountInfo != null ? accountInfo.ticket : null);
        req.setStAccountTicket(accountTicket);
        req.setSFeedId(feedId);
        req.setEFeedType(feedType);
        req.setIsAdd(isAddd);
        DataEngine.getInstance().request(EntityObject.ET_FEED_DO_LIKE, req, callback);
    }

    @Override
    public void callback(boolean success, EntityObject entity) {
        int entityType = entity.getEntityType();
        String extra = (String) entity.getExtra();
        MapProtoLite packet;
        switch (entityType) {
            case EntityObject.ET_POST_FEED:
                PoFeedRsp poFeedRsp = (PoFeedRsp) entity.getEntity();
                handlePostFeedResult(success, poFeedRsp, extra);
                break;
            case EntityObject.ET_POST_COMMENT:
                PoCommentRsp rsp = (PoCommentRsp) entity.getEntity();
                handlePostCommentResult(success, extra);
                break;
            case EntityObject.ET_GET_FEED:
                GetFeedRsp getFeedRsp = (GetFeedRsp) entity.getEntity();
                handleGetFeedResult(success, getFeedRsp);
                break;
            case EntityObject.ET_DELETE_FEED:
                packet = (MapProtoLite) entity.getEntity();
                if (success) {
                    if (packet != null) {
                        int retCode = packet.read("", -1);
                        handleDeleteFeedResult(retCode == 0, extra);
                    } else {
                        handleDeleteFeedResult(false, extra);
                    }
                } else {
                    handleDeleteFeedResult(false, extra);
                }
                break;
            case EntityObject.ET_DELETE_COMMENT:
                packet = (MapProtoLite) entity.getEntity();
                if (success) {
                    if (packet != null) {
                        int retCode = packet.read("", -1);
                        handleDeleteCommentResult(retCode == 0, extra);
                    } else {
                        handleDeleteCommentResult(false, extra);
                    }
                } else {
                    handleDeleteCommentResult(false, extra);
                }
                break;
            default:
                break;
        }
    }

    private void handleDeleteFeedResult(boolean success, String feedId) {
        DtLog.d(TAG, "handleDeleteFeedResult");
        EventBus.getDefault().post(new DeleteFeedEvent(success, feedId));
    }

    private void handleDeleteCommentResult(boolean success, String feedId) {
        DtLog.d(TAG, "handleDeleteCommentResult");
        EventBus.getDefault().post(new DeleteCommentEvent(success, feedId));

        getFeed(feedId);
    }

    private void handleGetFeedResult(boolean success, GetFeedRsp getFeedRsp) {
        DtLog.d(TAG, "handleGetFeedResult, success = " + success);
        if (getFeedRsp != null) {
            GetFeedResultEvent event = new GetFeedResultEvent(getFeedRsp.getStFeedItem());
            EventBus.getDefault().post(event);
        }
    }

    private void handlePostFeedResult(boolean success, PoFeedRsp poFeedRsp, String dtSecCode) {
        DtLog.d(TAG, "handlePostFeedResult");
        PostFeedResultEvent event = new PostFeedResultEvent(success, dtSecCode);
        EventBus.getDefault().post(event);
        if (success) {
            DengtaApplication.getApplication().getBonusPointManager().reportFinishedTask(AccuPointTaskType.E_ACCU_POINT_TASK_PO_FEED);
        }
    }

    private void handlePostCommentResult(boolean success, String feedId) {
        DtLog.d(TAG, "handlePostCommentResult");
        PostCommentResultEvent event = new PostCommentResultEvent(success, feedId);
        EventBus.getDefault().post(event);

        getFeed(feedId);

        if (success) {
            DengtaApplication.getApplication().getBonusPointManager().reportFinishedTask(AccuPointTaskType.E_ACCU_POINT_TASK_COMMENT_FEED);
        }
    }

    public static void getInvestmentAdviserRecommendedListRequest(final DataSourceProxy.IRequestCallback callback) {
        final GetInvestRecommendReq req = new GetInvestRecommendReq();
        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_INVESTMENT_ADVISER_RECOMMENDED_LIST, req, callback);
    }

    public static void getInvestmentAdviserListTitleRequest(final DataSourceProxy.IRequestCallback callback) {
        final GetInvestListReq req = new GetInvestListReq();
        req.sStartId = "";
        req.iDirection = 0;
        req.bGetListConf = true;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_INVESTMENT_ADVISER_LIST, req, callback);
    }

    public static void getInvestmentAdviserListRequest(final String startId, final int type, final DataSourceProxy.IRequestCallback callback) {
        final GetInvestListReq req = new GetInvestListReq();
        req.eListType = type;
        req.sStartId = startId;
        req.iDirection = TextUtils.isEmpty(startId) ? 0 : 1;
        req.bGetListConf = false;

        req.stUserInfo = DengtaApplication.getApplication().getAccountManager().getUserInfo();
        DataEngine.getInstance().request(EntityObject.ET_GET_INVESTMENT_ADVISER_LIST, req, callback, startId);
    }

    public void doLikeComment(JSONObject jsonObject) {
        final String feedId = jsonObject.optString("feedId");
        final boolean isAdd = jsonObject.optBoolean("isAdd");
        final int feedType = jsonObject.optInt("feedType");
        final long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();

        final FeedRequestManager feedRequestManager = DengtaApplication.getApplication().getFeedRequestManager();
        feedRequestManager.doLike(feedId, feedType, isAdd, feedRequestManager);

        if (isAdd) {
            DengtaApplication.getApplication().getBonusPointManager().reportFinishedTask(AccuPointTaskType.E_ACCU_POINT_TASK_PRAISE_FEED);
        }

        EventBus.getDefault().post(new DoLikeByWebEvent(feedId, isAdd));

        saveFeedLike(accountId, feedId, isAdd);
    }

    public boolean queryIsLike(String feedId) {
        if (TextUtils.isEmpty(feedId)) {
            return false;
        }

        final long accountId = DengtaApplication.getApplication().getAccountManager().getAccountId();
        final FeedLikeEntity feedLikeEntity = getFeedLikeEntityFromDb(accountId, feedId);
        return feedLikeEntity != null && feedLikeEntity.isLike();
    }

    @Override
    public void showFeedDeleteDialog(Context context, String dtSecCode, String feedId, String commentId) {
        FeedOperationDialog feedOperationDialog = new FeedOperationDialog(context, FeedOperationDialog.TYPE_MY_COMMENT, dtSecCode, null, null, false);
        feedOperationDialog.setCommentIds(feedId, commentId);
        feedOperationDialog.show();
    }

    public static FeedLikeEntity getFeedLikeEntityFromDb(final long accountId, final String feedId) {
        final String where = String.format("accountId=%s and feedId='%s'", accountId, feedId);
        final List<FeedLikeEntity> entities = DBHelper.getInstance().findAllByWhere(FeedLikeEntity.class, where);
        return entities != null && entities.size() > 0 ? entities.get(0) : null;
    }

    public static FeedLikeEntity saveFeedLike(final long accountId, final String feedId, final boolean isAdd) {
        // 保存到数据库缓存
        FeedLikeEntity feedLikeEntity = getFeedLikeEntityFromDb(accountId, feedId);
        if (feedLikeEntity == null) {
            feedLikeEntity = new FeedLikeEntity();
            feedLikeEntity.setFeedId(feedId);
            feedLikeEntity.setAccountId(accountId);
            feedLikeEntity.setLike(isAdd);
            DBHelper.getInstance().add(feedLikeEntity);
        } else {
            feedLikeEntity.setLike(isAdd);
            DBHelper.getInstance().update(feedLikeEntity);
        }
        return feedLikeEntity;
    }

    public static FeedDraftEntity getFeedDraftEntityFromDb(final long accountId, final String dtSecCode) {
        final String where = String.format("accountId=%s and dtSecCode='%s'", accountId, dtSecCode);
        final List<FeedDraftEntity> entities = DBHelper.getInstance().findAllByWhere(FeedDraftEntity.class, where);
        return entities != null && entities.size() > 0 ? entities.get(0) : null;
    }

    private static void deleteFeedDraftEntity(final long accountId, final String dtSecCode) {
        final String where = String.format("accountId=%s and dtSecCode='%s'", accountId, dtSecCode);
        DBHelper.getInstance().deleteByWhere(FeedDraftEntity.class, where);
    }

    public static void saveFeedToDraftCache(final String comment, final long accountId, final String dtSecCode) {
        final DBHelper dbHelper = DBHelper.getInstance();
        if (TextUtils.isEmpty(comment)) {
            deleteFeedDraftEntity(accountId, dtSecCode);
            return;
        }
        FeedDraftEntity draft = getFeedDraftEntityFromDb(accountId, dtSecCode);
        if (draft == null) {
            draft = new FeedDraftEntity();
            draft.setAccountId(accountId);
            draft.setDtSecCode(dtSecCode);
            draft.setContent(comment);
            dbHelper.add(draft);
        } else {
            draft.setContent(comment);
            dbHelper.update(draft);
        }
    }

    public static CommentDraftEntity getCommentDraftEntityFromDb(final long accountId, final String feedId, final long replyAccountId) {
        final String where = String.format("accountId=%s and replyAccountId=%s and feedId='%s'", accountId, replyAccountId, feedId);
        final List<CommentDraftEntity> entities = DBHelper.getInstance().findAllByWhere(CommentDraftEntity.class, where);
        return entities != null && entities.size() > 0 ? entities.get(0) : null;
    }

    public static void deleteCommentDraftEntity(final long accountId, final String feedId, final long replyAccountId) {
        final String where = String.format("accountId=%s and replyAccountId=%s and feedId='%s'", accountId, replyAccountId, feedId);
        DBHelper.getInstance().deleteByWhere(CommentDraftEntity.class, where);
    }

    public static void saveCommentToDraftCache(final String comment, final long accountId, final String feedId, final long replyAccountId) {
        if (TextUtils.isEmpty(comment)) {
            deleteCommentDraftEntity(accountId, feedId, replyAccountId);
            return;
        }
        final DBHelper dbHelper = DBHelper.getInstance();
        CommentDraftEntity draft = getCommentDraftEntityFromDb(accountId, feedId, replyAccountId);
        if (draft == null) {
            draft = new CommentDraftEntity();
            draft.setAccountId(accountId);
            draft.setFeedId(feedId);
            draft.setReplyAccountId(replyAccountId);
            draft.setContent(comment);
            dbHelper.add(draft);
        } else {
            draft.setContent(comment);
            dbHelper.update(draft);
        }
    }
}
