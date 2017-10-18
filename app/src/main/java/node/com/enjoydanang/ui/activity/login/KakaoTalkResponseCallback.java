package node.com.enjoydanang.ui.activity.login;

import android.util.Log;

import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.network.ErrorResult;

/**
 * Author: Tavv
 * Created on 16/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public abstract class KakaoTalkResponseCallback<T> extends TalkResponseCallback<T>{

    private static final String TAG = KakaoTalkResponseCallback.class.getSimpleName();

    @Override
    public void onNotKakaoTalkUser() {
        Log.i(TAG, "onNotKakaoTalkUser ");
    }

    @Override
    public void onFailure(ErrorResult errorResult) {
        Log.i(TAG, "onFailure ");
    }

    @Override
    public void onSessionClosed(ErrorResult errorResult) {
        Log.i(TAG, "onSessionClosed ");
    }

    @Override
    public void onNotSignedUp() {
        Log.i(TAG, "onNotSignedUp ");
    }

}
