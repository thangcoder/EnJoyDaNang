package node.com.enjoydanang.ui.activity.main;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.ColorDialog;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.ui.activity.scan.ScanActivity;
import node.com.enjoydanang.ui.fragment.change_password.ChangePwdFragment;
import node.com.enjoydanang.ui.fragment.contact.ContactUsFragment;
import node.com.enjoydanang.ui.fragment.favorite.FavoriteFragment;
import node.com.enjoydanang.ui.fragment.home.HomeFragment;
import node.com.enjoydanang.ui.fragment.home.HomeTab;
import node.com.enjoydanang.ui.fragment.introduction.IntroductionFragment;
import node.com.enjoydanang.ui.fragment.logcheckin.CheckinHistoryFragment;
import node.com.enjoydanang.ui.fragment.profile.ProfileFragment;
import node.com.enjoydanang.ui.fragment.profile_menu.ProfileMenuFragment;
import node.com.enjoydanang.ui.fragment.search.SearchFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;
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

    //    private CircleImageView imgAvatarUser;
    @BindView(R.id.imgAvatarUser)
    SimpleDraweeView imgAvatarUser;

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
            }

            @Override
            public void onDrawerClosed(View drawerView) {
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
                addFrMenu(HomeFragment.class.getName(), true);

            }
        } else {
            addFrMenu(HomeFragment.class.getName(), true);
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

        String[] titles = hasLogin ? LanguageHelper.getTitleMenuNormal() : LanguageHelper.getTitleMenuNoLogin();

        NavigationAdapter mNavigationAdapter = new NavigationAdapter(this,
                NavigationListItem.getNavigationAdapter(this, lstIndexHeaders, null, icons, titles));

        lvDrawerNav.setAdapter(mNavigationAdapter);
    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
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
            if (!popFragment()) {
                if (Utils.hasLogin()) {
                    DialogUtils.showDialogConfirm(MainActivity.this, Constant.TITLE_WARNING, Utils.getLanguageByResId(R.string.Message_Confirm_Action_Logout),
                            Utils.getString(android.R.string.yes), Utils.getString(android.R.string.cancel),
                            new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog colorDialog) {
                                    GlobalApplication.setUserInfo(null);
                                    finish();
                                }
                            }, new ColorDialog.OnNegativeListener() {
                                @Override
                                public void onClick(ColorDialog colorDialog) {
                                    colorDialog.dismiss();
                                }
                            });
                } else {
                    GlobalApplication.setUserInfo(null);
                    finish();
                }

