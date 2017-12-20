package node.com.enjoydanang.ui.activity.term;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Content;
import node.com.enjoydanang.ui.fragment.term.TermPresenter;
import node.com.enjoydanang.ui.fragment.term.TermView;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;

import static node.com.enjoydanang.utils.Utils.getContext;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class TermActivity extends MvpActivity<TermPresenter> implements TermView {

    @BindView(R.id.txtTermSystemTitle)
    TextView txtTermSystemTitle;

    @BindView(R.id.txtContent)
    TextView txtContent;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.lrlTermContent)
    LinearLayout lrlTermContent;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected TermPresenter createPresenter() {
        return new TermPresenter(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.fragment_term);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvpPresenter.getTermSystem();
    }

    @Override
    public void init() {

    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {
        initToolbar(toolbar);
        toolbar.setTitle(Utils.getLanguageByResId(R.string.Home_Term_System).toUpperCase());
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEvent() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransitionExit();
            }
        });
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

    @Override
    public void initViewLabel() {
        LanguageHelper.getValueByViewId(txtTermSystemTitle);
    }
}
