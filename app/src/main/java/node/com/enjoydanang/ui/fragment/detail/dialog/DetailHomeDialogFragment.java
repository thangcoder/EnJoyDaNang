package node.com.enjoydanang.ui.fragment.detail.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.ui.fragment.detail.DetailPagerAdapter;
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

    private Toolbar mToolbar;

    private ViewPager mViewPager;

    private TabLayout mTabLayout;

    private Partner partner = null;

    public static DetailHomeDialogFragment newInstance(Partner partner) {
        DetailHomeDialogFragment fragment = new DetailHomeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_home, container, false);
        initToolbar(rootView);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
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
            partner = (Partner) bundle.getSerializable(TAG);
            DetailPagerAdapter adapter = new DetailPagerAdapter(getChildFragmentManager(), mTabLayout.getTabCount(), partner);
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


    private void initToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(LanguageHelper.getValueByKey(Utils.getString(R.string.Tab_Detail)).toUpperCase());

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
    }

    private void setEvents() {
        mTabLayout.addOnTabSelectedListener(this);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // handle close button click here
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
