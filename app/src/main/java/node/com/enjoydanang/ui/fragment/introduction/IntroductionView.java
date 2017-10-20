package node.com.enjoydanang.ui.fragment.introduction;

import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Introduction;
import node.com.enjoydanang.utils.network.NetworkError;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface IntroductionView extends iBaseView {

    void onGetIntroductionSuccess(BaseRepository<Introduction> data);

    void onLoadFailure(NetworkError error);
}
