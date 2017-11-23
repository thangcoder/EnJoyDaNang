package node.com.enjoydanang.ui.fragment.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.ui.fragment.home.adapter.PartnerAdapter;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.EndlessRecyclerViewScrollListener;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.helper.SeparatorDecoration;

import static node.com.enjoydanang.R.id.fabFavorite;

/**
 * Author: Tavv
 * Created on 22/11/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class PartnerCategoryFragment extends MvpFragment<PartnerCategoryPresenter> implements PartnerCategoryView, OnItemClickListener {
    private static final String TAG = PartnerCategoryFragment.class.getSimpleName();
    private static final String KEY_EXTRAS_TITLE = "title_category";

    private static final int VERTICAL_ITEM_SPACE = 8;

    private int categoryId;

    private UserInfo userInfo;

    private static final int START_PAGE = 0;

    private LinearLayoutManager mLayoutManager;

    private List<Partner> lstPartner;

    private PartnerAdapter mPartnerAdapter;


    @BindView(R.id.rcvPartnerByCate)
    RecyclerView rcvPartnerCategory;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    @BindView(R.id.txtPartnerEmpty)
    TextView txtPartnerEmpty;


    public static PartnerCategoryFragment newInstance(int categoryId, String title) {
        PartnerCategoryFragment fragment = new PartnerCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, categoryId);
        bundle.putString(KEY_EXTRAS_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        userInfo = Utils.getUserInfo();
        if (categoryId != -1) {
            mvpPresenter.getPartnerByCategory(categoryId, START_PAGE, userInfo.getUserId());
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
        if (view.getId() == fabFavorite) {
            if (userInfo.getUserId() != 0) {
                FloatingActionButton fabFavorite = (FloatingActionButton) view;
                mvpPresenter.addFavorite(userInfo.getUserId(), lstPartner.get(position).getId());
                boolean isFavorite = lstPartner.get(position).getFavorite() > 0;
                fabFavorite.setImageResource(isFavorite ? R.drawable.unfollow : R.drawable.follow);
                lstPartner.get(position).setFavorite(isFavorite ? 0 : 1);
            } else {
                DialogUtils.showDialog(getContext(), DialogType.WARNING, DialogUtils.getTitleDialog(2), Utils.getLanguageByResId(R.string.Message_You_Need_Login));
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
                        DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(partner);
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
    }

    @Override
    protected void setEvent(View view) {
        rcvPartnerCategory.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public int getFooterViewType(int defaultNoFooterViewType) {
                return 1;
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mvpPresenter.getPartnerByCategory(categoryId, page, userInfo.getUserId());
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
        if (!Utils.isResponseError(data)) {
            updateItems(data.getData());
        }
        prgLoading.setVisibility(View.GONE);
        rcvPartnerCategory.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetPartnerByCategoryFailure(AppError error) {
        prgLoading.setVisibility(View.GONE);
        txtPartnerEmpty.setVisibility(View.VISIBLE);
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
            String title = bundle.getString(KEY_EXTRAS_TITLE);
            mMainActivity.setNameToolbar(title);
        }
    }

    private void initRecyclerView() {
        lstPartner = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mPartnerAdapter = new PartnerAdapter(getContext(), lstPartner, this);
        rcvPartnerCategory.setLayoutManager(mLayoutManager);
        rcvPartnerCategory.addItemDecoration(
                new SeparatorDecoration(getContext(), Utils.getColorRes(R.color.material_grey_300), VERTICAL_ITEM_SPACE));
        rcvPartnerCategory.setAdapter(mPartnerAdapter);
    }

    private void updateItems(List<Partner> lstPartners) {
        int oldSize = lstPartner.size();
        int newSize = lstPartners.size() + oldSize;
        lstPartner.addAll(lstPartners);
        mPartnerAdapter.notifyItemRangeChanged(0, newSize);
        mPartnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void initViewLabel(View view) {
        LanguageHelper.getValueByViewId(txtPartnerEmpty);
    }
}