//                if (exit) {
//                    finish();
//                } else {
//                    Toast.makeText(this, getString(R.string.exit),
//                            Toast.LENGTH_SHORT).show();
//                    exit = true;
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            exit = false;
//                        }
//                    }, 2500);
//                }
            }
        }
    }

    boolean popFragment() {
        boolean isPop = false;

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            Fragment fragment = getActiveFragment();
            if (fragment.getTag().equals(HomeFragment.class.getName())) {
                currentTab = HomeTab.None;
            } else {
                currentTab = HomeTab.Home;
            }
            isPop = true;
            getSupportFragmentManager().popBackStack();
            setStateTabSelected();
        }
        return isPop;
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
                if (currentTab != HomeTab.Home) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());
                    if (fragment == null) {
                        addFrMenu(HomeFragment.class.getName(), true);
                    } else {
                        backToFragment(fragment);
                    }

                }
                currentTab = HomeTab.Home;
                break;
            case R.id.img_search:
                if (currentTab != HomeTab.Search) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(SearchFragment.class.getName());
                    if (fragment == null) {
                        addFrMenu(SearchFragment.class.getName(), true);
                    } else {
                        resurfaceFragment(SearchFragment.class.getName());
                    }
                }
                currentTab = HomeTab.Search;
                break;
            case R.id.ll_profile:
                if (currentTab != HomeTab.Profile && Utils.hasLogin()) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProfileMenuFragment.class.getName());
                    if (fragment == null) {
                        addFrMenu(ProfileMenuFragment.class.getName(), true);
                    } else {
                        resurfaceFragment(ProfileMenuFragment.class.getName());
                    }
                    currentTab = HomeTab.Profile;
                } else {
                    DialogUtils.showDialog(MainActivity.this, 4, Constant.TITLE_WARNING, Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                }
                break;
        }
        setStateTabSelected();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem editItem = menu.findItem(R.id.menu_edit);
        MenuItem scanItem = menu.findItem(R.id.menu_scan);
        switch (currentTab) {
            case Home:
                editItem.setVisible(false);
                scanItem.setVisible(true);
                break;
            case Search:
                editItem.setVisible(false);
                scanItem.setVisible(false);
                break;
            case Profile:
                editItem.setVisible(true);
                scanItem.setVisible(false);
                break;
            case None:
                editItem.setVisible(false);
                scanItem.setVisible(false);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                if(Utils.hasLogin()){
                    startActivity(new Intent(MainActivity.this, ScanActivity.class));
                    overridePendingTransitionEnter();
                }else{
                    DialogUtils.showDialog(MainActivity.this, 4, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                }
                return true;
            case R.id.menu_edit:
                addFr(ProfileFragment.class.getName(), 6);
                currentTab = HomeTab.None;
                return true;
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        setStateTabSelected();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
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

    private void replaceFr(String fragmentTag) {
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        replaceFragment(R.id.container_fragment, fragmentTag, false, null, transitionInfo);
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
                    addFr(CheckinHistoryFragment.class.getName(), position);
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
                    DialogUtils.showDialogConfirm(this, Utils.getLanguageByResId(R.string.Home_Account_Logout),
                            Utils.getLanguageByResId(R.string.Message_Confirm_Action_Logout),
                            Utils.getString(android.R.string.ok),
                            Utils.getString(android.R.string.no),
                            new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog colorDialog) {
                                    mDrawerLayout.closeDrawer(GravityCompat.START);
                                    GlobalApplication.setUserInfo(null);
                                    finish();
                                    overridePendingTransitionExit();
//                                    startActivity(getIntent());
//                                    overridePendingTransition(0, 0);
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
        currentTab = HomeTab.None;
        setStateTabSelected();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setCurrentTab(HomeTab homeTab) {
        currentTab = homeTab;
        setStateTabSelected();
    }

    public Fragment getTopFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(fragmentTag);
    }

    private void setStateFragment() {
        String tag = getTopFragment().getTag();
        if (tag.equals(HomeFragment.class.getName())) {
            currentTab = HomeTab.Home;
        } else if (tag.equals(ProfileMenuFragment.class.getName())) {
            currentTab = HomeTab.Profile;
        } else {
            currentTab = HomeTab.None;
        }
        setStateTabSelected();
    }

    public void setStateTabSelected() {
        switch (currentTab) {
            case Home:
                imgHome.setImageResource(R.drawable.tab1_selected_3x);
                imgSearch.setImageResource(R.drawable.tab2_default_3x);
                imgProfile.setImageResource(R.drawable.tab3_default_3x);
                getToolbar().setTitle(Utils.getLanguageByResId(R.string.Home).toUpperCase());
                break;
            case Search:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_selected_3x);
                imgProfile.setImageResource(R.drawable.tab3_default_3x);
                getToolbar().setTitle(Utils.getLanguageByResId(R.string.Home_Search).toUpperCase());
                break;
            case Profile:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_default_3x);
                imgProfile.setImageResource(R.drawable.tab3_selected_3x);
                getToolbar().setTitle(Utils.getLanguageByResId(R.string.Home_Account_Profile).toUpperCase());
                break;
            case None:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_default_3x);
                imgProfile.setImageResource(R.drawable.tab3_default_3x);
                break;
            default:
                break;
        }

    }

    private void replaceFr(String fragmentTag, int position) {
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        lvDrawerNav.setItemChecked(position, true);
        replaceFragment(R.id.container_fragment, fragmentTag, false, null, transitionInfo);
    }

    private void addFrMenu(String fragmentTag, boolean isBackStack) {
//        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        addFragment(R.id.container_fragment, fragmentTag, isBackStack, null, null);
    }

    private void addFr(String fragmentTag, int position) {
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, R.anim.slide_to_left, R.anim.slide_up_in, R.anim.slide_to_left);
        if (position <= LOGOUT) {
            lvDrawerNav.setItemChecked(position, true);
        }
        Fragment fragment = getActiveFragment();
        String tag = fragment.getTag();
        if (tag.equals(HomeFragment.class.getName())) {
            addFragment(R.id.container_fragment, fragmentTag, true, null, transitionInfo);
        } else {
            replaceFragment(R.id.container_fragment, fragmentTag, true, null, transitionInfo);
        }
    }

    public boolean resurfaceFragment(String tag) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(tag);
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null) {
            for (int i = 0; i < manager.getFragments().size(); i++) {
                Fragment f = manager.getFragments().get(i);
                transaction.hide(f);

            }
            transaction.show(fragment).commit();
            return true;
        }

        return false;
    }

    public void backToFragment(final Fragment fragment) {
        getSupportFragmentManager().popBackStack(
                fragment.getClass().getName(), 0);
    }

    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}
