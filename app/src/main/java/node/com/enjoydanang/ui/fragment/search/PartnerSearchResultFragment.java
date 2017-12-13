package node.com.enjoydanang.ui.fragment.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.collections.CollectionUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
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
    public static String arrayPartner;
    public static PartnerSearchResultFragment getIntance(OnFetchSearchResult onFetchSearchResult,String data) {
        PartnerSearchResultFragment fragment = new PartnerSearchResultFragment();
        fragment.setFetchSearchResult(onFetchSearchResult);
        arrayPartner = data;

        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        lstPartner = convertListJsonMessageToObject(arrayPartner);
        if (CollectionUtils.isEmpty(lstPartner)) {
            rcvPartnerSearchResult.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            return;
        }else {
            rcvPartnerSearchResult.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
            mAdapter = new SearchPartnerResultAdapter(lstPartner, getContext(), this);
            rcvPartnerSearchResult.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mOnFetchSearchResult.onFetchCompleted(true);
        }
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvPartnerSearchResult.setLayoutManager(layoutManager);
        lstPartner = new ArrayList<>();
//        mAdapter = new SearchPartnerResultAdapter(lstPartner, getContext(), this);
//        rcvPartnerSearchResult.setAdapter(mAdapter);
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

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onSearchResultReceive(List<Partner> lstPartners) {
//        if (CollectionUtils.isEmpty(lstPartners)) {
//            return;
//        }
//        lstPartner.clear();
//        showDataList(true);
//        lstPartner.addAll(lstPartners);
//        mAdapter.notifyItemRangeChanged(0, lstPartner.size());
//        mAdapter.notifyDataSetChanged();
//        mOnFetchSearchResult.onFetchCompleted(true);
//    }

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
    public static Gson gson = new Gson();
    public static List<Partner> convertListJsonMessageToObject(String listMessage) {
        Type founderListType = new TypeToken<ArrayList<Partner>>() {
        }.getType();
        List<Partner> list = gson.fromJson(listMessage, founderListType);
        return list;
    }
}
