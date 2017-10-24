package node.com.enjoydanang.ui.fragment.schedule_utility.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Schedule;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScheduleAdapter extends BaseAdapter {

    private Context context;
    private List<Schedule> lstSchedules;

    public ScheduleAdapter(Context context, List<Schedule> lstSchedules) {
        this.context = context;
        this.lstSchedules = lstSchedules;
    }


    @Override
    public int getCount() {
        return CollectionUtils.isEmpty(lstSchedules) ? 0 : lstSchedules.size();
    }

    @Override
    public Schedule getItem(int i) {
        return CollectionUtils.isEmpty(lstSchedules) ? null : lstSchedules.get(i);
    }

    @Override
    public long getItemId(int i) {
        return CollectionUtils.isEmpty(lstSchedules) ? 0 : lstSchedules.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_schedule, viewGroup, false);
            holder = new ViewHolder();
            holder.txtDayOfWeek = (TextView) view.findViewById(R.id.txtDayOfWeek);
            holder.txtSchedule = (TextView) view.findViewById(R.id.txtSchedule);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Schedule schedule = lstSchedules.get(position);
        holder.txtDayOfWeek.setText(schedule.getDayOfWeek());
        holder.txtSchedule.setText(schedule.getOpen() + " : " + schedule.getClose());
        return view;
    }

    static class ViewHolder {
        public TextView txtDayOfWeek;
        public TextView txtSchedule;
    }
}
