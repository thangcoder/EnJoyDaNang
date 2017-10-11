package node.com.enjoydanang.ui.fragment.detail;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;

/**
 * Author: Tavv
 * Created on 11/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailHomeFragment extends MvpFragment<DetailHomePresenter> implements iDetailHomeView, TabLayout.OnTabSelectedListener{

    //This is our tablayout
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    //This is our viewPager
    @BindView(R.id.viewpager)
    ViewPager viewPager;

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
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.album_tab_name)));
        //Creating our pager adapter
        DetailPagerAdapter adapter = new DetailPagerAdapter(mFragmentManager, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

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

}

