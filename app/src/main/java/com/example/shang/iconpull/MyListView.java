package com.example.shang.iconpull;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Shang on 2017/10/17.
 */
public class MyListView extends ListView {
    private static final String TAG = "TAG";
    private int mOriginalHeight;
    private int drawableHeight;
    private ImageView mImage;

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    /**
     * 设置ImageView图片, 拿到引用
     * @param mImage
     */
    public void setParallaxImage(ImageView mImage) {
        this.mImage = mImage;
        mOriginalHeight = mImage.getHeight()+1;// 160
        mOriginalHeight -= 1;
        drawableHeight = mImage.getDrawable().getIntrinsicHeight(); // 488
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY,
                                   int scrollX, int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if(isTouchEvent && deltaY < 0){
            // 把拉动的瞬时变化量的绝对值交给Header, 就可以实现放大效果
            if(mImage.getHeight() <= drawableHeight){
                int newHeight = (int) (mImage.getHeight() + Math.abs(deltaY / 3.0f));

                // 高度不超出图片最大高度时,才让其生效
                mImage.getLayoutParams().height = newHeight;
                mImage.requestLayout();
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                final int startHeight = mImage.getHeight();
                final int endHeight = mOriginalHeight;
				valueAnimator(startHeight, endHeight);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void valueAnimator(final int startHeight, final int endHeight) {
        ValueAnimator mValueAnim = ValueAnimator.ofInt(1);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator mAnim) {
                float fraction = mAnim.getAnimatedFraction();
                Integer newHeight = evaluate(fraction, startHeight, endHeight);
                mImage.getLayoutParams().height = newHeight;
                mImage.requestLayout();
            }
        });

        mValueAnim.setInterpolator(new OvershootInterpolator());
        mValueAnim.setDuration(500);
        mValueAnim.start();
    }

    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int)(startInt + fraction * (endValue - startInt));
    }
}
