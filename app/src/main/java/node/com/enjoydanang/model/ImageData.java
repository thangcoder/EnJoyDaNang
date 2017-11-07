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
    private String url;

    public ImageData() {
    }

    public ImageData(Bitmap icon, Uri uri, String url) {
        this.icon = icon;
        this.uri = uri;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
