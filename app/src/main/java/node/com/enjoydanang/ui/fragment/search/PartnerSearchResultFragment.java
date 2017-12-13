package node.com.enjoydanang.ui.fragment.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.common.Common;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.event.OnFetchSearchResult;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 12/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class PartnerSearchResultFragment extends MvpFragment<SearchPresenter> implements OnItemClickListener {
    private static final String TAG = PartnerSearchResultFragment.class.getSimpleName();

    private List<Partner> lstPartner;

    private SearchPartnerResultAdapter mAdapter;

    @BindView(R.id.rcvPartnerSearchResult)
    RecyclerView rcvPartnerSearchResult;

    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    private OnFetchSearchResult mOnFetchSearchResult;

    public static PartnerSearchResultFragment getIntance(OnFetchSearchResult onFetchSearchResult) {
        PartnerSearchResultFragment fragment = new PartnerSearchResultFragment();
        fragment.setFetchSearchResult(onFetchSearchResult);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        Common.registerEventBus(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Common.unregisterEventBus(this);
    }


    @Override
    protected SearchPresenter createPresenter() {
        return null;
    }

    @Override
    protected void init(View view) {
        showDataList(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvPartnerSearchResult.setLayoutManager(layoutManager);
        lstPartner = new ArrayList<>();
        mAdapter = new SearchPartnerResultAdapter(lstPartner, getContext(), this);
        rcvPartnerSearchResult.setAdapter(mAdapter);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchResultReceive(List<Partner> lstPartners) {
        if (CollectionUtils.isEmpty(lstPartners)) {
            return;
        }
        lstPartner.clear();
        showDataList(true);
        lstPartner.addAll(lstPartners);
        mAdapter.notifyItemRangeChanged(0, lstPartner.size());
        mAdapter.notifyDataSetChanged();
        //Chỗ này là khi fetch hết dữ liệu xong thì a thông báo cho thằng cha dismiss dialog hoặc hide cái progressbar đi
        //Vấn đề là lúc đầu vào nó đéo chịu show cái củ đậu gì hết
        //ok de a coi thu
        mOnFetchSearchResult.onFetchCompleted(true);
    }

    @Override
    public void onClick(View view, int position) {

    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(txtEmpty);
    }

    public void setFetchSearchResult(OnFetchSearchResult onFetchSearchResult){
        mOnFetchSearchResult = onFetchSearchResult;
    }

}
