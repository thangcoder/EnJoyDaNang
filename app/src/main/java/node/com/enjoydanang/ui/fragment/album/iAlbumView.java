package node.com.enjoydanang.ui.fragment.album;

import java.util.List;

import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.AlbumModel;
import node.com.enjoydanang.utils.network.NetworkError;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface iAlbumView extends iBaseView {
    void onFetchAlbumSuccess(List<AlbumModel> images);

    void onFetchFail(NetworkError error);

}
