package node.com.enjoydanang.ui.fragment.album;

import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.model.AlbumModel;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class AlbumDetailPresenter extends BasePresenter<iAlbumView>{

    public AlbumDetailPresenter(iAlbumView view) {
        super(view);
    }

    public void getAlbum(List<AlbumModel> images){
        mvpView.onFetchImage(images);
    }
}
