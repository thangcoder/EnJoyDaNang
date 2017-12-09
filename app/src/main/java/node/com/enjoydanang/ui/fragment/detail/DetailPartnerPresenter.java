package node.com.enjoydanang.ui.fragment.detail;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.DetailPartner;
import node.com.enjoydanang.model.DetailPartnerCombined;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.utils.Utils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

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

    void getAllDataHome(int partnerId) {
        Observable.zip(apiStores.getDetailPartnerById(partnerId), apiStores.getSlideByPartnerId(partnerId), new Func2<Repository<DetailPartner>, Repository<PartnerAlbum>, DetailPartnerCombined>() {
            @Override
            public DetailPartnerCombined call(Repository<DetailPartner> detailPartnerRepository, Repository<PartnerAlbum> partnerAlbumRepository) {
                return new DetailPartnerCombined(detailPartnerRepository, partnerAlbumRepository);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiCallback<DetailPartnerCombined>() {
                    @Override
                    public void onSuccess(DetailPartnerCombined data) {
                        Repository<DetailPartner> detailPartnerRepository = data.getDetailPartnerRepository();
                        Repository<PartnerAlbum> partnerAlbumRepository = data.getPartnerAlbumRepository();
                        if(Utils.isResponseError(detailPartnerRepository)){
                            mvpView.onFetchFailure(new AppError(new Throwable(detailPartnerRepository.getMessage())));
                        }
                        if(Utils.isResponseError(partnerAlbumRepository)){
                            mvpView.onFetchFailure(new AppError(new Throwable(partnerAlbumRepository.getMessage())));
                        }
                        mvpView.onFetchAllData(detailPartnerRepository.getData(), partnerAlbumRepository.getData());
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
