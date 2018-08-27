package node.com.enjoydanang.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.kakaostory.KakaoStoryService;
import com.kakao.kakaostory.api.KakaoStoryApi;
import com.kakao.kakaostory.callback.StoryResponseCallback;
import com.kakao.kakaostory.response.model.MyStoryInfo;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.zing.zalo.zalosdk.oauth.ValidateOAuthCodeCallback;
import com.zing.zalo.zalosdk.oauth.ZaloSDK;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import node.com.enjoydanang.R;
import node.com.enjoydanang.annotation.DialogType;
import node.com.enjoydanang.model.PartnerAlbum;
import node.com.enjoydanang.model.PostZalo;
import node.com.enjoydanang.ui.fragment.album.SlideshowDialogFragment;

import static node.com.enjoydanang.utils.Utils.getString;

/**
 * Author: Tavv
 * Created on 29/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class DialogUtils {
    private static final String TAG = DialogUtils.class.getSimpleName();

    /**
     * @param context Context
     * @param type    {DIALOG_TYPE_INFO : 0, DIALOG_TYPE_HELP : 1, DIALOG_TYPE_WRONG : 2, DIALOG_TYPE_SUCCESS : 3, DIALOG_TYPE_WARNING : 4, DIALOG_TYPE_DEFAULT }
     * @param title   Title dialog
     * @param msg     Message want to display
     */
    public static void showDialog(Context context, @DialogType int type, String title, String msg) {
        new PromptDialog(context)
                .setDialogType(type)
                .setAnimationEnable(true)
                .setTitleText(title)
                .setContentText(msg)
                .setPositiveListener(getString(android.R.string.ok), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }


    /**
     * @param context Context
     * @param type    {DIALOG_TYPE_INFO : 0, DIALOG_TYPE_HELP : 1, DIALOG_TYPE_WRONG : 2, DIALOG_TYPE_SUCCESS : 3, DIALOG_TYPE_WARNING : 4, DIALOG_TYPE_DEFAULT }
     * @param title   Title dialog
     * @param msg     Message want to display
     */
    public static void showDialog(Context context, int type, String title, String msg, PromptDialog.OnPositiveListener positiveListener) {
        new PromptDialog(context)
                .setDialogType(type)
                .setAnimationEnable(true)
                .setTitleText(title)
                .setContentText(msg)
                .setPositiveListener(getString(android.R.string.ok), positiveListener).show();
    }

    /**
     * @param context            Context
     * @param title              title dialog
     * @param content            content dialog
     * @param titleBtnPositive   title button Positive
     * @param titleBtnNegative   title button Negative
     * @param onPositiveListener Event positive click
     * @param onNegativeListener Event negative click
     */
    public static void showDialogConfirm(Context context, String title, String content,
                                         String titleBtnPositive, String titleBtnNegative,
                                         ColorDialog.OnPositiveListener onPositiveListener,
                                         ColorDialog.OnNegativeListener onNegativeListener) {
        ColorDialog colorDialog = new ColorDialog(context);
        colorDialog.setTitle(title);
        colorDialog.setContentText(content);
        colorDialog.setPositiveListener(titleBtnPositive, onPositiveListener);
        colorDialog.setNegativeListener(titleBtnNegative, onNegativeListener);
        colorDialog.show();
    }


    /**
     * @param context            Context
     * @param title              title dialog
     * @param content            content dialog
     * @param titleBtnPositive   title button Positive
     * @param titleBtnNegative   title button Negative
     * @param image              image
     * @param onPositiveListener Event positive click
     * @param onNegativeListener Event negative click
     */
    public static void showDialogConfirm(Context context, String title, String content,
                                         String titleBtnPositive, String titleBtnNegative,
                                         Drawable image,
                                         ColorDialog.OnPositiveListener onPositiveListener,
                                         ColorDialog.OnNegativeListener onNegativeListener) {

        ColorDialog colorDialog = new ColorDialog(context);
        colorDialog.setTitle(title);
        colorDialog.setContentText(content);
        colorDialog.setContentImage(image);
        colorDialog.setPositiveListener(titleBtnPositive, onPositiveListener);
        colorDialog.setNegativeListener(titleBtnNegative, onNegativeListener);
        colorDialog.show();
    }

    /**
     * @param context        Context
     * @param layoutId       layout resource id
     * @param title          title dialog
     * @param btnOkRes       button Ok resource id
     * @param btnCancelRes   button Cancel resource id
     * @param okListener     Event positive click
     * @param cancelListener Event negative click
     * @param hasTitle       boolean hasTitle
     */
    public static void showAlertDialogCustom(Context context, @LayoutRes int layoutId, String title,
                                             @IdRes int btnOkRes, @IdRes int btnCancelRes,
                                             View.OnClickListener okListener,
                                             View.OnClickListener cancelListener, boolean hasTitle) {
        AlertDialog alertDialog = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(layoutId, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        if (hasTitle) {
            builder.setTitle(title);
        } else {
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        alertDialog = builder.create();
        AppCompatButton btnOk = (AppCompatButton) dialogView.findViewById(btnOkRes);
        AppCompatButton btnCancel = (AppCompatButton) dialogView.findViewById(btnCancelRes);
        btnOk.setOnClickListener(okListener);
        btnCancel.setOnClickListener(cancelListener);
        alertDialog.show();
    }

    /**
     * @param type type : 1 - Success, 2 - Warning, 3 - Wrong
     * @return String title
     */

    public static String getTitleDialog(int type) {
        switch (type) {
            case 1:
                return Utils.getLanguageByResId(R.string.Dialog_Title_Success);
            case 2:
                return Utils.getLanguageByResId(R.string.Dialog_Title_Warning);
            case 3:
                return Utils.getLanguageByResId(R.string.Dialog_Title_Wrong);
            default:
                return StringUtils.EMPTY;
        }
    }

    public static void showDialogAlbum(FragmentManager mFragmentManager, ArrayList<PartnerAlbum> images, int position) {
        if (CollectionUtils.isNotEmpty(images)) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("images", images);
            bundle.putInt("position", position);

            SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
            newFragment.setArguments(bundle);
            newFragment.show(mFragmentManager, "slideshow");
        }
    }

    public static void openDialogFragment(FragmentManager mFragmentManager, DialogFragment dialogFragment) {
        if (mFragmentManager != null) {
            Fragment prev = mFragmentManager.findFragmentByTag(dialogFragment.getClass().getName());
            if (prev == null) {
                dialogFragment.show(mFragmentManager, dialogFragment.getClass().getName());
            }
        }
    }

    public static void reOpenDialogFragment(FragmentManager mFragmentManager, DialogFragment dialogFragment) {
        if (mFragmentManager != null) {
            Fragment prev = mFragmentManager.findFragmentByTag(dialogFragment.getClass().getName());
            if (prev != null) {
                FragmentTransaction trans = mFragmentManager.beginTransaction();
                trans.remove(prev);
                trans.commit();
                dialogFragment.show(mFragmentManager, dialogFragment.getClass().getName());
            } else {
                dialogFragment.show(mFragmentManager, dialogFragment.getClass().getName());
            }
        }
    }

    public static void showPopupShare(final Context context, final ShareDialog shareDialogFb, final Activity activity,
                                      final String url, final String title) {
        if (StringUtils.isBlank(url)) return;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.popup_share, null);
        dialogBuilder.setView(dialogView);

        ImageView imgShareKakao = (ImageView) dialogView.findViewById(R.id.img_share_kakao);
        ImageView imgShareFb = (ImageView) dialogView.findViewById(R.id.img_share_fb);
        ImageView imgShareZalo = (ImageView) dialogView.findViewById(R.id.img_share_zalo);

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(true);

        imgShareKakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                KakaoStoryService.requestPostLink(new StoryResponseCallback<MyStoryInfo>() {
                    @Override
                    public void onNotKakaoStoryUser() {

                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.d(TAG, "onSessionClosed: ");
                    }

                    @Override
                    public void onNotSignedUp() {
                        Log.d(TAG, "onNotSignedUp: ");
                    }

                    @Override
                    public void onSuccess(MyStoryInfo result) {
                        Log.d(TAG, "onSuccess: " + result);
                    }
                }, url, title);
            }
        });
        imgShareFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(url))
                            .build();
                    shareDialogFb.show(content);
                }
            }
        });

        imgShareZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                ZaloSDK.Instance.isAuthenticate(new ValidateOAuthCodeCallback() {
                    @Override
                    public void onValidateComplete(boolean isValidated, int errorCode, long userId, String oauthCode) {
                        if (isValidated) {
                            ZaloUtils.postToWall(context, url, title);
                        } else {
                            //TODO: Do Login Zalo before share
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

}
