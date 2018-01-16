package node.com.enjoydanang.api;


import org.apache.commons.lang3.StringUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.utils.Utils;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.exceptions.OnErrorFailedException;

public abstract class ApiCallback<M> extends Subscriber<M> {

    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();


    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        Throwable cause = e.getCause();
        if(cause instanceof OnErrorFailedException || e instanceof OnErrorFailedException){
            onFinish();
            return;
        }
        String status;
        String message = e.getMessage();
        if (StringUtils.isNotEmpty(message)) {
            if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                int code = httpException.code();
                status = String.valueOf(code);
                if (StringUtils.equals(status, Constant.API_500)) {
                    String msg = Utils.getLanguageByResId(R.string.Message_Server_Error);
                    msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_SERVER_ERROR_MSG : msg;
                    onFailure(msg);
                }
            } else if (e instanceof ConnectException) {
                String msg = Utils.getLanguageByResId(R.string.Message_Server_Error);
                msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_SERVER_ERROR_MSG : msg;
                onFailure(msg);
            } else if (e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
                if (AppError.ENABLE_CATCH_TIME_OUT) {
                    String msg = Utils.getLanguageByResId(R.string.Message_Internet_Poor);
                    msg = StringUtils.isBlank(msg) ? AppError.DEFAULT_NETWORK_ERROR_MSG : msg;
                    onFailure(msg);
                }
            }
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        onSuccess(model);
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}
