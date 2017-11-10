package node.com.enjoydanang.utils.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: Tavv
 * Created on 10/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumViewPager extends ViewPager {

    private boolean isPagingEnabled;

    public AlbumViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isPagingEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);

    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof TouchImageViewExtended) {
            //
            // canScrollHorizontally is not supported for Api < 14. To get around this issue,
            // ViewPager is extended and canScrollHorizontallyFroyo, a wrapper around
            // canScrollHorizontally supporting Api >= 8, is called.
            //
            return ((TouchImageViewExtended) v).canScrollHorizontallyFroyo(-dx);

        } else {
            return super.canScroll(v, checkV, dx, x, y);
        }
    }
}
