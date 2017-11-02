package node.com.enjoydanang.ui.fragment.introduction;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Introduction;
import node.com.enjoydanang.utils.Utils;

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
        addSubscription(apiStores.getIntroduction(), new ApiCallback<Repository<Introduction>>(){

            @Override
            public void onSuccess(Repository<Introduction> data) {
                if (Utils.isResponseError(data)) {
                    mvpView.onLoadFailure(new AppError(new Throwable(data.getMessage())));
                    return;
                }
                if(Utils.isNotEmptyContent(data)){
                    mvpView.onGetIntroductionSuccess(data.getData().get(0));
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
}
