package node.com.enjoydanang.ui.fragment.album;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.AlbumModel;
import node.com.enjoydanang.utils.network.NetworkError;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumDetailFragment extends MvpFragment<AlbumDetailPresenter> implements iAlbumView, AlbumAdapter.ClickListener {

    private AlbumAdapter mAdapter;

    @BindView(R.id.rcv_album)
    RecyclerView recyclerView;

    @BindView(R.id.progressLoading)
    ProgressBar progressLoading;

    private ArrayList<AlbumModel> images;

    @Override
    protected AlbumDetailPresenter createPresenter() {
        return new AlbumDetailPresenter(this);
    }

    @Override
    protected void init(View view) {
        images = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mMainActivity.getApplicationContext(), 2);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        mvpPresenter.doDummyData();
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
    public void onFetchAlbumSuccess(List<AlbumModel> images) {
        this.images.addAll(images);
        mAdapter = new AlbumAdapter(mMainActivity.getApplicationContext(), images);
        mAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        progressLoading.setVisibility(View.GONE);
    }

    @Override
    public void onFetchFail(NetworkError error) {

    }

    @Override
    public void onClick(View view, int position) {
        if (CollectionUtils.isNotEmpty(images)) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("images", images);
            bundle.putInt("position", position);

            SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
            newFragment.setArguments(bundle);
            newFragment.show(mFragmentManager, "slideshow");
        }
    }

    @Override
    public void onLongClick(View view, int position) {

    }
}
