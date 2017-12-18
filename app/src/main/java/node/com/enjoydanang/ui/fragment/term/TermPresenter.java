package node.com.enjoydanang.ui.fragment.term;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Content;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 18/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class TermPresenter extends BasePresenter<TermView>{
    public TermPresenter(TermView view) {
        super(view);
    }

    public void getTermSystem(){
        addSubscription(apiStores.getTermSystem("TERMSOFUSE"), new ApiCallback<Repository<Content>>(){

            @Override
            public void onSuccess(Repository<Content> model) {
                if(Utils.isResponseError(model)){
                    mvpView.onFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onLoadTermSuccess(model.getData().get(0));
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
