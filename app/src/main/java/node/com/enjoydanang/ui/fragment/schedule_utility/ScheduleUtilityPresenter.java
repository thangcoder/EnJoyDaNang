package node.com.enjoydanang.ui.fragment.schedule_utility;

import java.util.Collections;
import java.util.List;

import node.com.enjoydanang.BasePresenter;
import node.com.enjoydanang.api.ApiCallback;
import node.com.enjoydanang.api.model.Repository;
import node.com.enjoydanang.constant.AppError;
import node.com.enjoydanang.model.Schedule;
import node.com.enjoydanang.model.ScheduleComparator;
import node.com.enjoydanang.model.Utility;
import node.com.enjoydanang.utils.Utils;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScheduleUtilityPresenter extends BasePresenter<ScheduleUtilityView>{
    public ScheduleUtilityPresenter(ScheduleUtilityView view) {
        super(view);
    }

    void getSchedule(int partnerId){
        addSubscription(apiStores.getScheduleByPartnerId(partnerId), new ApiCallback<Repository<Schedule>>(){

            @Override
            public void onSuccess(Repository<Schedule> model) {
                mvpView.hideLoading();
                if(Utils.isNotEmptyContent(model)){
                    List<Schedule> schedules = model.getData();
                    Collections.sort(schedules, new ScheduleComparator());
                    mvpView.onFetchScheduleSuccess(schedules);
                }else{
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onFetchFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getUtility(int partnerId){
        addSubscription(apiStores.getUtilityByPartnerId(partnerId), new ApiCallback<Repository<Utility>>(){

            @Override
            public void onSuccess(Repository<Utility> model) {
                mvpView.hideLoading();
                if(Utils.isNotEmptyContent(model)){
                    mvpView.onFetchUtilitySuccess(model.getData());
                }else{
                    mvpView.onFetchFailure(new AppError(new Throwable(model.getMessage())));
                }
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
                mvpView.onFetchFailure(new AppError(new Throwable(msg)));
            }

            @Override
            public void onFinish() {

            }
        });
    }
}
