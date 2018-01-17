package node.com.enjoydanang.ui.fragment.search;

import android.location.Address;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.LocationUtils;
import node.com.enjoydanang.utils.Utils;
import rx.Observable;
import rx.Subscriber;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SearchPresenter extends BasePresenter<iSearchView> {
    public SearchPresenter(iSearchView view) {
        super(view);
    }

    void searchWithTitle(String query) {
        addSubscription(apiStores.searchWithQuery(query), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> model) {
                mvpView.OnQuerySearchResult(model.getData());
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    void searchPlaceByCurrentLocation(long customerId, int distance, String geoLat, String geoLng) {
        addSubscription(apiStores.listSearchByLocation(customerId, distance, geoLat, geoLng), new ApiCallback<Repository<Partner>>() {

            @Override
            public void onSuccess(Repository<Partner> model) {
                if (Utils.isResponseError(model)) {
                    mvpView.onError(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onResultPlaceByRange(model.getData());
            }

            @Override
            public void onFailure(String msg) {
                mvpView.onError(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });

    }

    void getAddress(final Address address) {
        addSubscription(Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String fullAddress = "";
                if (address != null) {
                    fullAddress = LocationUtils.getFullInfoAddress(address);
                }
                subscriber.onNext(fullAddress);
                subscriber.onCompleted();
            }
        }), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String strAddress) {
                mvpView.onGetAddress(strAddress);
            }
        });
    }


    void getAddressByGeoLocation(final List<Partner> lstPartner) {
        addSubscription(Observable.create(new Observable.OnSubscribe<List<Partner>>() {

            @Override
            public void call(Subscriber<? super List<Partner>> subscriber) {
                List<Partner> lstPartners = new ArrayList<Partner>();
                for (Partner partner : lstPartner) {
                    if (StringUtils.isBlank(partner.getAddress())) {
                        double lat = Double.parseDouble(partner.getGeoLat());
                        double lng = Double.parseDouble(partner.getGeoLng());
                        Address address = LocationUtils.getAddress(lat, lng);
                        String fullAddress = "";
                        if (address != null) {
                            fullAddress = LocationUtils.getFullInfoAddress(address);
                        }
                        partner.setAddress(fullAddress);
                        lstPartners.add(partner);
                    } else {
                        lstPartners.add(partner);
                    }
                }
                subscriber.onNext(lstPartners);
                subscriber.onCompleted();
            }
        }), new Subscriber<List<Partner>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Partner> lstPartner) {
                mvpView.onGetLocationAddress(lstPartner);
            }
        });
    }

    void showDialog() {
        mvpView.showLoading();
    }

    void hideDialog() {
        mvpView.hideLoading();
    }
}
