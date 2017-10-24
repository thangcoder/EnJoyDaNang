package node.com.enjoydanang.ui.fragment.schedule_utility;

import java.util.List;

import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.iBaseView;
import node.com.enjoydanang.model.Schedule;
import node.com.enjoydanang.model.Utility;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public interface ScheduleUtilityView extends iBaseView {

    void onFetchUtilitySuccess(List<Utility> utilities);

    void onFetchScheduleSuccess(List<Schedule> schedules);

    void onFetchFailure(AppError error);

}
