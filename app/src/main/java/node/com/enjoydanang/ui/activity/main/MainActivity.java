package node.com.enjoydanang.ui.activity.main;

import android.Manifest;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.ColorDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.ui.fragment.change_password.ChangePwdFragment;
import node.com.enjoydanang.ui.fragment.contact.ContactUsFragment;
import node.com.enjoydanang.ui.fragment.favorite.FavoriteFragment;
import node.com.enjoydanang.ui.fragment.home.HomeFragment;
import node.com.enjoydanang.ui.fragment.home.HomeTab;
import node.com.enjoydanang.ui.fragment.introduction.IntroductionFragment;
import node.com.enjoydanang.ui.fragment.profile.ProfileFragment;
import node.com.enjoydanang.ui.fragment.review.write.WriteReviewDialog;
import node.com.enjoydanang.ui.fragment.review.write.WriteReviewFragment;
import node.com.enjoydanang.ui.fragment.search.SearchFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends MvpActivity<MainPresenter> implements MainView, AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener, EasyPermissions.PermissionCallbacks {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private final int INTRODUCTION = 1;
    private final int CONTACT_US = 2;
    private final int FAVORITE = 3;
    private final int LOG_CHECKIN = 4;
    private final int CHANGE_PROFILE = 6;
    private final int CHANGE_PASSWORD = 7;
    private final int LOGOUT = 8;
    private final int LOGIN = 4;
    private boolean isOpen;
    private final String IS_OPEN = "IS_OPEN";
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
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                //Setting, Refresh and Rate App
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
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

        settingLeftMenu(Utils.hasLogin());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isOpen = (boolean) savedInstanceState.getSerializable(IS_OPEN);
            if (!isOpen) {
                addFr(HomeFragment.class.getName(), 0);

            }
        } else {
            addFr(HomeFragment.class.getName(), 0);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(IS_OPEN, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void settingLeftMenu(boolean hasLogin) {
        List<Integer> lstIndexHeaders = new ArrayList<>(Arrays.asList(hasLogin ? Constant.INDEX_HEADER_NORMAL : Constant.INDEX_HEADER_NO_LOGIN));

        int[] icons = hasLogin ? Constant.ICON_MENU_NORMAL : Constant.ICON_MENU_NO_LOGIN;

        String[] titles = hasLogin ? getResources().getStringArray(R.array.item_menu) : getResources().getStringArray(R.array.item_no_sign_in);

        NavigationAdapter mNavigationAdapter = new NavigationAdapter(this,
                NavigationListItem.getNavigationAdapter(this, lstIndexHeaders, null, icons, titles));

        lvDrawerNav.setAdapter(mNavigationAdapter);
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
            int countFragment = getSupportFragmentManager().getBackStackEntryCount();
            if (countFragment > 1) {
                super.onBackPressed();
            } else {
                this.finish();
            }
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
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, R.anim.slide_to_left, R.anim.slide_up_in, R.anim.slide_to_left);
        switch (item.getItemId()) {
            case R.id.menu_search:
                addFragment(R.id.container_fragment, SearchFragment.class.getName(), true, null, transitionInfo);
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

    private void replaceFr(String fragmentTag, int position) {
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        lvDrawerNav.setItemChecked(position, true);
        setTitle(navMenuTitles[position]);
        replaceFragment(R.id.container_fragment, fragmentTag, true, null, transitionInfo);
    }

    private void addFr(String fragmentTag, int position) {
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, R.anim.slide_to_left, R.anim.slide_up_in, R.anim.slide_to_left);
        lvDrawerNav.setItemChecked(position, true);
        addFragment(R.id.container_fragment, fragmentTag, true, null, transitionInfo);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (Utils.hasLogin()) {
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
                    addFr(ProfileFragment.class.getName(), position);
                    break;
                case CHANGE_PASSWORD:
                    addFr(ChangePwdFragment.class.getName(), position);
                    break;
                case LOGOUT:
                    DialogUtils.showDialogConfirm(this, Utils.getString(R.string.logout),
                            Utils.getString(R.string.msg_confirm_logout),
                            Utils.getString(android.R.string.ok),
                            Utils.getString(android.R.string.no),
                            new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog colorDialog) {
                                    GlobalApplication.setUserInfo(null);
                                    finish();
                                    overridePendingTransition( 0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition( 0, 0);
                                }
                            }, new ColorDialog.OnNegativeListener() {
                                @Override
                                public void onClick(ColorDialog colorDialog) {
                                    colorDialog.dismiss();
                                }
                            });
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    break;
                case INTRODUCTION:
                    addFr(IntroductionFragment.class.getName(), position);
                    break;
                case CONTACT_US:
                    addFr(ContactUsFragment.class.getName(), position);
                    break;
                case LOGIN:
                    finish();
                    break;
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

}
