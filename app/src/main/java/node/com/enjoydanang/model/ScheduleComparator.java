package node.com.enjoydanang.model;

import java.util.Comparator;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class ScheduleComparator implements Comparator<Schedule> {

    @Override
    public int compare(Schedule a, Schedule b) {
        return a.getSTT() < b.getSTT() ? -1 : a.getSTT() == b.getSTT() ? 0 : 1;
    }
}
