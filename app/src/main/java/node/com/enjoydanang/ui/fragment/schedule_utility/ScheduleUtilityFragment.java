package node.com.enjoydanang.ui.fragment.schedule_utility;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.MvpFragment;
import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Schedule;
import node.com.enjoydanang.model.Utility;
import node.com.enjoydanang.ui.fragment.schedule_utility.adapter.ScheduleAdapter;
import node.com.enjoydanang.ui.fragment.schedule_utility.adapter.UtilityAdapter;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScheduleUtilityFragment extends MvpFragment<ScheduleUtilityPresenter> implements ScheduleUtilityView {
    private static final String TAG = ScheduleUtilityFragment.class.getSimpleName();

    @BindView(R.id.lvUtility)
    ListView lvUtility;
    @BindView(R.id.lvSchedule)
    ListView lvSchedule;

    public static ScheduleUtilityFragment newInstance(int partnerId) {
        ScheduleUtilityFragment fragment = new ScheduleUtilityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG, partnerId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showToast(String desc) {

    }

    @Override
    public void unKnownError() {

    }

    @Override
    protected ScheduleUtilityPresenter createPresenter() {
        return new ScheduleUtilityPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
        Bundle bundle = getArguments();
        if (bundle != null) {
            int partnerId = bundle.getInt(TAG);
            showLoading();
            mvpPresenter.getSchedule(partnerId);
            mvpPresenter.getUtility(partnerId);
        }
    }

    @Override
    protected void init(View view) {

    }

    @Override
    protected void setEvent(View view) {

    }

    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_schedule_utility;
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    public void onFetchUtilitySuccess(List<Utility> utilities) {
        UtilityAdapter adapter = new UtilityAdapter(getContext(), utilities);
        lvUtility.setAdapter(adapter);
    }

    @Override
    public void onFetchScheduleSuccess(List<Schedule> schedules) {
        ScheduleAdapter adapter = new ScheduleAdapter(getContext(), schedules);
        lvSchedule.setAdapter(adapter);
    }

    @Override
    public void onFetchFailure(AppError error) {

    }
}
