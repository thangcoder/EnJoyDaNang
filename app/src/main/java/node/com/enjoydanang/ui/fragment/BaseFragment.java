package node.com.enjoydanang.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import node.com.enjoydanang.LogApp;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import node.com.enjoydanang.R;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by chientruong on 6/13/16.
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mMainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getRootLayoutId(), container, false);
        bindView(view);
        init(view);
        setEvent(view);
        executeBase();
        return view;
    }

    protected abstract void init(View view);

    protected abstract void setEvent(View view);

    public abstract int getRootLayoutId();

    public abstract void bindView(View view);


    protected void executeBase() {
    }

    public void switchChildFragment(Fragment parentFragment, Fragment childFragment, int viewContainer) {
        String FRAGMENT_TAG = childFragment.getClass().getSimpleName();
        parentFragment.getChildFragmentManager().beginTransaction()
                //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(viewContainer, childFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }
    public void switchChildFragmentBackStack(Fragment parentFragment, Fragment childFragment, int viewContainer) {
        String FRAGMENT_TAG = childFragment.getClass().getSimpleName();
        android.support.v4.app.FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment childFragmentOnStack = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if(childFragmentOnStack!=null){
            childFragment= childFragmentOnStack;
        }
        fragmentTransaction
                //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .addToBackStack(FRAGMENT_TAG)
                .replace(viewContainer, childFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }
    public void switchChildFragmentReload(Fragment parentFragment, Fragment childFragment, int viewContainer) {
        String FRAGMENT_TAG = childFragment.getClass().getSimpleName();
        android.support.v4.app.FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment childFragmentOnStack = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if(childFragmentOnStack!=null){
            childFragment= childFragmentOnStack;
        }
        fragmentTransaction
                .addToBackStack(FRAGMENT_TAG)
                .detach(childFragment)
                .attach(childFragment)
                .commit();
    }

    public void switchChildFragmentWithoutAddToBackStack(Fragment parentFragment, Fragment childFragment, int viewContainer) {
        String FRAGMENT_TAG = childFragment.getClass().getSimpleName();
        parentFragment.getChildFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(viewContainer, childFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }

    public void toastShow(int resId) {
    }

    public void toastShow(String resId) {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogApp.log("onDestroyView");
        onUnsubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogApp.log("dismissProgressDialog");
        dismissProgressDialog();
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
        }
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    public ProgressDialog progressDialog;

    public void showProgressDialog() {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        } else {
            progressDialog = new ProgressDialog(mMainActivity);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog.dismiss();
        }
    }
}
