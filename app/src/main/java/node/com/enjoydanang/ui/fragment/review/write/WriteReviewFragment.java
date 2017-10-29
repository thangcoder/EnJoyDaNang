package node.com.enjoydanang.ui.fragment.review.write;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;

import static android.R.attr.data;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

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
            DialogUtils.showDialog(getContext(), 2, Constant.TITLE_ERROR, data.getMessage());
            return;
        }
        DialogUtils.showDialog(getContext(), 3, Constant.TITLE_SUCCESS, "Review sent");
        mBaseActivity.onBackPressed();
    }

    @Override
    public void onSubmitFailure(AppError error) {
        DialogUtils.showDialog(getContext(), 2, Constant.TITLE_ERROR, error.getMessage());
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
            DialogUtils.showDialog(getContext(), 2, Constant.TITLE_ERROR, Utils.getString(R.string.enter_full_field));
            return;
        }
        if (Utils.hasLogin() && partner != null) {
            mvpPresenter.writeReview(userInfo.getUserId(), partner.getId(), (int) ratingCount, title, name, content);
        }
    }

}
