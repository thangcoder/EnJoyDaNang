package node.com.enjoydanang.ui.fragment.scan;

import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import node.com.enjoydanang.MvpFragmentWithToolbar;
import node.com.enjoydanang.R;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Author: Tavv
 * Created on 18/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScanQRCodeFragment extends MvpFragmentWithToolbar<ScanQRCodePresenter> implements ScanQRCodeView, QRCodeView.Delegate{

    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;

    @Override
    protected ScanQRCodePresenter createPresenter() {
        return new ScanQRCodePresenter(this);
    }

    @Override
    public void setupActionBar() {

    }

    @Override
    protected void init(View view) {
        mQRCodeView.setDelegate(this);
    }

    @Override
    protected void setEvent(View view) {
        mQRCodeView.hiddenScanRect();
    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_scan_qr_code;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        Toast.makeText(mMainActivity, result, Toast.LENGTH_SHORT).show();
        vibrate();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    public void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
    }

    @Override
    public void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) mMainActivity.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
