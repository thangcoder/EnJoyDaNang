package node.com.enjoydanang.ui.fragment.share;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.kakaostory.KakaoStoryService;
import com.kakao.kakaostory.callback.StoryResponseCallback;
import com.kakao.kakaostory.response.model.MyStoryInfo;
import com.kakao.network.ErrorResult;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.apache.commons.lang3.StringUtils;

import butterknife.OnClick;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.model.Share;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.ZaloUtils;
import node.com.enjoydanang.utils.event.OnShareZaloListener;
import retrofit2.http.POST;

/**
 * Author: Tavv
 * Created on 30/08/2018
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ShareDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener, OnShareZaloListener {
    private static final String TAG = ShareDialogFragment.class.getSimpleName();

    private SessionCallback sessionCallback;

    private CallbackManager fbCallbackManager;

    private Share share;

    private ShareDialog shareDialog;

    private ZaloUtils zaloUtils;

    private ShareDialogFragment currentDialog;


    public static ShareDialogFragment getInstance(Share share) {
        ShareDialogFragment fragment = new ShareDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, share);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            shareDialog = new ShareDialog(mainActivity);
            fbCallbackManager = mainActivity.fbCallbackManager;
        }
        zaloUtils = new ZaloUtils();
        zaloUtils.setLoginZaLoListener(this);
        LinearLayout lrlShareKakao = (LinearLayout) view.findViewById(R.id.lrlShareKakao);
        LinearLayout lrlShareZalo = (LinearLayout) view.findViewById(R.id.lrlShareZalo);
        LinearLayout lrlShareFb = (LinearLayout) view.findViewById(R.id.lrlShareFb);
        lrlShareFb.setOnClickListener(this);
        lrlShareKakao.setOnClickListener(this);
        lrlShareZalo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            share = (Share) bundle.getSerializable(TAG);
        }
        currentDialog = this;
        shareDialog.registerCallback(fbCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                currentDialog.dismiss();
                if(StringUtils.isNotBlank(result.getPostId())){
                    String title = Utils.getString(R.string.share_title_success);
                    title = String.format(title, "Facebook");
                    DialogUtils.showDialog(getContext(), DialogType.SUCCESS, title, Utils.getString(R.string.share_content));
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "onError: " + error);
            }
        });
    }

    public void onClick(View view) {
        if (share == null) return;
        switch (view.getId()) {
            case R.id.lrlShareZalo:
                if (ZaloSDK.Instance.getOAuthCode() != null && !ZaloSDK.Instance.getOAuthCode().equals("")) {
                    zaloUtils.shareFeed(getActivity(), share.getUrlShare(), share.getTitle());
                } else {
                    zaloUtils.login(getActivity());
                }
                break;
            case R.id.lrlShareFb:
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(share.getUrlShare()))
                            .build();
                    shareDialog.show(content, ShareDialog.Mode.WEB);
                }
                break;
            case R.id.lrlShareKakao:
                KakaoStoryService.requestPostLink(new StoryResponseCallback<MyStoryInfo>() {
                    @Override
                    public void onNotKakaoStoryUser() {
                        Log.d(TAG, "onNotKakaoStoryUser: ");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        if (sessionCallback == null) {
                            sessionCallback = new SessionCallback(share.getUrlShare(), share.getTitle());
                            Session.getCurrentSession().addCallback(sessionCallback);
                            Session.getCurrentSession().checkAndImplicitOpen();
                        }
                        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, getActivity());
                    }

                    @Override
                    public void onNotSignedUp() {
                        Log.d(TAG, "onNotSignedUp: ");
                    }

                    @Override
                    public void onSuccess(MyStoryInfo result) {
                        currentDialog.dismiss();
                        String title = Utils.getString(R.string.share_title_success);
                        title = String.format(title, "KakaoStory");
                        DialogUtils.showDialog(getContext(), DialogType.SUCCESS, title, Utils.getString(R.string.share_content));
                    }
                }, share.getUrlShare(), share.getTitle());
                break;
        }
    }

    @Override
    public void onShareSuccess() {
        currentDialog.dismiss();
        String title = Utils.getString(R.string.share_title_success);
        title = String.format(title, "Zalo");
        DialogUtils.showDialog(getContext(), DialogType.SUCCESS, title, Utils.getString(R.string.share_content));
    }

    @Override
    public void onShareFailure(String message) {

    }

    private class SessionCallback implements ISessionCallback {

        private String url, title;

        public SessionCallback(String url, String title) {
            this.url = url;
            this.title = title;
        }

        @Override
        public void onSessionOpened() {
            KakaoStoryService.requestPostLink(new StoryResponseCallback<MyStoryInfo>() {
                @Override
                public void onNotKakaoStoryUser() {

                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {

                }

                @Override
                public void onNotSignedUp() {
                    Log.d(TAG, "onNotSignedUp: ");
                }

                @Override
                public void onSuccess(MyStoryInfo result) {
                    currentDialog.dismiss();
                }
            }, url, title);
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

}
