package node.com.enjoydanang.ui.fragment.home;

import java.util.List;

import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.ExchangeRate;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.Weather;

/**
 * Created by chien on 10/8/17.
 */

public interface iHomeView  extends iBaseView {

    void onGetBannerSuccess(List<Banner> data);

    void onGetBannerFailure(AppError error);

    void onGetPartnerSuccess(List<Partner> data);

    void onGetPartnerFailure(AppError error);

    void onGetCategorySuccess(List<Category> data);

    void onGetCategoryFailure(AppError error);

    void onGetPartnerByCategorySuccess(Repository<Partner> data);

    void onGetPartnerByCategoryFailure(AppError error);

    void onFetchWeatherSuccess(List<Weather> lstWeathers);

    void onFetchWeatherFailure(AppError error);

    void onFetchExchangeRateSuccess(List<ExchangeRate> lstExchange);

    void onFetchExchangeRateFailure(AppError error);

}
