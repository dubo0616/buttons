package com.gaia.button.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.gaia.button.R;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class ArcSeekBarOutter extends View {
    private static final int DEFAULT_EDGE_LENGTH = 260;              // 默认宽高

    private static final float CIRCLE_ANGLE = 360;                  // 圆周角
    private static final int DEFAULT_ARC_WIDTH = 100;                // 默认宽度 dp
    private static final float DEFAULT_OPEN_ANGLE = 0;            // 开口角度
    private static final float DEFAULT_ROTATE_ANGLE = 90;           // 旋转角度
    private static final int DEFAULT_BORDER_WIDTH = 0;              // 默认描边宽度
    private static final int DEFAULT_BORDER_COLOR = 0xffffffff;     // 默认描边颜色

    private static final int DEFAULT_THUMB_COLOR = 0xffffffff;      // 拖动按钮颜色
    private static final int DEFAULT_THUMB_WIDTH = 2;               // 拖动按钮描边宽度 dp
    private static final int DEFAULT_THUMB_RADIUS = 15;             // 拖动按钮半径 dp
    private static final int DEFAULT_THUMB_SHADOW_RADIUS = 0;       // 拖动按钮阴影半径 dp
    private static final int DEFAULT_THUMB_SHADOW_COLOR = 0xFF000000; // 拖动按钮阴影颜色

    private static final int DEFAULT_SHADOW_RADIUS = 0;             // 默认阴影半径 dp

    private static final int THUMB_MODE_STROKE = 0;                 // 拖动按钮模式 - 描边
    private static final int THUMB_MODE_FILL = 1;                   // 拖动按钮模式 - 填充
    private static final int THUMB_MODE_FILL_STROKE = 2;            // 拖动按钮模式 - 填充+描边

    private static final int DEFAULT_MAX_VALUE = 100;               // 默认最大数值
    private static final int DEFAULT_MIN_VALUE = 0;                 // 默认最小数值

    private static final String KEY_PROGRESS_PRESENT = "PRESENT";   // 用于存储和获取当前百分比

    // 可配置数据
    private int[] mArcColors;       // Seek 颜色
    private float mArcWidth;        // Seek 宽度
    private float mOpenAngle;       // 开口的角度大小 0 - 360
    private float mSweepAngle;       // 开口的角度大小 0 - 360
    private float mRotateAngle;     // 旋转角度
    private int mBorderWidth;       // 描边宽度
    private int mBorderColor;       // 描边颜色


    private int mShadowRadius;      // 阴影半径

    private int mMaxValue;          // 最大数值
    private int mMinValue;          // 最小数值

    private float mCenterX;         // 圆弧 SeekBar 中心点 X
    private float mCenterY;         // 圆弧 SeekBar 中心点 Y


    private Path mSeekPath,mSeekPathNew;
    private Path mBorderPath,mBorderPathNew;
    private Paint mArcPaint;
    private Paint mBorderPaint;
    private Paint mShadowPaint;

    private PathMeasure mSeekPathMeasure;
    private PathMeasure mSeekPathMeasureNew;

    private float mProgressPresent = 0;         // 当前进度百分比
    private Matrix mInvertMatrix;               // 逆向 Matrix, 用于计算触摸坐标和绘制坐标的转换


    public ArcSeekBarOutter(Context context) {
        this(context, null);
    }

    public ArcSeekBarOutter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcSeekBarOutter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSaveEnabled(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        initAttrs(context, attrs);
        initData();
        initPaint();
    }

    //--- 初始化 -----------------------------------------------------------------------------------

    // 初始化各种属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcSeekBarInner);
        mArcColors = getArcColors(context, ta);
        mArcWidth = ta.getDimensionPixelSize(R.styleable.ArcSeekBarInner_arc_width, dp2px(DEFAULT_ARC_WIDTH));
        mOpenAngle = ta.getFloat(R.styleable.ArcSeekBarInner_arc_open_angle, DEFAULT_OPEN_ANGLE);
        mRotateAngle = ta.getFloat(R.styleable.ArcSeekBarInner_arc_rotate_angle, DEFAULT_ROTATE_ANGLE);
        mMaxValue = ta.getInt(R.styleable.ArcSeekBarInner_arc_max, DEFAULT_MAX_VALUE);
        mMinValue = ta.getInt(R.styleable.ArcSeekBarInner_arc_min, DEFAULT_MIN_VALUE);
        // 如果用户设置的最大值和最小值不合理，则直接按照默认进行处理
        if (mMaxValue <= mMinValue) {
            mMaxValue = DEFAULT_MAX_VALUE;
            mMinValue = DEFAULT_MIN_VALUE;
        }
        int progress = ta.getInt(R.styleable.ArcSeekBarInner_arc_progress, mMinValue);
        setProgress(progress);
        mBorderWidth = ta.getDimensionPixelSize(R.styleable.ArcSeekBarInner_arc_border_width, dp2px(DEFAULT_BORDER_WIDTH));
        mBorderColor = ta.getColor(R.styleable.ArcSeekBarInner_arc_border_color, DEFAULT_BORDER_COLOR);

        mShadowRadius = ta.getDimensionPixelSize(R.styleable.ArcSeekBarInner_arc_shadow_radius, dp2px(DEFAULT_SHADOW_RADIUS));
        ta.recycle();
    }

    // 获取 Arc 颜色数组
    private int[] getArcColors(Context context, TypedArray ta) {
        int[] ret;
        int resId = ta.getResourceId(R.styleable.ArcSeekBarInner_arc_colors, 0);
        if (0 == resId) {
            resId = R.array.arc_colors_custom;
        }
        ret = getColorsByArrayResId(context, resId);
        return ret;
    }

    // 根据 resId 获取颜色数组
    private int[] getColorsByArrayResId(Context context, int resId) {
        int[] ret;
        TypedArray colorArray = context.getResources().obtainTypedArray(resId);
        ret = new int[colorArray.length()];
        for (int i = 0; i < colorArray.length(); i++) {
            ret[i] = colorArray.getColor(i, 0);
        }
        return ret;
    }

    // 初始化数据
    private void initData() {
        mSeekPath = new Path();
        mSeekPathNew = new Path();
        mBorderPath = new Path();
        mBorderPathNew = new Path();
        mSeekPathMeasure = new PathMeasure();
        mSeekPathMeasureNew = new PathMeasure();
        mInvertMatrix = new Matrix();
    }

    // 初始化画笔
    private void initPaint() {
        initArcPaint();
        initBorderPaint();
        initShadowPaint();
    }

    // 初始化圆弧画笔
    private void initArcPaint() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setColor(Color.parseColor("#88e4e4e4"));
    }


    // 初始化拖动按钮画笔
    private void initBorderPaint() {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }

    // 初始化阴影画笔
    private void initShadowPaint() {
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStrokeWidth(mBorderWidth);
        mShadowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mShadowPaint.setColor(Color.parseColor("#88e4e4e4"));
    }

    //--- 初始化结束 -------------------------------------------------------------------------------

    //--- 状态存储 ---------------------------------------------------------------------------------

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putFloat(KEY_PROGRESS_PRESENT, mProgressPresent);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mProgressPresent = bundle.getFloat(KEY_PROGRESS_PRESENT);
            state = bundle.getParcelable("superState");
        }
        if (null != mOnProgressChangeListener) {
            mOnProgressChangeListener.onProgressChanged(this, getProgress(), false);
        }
        super.onRestoreInstanceState(state);
    }

    //--- 状态存储结束 -----------------------------------------------------------------------------

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int ws = MeasureSpec.getSize(widthMeasureSpec);     //取出宽度的确切数值
        int wm = MeasureSpec.getMode(widthMeasureSpec);     //取出宽度的测量模式
        int hs = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        int hm = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模

        if (wm == MeasureSpec.UNSPECIFIED) {
            wm = MeasureSpec.EXACTLY;
            ws = dp2px(DEFAULT_EDGE_LENGTH);
        } else if (wm == MeasureSpec.AT_MOST) {
            wm = MeasureSpec.EXACTLY;
            ws = Math.min(dp2px(DEFAULT_EDGE_LENGTH), ws);
        }
        if (hm == MeasureSpec.UNSPECIFIED) {
            hm = MeasureSpec.EXACTLY;
            hs = dp2px(DEFAULT_EDGE_LENGTH);
        } else if (hm == MeasureSpec.AT_MOST) {
            hm = MeasureSpec.EXACTLY;
            hs = Math.min(dp2px(DEFAULT_EDGE_LENGTH), hs);
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(ws, wm), MeasureSpec.makeMeasureSpec(hs, hm));
    }
    RectF content;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("TTTT","onSizeChanged ===="+mOpenAngle);
        // 计算在当前大小下,内容应该显示的大小和起始位置
        int safeW = w - getPaddingLeft() - getPaddingRight();
        int safeH = h - getPaddingTop() - getPaddingBottom();
        float edgeLength, startX, startY;
        float fix = mArcWidth / 2 + mBorderWidth + mShadowRadius * 2;  // 修正距离,画笔宽度的修正
        if (safeW < safeH) {
            // 宽度小于高度,以宽度为准
            edgeLength = safeW - fix;
            startX = getPaddingLeft();
            startY = (safeH - safeW) / 2.0f + getPaddingTop();
        } else {
            // 宽度大于高度,以高度为准
            edgeLength = safeH - fix;
            startX = (safeW - safeH) / 2.0f + getPaddingLeft();
            startY = getPaddingTop();
        }

        // 得到显示区域和中心的
        content = new RectF(startX + fix, startY + fix, startX + edgeLength, startY + edgeLength);
        mCenterX = content.centerX();
        mCenterY = content.centerY();
        // 得到路径
        mSeekPathNew.reset();
        mSeekPathNew.addArc(content, mOpenAngle / 2,  mSweepAngle);

        // 得到路径
        mSeekPath.reset();
        mSeekPath.addArc(content, mOpenAngle / 2, CIRCLE_ANGLE - mOpenAngle);
        mSeekPathMeasure.setPath(mSeekPath, false);

        resetShaderColor();

        mInvertMatrix.reset();
        mInvertMatrix.preRotate(-mRotateAngle, mCenterX, mCenterY);

        mArcPaint.getFillPath(mSeekPath, mBorderPath);
        mArcPaint.getFillPath(mSeekPathNew, mBorderPathNew);
        mBorderPath.close();
        mBorderPathNew.close();
    }

    // 重置 shader 颜色
    private void resetShaderColor() {
        // 计算渐变数组
        float startPos = (mOpenAngle / 2) / CIRCLE_ANGLE;
        float stopPos = (CIRCLE_ANGLE - (mOpenAngle / 2)) / CIRCLE_ANGLE;
        int len = mArcColors.length - 1;
        float distance = (stopPos - startPos) / len;
        float pos[] = new float[mArcColors.length];
        for (int i = 0; i < mArcColors.length; i++) {
            pos[i] = startPos + (distance * i);
        }
        mArcPaint.setColor(Color.TRANSPARENT);
        SweepGradient gradient = new SweepGradient(mCenterX, mCenterY, mArcColors, pos);
        mArcPaint.setShader(gradient);
    }

    // 具体绘制
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(mRotateAngle, mCenterX, mCenterY);
        mShadowPaint.setColor(Color.parseColor("#88e4e4e4"));
        canvas.drawPath(mBorderPath, mShadowPaint);
        mShadowPaint.setColor(Color.parseColor("#FF7256"));
        canvas.drawPath(mBorderPathNew, mShadowPaint);
        canvas.restore();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    //--- 线性取色 ---------------------------------------------------------------------------------

    /**
     * 获取当前进度的具体颜色
     *
     * @return 当前进度在渐变中的颜色
     */
    public int getColor() {
        return getColor(mProgressPresent);
    }

    /**
     * 获取某个百分比位置的颜色
     *
     * @param radio 取值[0,1]
     * @return 最终颜色
     */
    private int getColor(float radio) {
        float diatance = 1.0f / (mArcColors.length - 1);
        int startColor;
        int endColor;
        if (radio >= 1) {
            return mArcColors[mArcColors.length - 1];
        }
        for (int i = 0; i < mArcColors.length; i++) {
            if (radio <= i * diatance) {
                if (i == 0) {
                    return mArcColors[0];
                }
                startColor = mArcColors[i - 1];
                endColor = mArcColors[i];
                float areaRadio = getAreaRadio(radio, diatance * (i - 1), diatance * i);
                return getColorFrom(startColor, endColor, areaRadio);
            }
        }
        return -1;
    }

    /**
     * 计算当前比例在子区间的比例
     *
     * @param radio         总比例
     * @param startPosition 子区间开始位置
     * @param endPosition   子区间结束位置
     * @return 自区间比例[0, 1]
     */
    private float getAreaRadio(float radio, float startPosition, float endPosition) {
        return (radio - startPosition) / (endPosition - startPosition);
    }

    /**
     * 取两个颜色间的渐变区间 中的某一点的颜色
     *
     * @param startColor 开始的颜色
     * @param endColor   结束的颜色
     * @param radio      比例 [0, 1]
     * @return 选中点的颜色
     */
    private int getColorFrom(int startColor, int endColor, float radio) {
        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        return Color.argb(255, red, greed, blue);
    }


    //region 对外接口 -------------------------------------------------------------------------------

    /**
     * 设置进度
     *
     * @param progress 进度值
     */
    public void setProgress(int progress) {
        System.out.println("setProgress = " + progress);
        if (progress > mMaxValue) progress = mMaxValue;
        if (progress < mMinValue) progress = mMinValue;
        mSweepAngle = (270 / mMaxValue) * (progress);
        if(mSeekPathNew != null ) {
            mSeekPathNew.reset();
            if(content != null) {
                mSeekPathNew.addArc(content, mOpenAngle / 2, mSweepAngle);
            }
            mArcPaint.getFillPath(mSeekPathNew, mBorderPathNew);
            mBorderPathNew.close();
            postInvalidate();
        }
    }

    /**
     * 获取当前进度数值
     *
     * @return 当前进度数值
     */
    public int getProgress() {
        return (int) (mProgressPresent * (mMaxValue - mMinValue)) + mMinValue;
}

    /**
     * 设置颜色
     *
     * @param colors 颜色
     */
    public void setArcColors(int[] colors) {
        mArcColors = colors;
        resetShaderColor();
        postInvalidate();
    }

    /**
     * 设置最大数值
     * @param max 最大数值
     */
    public void setMaxValue(int max) {
        mMaxValue = max;
    }

    /**
     * 设置最小数值
     * @param min 最小数值
     */
    public void setMinValue(int min) {
        mMinValue = min;
    }

    /**
     * 设置颜色
     *
     * @param colorArrayRes 颜色资源 R.array.arc_color
     */
    public void setArcColors(int colorArrayRes) {
        setArcColors(getColorsByArrayResId(getContext(), colorArrayRes));
    }

    // endregion -----------------------------------------------------------------------------------
    // region 状态回调 ------------------------------------------------------------------------------

    private OnProgressChangeListener mOnProgressChangeListener;

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        mOnProgressChangeListener = onProgressChangeListener;
    }

    public interface OnProgressChangeListener {
        /**
         * 进度发生变化
         *
         * @param seekBar  拖动条
         * @param progress 当前进度数值
         * @param isUser   是否是用户操作, true 表示用户拖动, false 表示通过代码设置
         */
        void onProgressChanged(ArcSeekBarOutter seekBar, int progress, boolean isUser);

        /**
         * 用户开始拖动
         *
         * @param seekBar 拖动条
         */
        void onStartTrackingTouch(ArcSeekBarOutter seekBar);

        /**
         * 用户结束拖动
         *
         * @param seekBar 拖动条
         */
        void onStopTrackingTouch(ArcSeekBarOutter seekBar);
    }
    // endregion -----------------------------------------------------------------------------------
}
