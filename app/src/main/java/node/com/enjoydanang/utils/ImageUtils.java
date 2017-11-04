package node.com.enjoydanang.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;

import node.com.enjoydanang.R;
import node.com.enjoydanang.utils.helper.RoundedTransformation;

;

/**
 * Created by quangphuoc on 10/27/16.
 */

public class ImageUtils {
    public static void loadImage(Context context, ImageView imgView, String imgUrl) {
        Picasso.with(context).load(imgUrl)
                .transform(new RoundedTransformation(50, 0))
//                .error(R.drawable.error_image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
//                .centerInside()
                .fit()
                .into(imgView);
    }


    public static void loadImageRounded(Context context, ImageView imgView, String imgUrl) {
        Picasso.with(context).load(imgUrl)
                .transform(new CircleTransform())
//                .error(R.drawable.error_image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
//                .centerInside()
                .fit()
                .into(imgView);
    }

    public static void loadImageDrawable(Context context, ImageView imgView) {
        Picasso.with(context).load(R.drawable.img_product)
//                .transform(new RoundedTransformation(50, 0))
//                .error(R.drawable.error_image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)

                .into(imgView);
    }


    public static void loadImageNoRadius(Context context, ImageView imgView, String imgUrl) {
        Picasso.with(context).load(imgUrl)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .fit()
                .into(imgView);
    }
    public static void loadImageFromFile(Context context, ImageView imgView, File file) {
        Picasso.with(context).load(file)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .fit()
                .into(imgView);
    }
    public static void loadImageWithFreso(SimpleDraweeView imgView, String url) {
        Uri  imageUri = Uri.parse(decodeURL(url));
        imgView.setImageURI(imageUri);
    }
    public static void loadImageWithFresoURI(SimpleDraweeView imgView, Uri uri) {
        imgView.setImageURI(uri);
    }

    public static String encodeTobase64( File file ) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }
        return StringUtils.EMPTY;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public static String decodeURL(String url) {
        String link = null;
        String image = null;
        if (!TextUtils.isEmpty(url)) {
            link = url.substring(0, url.lastIndexOf('/') + 1);
            image = URLEncoder.encode(url.substring(link.length(), url.length()));
        }
        return link + image;
    }


}