package android.com.enjoydanang.ui.activity.main;

import android.app.Activity;
import android.com.enjoydanang.BasePresenter;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

/**
 * Created by chientruong on 3/27/17.
 */

public class MainPresenter extends BasePresenter<MainView> {
    public MainPresenter(MainView view) {
        attachView(view);
    }


}
