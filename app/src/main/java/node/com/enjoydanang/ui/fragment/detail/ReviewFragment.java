package node.com.enjoydanang.ui.fragment.detail;

import android.view.View;

import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ReviewFragment extends MvpFragment<ReviewPresenter> implements iReviewView{
    @Override
    protected ReviewPresenter createPresenter() {
        return null;
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_review;
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
