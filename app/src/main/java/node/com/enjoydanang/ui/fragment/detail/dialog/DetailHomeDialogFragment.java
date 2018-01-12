package node.com.enjoydanang.ui.fragment.detail.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.LruCache;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.activity.scan.ScanActivity;
import node.com.enjoydanang.ui.fragment.detail.DetailPagerAdapter;
import node.com.enjoydanang.ui.fragment.home.HomeFragment;
import node.com.enjoydanang.ui.fragment.home.PartnerCategoryFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 25/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailHomeDialogFragment extends DialogFragment implements TabLayout.OnTabSelectedListener {
    private static final String TAG = DetailHomeDialogFragment.class.getSimpleName();
    private static final String KEY_OPEN_FROM_NEARBY = "open_from_nearby";

    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private Partner partner = null;

    @BindView(R.id.imgLogo)
    ImageView imgLogo;
    @BindView(R.id.name)
    TextView toolbarName;
    @BindView(R.id.edit_profile)
    TextView tvProfile;
    @BindView(R.id.img_scan)
    ImageView imgScan;
    @BindView(R.id.frToolBar)
    FrameLayout frToolBar;

    private MainActivity mMainActivity;

    public int countGetResultFailed = 0;

    private boolean isOpenFromNearby;

    public static DetailHomeDialogFragment newInstance(Partner partner, boolean isOpenFromNearby) {
        DetailHomeDialogFragment fragment = new DetailHomeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, partner);
        bundle.putBoolean(KEY_OPEN_FROM_NEARBY, isOpenFromNearby);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_home, container, false);
        ButterKnife.bind(this, rootView);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        initToolbar();
        mMainActivity = (MainActivity) getActivity();
        return rootView;
    }

    @OnClick(R.id.img_back)
    public void onClick(View view) {
        if (mMainActivity != null) {
            Fragment fragment = mMainActivity.getActiveFragment();
            if (fragment instanceof PartnerCategoryFragment) {
                dismiss();
            } else {
                mMainActivity.setShowMenuItem(Constant.SHOW_QR_CODE);
                dismiss();
            }
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(), R.style.AppTheme) {
            @Override
            public void onBackPressed() {
                if (mMainActivity != null) {
                    Fragment fragment = mMainActivity.getActiveFragment();
                    if (fragment instanceof PartnerCategoryFragment) {
                        dismiss();
                    } else {
                        mMainActivity.setShowMenuItem(Constant.SHOW_QR_CODE);
                        dismiss();
                    }
                }
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTabLayout.addTab(mTabLayout.newTab().setText(LanguageHelper.getValueByKey(Utils.getString(R.string.Tab_Detail))));
        mTabLayout.addTab(mTabLayout.newTab().setText(LanguageHelper.getValueByKey(Utils.getString(R.string.Tab_Review))));
//        mTabLayout.addTab(mTabLayout.newTab().setText(LanguageHelper.getValueByKey(Utils.getString(R.string.Tab_Schedule))));
        mTabLayout.addTab(mTabLayout.newTab().setText(LanguageHelper.getValueByKey(Utils.getString(R.string.Tab_Album))));
        setEvents();
        //Creating our pager adapter
        Bundle bundle = getArguments();
        if (bundle != null) {
            partner = (Partner) bundle.getParcelable(TAG);
            isOpenFromNearby = bundle.getBoolean(KEY_OPEN_FROM_NEARBY);
            DetailPagerAdapter adapter = new DetailPagerAdapter(getChildFragmentManager(), mTabLayout.getTabCount(), partner, isOpenFromNearby);
            mViewPager.setAdapter(adapter);
            int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
            mViewPager.setOffscreenPageLimit(limit);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) return;
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation_fade;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }


    private void initToolbar() {
        toolbarName.setText(LanguageHelper.getValueByKey(Utils.getString(R.string.Tab_Detail)).toUpperCase());
        setHeightToolbar();
        tvProfile.setVisibility(View.GONE);
        imgScan.setVisibility(View.VISIBLE);
        toolbarName.setVisibility(View.GONE);
    }

    private void setEvents() {
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setHeightToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frToolBar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
    }

    @OnClick({R.id.img_scan, R.id.img_back})
    public void onMenuOptionsClick(View view) {
        switch (view.getId()) {
            case R.id.img_scan:
                if (Utils.hasLogin()) {
                    startActivity(new Intent(getActivity(), ScanActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                } else {
                    DialogUtils.showDialog(getActivity(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
                }
                break;
            case R.id.img_back:
                if (mMainActivity != null) {
                    Fragment fragment = mMainActivity.getActiveFragment();
                    if (fragment instanceof PartnerCategoryFragment) {
                        dismiss();
                    } else if (fragment instanceof HomeFragment) {
                        dismiss();
                        mMainActivity.setShowMenuItem(Constant.SHOW_QR_CODE);
                        HomeFragment homeFragment = (HomeFragment) fragment;
                        homeFragment.scrollToTop();
                    } else {
                        dismiss();
                        mMainActivity.setShowMenuItem(Constant.SHOW_QR_CODE);
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LruCache lruCache = new LruCache(getContext());
        lruCache.evictAll();
    }
}
