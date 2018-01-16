package node.com.enjoydanang.ui.fragment.search;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.LocationUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnFetchSearchResult;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;


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

    @BindView(R.id.frame)
    FrameLayout frame;

    @BindView(R.id.rcvSearchResult)
    RecyclerView rcvSearchResult;

    @BindView(R.id.lnlSearch)
    RelativeLayout lnlSearch;

    @BindView(R.id.rlrContent)
    LinearLayout rlrContent;

    @BindView(R.id.searchResultPager)
    ViewPager searchResultPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.txtSearching)
    TextView txtSearching;

    private SearchResultAdapter mAdapter;

    private List<Partner> lstPartner;

    private UserInfo userInfo;

    private Location currentLocation;

    private static final int DEFAULT_DISTANCE = 1;

    private ArrayList<String> tabNameChilds;

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
    protected void init(View view) {
        progressBar.setVisibility(View.VISIBLE);
        frame.setVisibility(View.INVISIBLE);
        tabNameChilds = new ArrayList<>();
        if (LocationUtils.isGpsEnabled() && LocationUtils.isLocationEnabled()) {
            userInfo = Utils.getUserInfo();
            if (mMainActivity != null && mMainActivity.getLocationService() != null) {
                currentLocation = mMainActivity.getLocationService().getLastLocation();
            }
            initRecyclerView();
        } else {
            lnlSearch.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            DialogUtils.showDialog(getContext(), DialogType.INFO, Utils.getLanguageByResId(R.string.Permisstion_Title),
                    Utils.getLanguageByResId(R.string.Map_Location_NotFound));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        if (LocationUtils.isGpsEnabled() && LocationUtils.isLocationEnabled()) {
            fetchNearPlace();
        }
    }

    private void fetchNearPlace() {
        if (currentLocation != null) {
            String geoLat = String.valueOf(currentLocation.getLatitude());
            String geoLng = String.valueOf(currentLocation.getLongitude());
            mvpPresenter.searchPlaceByCurrentLocation(userInfo.getUserId(), DEFAULT_DISTANCE, geoLat, geoLng);
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvSearchResult.setLayoutManager(layoutManager);
        lstPartner = new ArrayList<>();
        mAdapter = new SearchResultAdapter(getContext(), lstPartner, this);
        rcvSearchResult.setAdapter(mAdapter);
    }

    @Override
    protected void setEvent(View view) {
        searchView.setOnQueryTextListener(this);
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
        //String strJsonData = JsonUtils.convertObjectToJson(lstPartner);
        mvpPresenter.getAddressByGeoLocation(lstPartner);
        this.lstPartner = lstPartner;
    }

    @Override
    public void onGetLocationAddress(List<String> lstAddress) {
        for (int i = 0; i < lstAddress.size(); i++) {
            lstPartner.get(i).setLocationAddress(lstAddress.get(i));
        }
        ArrayList<Partner> data = new ArrayList<>();
        data.addAll(lstPartner);
        String strTab1 = Utils.getLanguageByResId(R.string.Search_Tab1_Title);
        String strTab2 = Utils.getLanguageByResId(R.string.Search_Tab2_Title);
        final String[] tabTitles = new String[]{strTab1, strTab2};
        SearchTabAdapter mSearchTabAdapter = new SearchTabAdapter(getChildFragmentManager(), tabTitles, this, data);
        searchResultPager.setAdapter(mSearchTabAdapter);
        tabLayout.setupWithViewPager(searchResultPager);
    }

    @Override
    public void onGetAddress(String strAddress) {

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
        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(lstPartner.get(position), false);
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
        LanguageHelper.getValueByViewId(searchView, txtSearching);
    }

    @Override
    public void onFetchCompleted(String tabNameInitCompleted) {
        tabNameChilds.add(tabNameInitCompleted);
        if (tabNameChilds.size() == 2) {
            progressBar.setVisibility(View.GONE);
            txtSearching.setVisibility(View.GONE);
            frame.setVisibility(View.VISIBLE);
        }
    }
}
