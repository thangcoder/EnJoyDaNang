package node.com.enjoydanang.ui.fragment.album;

import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.PartnerAlbum;

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

    void onFetchAlbumSuccess(List<PartnerAlbum> images){
        mvpView.onFetchAlbumSuccess(images);
    }

    void onFetchFail(AppError error){
        mvpView.onFetchFail(error);
    }
}
