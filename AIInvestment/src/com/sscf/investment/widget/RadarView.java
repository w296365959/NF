package com.sscf.investment.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.sscf.investment.R;
import com.sscf.investment.main.DengtaApplication;
import com.sscf.investment.sdk.utils.DtLog;
import com.sscf.investment.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import BEC.ConsultScore;
import BEC.ScoreDesc;

/**
 * 芝麻信用分
 * Created by yangle on 2016/9/26.
 */
public class RadarView extends View {

    private static final String TAG = RadarView.class.getSimpleName();
    //每个角的弧度
    private float deltaRadian;
    //雷达图半径
    private float radius;
    //中心X坐标
    private int centerX;
    //中心Y坐标
    private int centerY;

    private static final int RADAR_DIMENSION = 6;

    private static final int STROKE_ALPHA = 255;
    private static final int FILL_ALPHA = 204;

    private List<RadarData> mRadarDataList = new ArrayList<>(RADAR_DIMENSION);

    private float[] referenceLine = new float[] { 0.25f, 0.5f, 0.75f, 1f };

    //雷达图与标题的间距
    private int titleMargin;
    //雷达区画笔
    private Paint mainPaint;
    //数据区画笔
    private Paint valuePaint;
    //标题画笔
    private Paint titlePaint;
    //标题文字大小
    private int titleSize;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        titleMargin = getResources().getDimensionPixelSize(R.dimen.radar_view_title_margin);
        titleSize = getResources().getDimensionPixelSize(R.dimen.font_size_10);

        mainPaint = new Paint();
        mainPaint.setAntiAlias(true);
        mainPaint.setStrokeWidth(1);
        mainPaint.setColor(getResources().getColor(R.color.radar_view_line_color));
        mainPaint.setStyle(Paint.Style.STROKE);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(getResources().getColor(R.color.default_orange_color));

        titlePaint = new Paint();
        titlePaint.setAntiAlias(true);
        titlePaint.setTextSize(titleSize);
        titlePaint.setColor(getResources().getColor(R.color.default_text_color_60));
        titlePaint.setStyle(Paint.Style.FILL);

