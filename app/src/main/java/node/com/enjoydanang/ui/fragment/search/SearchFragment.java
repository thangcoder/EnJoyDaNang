package node.com.enjoydanang.ui.fragment.search;

import android.location.Location;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnFetchSearchResult;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.widget.SeekbarWithIntervals;


/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SearchFragment extends MvpFragment<SearchPresenter> implements iSearchView,
        SearchView.OnQueryTextListener, OnItemClickListener, OnFetchSearchResult {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private static final int DELAY_INTERVAL = 1000;

    @BindView(R.id.search)
    SearchView searchView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.rcvSearchResult)
    RecyclerView rcvSearchResult;

    @BindView(R.id.lnlSearch)
    LinearLayout lnlSearch;

    @BindView(R.id.rlrContent)
    LinearLayout rlrContent;

    @BindView(R.id.searchResultPager)
    ViewPager searchResultPager;

    @BindView(R.id.sbSearchInterval)
    SeekbarWithIntervals sbDistance;

    @BindView(R.id.btnFindPlace)
    AppCompatButton btnFindPlace;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private SearchResultAdapter mAdapter;

    private List<Partner> lstPartner;

    private UserInfo userInfo;

    private Location currentLocation;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    protected void init(View view) {
        userInfo = Utils.getUserInfo();
        currentLocation = mMainActivity.getLocationService().getLastLocation();
        initRecyclerView();
        initTabs();
        sbDistance.setIntervals(getIntervals());
        sbDistance.setIntervalColor(Utils.getColorRes(R.color.material_red_400));
    }

    private List<String> getIntervals() {
        List<String> intervals = new ArrayList<String>();
        for (int i = 0; i <= 10; i++) {
            intervals.add(i + "");
        }
        return intervals;
    }

    private void initTabs() {
        final String[] tabTitles = new String[]{"Map", "Partner"};
        SearchTabAdapter mSearchTabAdapter = new SearchTabAdapter(mFragmentManager, tabTitles, this);
        searchResultPager.setAdapter(mSearchTabAdapter);
        tabLayout.setupWithViewPager(searchResultPager);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvSearchResult.setLayoutManager(layoutManager);
        rcvSearchResult.setHasFixedSize(false);
        lstPartner = new ArrayList<>();
        mAdapter = new SearchResultAdapter(getContext(), lstPartner, this);
        rcvSearchResult.setAdapter(mAdapter);
    }

    @Override
    protected void setEvent(View view) {
        searchView.setOnQueryTextListener(this);
    }

    @OnClick(R.id.btnFindPlace)
    public void onFindClick(){
        showProgressDialog();
        int distance = sbDistance.getProgress();
        String geoLat = String.valueOf(currentLocation.getLatitude());
        String geoLng = String.valueOf(currentLocation.getLongitude());
        mvpPresenter.searchPlaceByCurrentLocation(userInfo.getUserId(), distance, geoLat, geoLng);
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
        customSearchView();
    }

    private void customSearchView() {
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Utils.getColorRes(R.color.color_title_category));
        searchEditText.setHintTextColor(Utils.getColorRes(R.color.material_grey_200));
    }

    @Override
    public void OnQuerySearchResult(List<Partner> lstPartner) {
        mAdapter.updateResult(lstPartner);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResultPlaceByRange(List<Partner> lstPartner) {
        EventBus.getDefault().post(lstPartner);
    }

    @Override
    public void onError(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (StringUtils.isNotEmpty(query)) {
            showResultContainer(true);
            mvpPresenter.searchWithTitle(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (StringUtils.isNotEmpty(newText)) {
            mvpPresenter.searchWithTitle(newText);
            showResultContainer(true);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            mAdapter.clearAllItem();
            Utils.hideSoftKeyboard(getActivity());
            showResultContainer(false);
            progressBar.setVisibility(View.GONE);
        }
        return false;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
    }

    @Override
    protected void setHasOptionMenu() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onClick(View view, int position) {
        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(lstPartner.get(position));
        dialog.show(mFragmentManager, TAG);
    }

    private void showResultContainer(final boolean isShow) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rcvSearchResult.setVisibility(isShow ? View.VISIBLE : View.GONE);
                rlrContent.setVisibility(isShow ? View.GONE : View.VISIBLE);
            }
        }, DELAY_INTERVAL);
    }

    @Override
    public void initViewLabel(View view) {
        super.initViewLabel(view);
        LanguageHelper.getValueByViewId(searchView, btnFindPlace);
    }

    @Override
    public void onFetchCompleted(boolean isCompleted) {
        if(isCompleted){
            dismissProgressDialog();
        }
    }
}
