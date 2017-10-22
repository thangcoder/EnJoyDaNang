package node.com.enjoydanang.ui.fragment.introduction;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.BaseRepository;
import node.com.enjoydanang.model.Introduction;
import node.com.enjoydanang.constant.AppError;

/**
 * Author: Tavv
 * Created on 20/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class IntroductionPresenter extends BasePresenter<IntroductionView>{
    private static final String TAG = IntroductionPresenter.class.getSimpleName();

    public IntroductionPresenter(IntroductionView view) {
        super(view);
    }

    void getIntroduction(){
        mvpView.showLoading();
        addSubscription(apiStores.getIntroduction(), new ApiCallback<BaseRepository<Introduction>>(){

            @Override
            public void onSuccess(BaseRepository<Introduction> data) {
                mvpView.hideLoading();
                mvpView.onGetIntroductionSuccess(data);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onLoadFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
