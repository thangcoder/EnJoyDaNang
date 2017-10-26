package node.com.enjoydanang.ui.fragment.introduction;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Introduction;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class IntroductionFragment extends MvpFragment<IntroductionPresenter> implements IntroductionView {
    private static final String TAG = IntroductionFragment.class.getSimpleName();

    @BindView(R.id.txtIntroductName)
    TextView txtIntroductionName;

    @BindView(R.id.txtContent)
    TextView txtContent;


    @Override
    protected IntroductionPresenter createPresenter() {
        return new IntroductionPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        showLoading();
        mvpPresenter.getIntroduction();
    }

    @Override
    protected void init(View view) {
        mBaseActivity.getToolbar().setTitle(StringUtils.EMPTY);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_introduction;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onGetIntroductionSuccess(Introduction introduction) {
        txtIntroductionName.setText(introduction.getTitle());
        Spanned spanned = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(introduction.getContent(), Html.FROM_HTML_MODE_LEGACY);
        }else{
            spanned = Html.fromHtml(introduction.getContent());
        }
        txtContent.setText(spanned);
    }

    @Override
    public void onLoadFailure(AppError error) {
        Utils.showDialog(getContext(), 4, Constant.TITLE_WARNING, error.getMessage());
    }
}
