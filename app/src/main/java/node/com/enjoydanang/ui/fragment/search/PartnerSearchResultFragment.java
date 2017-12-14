package node.com.enjoydanang.ui.fragment.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.JsonUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnFetchSearchResult;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.LocationHelper;
import node.com.enjoydanang.utils.helper.SeparatorDecoration;

/**
 * Author: Tavv
 * Created on 12/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class PartnerSearchResultFragment extends MvpFragment<SearchPresenter> implements OnItemClickListener {
    private static final String TAG = PartnerSearchResultFragment.class.getSimpleName();

    private List<Partner> lstPartner;

    @BindView(R.id.rcvPartnerSearchResult)
    RecyclerView rcvPartnerSearchResult;

    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    private OnFetchSearchResult mOnFetchSearchResult;

    public static PartnerSearchResultFragment getIntance(OnFetchSearchResult onFetchSearchResult, ArrayList<Partner> data) {
        PartnerSearchResultFragment fragment = new PartnerSearchResultFragment();
        fragment.setFetchSearchResult(onFetchSearchResult);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TAG, data);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            lstPartner = bundle.getParcelableArrayList(TAG);
            if (CollectionUtils.isEmpty(lstPartner)) {
                rcvPartnerSearchResult.setVisibility(View.GONE);
                txtEmpty.setVisibility(View.VISIBLE);
            } else {
                rcvPartnerSearchResult.setVisibility(View.VISIBLE);
                txtEmpty.setVisibility(View.GONE);
                SearchPartnerResultAdapter mAdapter = new SearchPartnerResultAdapter(lstPartner, getContext(), this);
                rcvPartnerSearchResult.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            mOnFetchSearchResult.onFetchCompleted(TAG);
        }
    }

    @Override
    protected SearchPresenter createPresenter() {
        return null;
    }

    @Override
    protected void init(View view) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvPartnerSearchResult.setLayoutManager(layoutManager);
        rcvPartnerSearchResult.setNestedScrollingEnabled(false);
        rcvPartnerSearchResult.addItemDecoration(new SeparatorDecoration(getContext(), Utils.getColorRes(R.color.grey_700), 5));
        lstPartner = new ArrayList<>();
        initView();
    }

    private void showDataList(boolean show) {
        if (show) {
            rcvPartnerSearchResult.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
        } else {
            rcvPartnerSearchResult.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_partner_search_result;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onClick(View view, int position) {
        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(lstPartner.get(position));
        DialogUtils.openDialogFragment(mFragmentManager, dialog);
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(txtEmpty);
    }

    public void setFetchSearchResult(OnFetchSearchResult onFetchSearchResult) {
        mOnFetchSearchResult = onFetchSearchResult;
    }
}
