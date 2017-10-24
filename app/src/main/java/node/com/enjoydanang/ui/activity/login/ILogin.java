package node.com.enjoydanang.ui.activity.login;

import android.widget.ProgressBar;

import node.com.enjoydanang.ui.activity.BaseActivity;

/**
 * Author: Tavv
 * Created on 13/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

/**
 * @param <C> Callback
 * @param <M> Model
 */
public interface ILogin<C, M> {

    void init();

    void login();

    void handleCallbackResult(C callback);

    BaseActivity getActivity();

    void pushToServer(M model);

    void removeAccessToken();

    void setLoginPresenter(LoginPresenter loginPresenter);

}
