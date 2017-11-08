package node.com.enjoydanang.ui.fragment.review.write;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */
@Deprecated
public class WriteReviewFragment extends MvpFragment<WriteReviewPresenter> implements WriteReviewView, View.OnTouchListener {
    private static final String TAG = WriteReviewFragment.class.getSimpleName();

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtTitle)
    EditText edtTitle;

    @BindView(R.id.edtAriaContent)
    EditText edtAriaContent;


    @BindView(R.id.lblFullName)
    TextView lblFullName;
    @BindView(R.id.lblTitle)
    TextView lblTitle;
    @BindView(R.id.lblContent)
    TextView lblContent;

    @BindView(R.id.btnSubmitReview)
    TextView btnSubmitReview;

    private UserInfo userInfo;

    private Partner partner;

    public static WriteReviewFragment newInstance(Partner partner) {
        WriteReviewFragment fragment = new WriteReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected WriteReviewPresenter createPresenter() {
        return new WriteReviewPresenter(this);
    }

    @Override
    protected void init(View view) {
        userInfo = GlobalApplication.getUserInfo();
        if (Utils.hasLogin()) {
            edtName.setText(userInfo.getFullName());
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            partner = (Partner) bundle.getSerializable(TAG);
        }
    }


    @Override
    protected void setEvent(View view) {
        edtAriaContent.setOnTouchListener(this);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_write_review;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.btnSubmitReview, R.id.btnAttachImage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmitReview:
                writeReview();
                break;
            case R.id.btnAttachImage:
                //TODO : new feature
                break;
        }
    }

    @Override
    public void onSubmitSuccess(Repository data) {
        if(Utils.isResponseError(data)){
            DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), data.getMessage());
            return;
        }
        DialogUtils.showDialog(getContext(), DialogType.SUCCESS, DialogUtils.getTitleDialog(1), Utils.getLanguageByResId(R.string.Message_Submit_Contact_Success));
        mBaseActivity.onBackPressed();
    }

    @Override
    public void onSubmitFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
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

    private void writeReview() {
        String name = String.valueOf(edtName.getText());
        String title = String.valueOf(edtTitle.getText());
        String content = String.valueOf(edtAriaContent.getText());
        float ratingCount = ratingBar.getRating();
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(title) || StringUtils.isEmpty(content) || ratingCount == 0) {
            DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), Utils.getLanguageByResId(R.string.Validate_Message_All_Field_Empty));
            return;
        }
        if (Utils.hasLogin() && partner != null) {
//            mvpPresenter.writeReview(userInfo.getUserId(), partner.getId(), (int) ratingCount, title, name, content);
        }
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(lblFullName, lblTitle, lblContent, btnSubmitReview);
    }
}
