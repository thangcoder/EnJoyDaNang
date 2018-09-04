package node.com.enjoydanang.ui.fragment.home;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.share.widget.ShareDialog;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.Share;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.base.BaseRecyclerViewAdapter;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.ui.fragment.home.adapter.PartnerCategoryAdapter;
import node.com.enjoydanang.ui.fragment.share.ShareDialogFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.ZaloUtils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.event.OnShareZaloListener;
import node.com.enjoydanang.utils.helper.EndlessScrollListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;

/**
 * Author: Tavv
 * Created on 22/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class PartnerCategoryFragment extends MvpFragment<PartnerCategoryPresenter> implements PartnerCategoryView, OnItemClickListener,
        BaseRecyclerViewAdapter.ItemClickListener, OnShareZaloListener {
    private static final String TAG = PartnerCategoryFragment.class.getSimpleName();
    private static final String KEY_EXTRAS_TITLE = "title_category";
    private static final String KEY_EXTRAS_LOCATION = "current_location";

    private static final int VERTICAL_ITEM_SPACE = 8;

    private int categoryId;

    private UserInfo userInfo;

    private static final int START_PAGE = 0;

    private LinearLayoutManager mLayoutManager;

    private List<Partner> lstPartner;

    private PartnerCategoryAdapter partnerCategoryAdapter;

    @BindView(R.id.rcvPartnerByCate)
    RecyclerView rcvPartnerCategory;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.txtPartnerEmpty)
    TextView txtPartnerEmpty;

    private boolean hasLoadmore;

    private Location mLocation;

    private ZaloUtils zaloUtils;

    private Share share;

    private ShareDialogFragment bottomShareDialog;

    public static PartnerCategoryFragment newInstance(int categoryId, String title, Location location) {
        PartnerCategoryFragment fragment = new PartnerCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, categoryId);
        bundle.putString(KEY_EXTRAS_TITLE, title);
        bundle.putParcelable(KEY_EXTRAS_LOCATION, location);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        userInfo = Utils.getUserInfo();
        if (categoryId != -1) {
            if (mLocation == null) {
                mvpPresenter.getListByLocation(categoryId, userInfo.getUserId(), START_PAGE, StringUtils.EMPTY, StringUtils.EMPTY);
            } else {
                String strGeoLat = String.valueOf(mLocation.getLatitude());
                String strGeoLng = String.valueOf(mLocation.getLongitude());
                mvpPresenter.getListByLocation(categoryId, userInfo.getUserId(), START_PAGE, strGeoLat, strGeoLng);
            }
        }
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onClick(View view, final int position) {
        if (view.getId() == R.id.fabFavorite) {
            if (userInfo.getUserId() != 0) {
                FloatingActionButton fabFavorite = (FloatingActionButton) view;
                mvpPresenter.addFavorite(userInfo.getUserId(), lstPartner.get(position).getId());
                boolean isFavorite = lstPartner.get(position).getFavorite() > 0;
                fabFavorite.setImageResource(isFavorite ? R.drawable.unfollow : R.drawable.follow);
                lstPartner.get(position).setFavorite(isFavorite ? 0 : 1);
            } else {
                DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
            }

        } else if (view.getId() == R.id.btnShare) {
            String urlShare = lstPartner.get(position).getShareUrl();
            if (StringUtils.isNotBlank(urlShare)) {
                urlShare = Constant.URL_HOST_IMAGE + urlShare;
                share = new Share(lstPartner.get(position).getName(), urlShare, "");
                bottomShareDialog = DialogUtils.showSheetShareDialog(mMainActivity, share);
//                DialogUtils.showPopupShare(getContext(), shareDialog, zaloUtils, mMainActivity, urlShare, lstPartner.get(position).getName());
            }
        } else {
            MainActivity activity = (MainActivity) getActivity();
            activity.currentTab = HomeTab.None;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CollectionUtils.isNotEmpty(lstPartner)) {
                        Partner partner;
                        try {
                            partner = lstPartner.get(position);
                        } catch (IndexOutOfBoundsException ex) {
                            partner = null;
                        }
                        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(partner, false);
                        DialogUtils.openDialogFragment(mFragmentManager, dialog);
                    }
                }
            }, 50);
        }
    }

    @Override
    protected void init(View view) {
        getDataSent();
        initRecyclerView();
        zaloUtils = new ZaloUtils();
        zaloUtils.setLoginZaLoListener(this);
    }

    @Override
    protected void setEvent(View view) {
        rcvPartnerCategory.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page) {
                hasLoadmore = true;
                partnerCategoryAdapter.startLoadMore();
                if (mLocation == null) {
                    mvpPresenter.getListByLocation(categoryId, userInfo.getUserId(), page, StringUtils.EMPTY, StringUtils.EMPTY);
                } else {
                    String strGeoLat = String.valueOf(mLocation.getLatitude());
                    String strGeoLng = String.valueOf(mLocation.getLongitude());
                    mvpPresenter.getListByLocation(categoryId, userInfo.getUserId(), page, strGeoLat, strGeoLng);
                }
            }
        });
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_partner_category;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onGetPartnerByCategorySuccess(Repository<Partner> data) {
        if (CollectionUtils.isEmpty(lstPartner) && !Utils.isNotEmptyContent(data)) {
            prgLoading.setVisibility(View.GONE);
            rcvPartnerCategory.setVisibility(View.GONE);
            txtPartnerEmpty.setVisibility(View.VISIBLE);
            return;
        }
        if (hasLoadmore) {
            partnerCategoryAdapter.onReachEnd();
        }
        if (!Utils.isResponseError(data)) {
            updateItems(data.getData());
            partnerCategoryAdapter.set(lstPartner);
        }
        prgLoading.setVisibility(View.GONE);
        rcvPartnerCategory.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetPartnerByCategoryFailure(AppError error) {
        prgLoading.setVisibility(View.GONE);
        txtPartnerEmpty.setVisibility(View.VISIBLE);
        partnerCategoryAdapter.onLoadMoreFailed();
        Log.e(TAG, "onGetPartnerByCategoryFailure " + error.getMessage());
    }

    @Override
    public void addFavoriteSuccess() {

    }

    @Override
    public void addFavoriteFailure(AppError error) {
        Log.e(TAG, "onError: " + error.getMessage());
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    protected PartnerCategoryPresenter createPresenter() {
        return new PartnerCategoryPresenter(this);
    }

    private void getDataSent() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getInt(TAG, -1);
            mLocation = bundle.getParcelable(KEY_EXTRAS_LOCATION);
        }
    }

    private void initRecyclerView() {
        lstPartner = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        partnerCategoryAdapter = new PartnerCategoryAdapter(getContext(), this);
        rcvPartnerCategory.addItemDecoration(Utils.getDividerDecoration(mLayoutManager.getOrientation()));
        rcvPartnerCategory.setLayoutManager(mLayoutManager);
        rcvPartnerCategory.setHasFixedSize(false);
        rcvPartnerCategory.setAdapter(partnerCategoryAdapter);
    }

    private void updateItems(List<Partner> lstPartners) {
        lstPartner.addAll(lstPartners);
    }

    @Override
    public void initViewLabel(View view) {
        LanguageHelper.getValueByViewId(txtPartnerEmpty);
    }

    @Override
    public void onItemClick(View view, final int position) {
        if (view.getId() == R.id.fabFavorite) {
            if (userInfo.getUserId() != 0) {
                FloatingActionButton fabFavorite = (FloatingActionButton) view;
                mvpPresenter.addFavorite(userInfo.getUserId(), lstPartner.get(position).getId());
                boolean isFavorite = lstPartner.get(position).getFavorite() > 0;
                fabFavorite.setImageResource(isFavorite ? R.drawable.unfollow : R.drawable.follow);
                lstPartner.get(position).setFavorite(isFavorite ? 0 : 1);
            } else {
                DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
            }

        } else if (view.getId() == R.id.btnShare) {
            String urlShare = Constant.URL_HOST_IMAGE + lstPartner.get(position).getShareUrl();
            if (StringUtils.isNotBlank(urlShare)) {
                share = new Share(lstPartner.get(position).getName(), urlShare, "");
                bottomShareDialog =  DialogUtils.showSheetShareDialog(mMainActivity, share);
//                DialogUtils.showPopupShare(getContext(), shareDialog, zaloUtils, getActivity(), urlShare, lstPartner.get(position).getName());
            }
        } else {
            MainActivity activity = (MainActivity) getActivity();
            activity.currentTab = HomeTab.None;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (CollectionUtils.isNotEmpty(lstPartner)) {
                        Partner partner;
                        try {
                            partner = lstPartner.get(position);
                        } catch (IndexOutOfBoundsException ex) {
                            partner = null;
                        }
                        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(partner, false);
                        DialogUtils.openDialogFragment(mFragmentManager, dialog);
                    }
                }
            }, 50);
        }
    }

    public Share getShare() {
        return share;
    }

    public void setShare(Share share) {
        this.share = share;
    }

    @Override
    public void onShareSuccess() {
        if(bottomShareDialog != null){
            bottomShareDialog.dismiss();
            String title = Utils.getString(R.string.share_title_success);
            title = String.format(title, "Zalo");
            DialogUtils.showDialog(getContext(), DialogType.SUCCESS, title, Utils.getString(R.string.share_content));
        }
    }

    @Override
    public void onShareFailure(String message) {

    }
}