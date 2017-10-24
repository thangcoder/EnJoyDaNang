package node.com.enjoydanang.ui.fragment.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import node.com.enjoydanang.ui.fragment.album.AlbumDetailFragment;
import node.com.enjoydanang.ui.fragment.schedule_utility.ScheduleUtilityFragment;

/**
 * Author: Tavv
 * Created on 10/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class DetailPagerAdapter extends FragmentStatePagerAdapter {
    //integer to count number of tabs
    private int tabCount;
    private int partnerId;
    //Constructor to the class
    public DetailPagerAdapter(FragmentManager fm, int tabCount, int partnerId) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
        this.partnerId = partnerId;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return DetailPartnerFragment.newInstance(partnerId);
            case 1:
                return ScheduleUtilityFragment.newInstance(partnerId);
            case 2:
                return AlbumDetailFragment.newInstance(partnerId);
            default:
                return null;
        }
    }



    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
