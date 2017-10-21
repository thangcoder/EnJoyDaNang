package node.com.enjoydanang.ui.activity.splash;

import org.json.JSONObject;

import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Language;
import node.com.enjoydanang.utils.network.NetworkError;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public interface SplashScreenView extends iBaseView {
    void onLoadLanguageSuccess(JSONObject json);
    void onLoadFailre(NetworkError networkError);
}
