package node.com.enjoydanang.model;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Author: Tavv
 * Created on 06/11/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class ImageData {
    private Bitmap icon;
    private Uri uri;

    public ImageData() {
    }

    public ImageData(Bitmap icon, Uri uri) {
        this.icon = icon;
        this.uri = uri;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
