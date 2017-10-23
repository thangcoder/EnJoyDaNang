package node.com.enjoydanang.ui.activity.splash;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.model.Language;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SplashScreenPresenter extends BasePresenter<SplashScreenView>{


    public SplashScreenPresenter(SplashScreenView view) {
        super(view);
    }

    void loadLanguage(){
        addSubscription(apiStores.getLanguage(), new ApiCallback<Repository<Language>>(){

            @Override
            public void onSuccess(Repository<Language> data) {
                if(Utils.isNotEmptyContent(data)){
                    List<Language> lstLanguages = data.getData();
                    Map<String, String> maps = new HashMap<String, String>();
                    int length = lstLanguages.size();
                    for (int i = 0; i < length; i++) {
                        Language language = lstLanguages.get(i);
                        maps.put(language.getName(), language.getValue());
                    }
                    JSONObject json = new JSONObject(maps);
                    mvpView.onLoadLanguageSuccess(json);
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onLoadFailre(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
