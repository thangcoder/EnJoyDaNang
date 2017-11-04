package node.com.enjoydanang.ui.fragment.review.write;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.ApiStores;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.api.module.AppClient;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnBackFragmentListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class WriteReviewDialog extends DialogFragment implements View.OnTouchListener {
    private static final String TAG = WriteReviewDialog.class.getSimpleName();

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtTitle)
    EditText edtTitle;

    @BindView(R.id.edtAriaContent)
    EditText edtAriaContent;

    private UserInfo userInfo;

    private Partner partner;

    private DialogInterface.OnDismissListener onDismissListener;

    private OnBackFragmentListener onBackListener;

    private boolean isBack;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnBackListener(OnBackFragmentListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public static WriteReviewDialog newInstance(Partner partner) {
        WriteReviewDialog fragment = new WriteReviewDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(Utils.getLanguageByResId(R.string.WriteReview));
        return inflater.inflate(R.layout.fragment_write_review, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        userInfo = GlobalApplication.getUserInfo();
        if (Utils.hasLogin()) {
            edtName.setText(userInfo.getFullName());
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            partner = (Partner) bundle.getSerializable(TAG);
        }
        edtAriaContent.setOnTouchListener(this);
    }


    @OnClick({R.id.btnSubmitReview, R.id.btnAttachImage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitReview:
                submitWriteReview();
                break;
            case R.id.btnAttachImage:
                //TODO : new feature
                break;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//         creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                isBack = true;
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
    public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                v.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }


    private void submitWriteReview() {
        String name = String.valueOf(edtName.getText());
        String title = String.valueOf(edtTitle.getText());
        String content = String.valueOf(edtAriaContent.getText());
        float ratingCount = ratingBar.getRating();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || ratingCount == 0) {
            DialogUtils.showDialog(getActivity(), DialogType.WRONG, DialogUtils.getTitleDialog(3), Utils.getLanguageByResId(R.string.Validate_Message_All_Field_Empty));
            return;
        }
        if (partner != null) {
            long userId = Utils.hasLogin() ? userInfo.getUserId() : 0;
            ApiStores apiStores = AppClient.getClient().create(ApiStores.class);
            new CompositeSubscription().add(apiStores.writeReview(userId, partner.getId(), (int) ratingCount, title, name, content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ApiCallback<Repository>() {
                        @Override
                        public void onSuccess(Repository model) {
                            if (Utils.isResponseError(model)) {
                                DialogUtils.showDialog(getActivity(), DialogType.WRONG, DialogUtils.getTitleDialog(3), model.getMessage());
                                return;
                            }
                            DialogUtils.showDialog(getActivity(), 3, DialogUtils.getTitleDialog(1), Utils.getLanguageByResId(R.string.Dialog_Title_Success), new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog promptDialog) {
                                    promptDialog.dismiss();
                                   dismiss();
                                }
                            });
                        }

                        @Override
                        public void onFailure(String msg) {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFinish() {

                        }
                    }));
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        if (onDismissListener != null) {
//            onDismissListener.onDismiss(dialog);
//        }
        if(onBackListener != null){
            onBackListener.onDismiss(dialog, isBack);
        }

    }

}
