package apps.test.kanj.barcodetrial;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by kanj on 11/5/16.
 */
public class SwipeButton extends Button {
    private static final int GRADIENT_COLOURS[] = {0xFF71EFED, 0xFF5bc0be, 0xFF489998};
    private static final float GRADIENT_POSITIONS[] = {0, 0.5f, 1};
    private static final int GRADIENT_COLOUR_2_WIDTH = 50;
    private static final double SWIPE_CONFIRM_FRACTION = 0.6;

    private OnSwipeListener mListener;

    private float x1, swipeStartX;
    private boolean swiping, thresholdCrossed, swipeTextShown;
    private String originalButtonText;

    private ValueAnimator animation;

    public SwipeButton(Context context) {
        super(context);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public interface OnSwipeListener {
        void onSwipe();
    }

    public void setOnSwipeListener(OnSwipeListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(3000);
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
                int width = SwipeButton.this.getWidth();
                float start = fraction * width;
                float end = ((start+GRADIENT_COLOUR_2_WIDTH > width) ? width : start+GRADIENT_COLOUR_2_WIDTH);
                Shader shader = new LinearGradient(start, 0, end, 0,
                        GRADIENT_COLOURS,
                        GRADIENT_POSITIONS,
                        Shader.TileMode.CLAMP);
                mDrawable.getPaint().setShader(shader);
                SwipeButton.this.setBackground(mDrawable);
            }
        });
        animation.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Initialise everything
                x1 = event.getX();
                originalButtonText = this.getText().toString();
                thresholdCrossed = false;
                animation.cancel();
                if (!swipeTextShown) {
                    this.setText(">> Swipe >>");
                    swipeTextShown = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float x2 = event.getX();

                if (!swiping) {
                    swipeStartX = x2;
                    swiping = true;
                }

                if (x2 > x1 && !thresholdCrossed) {
                    this.setBackground(null);
                    ShapeDrawable mDrawable = new ShapeDrawable(new RectShape());
                    Shader shader = new LinearGradient(x2, 0, x2 - GRADIENT_COLOUR_2_WIDTH, 0,
                            GRADIENT_COLOURS,
                            GRADIENT_POSITIONS,
                            Shader.TileMode.CLAMP);
                    mDrawable.getPaint().setShader(shader);
                    this.setBackground(mDrawable);

                    if (!swipeTextShown) {
                        this.setText(">> Swipe >>");
                        swipeTextShown = true;
                    }

                    if ((x2 - swipeStartX) > this.getWidth() * SWIPE_CONFIRM_FRACTION) {
                        if (mListener != null) {
                            mListener.onSwipe();
                            reset();
                        }
                        thresholdCrossed = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                reset();
                break;

        }

        return super.onTouchEvent(event);
    }

    private void reset() {
        swiping = false;
        this.setText(originalButtonText);
        swipeTextShown = false;
        //setBackground(originalBackground);

        animation.start();

    }
}
