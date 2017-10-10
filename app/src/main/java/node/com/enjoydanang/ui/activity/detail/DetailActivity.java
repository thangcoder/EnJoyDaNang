package node.com.enjoydanang.ui.activity.detail;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.ui.fragment.detail.DetailPagerAdapter;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailActivity extends MvpActivity<DetailActivityPresenter> implements iDetailView, TabLayout.OnTabSelectedListener{

    //This is our tablayout
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    //This is our viewPager
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected DetailActivityPresenter createPresenter() {
        return null;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_detail);
    }

    @Override
    public void init() {
        setSupportActionBar(toolbar);
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.detail_tab_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.menu_tab_name)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.review_tab_name)));

        //Creating our pager adapter
        DetailPagerAdapter adapter = new DetailPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        //Adding onTabSelectedListener to swipe views

    }

    @Override
    public void bindViews() {

    }

    @Override
    public void setValue(Bundle savedInstanceState) {

    }

    @Override
    public void setEvent() {
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
