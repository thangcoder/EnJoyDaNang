package node.com.enjoydanang.ui.activity.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

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
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.model.UserInfo;
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
import node.com.enjoydanang.utils.event.OnUpdateProfileSuccess;
import node.com.enjoydanang.utils.helper.LanguageHelper;

public class MainActivity extends MvpActivity<MainPresenter> implements MainView, AdapterView.OnItemClickListener,
        NavigationView.OnNavigationItemSelectedListener, OnUpdateProfileSuccess {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 200;

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

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }


    @Override
    public void init() {
        initToolbar(toolbar);
        setToolbar(toolbar);
        String[] navMenuTitles = getResources().getStringArray(R.array.item_menu);
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
        if (!checkPermission()) {
            requestPermission();
        }

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
        validAndUpdateFullName();
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
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getActiveFragment();
                if (fragment != null) {
                    String tag = fragment.getTag();
                    if (tag.equals(HomeFragment.class.getName())) {
                        currentTab = HomeTab.Home;
                        lvDrawerNav.clearChoices();
                    } else if (tag.equals(SearchFragment.class.getName())) {
                        currentTab = HomeTab.Search;
                        lvDrawerNav.clearChoices();
                    } else if (tag.equals(ProfileMenuFragment.class.getName())) {
                        currentTab = HomeTab.Profile;
                        lvDrawerNav.clearChoices();
                    } else {
                        currentTab = HomeTab.None;
                        if (tag.equals(ProfileFragment.class.getName())) {
                            lvDrawerNav.setSelection(7);
                        }
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
                if(fragment instanceof ProfileFragment){
                    if(!isUpdatedFullName(fragment)){
                        DialogUtils.showDialog(fragment.getContext(), DialogType.WARNING,
                                DialogUtils.getTitleDialog(2),
                                Utils.getLanguageByResId(R.string.Home_Account_Fullname_NotEmpty));
                        return;
                    }
                }
                getSupportFragmentManager().popBackStack();
            } else {
                if (Utils.hasLogin()) {
                    DialogUtils.showDialogConfirm(this, Utils.getLanguageByResId(R.string.Home_Account_Logout),
                            Utils.getLanguageByResId(R.string.Message_Confirm_Action_Logout),
                            Utils.getLanguageByResId(R.string.Message_Confirm_Ok),
                            Utils.getLanguageByResId(R.string.Message_Confirm_Cancel),
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

    @OnClick({R.id.img_home, R.id.img_profile, R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_home:
                if (currentTab != HomeTab.Home) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getName());
                    if (fragment == null) {
                        addFrMenu(HomeFragment.class.getName(), true);
                    } else {
//                        fragment.onResume();
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
                        resurfaceFragment(fragment,SearchFragment.class.getName());
                    }
                }
                break;
            case R.id.img_profile:
                if (currentTab != HomeTab.Profile) {
                    if (Utils.hasLogin()) {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ProfileMenuFragment.class.getName());
                        if (fragment == null) {
                            addFrMenu(ProfileMenuFragment.class.getName(), true);
                        } else {
                            currentTab = HomeTab.Profile;
                            resurfaceFragment(fragment,ProfileMenuFragment.class.getName());
                        }
                    } else {
                        DialogUtils.showDialog(MainActivity.this, DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                    }
                }
                break;
        }
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
                if (Utils.hasLogin()) {
                    startActivity(new Intent(MainActivity.this, ScanActivity.class));
                    overridePendingTransitionEnter();
                } else {
                    DialogUtils.showDialog(MainActivity.this, DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                }
                return true;
            case R.id.menu_edit:
                addFr(ProfileFragment.class.getName(), CHANGE_PROFILE);
                currentTab = HomeTab.None;
                return true;
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        setStateTabSelected();
        return super.onOptionsItemSelected(item);
    }

    private void replaceFr(String fragmentTag) {
        FragmentTransitionInfo transitionInfo = new FragmentTransitionInfo(R.anim.slide_up_in, 0, 0, 0);
        replaceFragment(R.id.container_fragment, fragmentTag, false, null, transitionInfo);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (Utils.hasLogin()) {
            if (StringUtils.isNotEmpty(Utils.getUserInfo().getFullName())) {
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

    public void resurfaceFragment(Fragment fragment,String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
        addFrMenu(tag, true);

//        FragmentTransaction transaction = manager.beginTransaction();
//        if (fragment != null) {
//            for (int i = 0; i < manager.getFragments().size(); i++) {
//                Fragment f = manager.getFragments().get(i);
//                transaction.hide(f);
//            }
//            transaction.show(fragment).commit();
//            setStateTabSelected();
//            return true;
//        }
//        return false;
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

        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultWrite == PackageManager.PERMISSION_GRANTED && resultRead == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

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
                        //TODO : do nothing
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
}
