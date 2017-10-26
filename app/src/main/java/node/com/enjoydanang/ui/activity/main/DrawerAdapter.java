package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        return 9;
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
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.imgs);
        ItemType type = getItemType(i);
//        if (view == null) {
//            holder = new NormalItemHolder();
//            switch (type) {
//                case Group:
//                    view = mInflater.inflate(R.layout.drawer_nav_item_group, viewGroup, false);
//                    holder.txtTitle = (TextView) view.findViewById(R.id.txt_group_item_title);
//
//                    break;
//                case Normal:
//                    view = mInflater.inflate(R.layout.drawer_nav_item, viewGroup, false);
//                    holder.txtNormal = (TextView) view.findViewById(R.id.txt_normal_title);
//                    holder.image = (ImageView) view.findViewById(R.id.img_icon);
//                    break;
//            }
//            view.setTag(holder);
//        } else {
//            holder = (NormalItemHolder) view.getTag();
//        }
//        switch (type) {
//            case Group:
//                holder.txtTitle.setText(context.getResources().getStringArray(R.array.item_menu)[i]);
//                break;
//            case Normal:
//                holder.txtNormal.setText(context.getResources().getStringArray(R.array.item_menu)[i]);
//                holder.image.setImageResource(imgs.getResourceId(i, -1));
//                break;
//            default:
//                break;
//        }
        Log.d("CHENG", "getView: "+context.getResources().getStringArray(R.array.item_menu)[i]);

        switch (type) {
            case Group:
//                GroupItemHolder groupItemHolder = null;
//                View v = view;
//                if (view == null) {
//                    groupItemHolder = new GroupItemHolder();
                    view = mInflater.inflate(R.layout.drawer_nav_item_group, viewGroup, false);
                    TextView txtTitle = (TextView) view.findViewById(R.id.txt_group_item_title);
//                    groupItemHolder.txtTitle = (TextView) view.findViewById(R.id.txt_group_item_title);
//                    view.setTag(groupItemHolder);
//                }else {
//                    groupItemHolder = (GroupItemHolder) view.getTag();
//                }
//                groupItemHolder.txtTitle.setText(context.getResources().getStringArray(R.array.item_menu)[i]);
                txtTitle.setText(context.getResources().getStringArray(R.array.item_menu)[i]);
                return view;
            case Normal:
//                NormalItemHolder normalItemHolder = null;
//                if (view == null) {
//                    normalItemHolder = new NormalItemHolder();
                    view = mInflater.inflate(R.layout.drawer_nav_item, viewGroup, false);
//                    normalItemHolder.txtTitle = (TextView) view.findViewById(R.id.txt_group_item_title);
//                    normalItemHoldenormalItemHolder.txtTitle = (TextView) view.findViewById(R.id.txt_group_item_title);
                        TextView txtTitleNormal = (TextView) view.findViewById(R.id.txt_normal_title);
                     ImageView image = (ImageView) view.findViewById(R.id.img_icon);
//                    view.setTag(normalItemHolder);
//                }else {
//                    normalItemHolder = (NormalItemHolder) view.getTag();
//                }
                txtTitleNormal.setText(context.getResources().getStringArray(R.array.item_menu)[i]);
                image.setImageResource(imgs.getResourceId(i, -1));
                return view;
        }
        return null;
    }

    private enum ItemType {
        Group, Normal
    }

    private static class NormalItemHolder {
        TextView txtTitle,txtNormal;
        ImageView image;
    }

    private static class GroupItemHolder {
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