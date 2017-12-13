package node.com.enjoydanang.model;

import node.com.enjoydanang.api.model.Repository;

/**
 * Author: Tavv
 * Created on 13/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class SplashScreenCombined {

    private Repository<Language> languageRepository;
    private  Repository<UserInfo> userInfoRepository;


    public SplashScreenCombined() {
    }

    public SplashScreenCombined(Repository<Language> languageRepository, Repository<UserInfo> userInfoRepository) {
        this.languageRepository = languageRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public Repository<Language> getLanguageRepository() {
        return languageRepository;
    }

    public void setLanguageRepository(Repository<Language> languageRepository) {
        this.languageRepository = languageRepository;
    }

    public Repository<UserInfo> getUserInfoRepository() {
        return userInfoRepository;
    }

    public void setUserInfoRepository(Repository<UserInfo> userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }
}
