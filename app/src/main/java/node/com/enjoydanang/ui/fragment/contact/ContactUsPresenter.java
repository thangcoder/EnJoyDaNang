package node.com.enjoydanang.ui.fragment.contact;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Contact;
import node.com.enjoydanang.utils.Utils;

import static com.kakao.usermgmt.StringSet.email;

/**
 * Author: Tavv
 * Created on 26/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ContactUsPresenter extends BasePresenter<ContactUsView> {

    public ContactUsPresenter(ContactUsView view) {
        super(view);
    }

    void sendContact(String name, String phone, String email, String title, String content) {
        addSubscription(apiStores.sendContact(name, phone, email, title, content), new ApiCallback<Repository>() {

            @Override
            public void onSuccess(Repository model) {
                if(Utils.isResponseError(model)){
                    mvpView.sendContactFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.sendContactSuccess();
            }

            @Override
            public void onFailure(String msg) {
                mvpView.sendContactFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    void getInformation(){
        addSubscription(apiStores.getInformation(), new ApiCallback<Repository<Contact>>() {

            @Override
            public void onSuccess(Repository<Contact> model) {
                if(Utils.isResponseError(model)){
                    mvpView.sendContactFailure(new AppError(new Throwable(model.getMessage())));
                    return;
                }
                mvpView.onGetInformation(model.getData().get(0));
            }

            @Override
            public void onFailure(String msg) {
                mvpView.sendContactFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
