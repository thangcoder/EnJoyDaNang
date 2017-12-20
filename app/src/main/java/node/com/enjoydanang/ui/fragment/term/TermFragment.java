package node.com.enjoydanang.ui.fragment.term;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Content;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class TermFragment extends MvpFragment<TermPresenter> implements TermView {
    private static final String TAG = TermFragment.class.getSimpleName();

    @BindView(R.id.txtTermSystemTitle)
    TextView txtTermSystemTitle;

    @BindView(R.id.txtContent)
    TextView txtContent;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.lrlTermContent)
    LinearLayout lrlTermContent;

    @Override
    protected TermPresenter createPresenter() {
        return new TermPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        mvpPresenter.getTermSystem();
    }

    @Override
    protected void init(View view) {
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_term;
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
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(txtTermSystemTitle);
    }

    @Override
    public void onLoadTermSuccess(Content content) {
        if(StringUtils.isNotBlank(content.getContent())){
            Spanned spanned = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                spanned = Html.fromHtml(content.getContent(), Html.FROM_HTML_MODE_LEGACY);
            }else{
                spanned = Html.fromHtml(content.getContent());
            }
            txtContent.setText(spanned);
        }else{
            txtContent.setText(Utils.getLanguageByResId(R.string.Home_Empty));
            txtContent.setTextColor(Utils.getColorRes(R.color.red));
            txtContent.setGravity(Gravity.CENTER);
        }
        lrlTermContent.setVisibility(View.VISIBLE);
        prgLoading.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }
}

