package node.com.enjoydanang.ui.activity.main;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.ui.fragment.change_password.ChangePwdFragment;
import node.com.enjoydanang.ui.fragment.contact.ContactUsFragment;
import node.com.enjoydanang.ui.fragment.favorite.FavoriteFragment;
import node.com.enjoydanang.ui.fragment.home.HomeFragment;
import node.com.enjoydanang.ui.fragment.home.HomeTab;
import node.com.enjoydanang.ui.fragment.introduction.IntroductionFragment;
import node.com.enjoydanang.ui.fragment.personal.PersonalFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends MvpActivity<MainPresenter> implements MainView,AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private final int INTRODUCTION = 1;
    private final int CONTACT_US = 2;
    private final int FAVORITE = 3;
    private final int LOG_CHECKIN = 4;
    private final int CHANGE_PROFILE = 6;
    private final int CHANGE_PASSWORD = 7;
    private final int LOGOUT = 8;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.left_drawer)
    NavigationView mNavigationView;
    @BindView(R.id.img_home)
    ImageView imgHome;

    @BindView(R.id.container_fragment)
    FrameLayout frlContainer;

    @BindView(R.id.img_search)
    ImageView imgSearch;

    @BindView(R.id.img_profile)
    ImageView imgProfile;

    @BindView(R.id.lv_drawer_nav)
    ListView lvDrawerNav;

    private CircleImageView imgAvatarUser;

    private TextView txtFullName;

    private TextView txtEmail;

    private HomeTab currentTab;

    private DrawerLayout mDrawerLayout;

    private CharSequence mTitle;

    private CharSequence mDrawerTitle;

    private String[] navMenuTitles;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }


    @Override
    public void init() {
        initToolbar(toolbar);
        setToolbar(toolbar);
        navMenuTitles = getResources().getStringArray(R.array.item_menu);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                setTitle(mTitle);
                //Setting, Refresh and Rate App
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        currentTab = HomeTab.getCurrentTab(0);
        setStateTabSelected();

        mvpPresenter.loadInfoUserMenu(this, imgAvatarUser, txtFullName, txtEmail);
        requestCodeQRCodePermissions();

        DrawerAdapter drawerAdapter = new DrawerAdapter(this);
        lvDrawerNav.setAdapter(drawerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        replaceFr(HomeFragment.class.getName(), 0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
        imgAvatarUser = (CircleImageView) mNavigationView.findViewById(R.id.imgAvatarUser);
        txtFullName = (TextView) mNavigationView.findViewById(R.id.txtFullName);
        txtEmail = (TextView) mNavigationView.findViewById(R.id.txtEmail);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }

    @Override
    public void setValue(Bundle savedInstanceState) {

    }

    @Override
    public void setEvent() {
        lvDrawerNav.setOnItemClickListener(this);
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                break;
            case R.id.menu_scan:
                break;
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setStateTabSelected() {
        switch (currentTab) {
            case Home:
                imgHome.setImageResource(R.drawable.tab1_selected_3x);
                imgSearch.setImageResource(R.drawable.tab2_default_3x);
                imgProfile.setImageResource(R.drawable.tab3_default_3x);
                break;
            case Search:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_selected_3x);
                imgProfile.setImageResource(R.drawable.tab3_default_3x);
                break;
            case Profile:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_default_3x);
                imgProfile.setImageResource(R.drawable.tab3_selected_3x);
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
    private void replaceFr(String fragmentTag, int position){
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        lvDrawerNav.setItemChecked(position, true);
        setTitle(navMenuTitles[position]);
        replaceFragment(R.id.container_fragment, fragmentTag, true, null, transitionInfo);
    }

    private void addFr(String fragmentTag, int position){
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, R.anim.slide_to_left, R.anim.slide_up_in, R.anim.slide_to_left);
        lvDrawerNav.setItemChecked(position, true);
        setTitle(navMenuTitles[position]);
        addFragment(R.id.container_fragment, fragmentTag, true, null, transitionInfo);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                break;
            case INTRODUCTION:
                addFr(IntroductionFragment.class.getName(), position);
                break;
            case CONTACT_US:
                addFr(ContactUsFragment.class.getName(), position);
                break;
            case FAVORITE:
                addFr(FavoriteFragment.class.getName(), position);
                break;
            case LOG_CHECKIN:
                break;
            case 5:
                break;
            case CHANGE_PROFILE:
                addFr(PersonalFragment.class.getName(), position);
                break;
            case CHANGE_PASSWORD:
                addFr(ChangePwdFragment.class.getName(), position);
                break;
            case LOGOUT:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

}
