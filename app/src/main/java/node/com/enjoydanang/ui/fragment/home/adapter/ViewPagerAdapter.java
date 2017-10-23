package node.com.enjoydanang.ui.fragment.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import node.com.enjoydanang.model.Banner;
import node.com.enjoydanang.ui.fragment.home.PageFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Banner> banners;

    public ViewPagerAdapter(FragmentManager fm, List<Banner> bannerList) {
        super(fm);
        banners = bannerList;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.getInstance(banners.get(position).getPicture());
    }

    @Override
    public int getCount() {
        return banners.size();
    }
}
