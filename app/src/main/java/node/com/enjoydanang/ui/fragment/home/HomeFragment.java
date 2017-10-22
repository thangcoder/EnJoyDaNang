package node.com.enjoydanang.ui.fragment.home;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.MenuItem;
import node.com.enjoydanang.model.Products;

import static node.com.enjoydanang.ui.fragment.home.MenuAdapter.*;

/**
 * Created by chien on 10/8/17.
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements iHomeView, OnItemClick {


    @BindView(R.id.rcv_product)
    RecyclerView rcvProduct;
    @BindView(R.id.grv_menu)
    GridView gridView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;


    private MenuAdapter homeAdapter;
    private List<MenuItem> menuItems;
    private ArrayList<Products> products;
    private ProductAdapter productAdapter;
    private LinearLayoutManager mLayoutManager;
    private FragmentStatePagerAdapter adapter;
    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }


    @Override
    protected void init(View view) {

        /**
         * Init Data Product list
         */

        rcvProduct.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rcvProduct.setLayoutManager(mLayoutManager);


    }

    @Override
    protected void setEvent(View view) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setBackgroundColor(ContextCompat.getColor(mMainActivity, R.color.color_gray));
                homeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpPresenter.getCategorys(0);
        mvpPresenter.getBanner();

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }




    @Override
    public void getCategorysFinish(List<Category> products) {
        homeAdapter = new MenuAdapter(mMainActivity,products,this);
        gridView.setAdapter(homeAdapter);
    }

    @Override
    public void getCategorysFail() {

    }

    @Override
    public void getListByCategoryFinish(List<Category> products) {
        productAdapter = new ProductAdapter(mMainActivity,products);
        rcvProduct.setAdapter(productAdapter);
    }


    @Override
    public void getListByCategoryFail() {

    }

    @Override
    public void getBannerFinish(List<Banner> banner) {
        // init viewpager adapter and attach
        adapter = new ViewPagerAdapter(getFragmentManager(),banner);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void getBannerFail() {

    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    public void onItemClick(int id, int position) {
        for (int i = 0 ; i < gridView.getCount() ; i++){
            View v = gridView.getChildAt(i);
            if(i==position){
                v.setBackgroundColor(ContextCompat.getColor(mMainActivity, R.color.boder));

            }else{
                v.setBackgroundColor(ContextCompat.getColor(mMainActivity, R.color.white));

            }
        }

        mvpPresenter.getListByCategorys(id,0);
    }
    @OnClick({R.id.prev, R.id.next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                if (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                break;
            case R.id.prev:
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
                break;
        }
    }
}
