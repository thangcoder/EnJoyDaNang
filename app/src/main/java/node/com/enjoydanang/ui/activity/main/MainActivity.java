package node.com.enjoydanang.ui.activity.main;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.constant.Extras;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.model.NavigationItem;
import node.com.enjoydanang.model.Popup;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.receiver.LocationReceiver;
import node.com.enjoydanang.service.LocationService;
import node.com.enjoydanang.ui.activity.login.LoginActivity;
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
import node.com.enjoydanang.utils.DateUtils;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.SharedPrefsUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.config.ForceUpdateChecker;
import node.com.enjoydanang.utils.event.OnUpdateProfileSuccess;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.LocationHelper;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView, AdapterView.OnItemClickListener,
        NavigationView.OnNavigationItemSelectedListener, OnUpdateProfileSuccess, ForceUpdateChecker.OnUpdateNeededListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 200;
    private Menu mMenu;
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

    @BindView(R.id.name)
    TextView toolbarName;
    @BindView(R.id.img_scan)
    ImageView imgScan;

    @BindView(R.id.img_back)
    ImageView imgBack;

    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @BindView(R.id.frToolBar)
    FrameLayout frToolBar;

    @BindView(R.id.left_drawer)
    NavigationView mNavigationView;
    @BindView(R.id.img_home)
    ImageView imgHome;

    @BindView(R.id.container_fragment)
    FrameLayout frlContainer;

    @BindView(R.id.img_search)
    ImageView imgSearch;

    @BindView(R.id.img_menu)
    ImageView imgMenu;

    @BindView(R.id.lv_drawer_nav)
    ListView lvDrawerNav;

    @BindView(R.id.imgAvatarUser)
    SimpleDraweeView imgAvatarUser;

    private TextView txtFullName;

    private TextView txtEmail;

    public HomeTab currentTab;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private boolean isExit;

    private boolean hasLogin;

    private boolean mToolBarNavigationListenerIsRegistered;


    private Location mLastLocation;

    private LocationReceiver mLocationReceiver;

    private Intent locationService;

    public LocationService mLocationService;

    private boolean isServiceConnected;

    public LocationHelper mLocationHelper;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void init() {
        setHeightToolbar();
        if (mLocationReceiver == null) {
            mLocationReceiver = new LocationReceiver();
        }
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        currentTab = HomeTab.getCurrentTab(0);
        setStateTabSelected();
        setShowMenuItem(Constant.HIDE_ALL_ITEM_MENU);
        mvpPresenter.loadInfoUserMenu(this, imgAvatarUser, txtFullName, txtEmail);
        if (!disableShowPopup()) {
            mvpPresenter.getPopup();
        }
        if (!checkPermission()) {
            requestPermission();
        } else {
            startTrackLocation();
        }
        settingLeftMenu(Utils.hasLogin());
        hasLogin = Utils.hasLogin();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(IS_OPEN, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GlobalApplication.getGlobalApplicationContext().isHasSessionLogin()) {
            ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        }
        validAndUpdateFullName();
        IntentFilter intentFilter = new IntentFilter(Extras.KEY_RECEIVER_LOCATION_FILTER);
        LocalBroadcastManager
                .getInstance(MainActivity.this).registerReceiver(mLocationReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationReceiver != null) {
            LocalBroadcastManager
                    .getInstance(MainActivity.this).unregisterReceiver(mLocationReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTrackingService();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void settingLeftMenu(boolean hasLogin) {
        List<Integer> lstIndexHeaders = null;
        int[] icons;
        String[] titles;
        List<NavigationItem> navigationItems = null;
        try {
            lstIndexHeaders = new ArrayList<>(Arrays.asList(hasLogin ? Constant.INDEX_HEADER_NORMAL : Constant.INDEX_HEADER_NO_LOGIN));

            icons = hasLogin ? Constant.ICON_MENU_NORMAL : Constant.ICON_MENU_NO_LOGIN;

            titles = hasLogin ? LanguageHelper.getTitleMenuNormal() : LanguageHelper.getTitleMenuNoLogin();

            navigationItems = NavigationListItem.getNavigationAdapter(this, lstIndexHeaders, null, icons, titles);

            if (CollectionUtils.isNotEmpty(navigationItems)) {
                NavigationAdapter mNavigationAdapter = new NavigationAdapter(this, navigationItems);
                lvDrawerNav.setAdapter(mNavigationAdapter);
            }
        } catch (Exception e) {
            lstIndexHeaders = new ArrayList<>(Arrays.asList(this.hasLogin ? Constant.INDEX_HEADER_NORMAL : Constant.INDEX_HEADER_NO_LOGIN));
            icons = this.hasLogin ? Constant.ICON_MENU_NORMAL : Constant.ICON_MENU_NO_LOGIN;
            titles = this.hasLogin ? LanguageHelper.getTitleMenuNormal() : LanguageHelper.getTitleMenuNoLogin();
            if (ArrayUtils.isNotEmpty(titles) && ArrayUtils.isNotEmpty(icons)) {
                navigationItems = NavigationListItem.getNavigationAdapter(this, lstIndexHeaders, null, icons, titles);
                if (CollectionUtils.isNotEmpty(navigationItems)) {
                    NavigationAdapter mNavigationAdapter = new NavigationAdapter(this, navigationItems);
                    lvDrawerNav.setAdapter(mNavigationAdapter);
                }
            } else {
                finish();
                startActivity(getIntent());
            }
        }

    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
        txtFullName = (TextView) mNavigationView.findViewById(R.id.txtFullName);
        txtEmail = (TextView) mNavigationView.findViewById(R.id.txtEmail);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {
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
    public void setEvent() {
        lvDrawerNav.setOnItemClickListener(this);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getActiveFragment();
                if (fragment != null) {
                    String tag = fragment.getTag();
                    if (tag.equals(HomeFragment.class.getName())) {
                        setShowMenuItem(Constant.SHOW_QR_CODE);
                        EventBus.getDefault().post("hasBackFragment");
                        currentTab = HomeTab.Home;
                        lvDrawerNav.clearChoices();
                    } else if (tag.equals(SearchFragment.class.getName())) {
                        setShowMenuItem(Constant.HIDE_ALL_ITEM_MENU);
                        currentTab = HomeTab.Search;
                        lvDrawerNav.clearChoices();
                    } else {
                        currentTab = HomeTab.None;
                        if (tag.equals(ProfileFragment.class.getName())) {
                            lvDrawerNav.setSelection(7);
                        }
                        setShowMenuItem(Constant.SHOW_MENU_BACK);
                    }
                    setStateTabSelected();
                    lvDrawerNav.requestLayout();
                }

            }
        });
    }

    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                Fragment fragment = getTopFragment();
                if (fragment instanceof ProfileFragment) {
                    if (!isUpdatedFullName(fragment)) {
                        DialogUtils.showDialog(fragment.getContext(), DialogType.WARNING,
                                DialogUtils.getTitleDialog(2),
                                Utils.getLanguageByResId(R.string.Home_Account_Fullname_NotEmpty));
                        return;
                    }
                }
                getSupportFragmentManager().popBackStack();
//                enableBackButton(false);
                setShowMenuItem(Constant.SHOW_MENU_BACK);
            } else {
                if (Utils.hasLogin()) {
                    if (isExit) {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, Utils.getLanguageByResId(R.string.Action_DoubleTap),
                                Toast.LENGTH_SHORT).show();
                        isExit = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isExit = false;
                            }
                        }, 2500);
                    }
                } else {
                    GlobalApplication.setUserInfo(null);
                    finish();
                }
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
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @OnClick({R.id.img_home, R.id.img_search, R.id.img_scan, R.id.img_menu, R.id.edit_profile, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                if (currentTab != HomeTab.Home) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());
                    if (fragment == null) {
                        addFrMenu(HomeFragment.class.getName(), true);
                    } else {
                        backToFragment(fragment);
                    }

                }
                break;
            case R.id.img_search:
                if (currentTab != HomeTab.Search) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(SearchFragment.class.getName());
                    if (fragment == null) {
                        addFrMenu(SearchFragment.class.getName(), true);
                    } else {
                        currentTab = HomeTab.Search;
                        resurfaceFragment(fragment, SearchFragment.class.getName());
                    }
                }
                break;
            case R.id.img_menu:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }

                break;
            case R.id.img_scan:
                if (Utils.hasLogin()) {
                    startActivity(new Intent(MainActivity.this, ScanActivity.class));
                    overridePendingTransitionEnter();
                } else {
                    DialogUtils.showDialog(MainActivity.this, DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                }
                break;
            case R.id.edit_profile:
                addFr(ProfileFragment.class.getName(), CHANGE_PROFILE);
                currentTab = HomeTab.None;
                break;
            case R.id.img_back:
                this.onBackPressed();
                break;
        }
    }


    private void replaceFr(String fragmentTag) {
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        replaceFragment(R.id.container_fragment, fragmentTag, false, null, transitionInfo);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (Utils.hasLogin()) {
            if (StringUtils.isNotEmpty(Utils.getUserInfo().getFullName())) {
                if (position != LOGOUT) {
                    setShowMenuItem(Constant.SHOW_BACK_ICON);
                }
                setShowMenuItem(1);
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
                                Utils.getLanguageByResId(R.string.Message_Confirm_Ok),
                                Utils.getLanguageByResId(R.string.Message_Confirm_Cancel),
                                new ColorDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(ColorDialog colorDialog) {
                                        mDrawerLayout.closeDrawer(GravityCompat.START);
                                        GlobalApplication.setUserInfo(null);
                                        hasLogin = false;
                                        validRedirectLogin();
                                        finish();
                                        overridePendingTransitionExit();
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
                lvDrawerNav.setItemChecked(CHANGE_PROFILE, true);
                DialogUtils.showDialog(this, DialogType.WARNING,
                        DialogUtils.getTitleDialog(2),
                        Utils.getLanguageByResId(R.string.Home_Account_Fullname_NotEmpty));
            }
        } else {
            setShowMenuItem(Constant.SHOW_BACK_ICON);
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
                setNameToolbar(Utils.getLanguageByResId(R.string.Home).toUpperCase());
                break;
            case Search:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_selected_3x);
                break;
            case None:
                imgHome.setImageResource(R.drawable.tab1_default_3x);
                imgSearch.setImageResource(R.drawable.tab2_default_3x);
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

    public void addFrMenu(String fragmentTag, boolean isBackStack) {
//        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        addFragment(R.id.container_fragment, fragmentTag, isBackStack, null, null);
    }

    public void addFr(String fragmentTag, int position) {
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

    public void resurfaceFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
        addFrMenu(tag, true);
    }

    public void backToFragment(final Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        getSupportFragmentManager().popBackStack(
                fragment.getClass().getName(), 0);
        transaction.show(fragment).commit();

    }

    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    public void refreshHeader() {
        mvpPresenter.loadInfoUserMenu(this, imgAvatarUser, txtFullName, txtEmail);
    }


    private boolean checkPermission() {
        int resultCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        int resultWrite = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int resultRead = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultLocation = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int resultCroarse = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultWrite == PackageManager.PERMISSION_GRANTED && resultRead == PackageManager.PERMISSION_GRANTED
                && resultLocation == PackageManager.PERMISSION_GRANTED &&
                resultCroarse == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeAccepted && readAccepted) {
                        startTrackLocation();
                    } else {
                        DialogUtils.showDialog(MainActivity.this, DialogType.WARNING, DialogUtils.getTitleDialog(2),
                                Utils.getLanguageByResId(R.string.Permission_Request_CAMERA_WRITE_READ));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                DialogUtils.showDialog(MainActivity.this,
                                        DialogType.INFO,
                                        Utils.getLanguageByResId(R.string.Permisstion_Title),
                                        Utils.getLanguageByResId(R.string.Permission_Request_Content),
                                        new PromptDialog.OnPositiveListener() {
                                            @Override
                                            public void onClick(PromptDialog promptDialog) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermission();
                                                }
                                            }
                                        }
                                );
                                return;
                            }
                        }

                    }
                }

                break;
        }
    }

    private void validAndUpdateFullName() {
        if (Utils.hasLogin()) {
            UserInfo userInfo = GlobalApplication.getUserInfo();
            if (StringUtils.isEmpty(userInfo.getFullName())) {
                DialogUtils.showDialog(this, DialogType.INFO, DialogUtils.getTitleDialog(2),
                        Utils.getLanguageByResId(R.string.Message_Update_FullName),
                        new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog promptDialog) {
                                promptDialog.dismiss();
                                addFr(ProfileFragment.class.getName(), CHANGE_PROFILE);
                            }
                        });
            }
        }
    }

    private boolean isUpdatedFullName(Fragment fragment) {
        return StringUtils.isNotEmpty(GlobalApplication.getUserInfo().getFullName())
                && !((ProfileFragment) fragment).isEmptyFullName();
    }

    public void setNameToolbar(String name) {
        toolbarName.setText(name);
    }


    /**
     * 1:Show Scan Menu Item
     * 2: Show Edit Menu Item
     * 3: Hide All Menu item
     *
     * @param type
     */
    public void setShowMenuItem(int type) {
        switch (type) {
            case Constant.SHOW_MENU_BACK:
                imgScan.setVisibility(View.VISIBLE);
                installNav(Constant.SHOW_BACK_ICON);
                break;
            case Constant.SHOW_QR_CODE:
                imgScan.setVisibility(View.VISIBLE);
                installNav(Constant.HIDE_BACK_ICON);
                break;
            case Constant.HIDE_ALL_ITEM_MENU:
                imgScan.setVisibility(View.GONE);
                installNav(Constant.SHOW_BACK_ICON);
                break;
        }
    }

    /**
     * Show/hide back icon
     *
     * @param type
     */
    public void installNav(int type) {
        switch (type) {
            case Constant.HIDE_BACK_ICON:
                imgBack.setVisibility(View.GONE);
                if (toolbarName.getText().toString().equalsIgnoreCase(Constant.TITLE_HOME_VN)) {
                    toolbarName.setVisibility(View.GONE);
                } else {
                    toolbarName.setVisibility(View.VISIBLE);
                }
                break;
            case Constant.SHOW_BACK_ICON:
                imgBack.setVisibility(View.VISIBLE);
                toolbarName.setVisibility(View.GONE);
                break;
        }
    }

    private void setHeightToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frToolBar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
    }

    private void validRedirectLogin() {
        if (GlobalApplication.getGlobalApplicationContext().isHasSessionLogin()) {
            GlobalApplication.getGlobalApplicationContext().setHasSessionLogin(false);
            SharedPrefsUtils.removeVariableFromPrefs(Constant.SHARED_PREFS_NAME, Constant.KEY_EXTRAS_USER_INFO);
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        DialogUtils.showDialog(MainActivity.this, DialogType.WARNING, Utils.getLanguageByResId(R.string.Message_Warning_Version_Title),
                Utils.getLanguageByResId(R.string.Message_Confirm_Update_Title), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog promptDialog) {
                        promptDialog.dismiss();
                        Utils.redirectStore(MainActivity.this, updateUrl);
                    }
                });
    }

    public void enableBackButton(boolean enable) {
        if (enable) {
            // Remove hamburger
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            // Show back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            // Remove back button
//            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            mDrawerToggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }

    }

    @Override
    public void onShowPopup(Repository<Popup> response) {
        showPopupMain(response.getData().get(0));
    }

    @Override
    public void onError(AppError error) {
        Log.e(TAG, "onError " + error.getMessage());
    }

    private void showPopupMain(final Popup popup) {
        if (popup == null) return;
        if (popup.getIsPublish() == 1) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.dialog_home, null);
            dialogBuilder.setView(dialogView);
//        AppCompatButton btnViewDetail = (AppCompatButton) dialogView.findViewById(R.id.btnPopupLeft);
            final AppCompatButton btnHide = (AppCompatButton) dialogView.findViewById(R.id.btnPopupCenter);
            AppCompatButton btnClose = (AppCompatButton) dialogView.findViewById(R.id.btnPopupRight);

//        btnViewDetail.setText(popup.getTextButtonLeft());
            btnHide.setText(popup.getTextButtonCenter());
            btnClose.setText(popup.getTextButtonRight());

            SimpleDraweeView imgPopup = (SimpleDraweeView) dialogView.findViewById(R.id.imgPopup);

            ImageUtils.loadImageWithFreso(imgPopup, popup.getImage());

            final AlertDialog alertDialog = dialogBuilder.create();

            alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(false);

//        btnViewDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(popup.getHref().length() > 1){
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(popup.getHref()));
//                    startActivity(intent);
//                }
//            }
//        });

            btnHide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPrefsUtils.addDataToPrefs(Constant.SHARED_PREFS_NAME, Constant.KEY_EXTRAS_CLOSE_POPUP, false);
                    alertDialog.dismiss();
                }
            });
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentTime = DateUtils.getCurrentTime();
                    SharedPrefsUtils.addDataToPrefs(Constant.SHARED_PREFS_NAME, Constant.KEY_EXTRAS_CLOSE_POPUP, true);
                    SharedPrefsUtils.addDataToPrefs(Constant.SHARED_PREFS_NAME, Constant.KEY_EXTRAS_DATE_CLOSE_POPUP, currentTime);
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    private boolean disableShowPopup() {
        String strTimeClosePopup = SharedPrefsUtils.getStringFromPrefs(Constant.SHARED_PREFS_NAME, Constant.KEY_EXTRAS_DATE_CLOSE_POPUP);
        boolean hasClose = SharedPrefsUtils.getBooleanFromPrefs(Constant.SHARED_PREFS_NAME, Constant.KEY_EXTRAS_CLOSE_POPUP);
        return hasClose && (StringUtils.isNotBlank(strTimeClosePopup) && !DateUtils.dayIsYesterday(strTimeClosePopup));
    }


    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }

    private void startTrackLocation() {
        if (isServiceConnected)
            return;
        if (locationService == null) {
            if (mLocationHelper == null) {
                mLocationHelper = new LocationHelper(this);
                mLocationHelper.checkpermission();
            }
        }
        if (mLocationHelper.isPermissionGranted()) {
            locationService = new Intent(this, LocationService.class);
            bindService(locationService, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void stopTrackingService() {
        if (!isServiceConnected)
            return;
        if (locationService != null) {
            unbindService(serviceConnection);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            mLocationService = binder.getService();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
        }
    };

}
