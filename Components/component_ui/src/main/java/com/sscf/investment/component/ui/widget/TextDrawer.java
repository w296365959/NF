package com.sscf.investment.component.ui.widget;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import com.sscf.investment.sdk.SDKManager;

@SuppressLint("FloatMath")
public class TextDrawer {
    public static final int LEFT   = 0x0000;
    public static final int TOP    = 0x0010;
    public static final int RIGHT  = 0x0001;
    public static final int BOTTOM = 0x0020;
    public static final int CENTER_VERTICAL   = 0x0030;
    public static final int CENTER_HORIZONTAL = 0x0002;
    public static final int CENTER = CENTER_VERTICAL | CENTER_HORIZONTAL;

    private static final int VERTICAL_MASK = TOP | BOTTOM | CENTER_VERTICAL;
    private static final int HORIZONTAL_MASK = LEFT | RIGHT | CENTER_HORIZONTAL;

    public static final int DRAW_MODE_NORMAL  = 0;   // 图片不做任何处理
    public static final int DRAW_MODE_STRETCH = 1;   // 按比例拉伸居中

    protected TextPaint mTextPaint = null;

    private static final DrawResult RESULT = new DrawResult();

    private FontMetrics mFontMetrics;
    // 多行文本的限制
    private static final String TEXT_SHENGLUE_HAO = "...";
    public static final int LINE_COUNT_INVAILD = -1;

    // 灯塔自定义字体
    private static Typeface mTypeface = null;

    public TextDrawer() {
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setSubpixelText(true);
        mFontMetrics = new FontMetrics();
    }

