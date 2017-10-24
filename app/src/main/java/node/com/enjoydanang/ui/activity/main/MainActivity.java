package node.com.enjoydanang.ui.activity.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.ui.fragment.home.HomeFragment;
import node.com.enjoydanang.ui.fragment.home.HomeTab;
import node.com.enjoydanang.ui.fragment.personal.PersonalFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView, NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.left_drawer)
    NavigationView navigationView;
    @BindView(R.id.img_home)
    ImageView imgHome;
    @BindView(R.id.tv_home)
    TextView tvHome;

    @BindView(R.id.img_search)
    ImageView imgSearch;

    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.tv_profile)
    TextView tvProfile;
    @BindView(R.id.lv_drawer_nav)
    ListView lvDrawerNav;


    private HomeTab currentTab;
    private DrawerAdapter drawerAdapter;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void init() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        currentTab = HomeTab.getCurrentTab(0);
        setStateTabSelected();

        requestCodeQRCodePermissions();

        drawerAdapter = new DrawerAdapter(this);
        lvDrawerNav.setAdapter(drawerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
//        replaceFragment(R.id.container_fragment, HomeFragment.class.getName(), false, null, transitionInfo);
        replaceFr(HomeFragment.class.getName());
    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {

    }

    @Override
    public void setEvent() {

    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        switch (id){
//            case R.id.history_check_in:
//                break;
//            case R.id.personal_info:
//                replaceFr(PersonalFragment.class.getName());
//                break;
//            case R.id.nav_info:
//                break;
//            case R.id.change_pwd:
//                break;
//            case R.id.introduction:
//                break;
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @OnClick({R.id.ll_home, R.id.ll_profile, R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                currentTab = HomeTab.Home;
                setStateTabSelected();
                break;
            case R.id.img_search:
                currentTab = HomeTab.Search;
                setStateTabSelected();
                break;
            case R.id.ll_profile:
                currentTab = HomeTab.Profile;
                setStateTabSelected();
                break;
            default:
                setStateTabSelected();
                break;
        }


    }

    public void setStateTabSelected() {
        switch (currentTab) {
            case Home:
                imgHome.setImageResource(R.drawable.tab1_selected_3x);
                imgSearch.setImageResource(R.drawable.tab2_default_3x);
                imgProfile.setImageResource(R.drawable.tab3_default_3x);
                tvHome.setTextColor(ContextCompat.getColor(this, R.color.tab_select));
                tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
            case Search:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_selected_3x);
                imgProfile.setImageResource(R.drawable.tab3_default_3x);
                tvProfile.setTextColor(ContextCompat.getColor(this, R.color.black));
                tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
            case Profile:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_default_3x);
                imgProfile.setImageResource(R.drawable.tab3_selected_3x);
                tvProfile.setTextColor(ContextCompat.getColor(this, R.color.tab_select));
                tvHome.setTextColor(ContextCompat.getColor(this, R.color.black));
                break;
            default:
                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Scanning a two-dimensional code requires permission to open the camera and the astigmatism", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }
    private void replaceFr(String fragmentTag){
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        replaceFragment(R.id.container_fragment, fragmentTag, false, null, transitionInfo);
    }
}
