package node.com.enjoydanang;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by tonkhanh on 2/27/17.
 */

public class LogApp {
    public static AlertDialog diag ;
    public static void log(String txt){
        if(BuildConfig.DEBUG){
            Log.i("Arubaito",txt);
        }
    }
    public static void log(String tag,String txt){
        if(BuildConfig.DEBUG){
            Log.i(tag,txt);
        }
    }
    public static void show(Context mContext, String txt){
        Toast.makeText(mContext,txt,Toast.LENGTH_SHORT).show();
    }
    public static  void showToastDebug(Context mContext, String txt){
        if(BuildConfig.DEBUG){
            show(mContext,txt);
        }
    }
//    public static void showDialog(Activity context,String error) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            //builder.setTitle(context.getResources().getString(R.string.dialog_title));
//            builder.setPositiveButton(context.getResources().getString(R.string.dialog_OK), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int arg1) {
//                    dialog.cancel();
//                }
//            });
//        builder.setMessage(error);
//        diag = builder.create();
//        if(!diag.isShowing()&& context!=null)
//        diag.show();
//    }
//    public static AlertDialog alertDialog(Activity context, String error) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        //builder.setTitle(context.getResources().getString(R.string.dialog_title));
//        builder.setPositiveButton(context.getResources().getString(R.string.dialog_OK), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int arg1) {
//                dialog.cancel();
//            }
//        });
//        builder.setMessage(error);
//        diag = builder.create();
//        return diag;
//    }
//    public static void showDialog(Activity context) {
//        if(diag!=null){
//            if(!diag.isShowing()){
//                diag.show();
//            }
//        }else{
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle(context.getResources().getString(R.string.dialog_title));
//            diag = builder.create();
//            diag.setCancelable(false);
//            diag.show();
//        }
//    }
    public static void hideDilog(){
        if(diag!=null&&diag.isShowing()){
            diag.hide();
        }
    }
}
