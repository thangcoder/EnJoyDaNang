package node.com.enjoydanang.api;


import node.com.enjoydanang.LogApp;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.BaseReponse;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import node.com.enjoydanang.model.NetworkStatus;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class ApiCallback<M> extends Subscriber<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        String status;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            status = String.valueOf(code);
            onFailure(status);
        }
        else if(e instanceof ConnectException){
            onFailure("500");
        }
        else if(e instanceof SocketTimeoutException || e instanceof UnknownHostException){
            onFailure("408");
        }
        else {
            onFailure(e.getMessage());
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        if(model instanceof BaseReponse){
            if(!Constant.API_OK.equals(((BaseReponse)model).getStatus())){
                if(Constant.API_405.equals(((BaseReponse) model).getStatus())) {
                    LogApp.log("Device token expired");
                    EventBus.getDefault().post(new NetworkStatus("405"));
                    return;
                }else{
                    switch (((BaseReponse) model).getStatus()){
                        case Constant.API_402:
                            ((BaseReponse) model).setMessage("交換に必要なポイントが不足しています。現金で購入することも可能です。");
                            break;
                        case Constant.API_403:
                            ((BaseReponse) model).setMessage("終了報告済みの現場です。現場詳細は終了報告画面から確認してください。");
                            break;
                        case Constant.API_404:
                            ((BaseReponse) model).setMessage("現在品切れの商品です。");
                            break;
                        case Constant.API_501:
                            ((BaseReponse) model).setMessage("ただいまシステムメンテナンス中です。ご迷惑をおかけし申し訳ございません。");
                            break;
                        case Constant.API_502:
                            ((BaseReponse) model).setMessage("処理に失敗しました。しばらく時間をおいてから再度お試しください。");
                            break;
                    }
                }
            }
        }
        onSuccess(model);
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}
