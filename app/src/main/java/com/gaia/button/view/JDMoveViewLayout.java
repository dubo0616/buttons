//package com.gaia.button.view;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.PixelFormat;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.jindan.p2p.R;
//import com.jindan.p2p.utils.ScreenUtils;
//
///**
// * Created by dubo on 2018/4/26.
// */
//public class JDMoveViewLayout extends RelativeLayout {
//
//    // 悬浮栏位置
//    private final static int LEFT = 0;
//    private final static int RIGHT = 1;
//    private final static int TOP = 3;
//    private final static int BUTTOM = 4;
//
//    private int dpi;
//    private int screenHeight;
//    private int screenWidth;
//    private WindowManager.LayoutParams wmParams;
//    private WindowManager wmWindowManager;
//    private float x, y;
//    private float mTouchStartX;
//    private float mTouchStartY;
//    private boolean isScroll;
//    private MoveSmartImageView finance_gift;
//
//    public JDMoveViewLayout(Activity activity, String url) {
//        super(activity);
//        initWindowManager(activity);
//        initView(activity,url);
//        hide();
//    }
//    public void setImageUrl(String url) {
//
//        if (!TextUtils.isEmpty(url)) {
//            finance_gift.setImageUrl(url);
//        }
//    }
//
//    private void initView(Activity activity, String url){
//        LinearLayout linearLayout = new LinearLayout(activity);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT  );
//        linearLayout.setLayoutParams(params);
//        View view = LayoutInflater.from(activity).inflate(R.layout.view_chat, null);
//        finance_gift = view.findViewById(R.id.finance_gift);
//        finance_gift.setImageUrl(url);
//        linearLayout.addView(view);
//        addView(linearLayout);
//    }
//    private void initWindowManager(Activity activity){
//        wmWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        //通过像素密度来设置按钮的大小
//        dpi = (int) getResources().getDimension(R.dimen.home_gift_size);
//        //屏宽
//        screenWidth = ScreenUtils.getScreenWidth(activity);
//        //屏高
//        screenHeight =ScreenUtils.getScreenHeight(activity);
//        //布局设置
//        wmParams = new WindowManager.LayoutParams();
//        // 设置window type
//        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
//        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
//        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
//        // 设置Window flag
//        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        wmParams.width = dpi;
//        wmParams.height = dpi;
//        wmParams.y = (screenHeight - 5*dpi/2);
//        wmParams.x = (screenWidth - dpi/2);
//        wmWindowManager.addView(this, wmParams);
//    }
//    public void show() {
//        if (isShown()) {
//            return;
//        }
//        setVisibility(View.VISIBLE);
//    }
//
//
//    public void hide() {
//        setVisibility(View.GONE);
//    }
//
//    public void destory() {
//        hide();
//        if( null != wmWindowManager)
//            wmWindowManager.removeViewImmediate(this);
//    }
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        // 获取相对屏幕的坐标， 以屏幕左上角为原点
//        x = event.getRawX();
//        y = event.getRawY();
//        boolean isMove = false;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // setBackgroundDrawable(openDrawable);
//                // invalidate();
//                // 获取相对View的坐标，即以此View左上角为原点
//                mTouchStartX = event.getX();
//                mTouchStartY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (isScroll) {
//                    updateViewPosition();
//
//                } else {
//                    // 当前不处于连续滑动状态 则滑动小于图标1/3则不滑动
//                    if (Math.abs(mTouchStartX - event.getX()) > dpi / 3
//                            || Math.abs(mTouchStartY - event.getY()) > dpi / 3) {
//                        updateViewPosition();
//                    } else {
//                        break;
//                    }
//                }
//                isScroll = true;
//                break;
//            case MotionEvent.ACTION_UP:
//                float currentX  = wmParams.x;
//                float currentY  = wmParams.y;
//                // 拖动
//                if (isScroll) {
//                    autoView();
//                    // setBackgroundDrawable(closeDrawable);
//                    // invalidate();
//                } else {
//                    // 当前显示功能区，则隐藏
//                    // setBackgroundDrawable(openDrawable);
//                    // invalidate();
//
//                }
//                isScroll = false;
//
//                if (Math.abs(Math.abs(currentX) - Math.abs(wmParams.x)) > 10 || (Math.abs(Math.abs(currentY) - Math.abs(wmParams.y)) > 10)) {
//                    isMove = true;
//                }
//                mTouchStartX = mTouchStartY = 0;
//                break;
//        }
//        if (isMove) {
//            return true;
//        } else {
//            return super.onTouchEvent(event);
//        }
//    }
//
//    /**
//     * 自动移动位置
//     */
//    private void autoView() {
//        // 得到view在屏幕中的位置
//        int[] location = new int[2];
//        getLocationOnScreen(location);
//        //左侧
//        if (location[0] < screenWidth / 2 - getWidth() / 2) {
//            updateViewPosition(LEFT);
//        } else {
//            updateViewPosition(RIGHT);
//        }
//    }
//
//    /**
//     * 手指释放更新悬浮窗位置
//     *
//     */
//    private void updateViewPosition(int l) {
//        switch (l) {
//            case LEFT:
//                wmParams.x = 0;
//                break;
//            case RIGHT:
//                int x = screenWidth - dpi;
//                wmParams.x = x;
//                break;
//            case TOP:
//                wmParams.y = dpi;
//                break;
//            case BUTTOM:
//                wmParams.y = screenHeight - 5*dpi/2;
//                break;
//        }
//        wmWindowManager.updateViewLayout(this, wmParams);
//    }
//
//    // 更新浮动窗口位置参数
//    private void updateViewPosition() {
//        wmParams.x = (int) (x-mTouchStartX );
//        //是否存在状态栏（提升滑动效果）
//        // 不设置为全屏（状态栏存在） 标题栏是屏幕的1/25
//        wmParams.y = (int) (y-mTouchStartY - screenHeight / 25 );
//        wmWindowManager.updateViewLayout(this, wmParams);
//    }
//}
