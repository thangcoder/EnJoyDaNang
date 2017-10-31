package node.com.enjoydanang.ui.fragment.logcheckin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import node.com.enjoydanang.GlobalApplication;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.HistoryCheckin;
import node.com.enjoydanang.model.UserInfo;
import node.com.enjoydanang.utils.DialogUtils;
import node.com.enjoydanang.utils.Utils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.SeparatorDecoration;

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

    @BindView(R.id.txtFromDate)
    TextView txtFromDate;

    @BindView(R.id.txtToDate)
    TextView txtToDate;

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

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_log_checkin;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.txtFromDate, R.id.txtToDate})
    public void onClick(View view) {
        String strToDate = String.valueOf(txtToDate.getText());
        String fromDate = String.valueOf(txtFromDate.getText());
        switch (view.getId()) {
            case R.id.txtFromDate:
                initDatePicker(txtFromDate);
                if (StringUtils.isNotEmpty(strToDate) && StringUtils.isNotEmpty(fromDate)) {
                    if (Utils.hasLogin()) {
                        UserInfo userInfo = GlobalApplication.getUserInfo();
                        mvpPresenter.getListHistory(userInfo.getUserId(), fromDate, strToDate);
                    }
                }
                break;
            case R.id.txtToDate:
                initDatePicker(txtToDate);
                if (StringUtils.isNotEmpty(fromDate) && StringUtils.isNotEmpty(strToDate)) {
                    if (Utils.hasLogin()) {
                        UserInfo userInfo = GlobalApplication.getUserInfo();
                        mvpPresenter.getListHistory(userInfo.getUserId(), fromDate, strToDate);
                    }
                }
                break;
        }
    }

    private void initDatePicker(final TextView textView) {
        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar, textView);
            }

        };
        mDatePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.show();

    }

    private void updateLabel(Calendar calendar, TextView textView) {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        textView.setText(sdf.format(calendar.getTime()));
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
