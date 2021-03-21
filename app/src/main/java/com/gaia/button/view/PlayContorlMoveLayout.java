package com.gaia.button.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gaia.button.R;
import com.gaia.button.utils.DensityUtil;
import com.gaia.button.utils.PlayControl;

public class PlayContorlMoveLayout extends ConstraintLayout {
    private Context mContext;
    private ImageView iv_paly_pause, iv_pre, iv_next, iv_small;
    private ConstraintLayout play_contorl;
    private ImageView play_contorl_small;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private MainContorlListener mListener;
    private float x, y;
    private float mTouchStartX;
    private float mTouchStartY;
    private boolean isScroll;
    private int dpi;
    private int screenHeight;
    private int screenWidth;
    private boolean isSmallShow = false;

    // 悬浮栏位置
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    private final static int TOP = 3;
    private final static int BUTTOM = 4;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取相对屏幕的坐标， 以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY();
        boolean isMove = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // setBackgroundDrawable(openDrawable);
                // invalidate();
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScroll) {
                    updateViewPosition();

                } else {
                    // 当前不处于连续滑动状态 则滑动小于图标1/3则不滑动
                    if (Math.abs(mTouchStartX - event.getX()) > dpi / 3
                            || Math.abs(mTouchStartY - event.getY()) > dpi / 3) {
                        updateViewPosition();
                    } else {
                        break;
                    }
                }
                isScroll = true;
                break;
            case MotionEvent.ACTION_UP:
                float currentX = mLayoutParams.x;
                float currentY = mLayoutParams.y;
                // 拖动
                if (isScroll) {
                    if (isSmallShow) {
                        autoView();
                    }
                    // setBackgroundDrawable(closeDrawable);
                    // invalidate();
                } else {
                    // 当前显示功能区，则隐藏
                    // setBackgroundDrawable(openDrawable);
                    // invalidate();

                }
                isScroll = false;

                if (Math.abs(Math.abs(currentX) - Math.abs(mLayoutParams.x)) > 10 || (Math.abs(Math.abs(currentY) - Math.abs(mLayoutParams.y)) > 10)) {
                    isMove = true;
                } else {
                    if (isSmallShow) {
                        mLayoutParams.width = DensityUtil.getScreenWidth(mContext) - DensityUtil.dip2px(mContext, 90);
                        mLayoutParams.height = dpi;
                        mWindowManager.removeView(view);
                        mWindowManager.addView(view, mLayoutParams);
                        play_contorl_small.setVisibility(View.GONE);
                        play_contorl.setVisibility(View.VISIBLE);
                    }
                    isSmallShow = false;
                }
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        if (isMove) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 自动移动位置
     */
    private void autoView() {
        // 得到view在屏幕中的位置
        int[] location = new int[2];
        getLocationOnScreen(location);
        //左侧
        if (isSmallShow) {
            if (location[0] < screenWidth / 2 - getWidth() / 2) {
                updateViewPosition(LEFT);
            } else {
                updateViewPosition(RIGHT);
            }
        } else {
            updateViewPosition(RIGHT);
        }
    }

    /**
     * 手指释放更新悬浮窗位置
     */
    private void updateViewPosition(int l) {
        switch (l) {
            case LEFT:
                mLayoutParams.x = 0;
                break;
            case RIGHT:
                int x = screenWidth - dpi;
                mLayoutParams.x = x;
                break;
            case TOP:
                mLayoutParams.y = dpi;
                break;
            case BUTTOM:
                mLayoutParams.y = screenHeight - 5 * dpi / 2;
                break;
        }
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }

    // 更新浮动窗口位置参数
    private void updateViewPosition() {
        mLayoutParams.x = (int) (x - mTouchStartX);
        //是否存在状态栏（提升滑动效果）
        // 不设置为全屏（状态栏存在） 标题栏是屏幕的1/25
        mLayoutParams.y = ((int) (y - mTouchStartY));
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }

    public interface MainContorlListener {
        boolean sendPlayControlCommand(int comm);
    }

    private boolean allow;

    public PlayContorlMoveLayout(@NonNull Context context) {
        this(context, null);
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public PlayContorlMoveLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setListener(MainContorlListener listener) {
        this.mListener = listener;
    }

    public PlayContorlMoveLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private View view;

    private void initView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_play, this);
        iv_paly_pause = findViewById(R.id.iv_paly_pause);
        iv_pre = findViewById(R.id.iv_pre);
        iv_next = findViewById(R.id.iv_next);
        iv_small = findViewById(R.id.iv_small);
        play_contorl = findViewById(R.id.play_contorl);
        play_contorl_small = findViewById(R.id.iv_paly_pause_small);
        iv_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSmallShow = true;
                if (isSmallShow) {
                    mLayoutParams.width = DensityUtil.dip2px(mContext, 46);
                    mLayoutParams.height = DensityUtil.dip2px(mContext, 46);
                    mWindowManager.removeView(view);
                    mWindowManager.addView(view, mLayoutParams);
                }
                play_contorl_small.setVisibility(View.VISIBLE);
                play_contorl.setVisibility(View.GONE);
                updateViewPosition(RIGHT);
            }
        });
        iv_paly_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (mListener.sendPlayControlCommand(iv_paly_pause.isSelected() ? PlayControl.PLAY.getValue() : PlayControl.PAUSE.getValue())) {
                        iv_paly_pause.setSelected(!iv_paly_pause.isSelected());
                    }
                }
            }
        });
        iv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (mListener.sendPlayControlCommand(PlayControl.BACKWARD.getValue())) {
                        iv_paly_pause.setSelected(false);
                    }
                }
            }
        });
        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    if (mListener.sendPlayControlCommand(PlayControl.FORWARD.getValue())) {
                        iv_paly_pause.setSelected(false);
                    }
                }
            }
        });
    }

    //屏高
    private void initWindow(View view) {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        if (allow) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        }
        mLayoutParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        mLayoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //通过像素密度来设置按钮的大小
        dpi = DensityUtil.dip2px(mContext, 58);
        //屏宽
        screenWidth = DensityUtil.getScreenWidth(mContext);
        //屏高
        screenHeight = DensityUtil.getScreenHeight(mContext);
        int m = DensityUtil.dip2px(mContext, 90);
        mLayoutParams.width = DensityUtil.getScreenWidth(mContext) - m;
        mLayoutParams.height = dpi;
        try {
            if (view.getWindowToken() != null) {
                mWindowManager.removeView(view);
            }
            mWindowManager.addView(view, mLayoutParams);
        } catch (IllegalArgumentException e) {
        }
    }

    public void setPause(boolean show) {
        iv_paly_pause.setSelected(show);
    }

    public void show() {
        if (isShown()) {
            return;
        }
        initWindow(view);
        updateViewPosition(BUTTOM);
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void destory() {
        try {
            hide();
            if (view.getWindowToken() != null) {
                mWindowManager.removeView(view);
            }
        } catch (IllegalArgumentException e) {
        }


    }
}
