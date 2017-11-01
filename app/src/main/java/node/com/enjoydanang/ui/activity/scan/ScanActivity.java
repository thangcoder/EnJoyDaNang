package node.com.enjoydanang.ui.activity.scan;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.Result;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.PromptDialog;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import node.com.enjoydanang.MvpActivity;
import node.com.enjoydanang.R;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.HistoryCheckin;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.helper.LanguageHelper;
import node.com.enjoydanang.utils.widget.NumberTextWatcher;

import static node.com.enjoydanang.R.id.edtName;

/**
 * Author: Tavv
 * Created on 30/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScanActivity extends MvpActivity<ScanQRCodePresenter> implements ScanQRCodeView, ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    private UserInfo userInfo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.scan_container)
    ViewGroup contentFrame;

    private AlertDialog alertDialog;

    @Override
    public void handleResult(Result result) {

        if (result != null) {
            vibrate();
            mvpPresenter.getInfoScanQr(result.getText());
        }

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanActivity.this);
            }
        }, 500);
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    private void showDialogInfo(@NonNull final Partner partner) {
        final String formatPartnerName = Utils.getLanguageByResId(R.string.Name).concat(": %s");
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_scan_qr_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AppCompatButton btnOk = (AppCompatButton) dialogView.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = (AppCompatButton) dialogView.findViewById(R.id.btnCancel);
        TextView txtPartnerName = (TextView) dialogView.findViewById(R.id.txtPartnerName);
        ImageView imgPartner = (ImageView) dialogView.findViewById(R.id.imgPartner);
        TextView txtDiscount = (TextView) dialogView.findViewById(R.id.txtDiscount);
        final EditText edtAmount = (EditText) dialogView.findViewById(R.id.edtAmount);
        LanguageHelper.getValueByViewId(edtAmount, txtPartnerName, txtDiscount, btnCancel, btnOk);
        edtAmount.addTextChangedListener(new NumberTextWatcher(edtAmount));
        txtPartnerName.setText(String.format(Locale.getDefault(), formatPartnerName, partner.getName()));
        txtDiscount.setText(Utils.getLanguageByResId(R.string.Discount) + ": " + partner.getDiscount() + " (%)");
        ImageUtils.loadImageNoRadius(ScanActivity.this, imgPartner, partner.getPicture());
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount;
                try {
                    amount = Integer.valueOf(edtAmount.getText().toString());
                } catch (Exception e) {
                    DialogUtils.showDialog(ScanActivity.this, 4, Utils.getLanguageByResId(R.string.Dialog_Title_Warning),
                            Utils.getLanguageByResId(R.string.Message_Wrong_Amount));
                    return;
                }
                if (amount != 0) {
                    if (Utils.hasLogin()) {
                        v.setEnabled(false);
                        mvpPresenter.requestOrder(partner.getId(), userInfo.getUserId(), amount);
                    } else {
                        v.setEnabled(false);
                        mvpPresenter.requestOrder(partner.getId(), 0, amount);
                    }
                } else {
                    DialogUtils.showDialog(ScanActivity.this, 4, Utils.getLanguageByResId(R.string.Dialog_Title_Warning),
                            Utils.getLanguageByResId(R.string.Message_Wrong_Amount_Empty));
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.show();
        alertDialog.getWindow().setAttributes(lp);
    }

    @Override
    public void onFetchInfoSuccess(Partner partner) {
        showDialogInfo(partner);
    }

    @Override
    public void onRequestOrderSuccess(HistoryCheckin response) {
        String resultPattern = "%s\n%s: %s\n%s: %s%s\n%s: %s";
        String strAmount = NumberFormat.getInstance().format(response.getAmount());
        String strPayment = NumberFormat.getInstance().format(response.getPayment());
        String result = String.format(Locale.getDefault(), resultPattern,
                Utils.getLanguageByResId(R.string.Message_Payment_Success),
                Utils.getLanguageByResId(R.string.Amount), strAmount,
                Utils.getLanguageByResId(R.string.Discount), response.getDiscount(), "%",
                Utils.getLanguageByResId(R.string.Action_Payment), strPayment);
        DialogUtils.showDialog(ScanActivity.this, 3, Constant.TITLE_SUCCESS,
                result, new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog promptDialog) {
                        promptDialog.dismiss();
                        if (alertDialog != null && alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    public void onFetchError(AppError appError) {
        DialogUtils.showDialog(ScanActivity.this, 1, Utils.getLanguageByResId(R.string.Dialog_Title_Wrong), appError.getMessage());
    }

    @Override
    public void onRequestOrderSuccessError(AppError appError) {
        DialogUtils.showDialog(ScanActivity.this, 1, Utils.getLanguageByResId(R.string.Dialog_Title_Wrong), appError.getMessage());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransitionExit();
    }

    @Override
    protected ScanQRCodePresenter createPresenter() {
        return new ScanQRCodePresenter(this);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.fragment_scan);
    }

    @Override
    public void init() {
        userInfo = Utils.getUserInfo();
        initToolbar(toolbar);
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransitionExit();
            }
        });
    }


    private static class CustomViewFinderView extends ViewFinderView {
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setSquareViewFinder(true);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(StringUtils.EMPTY, tradeMarkLeft, tradeMarkTop, PAINT);
        }
    }
}
