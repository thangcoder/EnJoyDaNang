package node.com.enjoydanang.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.User;
import node.com.enjoydanang.ui.activity.main.MainActivity;
import node.com.enjoydanang.utils.helper.StatusBarCompat;

/**
 * Author: Tavv
 * Created on 09/10/2017.
 * Project Name: EnJoyDanang
 * Version : 1.0
 */

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginView, GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 0x1;

    @BindView(R.id.lrlSignIn)
    public LinearLayout lrlSignIn;

    @BindView(R.id.txtCreateAccount)
    public TextView txtCreateAccount;

    @BindView(R.id.txtForgotPwd)
    public TextView txtForgotPwd;

    private CallbackManager callbackManager;

    private AccessToken accessToken;

    private GoogleSignInOptions signInOptions;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void init() {
        callbackManager = CallbackManager.Factory.create();
        signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient  = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();


    }

    @Override
    public void bindViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setValue(Bundle savedInstanceState) {

    }

    @Override
    public void setEvent() {

    }

    @OnClick({R.id.lrlSignIn, R.id.txtCreateAccount, R.id.txtForgotPwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lrlSignIn:
                Intent intent = new Intent(mActivity, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.txtCreateAccount:
                Toast.makeText(this, "Create Acc Click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.txtForgotPwd:
                Toast.makeText(this, "Forgot Click", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick({R.id.btnLoginFb, R.id.btnLoginGPlus, R.id.btnLoginKakaotalk})
    public void onRegisterWithSocial(View view) {
        switch (view.getId()) {
            case R.id.btnLoginFb:
                accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null) {
                    getFacebookUserProfile(accessToken);
                } else {
                    LoginManager loginManager = LoginManager.getInstance();
                    List<String> permission = new ArrayList<String>();
                    permission.add("public_profile");
                    loginManager.logInWithReadPermissions(this, permission);

                    loginManager.registerCallback(callbackManager,
                            new FacebookCallback<LoginResult>() {
                                @Override
                                public void onSuccess(LoginResult loginResult) {
                                    accessToken = loginResult.getAccessToken();
                                    getFacebookUserProfile(accessToken);
                                }

                                @Override
                                public void onCancel() {

                                }

                                @Override
                                public void onError(FacebookException exception) {
                                    Log.i(TAG, "onError " + exception);
                                }
                            });
                }
                break;
            case R.id.btnLoginGPlus:
                signInGoogle();
                break;
            case R.id.btnLoginKakaotalk:
                Toast.makeText(this, "Forgot Click", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void getFacebookUserProfile(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                User user = toUserObject(response.getRawResponse());
                user.setAccessToken(accessToken.getToken());
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, first_name,last_name, email, birthday, gender, picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInGoogleResult(result);
        }
    }

    private void handleSignInGoogleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

        } else {
            // TODO: Signed out, show unauthenticated UI

        }
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    private User toUserObject(String jsonResponse) {
        return new Gson().fromJson(jsonResponse, User.class);
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void setTranslucentStatusBar() {
        StatusBarCompat.translucentStatusBar(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
