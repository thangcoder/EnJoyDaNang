package node.com.enjoydanang.ui.fragment.introduction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import node.com.enjoydanang.MvpFragmentWithToolbar;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.model.Introduction;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class IntroductionFragment extends MvpFragmentWithToolbar<IntroductionPresenter> implements IntroductionView {
    private static final String TAG = IntroductionFragment.class.getSimpleName();


    @Override
    protected IntroductionPresenter createPresenter() {
        return new IntroductionPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        mvpPresenter.getIntroduction();
    }

    @Override
    public void setupActionBar() {

    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_introduction;
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

    @Override
    public void onGetIntroductionSuccess(BaseRepository<Introduction> data) {
        Log.i(TAG, "onGetIntroductionSuccess " + data);
    }

    @Override
    public void onLoadFailure(AppError error) {
        Log.e(TAG, "onLoadFailure " + error.getMessage());
    }
}
