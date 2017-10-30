package node.com.enjoydanang.ui.fragment.logcheckin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.HistoryCheckin;
import node.com.enjoydanang.model.Review;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.SeparatorDecoration;

import static node.com.enjoydanang.R.id.edtToDate;
import static node.com.enjoydanang.R.id.rcvFavorite;

/**
 * Author: Tavv
 * Created on 30/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class CheckinHistoryFragment extends MvpFragment<CheckinHistoryPresenter> implements CheckinHistoryView,
        OnItemClickListener {
    private static final String TAG = CheckinHistoryFragment.class.getSimpleName();
    private static final int VERTICAL_ITEM_SPACE = 5;

    @BindView(R.id.edtFromDate)
    EditText edtFromDate;

    @BindView(R.id.edtToDate)
    EditText edtToDate;

    @BindView(R.id.rcvHistoryCheckin)
    RecyclerView rcvHistoryCheckin;

    private CheckinHistoryAdapter mAdapter;

    private List<HistoryCheckin> mLstHistoryCheckins;

    private DatePickerDialog mDatePickerDialog;

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected CheckinHistoryPresenter createPresenter() {
        return new CheckinHistoryPresenter(this);
    }

    @Override
    protected void init(View view) {
        mBaseActivity.getToolbar().setTitle(Utils.getString(R.string.Log_Check_Screen_Title));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvHistoryCheckin.addItemDecoration(new SeparatorDecoration(getContext(), Utils.getColorRes(R.color.grey_700), VERTICAL_ITEM_SPACE));
        rcvHistoryCheckin.setLayoutManager(layoutManager);
        rcvHistoryCheckin.setHasFixedSize(false);
        mLstHistoryCheckins = new ArrayList<>();
        mAdapter = new CheckinHistoryAdapter(mLstHistoryCheckins, getContext(), this);
        rcvHistoryCheckin.setAdapter(mAdapter);
    }

    @Override
    protected void setEvent(View view) {
        edtFromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strToDate = edtToDate.getText().toString();
                if (editable != null) {
                    String fromDate = editable.toString();
                    if (StringUtils.isNotEmpty(strToDate) && StringUtils.isNotEmpty(fromDate)) {
                        if (Utils.hasLogin()) {
                            UserInfo userInfo = GlobalApplication.getUserInfo();
                            mvpPresenter.getListHistory(userInfo.getUserId(), fromDate, strToDate);
                        }
                    }
                }
            }
        });

        edtToDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strFromDate = edtFromDate.getText().toString();
                if (editable != null) {
                    String toDate = editable.toString();
                    if (StringUtils.isNotEmpty(strFromDate) && StringUtils.isNotEmpty(toDate)) {
                        if (Utils.hasLogin()) {
                            UserInfo userInfo = GlobalApplication.getUserInfo();
                            mvpPresenter.getListHistory(userInfo.getUserId(), strFromDate, toDate);
                        }
                    }
                }
            }
        });

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_log_checkin;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @OnFocusChange({R.id.edtFromDate, R.id.edtToDate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edtFromDate:
                initDatePicker(edtFromDate);
                break;
            case R.id.edtToDate:
                initDatePicker(edtToDate);
                break;
        }
    }

    private void initDatePicker(final EditText editText) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, editText);
            }

        };
        mDatePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();

    }

    private void updateLabel(Calendar calendar, EditText editText) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        editText.setText(sdf.format(calendar.getTime()));
    }


    @Override
    public void onFetchHistorySuccess(List<HistoryCheckin> lstHistoryCheckins) {
        updateItems(lstHistoryCheckins);
        if(mDatePickerDialog.isShowing()){
            mDatePickerDialog.dismiss();
        }
    }

    @Override
    public void onFetchFailure(AppError error) {
        DialogUtils.showDialog(getContext(), 2, Constant.TITLE_ERROR, error.getMessage());
    }

    @Override
    public void onClick(View view, int position) {
        //TODO click history log
    }

    public void updateItems(List<HistoryCheckin> lstHistories) {
        int oldSize = this.mLstHistoryCheckins.size();
        int newSize = lstHistories.size();
        this.mLstHistoryCheckins.addAll(lstHistories);
        mAdapter.notifyItemRangeChanged(0, oldSize + newSize);
        mAdapter.notifyDataSetChanged();
    }

}
