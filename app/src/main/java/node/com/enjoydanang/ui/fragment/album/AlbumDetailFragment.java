package node.com.enjoydanang.ui.fragment.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumDetailFragment extends MvpFragment<AlbumDetailPresenter> implements iAlbumView, AlbumAdapter.ClickListener {
    private static final String TAG = AlbumDetailFragment.class.getSimpleName();

    private AlbumAdapter mAdapter;

    private static final int VERTICAL_ITEM_SPACE = 5;

    @BindView(R.id.rcv_album)
    RecyclerView recyclerView;

    private ArrayList<PartnerAlbum> images;


    public static AlbumDetailFragment newInstance(Partner partner) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TAG, partner);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected AlbumDetailPresenter createPresenter() {
        return new AlbumDetailPresenter(this);
    }

    @Override
    protected void init(View view) {
        images = new ArrayList<>();
//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mMainActivity.getApplicationContext(), 2);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);

        recyclerView.addItemDecoration(Utils.getDividerDecoration(mLayoutManager.getOrientation()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        Bundle bundle = getArguments();
        if (bundle != null) {
            Partner partner = (Partner) bundle.getParcelable(TAG);
            if (partner != null) {
                mvpPresenter.getAlbum(partner.getId());
            }
        }
    }

    @Override
    protected void setEvent(View view) {
        recyclerView.addOnItemTouchListener(new AlbumAdapter.RecyclerTouchListener(getContext(), recyclerView, this));
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_album;
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
    public void onFetchAlbumSuccess(List<PartnerAlbum> images) {
        this.images.addAll(images);
        mAdapter = new AlbumAdapter(mMainActivity.getApplicationContext(), images);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchFail(AppError error) {
        Log.e(TAG, "onFetchFail " + error.getMessage());
    }

    @Override
    public void onClick(View view, int position) {
        DialogUtils.showDialogAlbum(mFragmentManager, images, position);
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
