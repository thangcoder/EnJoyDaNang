package node.com.enjoydanang.ui.fragment.favorite;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.ColorDialog;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.ApiStores;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.api.module.AppClient;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.ui.fragment.detail.dialog.DetailHomeDialogFragment;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.SeparatorDecoration;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: Tavv
 * Created on 25/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class FavoriteFragment extends MvpFragment<FavoritePresenter> implements FavoriteView, OnItemClickListener {
    private static final String TAG = FavoriteFragment.class.getSimpleName();

    private static final int VERTICAL_ITEM_SPACE = 5;


    @BindView(R.id.rcvFavorite)
    RecyclerView rcvFavorite;

    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    @BindView(R.id.progress_bar)
    ProgressBar prgLoading;

    private UserInfo userInfo;

    private FavoriteAdapter mAdapter;

    private List<Partner> lstFavorites;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        prgLoading.post(new Runnable() {
            @Override
            public void run() {
                mvpPresenter.getFavorite(userInfo.getUserId());
            }
        });
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onFetchFavorite(List<Partner> lstFavorites) {
        prgLoading.setVisibility(View.GONE);
        if (CollectionUtils.isEmpty(lstFavorites)) {
            txtEmpty.setVisibility(View.VISIBLE);
            rcvFavorite.setVisibility(View.GONE);
            return;
        }
        this.lstFavorites = lstFavorites;
        txtEmpty.setVisibility(View.GONE);
        rcvFavorite.setVisibility(View.VISIBLE);
        mAdapter = new FavoriteAdapter(lstFavorites, getContext(), this);
        rcvFavorite.setAdapter(mAdapter);
    }

    @Override
    public void onFetchFailure(AppError error) {
        DialogUtils.showDialog(getContext(), DialogType.WRONG, DialogUtils.getTitleDialog(3), error.getMessage());
    }

    @Override
    protected FavoritePresenter createPresenter() {
        return new FavoritePresenter(this);
    }

    @Override
    protected void init(View view) {
        userInfo = Utils.getUserInfo();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvFavorite.addItemDecoration(new SeparatorDecoration(getContext(), Utils.getColorRes(R.color.grey_700), VERTICAL_ITEM_SPACE));
        rcvFavorite.setLayoutManager(layoutManager);
        rcvFavorite.setHasFixedSize(false);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_favorite;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onClick(View view, final int position) {
        final ApiStores apiStores = AppClient.getClient().create(ApiStores.class);
        if (view.getId() == R.id.btnDelete) {
            DialogUtils.showDialogConfirm(getContext(), DialogUtils.getTitleDialog(2),
                    Utils.getLanguageByResId(R.string.Message_Confirm_Remove_Favorite),
                    Utils.getLanguageByResId(R.string.Message_Confirm_Ok),
                    Utils.getLanguageByResId(R.string.Message_Confirm_Cancel),
                    new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog colorDialog) {
                            colorDialog.dismiss();
                            new CompositeSubscription().add(apiStores.addFavorite(userInfo.getUserId(), lstFavorites.get(position).getId())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new ApiCallback<Repository>() {
                                        @Override
                                        public void onSuccess(Repository model) {
                                            if (!Utils.isResponseError(model)) {
                                                mAdapter.removeAt(position);
                                            }
                                        }

                                        @Override
                                        public void onFailure(String msg) {
                                            Toast.makeText(mMainActivity, msg, Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFinish() {

                                        }
                                    }));
                        }
                    }, new ColorDialog.OnNegativeListener() {
                        @Override
                        public void onClick(ColorDialog colorDialog) {
                            colorDialog.dismiss();
                        }
                    }
            );
        } else {
            DetailHomeDialogFragment dialog = DetailHomeDialogFragment.newInstance(lstFavorites.get(position), false);
            DialogUtils.openDialogFragment(mFragmentManager, dialog);
        }

    }

}
