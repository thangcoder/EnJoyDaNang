package node.com.enjoydanang.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

    public static void loadResizeImage(Context context, ImageView imgView, Uri uri, float width, float height) {
        Picasso.with(context).load(uri).error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .resize(ScreenUtils.dp2px(width), ScreenUtils.dp2px(height))
                .into(imgView);
    }


    public static void loadResizeImage(Context context, ImageView imgView, String imgUrl, int width, int height) {
        Glide.with(context).load(imgUrl)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .override(width, height)
                .into(imgView);
    }


    public static void loadImageFromUri(Context context, ImageView imgView, Uri uri) {
        Picasso.with(context).load(uri).error(R.drawable.placeholder)
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
        Uri imageUri = Uri.parse(decodeURL(url));
        imgView.setImageURI(imageUri);
    }

    public static void loadImageWithFresoURI(SimpleDraweeView imgView, Uri uri) {
        imgView.setImageURI(uri);
    }

    public static String encodeTobase64(File file) {
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
    private static String rotateImage(int degree, String imagePath) {
        try{
            Bitmap b= decodeSampledBitmapFromResource(imagePath);
            Matrix matrix = new Matrix();
            if(b.getWidth()>b.getHeight()){
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 80, out);
            }else if (imageType.equalsIgnoreCase("jpeg")|| imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 80, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        return imagePath;
    }

    public static String getRightAngleImage(String photoPath) {
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree, photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoPath;
    }
    public static Bitmap decodeSampledBitmapFromResource(String patch) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(patch,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(patch, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int reqWidth, reqHeight;
        if(width>=1203){
            float per = (float)1203/width;
            reqWidth = (int) (width*per);
            reqHeight = (int) (height*per);
        }else if(width >= 730 && width < 1203){
            float per = (float)730/width;
            reqWidth = (int) (width*per);
            reqHeight = (int) (height*per);
        }
        else if(width >= 441 && width < 730){
            float per = (float)441/width;
            reqWidth = (int) (width*per);
            reqHeight = (int) (height*per);
        }else{
            float per = (float)376/width;
            reqWidth = (int) (width*per);
            reqHeight = (int) (height*per);
        }

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}