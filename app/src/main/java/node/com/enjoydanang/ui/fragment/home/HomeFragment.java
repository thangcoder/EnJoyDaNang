package node.com.enjoydanang.ui.fragment.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.MenuItem;
import node.com.enjoydanang.model.Products;
import node.com.enjoydanang.ui.fragment.setting.SettingPresenter;

/**
 * Created by chien on 10/8/17.
 */

public class HomeFragment extends MvpFragment<HomePresenter> implements iHomeView {


    @BindView(R.id.rcv_product)
    RecyclerView rcvProduct;
    @BindView(R.id.grv_menu)
    GridView gridView;
    private MenuAdapter homeAdapter;
    private List<MenuItem> menuItems;
    private ArrayList<Products> products;
    private ProductAdapter productAdapter;
    private LinearLayoutManager mLayoutManager;
    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }


    @Override
    protected void init(View view) {
        menuItems = new ArrayList<>();
        MenuItem menuItem1 =new MenuItem("Tranfer","");
        MenuItem menuItem2 =new MenuItem("Card Repay","");
        MenuItem menuItem3 =new MenuItem("Phone Topup","");
        MenuItem menuItem4 =new MenuItem("Air Forture","");
        menuItems.add(menuItem1);
        menuItems.add(menuItem2);
        menuItems.add(menuItem3);
        menuItems.add(menuItem4);
        menuItems.add(menuItem1);
        menuItems.add(menuItem2);
        menuItems.add(menuItem3);
        menuItems.add(menuItem4);
        homeAdapter = new MenuAdapter(mMainActivity,menuItems);
        gridView.setAdapter(homeAdapter);

        /**
         * Init Data Product list
         */

        rcvProduct.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rcvProduct.setLayoutManager(mLayoutManager);
        Products product1 = new Products();
        products = new ArrayList<>();
        products.add(product1);
        products.add(product1);
        products.add(product1);
        products.add(product1);
        productAdapter = new ProductAdapter(mMainActivity,products);
        rcvProduct.setAdapter(productAdapter);

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mvpPresenter.getCategorys(0);

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
    public void getMenuFinish(List<MenuItem> menuList) {

    }

    @Override
    public void getMenuFail() {

    }

    @Override
    public void getCategorysFinish(Category products) {

    }

    @Override
    public void getCategorysFail() {

    }


    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }
}