        deltaRadian = (float) (Math.PI * 2 / RADAR_DIMENSION);
        initData();
    }

    private synchronized void initData() {
        if(mRadarDataList.isEmpty()) {
            mRadarDataList.add(new RadarData(RadarData.RISE, R.string.score_rise_title, 0f));
            mRadarDataList.add(new RadarData(RadarData.MARKET_HOT, R.string.score_market_hot_title, 0f));
            mRadarDataList.add(new RadarData(RadarData.MAIN, R.string.score_main_title, 0f));
            mRadarDataList.add(new RadarData(RadarData.TREND, R.string.score_trend_title, 0f));
            mRadarDataList.add(new RadarData(RadarData.VALUE, R.string.score_value_title, 0f));
            mRadarDataList.add(new RadarData(RadarData.CONSULT, R.string.score_consult_title, 0f));
        }
    }

    public void setData(ConsultScore score) {
        initData();
        for(RadarData radarData : mRadarDataList) {
            radarData.percent = getScorePercent(radarData.type, score);
        }
        postInvalidate();
    }

    private float getScorePercent(int type, ConsultScore score) {
        ScoreDesc scoreDesc = null;
        String key = "";
        switch (type) {
            case RadarData.RISE:
                scoreDesc = score.getStRiseScoreDesc();
                key = "上涨";
                break;
            case RadarData.MARKET_HOT:
                scoreDesc = score.getStMktHotScoreDesc();
                key = "热度";
                break;
            case RadarData.MAIN:
                scoreDesc = score.getStMainScoreDesc();
                key = "主力";
                break;
            case RadarData.TREND:
                scoreDesc = score.getStTrendScoreDesc();
                key = "走势";
                break;
            case RadarData.VALUE:
                scoreDesc = score.getStValueScoreDesc();
                key = "价值";
                break;
            case RadarData.CONSULT:
                scoreDesc = score.getStConsultScoreDesc();
                key = "投顾";
                break;
            default:
        }

        if(scoreDesc != null) {
            float percent = getScorePercent(scoreDesc.getIScore());
            DtLog.d(TAG, key + "：" + StringUtil.getPercentString(percent));
            return percent;
        }
        return 0f;
    }

    private float getScorePercent(int score) {
        return score / 100f;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //雷达图半径
        radius = Math.min(h, w) * 0.75f / 2;
        //中心坐标
        centerX = w / 2;
        centerY = h / 2;
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(!mRadarDataList.isEmpty()) {
            drawPolygon(canvas);
            drawLines(canvas);
            drawRegion(canvas);
            drawTitle(canvas);
        }
    }

    /**
     * 绘制多边形
     *
     * @param canvas 画布
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        for(int rIndex=0; rIndex<referenceLine.length; rIndex++) {
            float percent = referenceLine[rIndex];
            for (int i = 0, size = mRadarDataList.size(); i < size; i++) {
                Point point = getPoint(i, 0, percent);
                if (i == 0) {
                    path.moveTo(point.x, point.y);
                } else {
                    path.lineTo(point.x, point.y);
                }
            }
            //闭合路径
            path.close();
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制连接线
     *
     * @param canvas 画布
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0, size=mRadarDataList.size(); i < size; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            Point point = getPoint(i);
            path.lineTo(point.x, point.y);
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制覆盖区域
     *
     * @param canvas 画布
     */
    private void drawRegion(Canvas canvas) {
        Path path = new Path();

        for (int i = 0, size=mRadarDataList.size(); i < size; i++) {
            float percent = mRadarDataList.get(i).percent;
            Point point = getPoint(i, 0, percent);
            int x = point.x;
            int y = point.y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        //绘制填充区域的边界
        path.close();
        valuePaint.setStyle(Paint.Style.STROKE);
        valuePaint.setAlpha(STROKE_ALPHA);
        canvas.drawPath(path, valuePaint);

        //绘制填充区域
        valuePaint.setStyle(Paint.Style.FILL);
        valuePaint.setAlpha(FILL_ALPHA);
        canvas.drawPath(path, valuePaint);
    }

    /**
     * 绘制标题
     *
     * @param canvas 画布
     */
    private void drawTitle(Canvas canvas) {
        for (int i = 0, size=mRadarDataList.size(); i < size; i++) {
            Point point = getPoint(i, titleMargin, 1);
            int x = point.x;
            int y = point.y;
            RadarData data = mRadarDataList.get(i);
            String title = data.title;

            float titleWidth = titlePaint.measureText(title);
            int titleHeight = getTextHeight(titlePaint);

            switch (i) {
                case 0:
                    x -= titleWidth/2;
                    y -= titleHeight/2;
                    break;
                case 1:
                    break;
                case 2:
                    y += titleHeight / 2;
                    break;
                case 3:
                    x -= titleWidth/2;
                    y += titleHeight;
                    break;
                case 4:
                    x -= titleWidth;
                    y += titleHeight/2;
                    break;
                case 5:
                    x -= titleWidth;
                    break;
                default:
            }

            canvas.drawText(title, x, y, titlePaint);
        }
    }

    /**
     * 获取雷达图上各个点的坐标
     *
     * @param position 坐标位置（右上角为0，顺时针递增）
     * @return 坐标
     */
    private Point getPoint(int position) {
        return getPoint(position, 0, 1);
    }

    /**
     * 获取雷达图上各个点的坐标（包括维度标题与图标的坐标）
     *
     * @param position    坐标位置
     * @param radarMargin 雷达图与维度标题的间距
     * @param percent     覆盖区的的百分比
     * @return 坐标
     */
    private Point getPoint(int position, int radarMargin, float percent) {
        float radian = getRadian(position);
        int x = (int) (centerX + (radius + radarMargin) * Math.cos(radian) * percent);
        int y = (int) (centerY + (radius + radarMargin) * Math.sin(radian) * percent);

        return new Point(x, y);
    }

    private float getRadian(int position) {
        return deltaRadian * position - (float) Math.PI / 2;
    }

    private class RadarData {

        public static final int RISE = 1;
        public static final int MARKET_HOT = 2;
        public static final int MAIN = 3;
        public static final int TREND = 4;
        public static final int VALUE = 5;
        public static final int CONSULT = 6;

        public int type;
        private String title;
        public float percent;

        public RadarData(int type, int titleRes, float percent) {
            this(type, DengtaApplication.getApplication().getString(titleRes), percent);
        }

        public RadarData(int type, String title, float percent) {
            this.type = type;
            this.title = title;
            this.percent = percent;
        }
    }

    /**
     * 获取文本的高度
     *
     * @param paint 文本绘制的画笔
     * @return 文本高度
     */
    private int getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) (fontMetrics.descent - fontMetrics.ascent);
    }
}
