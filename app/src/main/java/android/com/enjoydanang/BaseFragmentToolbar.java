package android.com.enjoydanang;

import android.com.enjoydanang.ui.fragment.BaseFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by tqk666 on 6/13/16.
 */
public abstract class BaseFragmentToolbar extends BaseFragment {

    protected Toolbar mToolbar;
    protected TextView mToolbarLeftTitle;
    protected TextView mToolbarTilte;
    protected TextView mToolbarRightTitle;
    protected ImageView mLeftToolbar;
    protected ImageView mRightToolbar;
    protected View vLine;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = (Toolbar)view.findViewById(R.id.toolbar);
        mToolbarLeftTitle = (TextView) view.findViewById(R.id.title_left);
        mToolbarTilte = (TextView) view.findViewById(R.id.title_toolbar);
        mToolbarRightTitle = (TextView) view.findViewById(R.id.title_right);
        mLeftToolbar = (ImageView) view.findViewById(R.id.left_too_bar);
        mRightToolbar = (ImageView) view.findViewById(R.id.right_tool_bar);
        vLine = (View) view.findViewById(R.id.vLine);
        setupActionBar();
    }

    public abstract void setupActionBar();
}
