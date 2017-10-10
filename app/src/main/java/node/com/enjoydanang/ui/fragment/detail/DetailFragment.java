package node.com.enjoydanang.ui.fragment.detail;

import android.view.View;

import node.com.enjoydanang.MvpFragment;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailFragment extends MvpFragment<DetailPresenter> implements iDetailView{
    @Override
    protected DetailPresenter createPresenter() {
        return new DetailPresenter(this);
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return 0;
    }

    @Override
    public void bindView(View view) {

    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }
}
