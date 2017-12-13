package node.com.enjoydanang.ui.activity.splash;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Language;
import node.com.enjoydanang.model.SplashScreenCombined;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.Utils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Author: Tavv
 * Created on 21/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SplashScreenPresenter extends BasePresenter<SplashScreenView> {


    public SplashScreenPresenter(SplashScreenView view) {
        super(view);
    }

    void loadLanguage() {
        addSubscription(apiStores.getLanguage(), new ApiCallback<Repository<Language>>() {

            @Override
            public void onSuccess(Repository<Language> data) {
                if (Utils.isNotEmptyContent(data)) {
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
                mvpView.onLoadFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getDataCombine(long userId){
        Observable.zip(apiStores.getLanguage(), apiStores.getUserInfoById(userId, "CUSTOMERINFO"), new Func2<Repository<Language>, Repository<UserInfo>, SplashScreenCombined>() {
            @Override
            public SplashScreenCombined call(Repository<Language> languageRepository, Repository<UserInfo> userInfoRepository) {
                return new SplashScreenCombined(languageRepository, userInfoRepository);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<SplashScreenCombined>() {
                    @Override
                    public void onSuccess(SplashScreenCombined data) {
                        Repository<Language> languageRepository = data.getLanguageRepository();
                        Repository<UserInfo> userInfoRepository = data.getUserInfoRepository();
                        if(Utils.isResponseError(languageRepository)){
                            mvpView.onFailure(new AppError(new Throwable(languageRepository.getMessage())));
                        }
                        if(Utils.isResponseError(userInfoRepository)){
                            mvpView.onFailure(new AppError(new Throwable(userInfoRepository.getMessage())));
                        }
                        JSONObject json = null;
                        if (Utils.isNotEmptyContent(languageRepository)) {
                            List<Language> lstLanguages = languageRepository.getData();
                            Map<String, String> maps = new HashMap<String, String>();
                            int length = lstLanguages.size();
                            for (int i = 0; i < length; i++) {
                                Language language = lstLanguages.get(i);
                                maps.put(language.getName(), language.getValue());
                            }
                            json = new JSONObject(maps);
                        }
                        mvpView.onCombined(json, userInfoRepository.getData().get(0));
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.onFailure(new AppError(new Throwable(msg)));
                    }

                    @Override
                    public void onFinish() {

                    }

                });
    }

}
