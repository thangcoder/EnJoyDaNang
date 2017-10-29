package node.com.enjoydanang.ui.fragment.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Partner;

/**
 * Author: Tavv
 * Created on 11/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailHomeFragment extends MvpFragment<DetailHomePresenter> implements iDetailHomeView, TabLayout.OnTabSelectedListener{
    private static final String TAG = DetailHomeFragment.class.getSimpleName();

    //This is our tablayout
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    //This is our viewPager
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public static DetailHomeFragment newInstance(Partner partner) {
        DetailHomeFragment fragment = new DetailHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected DetailHomePresenter createPresenter() {
        return new DetailHomePresenter(this);
    }

    @Override
    protected void init(View view) {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.detail_tab_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.review_tab_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.schedule_tab_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.album_tab_name)));
        //Creating our pager adapter
        Bundle bundle = getArguments();
        if (bundle != null) {
            Partner partner = (Partner) bundle.getSerializable(TAG);
            if (partner != null) {
                DetailPagerAdapter adapter = new DetailPagerAdapter(mFragmentManager, tabLayout.getTabCount(), partner);
                viewPager.setAdapter(adapter);
            }
        }

        //Adding adapter to pager
    }

    @Override
    protected void setEvent(View view) {
        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_detail_home;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

}

