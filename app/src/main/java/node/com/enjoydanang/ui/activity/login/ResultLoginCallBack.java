package node.com.enjoydanang.ui.activity.login;

import node.com.enjoydanang.utils.network.NetworkError;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface ResultLoginCallBack<M> {
    void onResultCallBack(M model);
    void onError(NetworkError error);
}
