package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.R;
import node.com.enjoydanang.constant.Constant;
import node.com.enjoydanang.model.NavigationItem;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class NavigationListItem {

    public static List<NavigationItem> getNavigationAdapter(Context context, List<Integer> lstIndexHeader, List<Integer> lstIndexItemHide, int[] icons, String[] titles) {

        List<NavigationItem> mList = new ArrayList<NavigationItem>();
        boolean isheader = false;
        boolean isVisible = false;

        for (int i = 0; i < titles.length; i++) {

            String title = titles[i];
            NavigationItem itemNavigation;

            if (lstIndexHeader != null) {
                isheader = lstIndexHeader.contains(i);
            }

            if (lstIndexItemHide != null) {
                isVisible = lstIndexItemHide.contains(i);
            }

            itemNavigation = new NavigationItem(title, icons[i], isheader , !isVisible);
            mList.add(itemNavigation);
        }

        return mList;
    }
}