    /**
     * 获取灯塔自定义字体
     * @return
     */
    public static Typeface getTypeface() {
        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(SDKManager.getInstance().getContext().getAssets(), "fonts/myfont.ttf");
        }
        return mTypeface;
    }

    /**
     * 计算单行text文字的宽和高
     *
     * @param text
     * @param typeface
     * @param textSize
     * @return int[]——宽 int[0]; 高int[1]
     *
     */
    public int[] measureTextSize(String text, float textSize, Typeface typeface) {
        Rect rect = new Rect();
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        return new int[] {rect.width(), rect.height()};
    }

    /**
     * 计算单行text文字的宽
     *
     * @param text
     * @param typeface
     * @param textSize
     * @return int
     *
     */
    public int measureSingleTextWidth(CharSequence text, float textSize, Typeface typeface) {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        return (int) Math.ceil(mTextPaint.measureText(text, 0, text.length()));
    }

    /**
     * 计算单行text文字的高
     *
     * @param typeface
     * @param textSize
     * @return int
     *
     */
    public int measureSingleTextDescent(float textSize, Typeface typeface) {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        mTextPaint.getFontMetrics(mFontMetrics);
        return (int) Math.ceil(mFontMetrics.descent);
    }

    /**
     * 计算单行text文字的basiline与文字底部的距离
     *
     * @param typeface
     * @param textSize
     * @return int
     *
     */
    public int measureSingleTextHeight(float textSize, Typeface typeface) {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        mTextPaint.getFontMetrics(mFontMetrics);
        return (int) Math.ceil(0 - mFontMetrics.ascent);
    }

    public int measureSingleTextBottom(float textSize, Typeface typeface) {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        mTextPaint.getFontMetrics(mFontMetrics);
        return (int) Math.ceil(mFontMetrics.bottom);
    }

    public int measureSingleTextTop(float textSize, Typeface typeface) {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        mTextPaint.getFontMetrics(mFontMetrics);
        return (int) Math.ceil(mFontMetrics.top);
    }

    /**
     * 从给定的坐标点绘制单行文字
     *
     * @param canvas
     * @param text
     * @param left
     * @param top
     * @param textColor
     * @param textSize
     * @param typeface
     */
    public void drawSingleText(Canvas canvas, String text, int left, int top,
                               int textColor, float textSize, Typeface typeface, Align align) {
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        mTextPaint.setTextAlign(align == null ? Align.LEFT : align);
        canvas.drawText(text, left, top, mTextPaint);
    }

    public void drawSingleTextByMaxWidth(Canvas canvas, String text, int left,
                                         int top, int maxLineWidth, int textColor, float textSize,
                                         Typeface typeface, Align align) {
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        mTextPaint.setTextAlign(Align.LEFT);

        int strIndex = 0;
        int strCount = text.length();
        int strLineWidth = maxLineWidth;
        int x = left;
        int y = top;
        float width = 0;

        // 计算每个字符的width
        float[] strWidths = new float[strCount];
        mTextPaint.getTextWidths(text, strWidths);
        int ellipsisSize = (int) mTextPaint.measureText("…");

        // 每行计算行宽度，当超过maxLineWidth的时候，尾部截断，并加上省略号
        int i;
        for (i = 0; i < strCount; i++) {
            if (width + strWidths[i] + ellipsisSize > strLineWidth) {
                text = text.substring(0, i - 1) + "…";
                break;
            } else {
                width += strWidths[i];
            }
        }
        canvas.drawText(text, strIndex, text.length(), x, y, mTextPaint);
    }

    private void drawTextByMaxWidth(Canvas canvas, String text, int maxWidth, float x, float y, int textColor) {
        mTextPaint.setColor(textColor);

        // 计算每个字符的width
        int strCount = text.length();
        float[] strWidths = new float[strCount];
        mTextPaint.getTextWidths(text, strWidths);

        // 每行计算行宽度，当超过maxLineWidth的时候，尾部截断，并加上省略号
        int width = 0;
        for (int i = 0; i < strCount; i++) {
            if (width + strWidths[i] > maxWidth) {
                if (i >= 1) {
                    text = text.substring(0, i - 1) + "…";
                } else {
                    text = "…";
                }
                break;
            } else {
                width += strWidths[i];
            }
        }
        canvas.drawText(text, 0, text.length(), x, y, mTextPaint);
    }

    private float getTotalWidths(String text) {
        // 计算每个字符的width
        int strCount = text.length();
        float[] strWidths = new float[strCount];
        mTextPaint.getTextWidths(text, strWidths);

        float sum = 0;
        int length = text.length();
        for (int i = 0; i < length; i++) {
            sum += strWidths[i];
        }
        return sum;
    }

    /**
     * 从给定的坐标点绘制多行文字（可自动换行）
     *
     * @param canvas
     * @param text
     * @param alignX
     *            换行后的左对齐基准点
     * @param left
     *            开始位置的X坐标
     * @param top
     *            开始位置的Y坐标
     * @param linePadding
     *            行间距
     * @param maxLineWidth
     *            每行的最大宽度
     * @param textColor
     * @param textSize
     * @param lineMaxCount(该值可以设置为无效)
     * @param typeface
     * @return 最后一行文字的Y坐标
     */
    public int drawMultiText(Canvas canvas, CharSequence text, int alignX, int left,
                             int top, int linePadding, int maxLineWidth, int textColor,
                             float textSize, int lineMaxCount, Typeface typeface) {
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTypeface(typeface == null ? Typeface.DEFAULT : typeface);
        mTextPaint.setTextAlign(Align.LEFT);

        int lineCount = 1;
        int strIndex = 0;
        int strCount = text.length();
        int strLineWidth = maxLineWidth - (left - alignX);
        int x = left;
        int y = top;
        float width = 0;

        // 计算每个字符的width
        float[] strWidths = new float[strCount + 1];
        mTextPaint.getTextWidths(text, 0, strCount, strWidths);

        //计算行高
        mTextPaint.getFontMetrics(mFontMetrics);
        int strHeight = (int) Math.ceil(0 - mFontMetrics.ascent);

        // 每行计算行宽度，当超过maxLineWidth的时候，自动换行
        for (int i = 0; i < strCount; i++) {
            if (width + strWidths[i] > strLineWidth) {
                char code = text.charAt(i);
                switch(code) {
                    case ',':
                    case '.':
                    case ';':
                    case '!':
                    case '、':
                    case '，':
                    case '。':
                    case ':':
                    case '：':
                        width += strWidths[i];
                        break;

                    default:
                        // 当限制了行的数目时候，最后一行会少绘一个字，变成省略号。
                        if (lineMaxCount != LINE_COUNT_INVAILD && lineCount == lineMaxCount) {
                            canvas.drawText(text, strIndex, i - 2, x, y, mTextPaint);
                            canvas.drawText(TEXT_SHENGLUE_HAO, 0, TEXT_SHENGLUE_HAO.length(),
                                maxLineWidth - strWidths[i - 1] * 3 / 4, y, mTextPaint);
                            return y;
                        } else {
                            canvas.drawText(text, strIndex, i, x, y, mTextPaint);
                        }

                        lineCount++;
                        strIndex = i;
                        width = strWidths[i];
                        x = alignX;
                        y = y + strHeight + linePadding; // 加上行距
                        strLineWidth = maxLineWidth;
                }
            } else {
                width += strWidths[i];
            }
            if (i == strCount - 1) {
                canvas.drawText(text, strIndex, strCount, x, y, mTextPaint);
            }
        }
        return y;
    }

    public static final class DrawResult {
        public float endX;
        public int endY;
        public int endIndex;
    }

    /**
     * 从给定的坐标点绘制Bitmap
     *
     * @param canvas
     * @param bitmap
     * @param left
     * @param top
     * @param isRepeat
     *            是否需要平铺
     * @param maxLineWidth
     *            在图片平铺显示每行的最大宽度（如果isRepeat为false，此参数无效）
     */
    public void drawBitmap(Canvas canvas, Bitmap bitmap, int left, int top,
                           boolean isRepeat, int maxLineWidth) {
        if (isRepeat) {
            int count = maxLineWidth / bitmap.getWidth();
            for (int i = 0; i < count; ++i) {
                canvas.drawBitmap(bitmap, left + i * bitmap.getWidth(), top,
                    mTextPaint);
            }
        } else {
            canvas.drawBitmap(bitmap, left, top, mTextPaint);
        }
    }

    /**
     * 绘制Bitmap到目标区域
     *
     * @param canvas
     * @param bitmap
     * @param desRect
     */
    public void drawBitmap(Canvas canvas, Bitmap bitmap, Rect srcRect,
                           Rect desRect) {
        if (srcRect == null) {
            srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        }
        canvas.drawBitmap(bitmap, srcRect, desRect, mTextPaint);
    }

    /**
     * 从起始点到终点绘制Line
     *
     * @param canvas
     * @param startX
     * @param startY
     * @param stopX
     * @param stopY
     * @param textColor
     */
    public void drawLine(Canvas canvas, int startX, int startY, int stopX,
                         int stopY, int textColor) {
        mTextPaint.setColor(textColor);
        canvas.drawLine(startX, startY, stopX, stopY, mTextPaint);
    }

    /**
     * 从起始点到终点绘制Line
     *
     * @param canvas
     * @param startX
     * @param startY
     * @param stopX
     * @param stopY
     * @param textColor
     */
    public void drawLine(Canvas canvas, int startX, int startY, int stopX,
                         int stopY, int textColor, float textWidth) {
        mTextPaint.setColor(textColor);
        final float width = mTextPaint.getStrokeWidth();
        mTextPaint.setStrokeWidth(textWidth);
        canvas.drawLine(startX, startY, stopX, stopY, mTextPaint);
        mTextPaint.setStrokeWidth(width);
    }

    /**
     * 填充一个Rect区域
     *
     * @param canvas
     * @param rect
     * @param textColor
     */
    public void drawRect(Canvas canvas, Rect rect, int textColor) {
        mTextPaint.setColor(textColor);
        canvas.drawRect(rect, mTextPaint);
    }

    /**
     * 将drawable绘制在指定的rect区域内（rect会改变Bounds的大小）
     *
     * @param canvas
     * @param drawable
     * @param rect
     */
    public void drawDrawable(Canvas canvas, Drawable drawable, Rect rect) {
        drawable.setBounds(rect);
        drawable.draw(canvas);
    }

    /**
     * 将drawable绘制在指定的rect区域内（rect会改变Bounds的大小）
     *
     * @param canvas
     * @param drawable
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void drawDrawable(Canvas canvas, Drawable drawable, int left, int top, int right, int bottom) {
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);
    }

    /**
     * 在一个新的偏移位置绘制drawable
     * （当drawable的Bounds已经被设定，只需要偏移坐标left、top即可）
     *
     * @param canvas
     * @param drawable
     * @param left
     * @param top
     */
    public void drawDrawableOffsetTo(Canvas canvas, Drawable drawable, int left, int top) {
        drawable.setBounds(left, top, drawable.getBounds().width() + left, drawable.getBounds().height() + top);
        drawable.draw(canvas);
    }

    /**
     * 绘制圆角矩形框
     *
     * @param canvas
     * @param rect
     * @param round
     * @param fillColor
     */
    public void drawRoundRect(Canvas canvas, RectF rect, int round, int fillColor) {
        mTextPaint.setColor(fillColor);
        canvas.drawRoundRect(rect, round, round, mTextPaint);
    }

    public void drawText(Canvas canvas, CharSequence text, Rect rect, float textSize, int textColor,
                         int align, Typeface typeface) {
        drawText(canvas, text, rect.left, rect.top, rect.right, rect.bottom, textSize, textColor, align, typeface);
    }

    public void drawText(Canvas canvas, CharSequence text, float left, float top, float right, float bottom,
                         float textSize, int textColor, int align, Typeface typeface) {
        drawText(canvas, mTextPaint, text, left, top, right, bottom, textSize, textColor, align, typeface);
    }

    public static void drawText(Canvas canvas, TextPaint paint, CharSequence text, float left, float top,
                                float right, float bottom, float textSize, int textColor, int align, Typeface typeface) {
        // Sets text color, text size, etc.
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(typeface);

        // Gets text metrics info.
        final FontMetrics fontMetrics = paint.getFontMetrics();

        // Computes x coordinate.
        Align textAlign = Align.LEFT;
        switch (align & HORIZONTAL_MASK) {
            case RIGHT:
                left = right;
                textAlign = Align.RIGHT;
                break;
            case CENTER_HORIZONTAL:
                left = (left + right) / 2;
                textAlign = Align.CENTER;
                break;
            default:
                break;
        }

        // Computes y coordinate.
        switch (align & VERTICAL_MASK) {
            case BOTTOM:
                top = bottom - fontMetrics.descent;
                break;

            case CENTER_VERTICAL:
                top = (top + bottom - fontMetrics.bottom - fontMetrics.ascent) / 2;
                break;

            default:
                top -= fontMetrics.ascent;
        }

        paint.setTextAlign(textAlign);
        canvas.drawText(text, 0, text.length(), left, top, paint);
    }

    public static void calcDrawRect(Rect srcRect, Rect destRect, int drawMode) {
        final int srcWidth   = srcRect.width();
        final int srcHeight  = srcRect.height();
        final int destWidth  = destRect.width();
        final int destHeight = destRect.height();

        int offset = 0;
        if (drawMode == DRAW_MODE_STRETCH) {
            offset = (destHeight - (int) ((float) srcHeight * (float) destWidth / (float) srcWidth)) / 2;
            if (offset > 0) {
                destRect.top    += offset;
                destRect.bottom -= offset;
            } else {
                offset = (destWidth - (int) ((float) srcWidth * (float) destHeight / (float) srcHeight)) / 2;
                destRect.left  += offset;
                destRect.right -= offset;
            }
        } else {
            offset = (destWidth - srcWidth) / 2;
            destRect.left  += offset;
            destRect.right -= offset;

            offset = (destHeight - srcHeight) / 2;
            destRect.top    += offset;
            destRect.bottom -= offset;
        }
    }
}
