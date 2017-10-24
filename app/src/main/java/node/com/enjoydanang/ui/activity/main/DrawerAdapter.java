package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import node.com.enjoydanang.R;

/**
 * Created by chien on 10/25/17.
 */

public class DrawerAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;

    public DrawerAdapter(Context context) {
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemType(position) != ItemType.Group;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //== inflate views
        NormalItemHolder holder;
        ItemType type = getItemType(i);
        if (view == null) {
            holder = new NormalItemHolder();
            switch (type) {
                case Group:
                    //inflate the new layout
                    view = mInflater.inflate(R.layout.drawer_nav_item_group, viewGroup, false);
                    holder.txtTitle = (TextView) view.findViewById(R.id.txt_group_item_title);
                    Log.d("group", "getView: "+i+"-----"+context.getResources().getStringArray(R.array.item_menu)[i]);

                    break;

                case Normal:
                    //inflate the new layout
                    view = mInflater.inflate(R.layout.drawer_nav_item, viewGroup, false);
                    holder.txtTitle = (TextView) view.findViewById(R.id.txt_item_title);
                    Log.d("group", "getView: "+i+"-----"+context.getResources().getStringArray(R.array.item_menu)[i]);

                    break;
                default:
                    break;
            }
            view.setTag(holder);
        } else {
            holder = (NormalItemHolder) view.getTag();
        }
        switch (type) {
            case Group:
                holder.txtTitle.setText(context.getResources().getStringArray(R.array.item_menu)[i]);

                break;
            case Normal:
                holder.txtTitle.setText(context.getResources().getStringArray(R.array.item_menu)[i]);
                break;
            default:
                break;
        }
        return view;
    }
    enum ItemType {
        Group, Normal
    }

    private static class NormalItemHolder {
        TextView txtTitle;
    }
    private static ItemType getItemType(int position) {
        switch (position) {
            case 0:
            case 5:
                return ItemType.Group;
            default:
                return ItemType.Normal;
        }
    }

}