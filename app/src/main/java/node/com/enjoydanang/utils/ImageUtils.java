package node.com.enjoydanang.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

;import com.squareup.picasso.Picasso;

import node.com.enjoydanang.R;

/**
 * Created by quangphuoc on 10/27/16.
 */

public class ImageUtils {
    public static void loadImage(Context context, ImageView imgView, String imgUrl){
        Picasso.with(context).load(imgUrl)
                .transform(new RoundedTransformation(50, 0))
//                .error(R.drawable.error_image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
//                .centerInside()
                .fit()
                .into(imgView);
    }
    public static void loadImageDrawable(Context context, ImageView imgView){
        Picasso.with(context).load(R.drawable.img_product)
//                .transform(new RoundedTransformation(50, 0))
//                .error(R.drawable.error_image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
//                .centerInside()
//                .fit()
                .into(imgView);
    }



    public static void loadImageNoRadius(Context context, ImageView imgView, String imgUrl){
        Picasso.with(context).load(imgUrl)
//                .error(R.drawable.error_image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
//                .centerInside()
                .fit()
                .into(imgView);
    }


//    public static void loadImageNoRadius(Context context,ImageView imageView,String url){
//        Glide.with(context)
//                .load(url)
////                .transform(new CircleTransform(context))
//                .error(R.drawable.placeholder)
//                .placeholder(R.drawable.placeholder)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .into((imageView));
//    }
     /*
        ImageView
         */
//    public static void loadAvatar(Context context, ImageView imgView, String imgUrl) {
//        if(imgUrl == null)
//        {
//            imgView.setImageResource(R.drawable.default_avatar);
//            return;
//        }
//        Picasso.with(context).load(imgUrl)
//                .transform(new RoundedTransformation(50, 0))
//                .error(R.drawable.ic_baby_default)
//                .placeholder(R.drawable.placeholder)
//                .into(imgView);
//    }
//


}