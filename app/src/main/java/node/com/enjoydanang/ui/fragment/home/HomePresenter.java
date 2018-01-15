package node.com.enjoydanang.ui.fragment.home;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.HomeCombined;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.Utils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * Created by chien on 10/8/17.
 */

public class HomePresenter extends BasePresenter<iHomeView> {

    public HomePresenter(iHomeView view) {
        super(view);
    }


    void getListHome(long customerId, String latitude, String longtitude) {
        addSubscription(apiStores.getListPartnerHome(customerId, latitude, longtitude), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> model) {
                if (Utils.isNotEmptyContent(model)) {
                    mvpView.onGetPartnerSuccess(model.getData());
                }
            }

            @Override
            public void onFailure(String msg) {
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void addFavorite(long userId, int partnerId) {
        addSubscription(apiStores.addFavorite(userId, partnerId), new ApiCallback<Repository>() {

            @Override
            public void onSuccess(Repository model) {
                if (Utils.isResponseError(model)) {
                    mvpView.addFavoriteFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.addFavoriteSuccess();
            }

            @Override
            public void onFailure(String msg) {
                mvpView.addFavoriteFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getAllDataHome(long userId, String latitude, String longtitude) {
        Observable.zip(apiStores.getListPartnerHome(userId, latitude, longtitude), apiStores.getBanner(), apiStores.getAllCategories(), new Func3<Repository<Partner>, Repository<Banner>, Repository<Category>, HomeCombined>() {
            @Override
            public HomeCombined call(Repository<Partner> partnerRepository, Repository<Banner> bannerRepository, Repository<Category> categoryRepository) {
                return new HomeCombined(partnerRepository, bannerRepository, categoryRepository);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<HomeCombined>() {
                    @Override
                    public void onSuccess(HomeCombined data) {
                        Repository<Partner> partnerRepository = data.getPartnerRepository();
                        Repository<Banner> bannerRepository = data.getBannerRepository();
                        Repository<Category> categoryRepository = data.getCategoryRepository();
                        if(Utils.isNotEmptyContent(partnerRepository) && Utils.isNotEmptyContent(bannerRepository) && Utils.isNotEmptyContent(categoryRepository)){
                            mvpView.onFetchAllDataSuccess(partnerRepository.getData(), bannerRepository.getData(), categoryRepository.getData());
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        mvpView.onFetchFailure(new AppError(new Throwable(msg)));
                    }

                    @Override
                    public void onFinish() {

                    }

                });
    }
}
