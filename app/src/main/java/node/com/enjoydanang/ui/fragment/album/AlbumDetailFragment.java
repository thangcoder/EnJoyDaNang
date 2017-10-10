package node.com.enjoydanang.ui.fragment.album;

import android.app.ProgressDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.AlbumModel;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumDetailFragment extends MvpFragment<AlbumDetailPresenter> implements iAlbumView {

    private ArrayList<AlbumModel> images;
    private ProgressDialog pDialog;
    private AlbumAdapter mAdapter;

//    @BindView(R.id.rcv_album)
//    RecyclerView recyclerView;

    @Override
    protected AlbumDetailPresenter createPresenter() {
        return new AlbumDetailPresenter(this);
    }

    @Override
    protected void init(View view) {
        pDialog = new ProgressDialog(mMainActivity);
        images = new ArrayList<>();
        mAdapter = new AlbumAdapter(mMainActivity.getApplicationContext(), images);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mMainActivity.getApplicationContext(), 2);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rcv_album);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    public void bindView(View view) {

    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onFetchImage(List<AlbumModel> listImages) {
        // TODO: Handle something here !!! onFetchImage

    }

    @Override
    public void onFetchFail() {

    }
}
