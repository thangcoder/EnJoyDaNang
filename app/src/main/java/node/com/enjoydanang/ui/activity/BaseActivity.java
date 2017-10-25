package node.com.enjoydanang.ui.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import node.com.enjoydanang.LogApp;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.ApiStores;
import node.com.enjoydanang.api.module.AppClient;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.utils.helper.FragmentHelper;
import node.com.enjoydanang.framework.FragmentTransitionInfo;
import node.com.enjoydanang.model.NetworkStatus;
import node.com.enjoydanang.utils.ConnectionUltils;
import retrofit2.Call;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by chientruong on 12/14/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public Activity mActivity;
    public ApiStores apiStores = AppClient.getClient().create(ApiStores.class);
    private CompositeSubscription mCompositeSubscription;
    private List<Call> calls;
    public Gson gson = new Gson();
    private boolean isConnect;
    private Subscriber subMessage;
    public boolean isHideApp = false;
    protected Toolbar mToolbar;

    public static int RESULT_UPDATE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configScreen();
        setContentView();
        bindViews();
        init();
        setValue(savedInstanceState);
        setEvent();
        setTranslucentStatusBar();


    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * Listening status internet by EvenBus
         */
    }

    MyReceiver myReceiver;

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            LogApp.log("BaseActivity", "registerEventBus: ");

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        /* Stop listening status internet by EvenBus
        */
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        dismissDialogAlarm();
        dissmissDialog();
        super.onDestroy();


    }

    protected void redirectMain(){
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        mActivity = this;
    }

    public abstract void setContentView();

    public abstract void init();

    public abstract void bindViews();

    public abstract void setValue(Bundle savedInstanceState);

    public abstract void setEvent();


    //-------------------------------------------------------------------------------------------------------------------


    /**
     * Check status nternet
     */

    private void checkStatusInternet() {
        if (!ConnectionUltils.isConnected(this)) {

        }
    }

    /*
    *Listener event bus when Network error or Timeout
   */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkStatus(NetworkStatus status) {
        switch (status.getCode()) {
            case "405":
                LogApp.log("Logout by 405");
                break;
            case "408":
                break;
            case "500":
                break;
            default:
                break;
        }
    }



    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    /**
     * Listener event bus when Network have alarm
     *
     * @param timeStarted
     */




    private AlertDialog.Builder builder;
    private AlertDialog.Builder builderLogout;
    AlertDialog dialog;
    AlertDialog dialogLogout;
    private AlertDialog.Builder builderAlarm;
    AlertDialog dialogAlarm;



    public void dismissDialogAlarm() {
        if (dialogAlarm != null) {
            dialogAlarm.dismiss();
            dialogAlarm = null;
        }
    }



    public void dissmissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = null;
    }


    /**
     * Add fragment to Fragment Activity
     *
     * @param fragment    Fragment will be added
     * @param viewGroupId Fragment container ID
     * @param tag         Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
     * @param transition  Transition animations
     * @return <code>true</code> if added successfully, otherwise is <code>false</code>
     */
    public boolean addFragment(final Fragment fragment, int viewGroupId, String tag, FragmentTransitionInfo transition) {
        return FragmentHelper.addFragment(this, fragment, viewGroupId, tag, transition);
    }

    /**
     * Add fragment to Fragment Activity by fragment class tag name.
     *
     * @param viewGroupId          Fragment container ID
     * @param fragmentClassNameTag Full quality fragment class name
     * @param addToBackStack       <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>fragmentClassNameTag</code> tag name
     * @param data                 Passed data
     * @param transition           Transition animations
     * @return <code>true</code> if added successfully, otherwise is <code>false</code>
     */
    public boolean addFragment(int viewGroupId, String fragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.addFragment(this, viewGroupId, fragmentClassNameTag, addToBackStack, data, transition);
    }


    /**
     * Replace fragment from Fragment Activity of specific ViewGroup ID
     *
     * @param fragment    Fragment will be replaced
     * @param viewGroupId Fragment container ID
     * @param tag         Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
     * @param transition  Transition animations
     * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
     */
    public boolean replaceFragment(final Fragment fragment, int viewGroupId, final String tag, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceFragment(this, fragment, viewGroupId, tag, transition);
    }

    /**
     * Replace fragment from Fragment Activity of specific ViewGroup ID by fragment class tag name.
     *
     * @param viewGroupId          Fragment container ID
     * @param fragmentClassNameTag Full quality fragment class name
     * @param addToBackStack       <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>fragmentClassNameTag</code> tag name
     * @param data                 Passed data
     * @param transition           Transition animations
     * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
     */
    public boolean replaceFragment(int viewGroupId, String fragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceFragment(this, viewGroupId, fragmentClassNameTag, addToBackStack, data, transition);
    }

    public boolean replaceFragment(int viewGroupId, Fragment fragment, String fragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceFragment(this, viewGroupId, fragment, fragmentClassNameTag, addToBackStack, data, transition);
    }


    /**
     * Pop Fragment Back Stack
     */
    public void popFragmentBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    public ProgressDialog progressDialog;

    public ProgressDialog showProgressDialog() {
        if (mActivity != null) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
            }
        }
        return progressDialog;
    }

    public ProgressDialog showProgressDialog(CharSequence message) {
        progressDialog = new ProgressDialog(mActivity);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }

    }

    public void openDialogFragment(DialogFragment dialogFragment) {
        FragmentManager mFragmentManager = getFragmentManager();
        android.app.Fragment prev = mFragmentManager.findFragmentByTag(dialogFragment.getClass().getName());
        if (prev == null) {
            dialogFragment.show(mFragmentManager, dialogFragment.getClass().getName());
        }
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber));
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions())
            mCompositeSubscription.unsubscribe();
    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    protected void overridePendingTransitionUp() {
        overridePendingTransition(R.anim.slide_from_bottom, 0);
    }

    protected void overridePendingTransitionDown() {
        overridePendingTransition(R.anim.slide_from_top, R.anim.slide_out_up);
    }


    @Override
    public void finish() {
        super.finish();
//        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
//        overridePendingTransitionEnter();
    }


    public void setTranslucentStatusBar(){}


    public void configScreen(){
        //// TODO: override when need config before set init layout
    }


    public void initToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setToolbar(Toolbar mToolbar) {
        this.mToolbar = mToolbar;
    }
}
