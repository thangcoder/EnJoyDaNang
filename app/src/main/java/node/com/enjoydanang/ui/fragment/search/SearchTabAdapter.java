package node.com.enjoydanang.ui.fragment.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import node.com.enjoydanang.utils.event.OnFetchSearchResult;

/**
 * Author: Tavv
 * Created on 12/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class SearchTabAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    private OnFetchSearchResult onFetchSearchResult;
    private String data;
    public SearchTabAdapter(FragmentManager fm, String[] tabTitles, OnFetchSearchResult onFetchSearchResult,String data) {
        super(fm);
        this.tabTitles = tabTitles;
        this.onFetchSearchResult = onFetchSearchResult;
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MapResultFragment.getIntance();
            case 1:
                return PartnerSearchResultFragment.getIntance(onFetchSearchResult,data);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public String[] getTabTitles() {
        return tabTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
