package node.com.enjoydanang.ui.fragment.event;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.ApiStores;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.api.module.AppClient;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Content;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class EventDialogFragment extends DialogFragment {
    private static final String TAG = EventDialogFragment.class.getSimpleName();

    @BindView(R.id.txtContent)
    WebView txtContent;

    @BindView(R.id.imgBanner)
    SimpleDraweeView imgBanner;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.rlrEventContent)
    LinearLayout rlrEventContent;

    @BindView(R.id.frToolBar)
    FrameLayout frToolBar;

    @BindView(R.id.imgLogo)
    ImageView imgLogo;

    @BindView(R.id.name)
    TextView toolbarName;

    @BindView(R.id.img_scan)
    ImageView imgScan;

    private CompositeSubscription mCompositeSubscription;

    private ApiStores apiStores;

    public static EventDialogFragment getInstance(int bannerId) {
        EventDialogFragment fragment = new EventDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, bannerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            int bannerId = bundle.getInt(TAG);
            addSubscription(apiStores.getBannerEvent(bannerId, "EVENT"), new ApiCallback<Repository<Content>>() {

                @Override
                public void onSuccess(Repository<Content> model) {
                    if (Utils.isResponseError(model)) {
                        onLoadFailure(new AppError(new Throwable(model.getMessage())));
                        return;
                    }
                    onLoadEventSuccess(model.getData().get(0));
                }

                @Override
                public void onFailure(String msg) {
                    onLoadFailure(new AppError(new Throwable(msg)));
                }

                @Override
                public void onFinish() {

                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null) return;
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation_fade;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, rootView);
        mCompositeSubscription = new CompositeSubscription();
        apiStores = AppClient.getClient().create(ApiStores.class);
        initToolbar();
        return rootView;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
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
    public void onDestroyView() {
        super.onDestroyView();
        onUnsubscribe();
    }

    public void onLoadEventSuccess(Content content) {
        if (StringUtils.isBlank(content.getContent())) {
            txtContent.loadDataWithBaseURL(null, Utils.getLanguageByResId(R.string.Home_Empty), "text/html", "utf-8", null);
        } else {
            txtContent.loadDataWithBaseURL(null, content.getContent(), "text/html", "utf-8", null);
        }
        ImageUtils.loadImageWithFreso(imgBanner, content.getPicture());
        rlrEventContent.setVisibility(View.VISIBLE);
        prgLoading.setVisibility(View.GONE);
    }

    public void onLoadFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.clear();
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

    private void initToolbar() {
        setHeightToolbar();
        imgScan.setVisibility(View.VISIBLE);
        toolbarName.setVisibility(View.GONE);
    }

    private void setHeightToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frToolBar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
    }

    @OnClick(R.id.img_back)
    public void onClick(View view) {
        dismiss();
    }
}


