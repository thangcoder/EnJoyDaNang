package node.com.enjoydanang.ui.fragment.detail;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.model.DetailPartnerCombined;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.Utils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

import static android.R.attr.data;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPartnerPresenter extends BasePresenter<iDetailPartnerView> {

    public DetailPartnerPresenter(iDetailPartnerView view) {
        super(view);
    }

    void getAllDataHome(long customerId, int partnerId, String geoLat, String geoLng) {
        addSubscription(Observable.zip(apiStores.getDetailPartnerById(partnerId), apiStores.getSlideByPartnerId(partnerId),
                apiStores.listPlaceAround(customerId, geoLat, geoLng),
                new Func3<Repository<DetailPartner>, Repository<PartnerAlbum>, Repository<Partner>, DetailPartnerCombined>() {
                    @Override
                    public DetailPartnerCombined call(Repository<DetailPartner> detailPartnerRepository,
                                                      Repository<PartnerAlbum> partnerAlbumRepository,
                                                      Repository<Partner> partnerRepository) {
                        return new DetailPartnerCombined(detailPartnerRepository, partnerAlbumRepository, partnerRepository);
                    }
                }), new ApiCallback<DetailPartnerCombined>() {

            @Override
            public void onSuccess(DetailPartnerCombined data) {
                Repository<DetailPartner> detailPartnerRepository = data.getDetailPartnerRepository();
                Repository<PartnerAlbum> partnerAlbumRepository = data.getPartnerAlbumRepository();
                Repository<Partner> partnerRepository = data.getPartnerRepository();
                if (Utils.isResponseError(detailPartnerRepository)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(detailPartnerRepository.getMessage())));
                }
                if (Utils.isResponseError(partnerAlbumRepository)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(partnerAlbumRepository.getMessage())));
                }
                if (Utils.isResponseError(partnerRepository)) {
                    mvpView.onFetchFailure(new AppError(new Throwable(partnerRepository.getMessage())));
                }
                mvpView.onFetchAllData(detailPartnerRepository.getData(),
                        partnerAlbumRepository.getData(), partnerRepository.getData());
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
