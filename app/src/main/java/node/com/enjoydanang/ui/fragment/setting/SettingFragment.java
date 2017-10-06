package node.com.enjoydanang.ui.fragment.setting;

import node.com.enjoydanang.MvpFragmentWithToolbar;
import android.view.View;

import butterknife.ButterKnife;

import node.com.enjoydanang.R;
import rx.Subscription;

/**
 * Created by chientruong on 1/5/17.
 */

public class SettingFragment extends MvpFragmentWithToolbar<SettingPresenter> implements SettingView {
    static SettingFragment settingFragment;
    private Subscription chatSubscription;

//    @BindView(R.id.mrsHelp)
//    MyRowSetting mrsHelp;
//    @BindView(R.id.mrsUsingTerm)
//    MyRowSetting mrsUsingTerm;
//    @BindView(R.id.mrsPolicy)
//    MyRowSetting mrsPolicy;
//    @BindView(R.id.mrsSetting)
//    MyRowSetting mrsSetting;
//    @BindView(R.id.mrsVersion)
//    MyRowSetting mrsVersion;
//    @BindView(R.id.mrsLogout)
//    MyRowSetting mrsLogout;

    public static SettingFragment getInstall() {
        if (settingFragment == null) {
            settingFragment = new SettingFragment();
        }
        return settingFragment;
    }

    @Override
    protected void init(View view) {
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected SettingPresenter createPresenter() {
        return new SettingPresenter(this);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void setupActionBar() {
        mToolbarTilte.setText("Title");
        mToolbar.setVisibility(View.GONE);
        mRightToolbar.setVisibility(View.GONE);

    }



}
