package node.com.enjoydanang.utils.helper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import node.com.enjoydanang.R;

/**
 * Author: Tavv
 * Created on 15/01/2018.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class MapFragmentWrapper extends RelativeLayout {

    public interface OnDragListener {
        void onDragStart();

        void onDragEnd();
    }

    private OnDragListener onDragListener;

    private ImageView mMarkImageView = null;

    private View mShadowView = null;

    private RelativeLayout.LayoutParams params;


    public MapFragmentWrapper(Context context) {
        super(context);
        init(context);
    }

    public MapFragmentWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapFragmentWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MapFragmentWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mMarkImageView = new ImageView(context);
        mMarkImageView.setImageResource(R.drawable.ic_pickup);

        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        mShadowView = new View(context);
        mShadowView.setBackgroundResource(R.drawable.map_pin_shadow);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    private void animateUp() {
        if (mMarkImageView != null && mShadowView != null) {
            ObjectAnimator translateY = ObjectAnimator.ofFloat(mMarkImageView,
                    "translationY",
                    -((float) mMarkImageView.getHeight()) / 10);

            ObjectAnimator alphaShadow = ObjectAnimator.ofFloat(mShadowView,
                    "alpha",
                    1f,
                    0.6f);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(translateY, alphaShadow);
            animatorSet.start();
        }
    }

    private void animateDown() {
        if (mMarkImageView != null && mShadowView != null) {
            ObjectAnimator translateYInverse = ObjectAnimator.ofFloat(mMarkImageView,
                    "translationY",
                    (float) mMarkImageView.getHeight() / 25);

            ObjectAnimator alphaShadowInverse = ObjectAnimator.ofFloat(mShadowView,
                    "alpha",
                    0.6f,
                    1f);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(translateYInverse, alphaShadowInverse);
            animatorSet.start();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                animateUp();
                onDragListener.onDragStart();
                break;
            case MotionEvent.ACTION_UP:
                animateDown();
                onDragListener.onDragEnd();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }
}
