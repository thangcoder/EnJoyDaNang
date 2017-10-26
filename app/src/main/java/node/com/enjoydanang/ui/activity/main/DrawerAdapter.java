package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.content.res.TypedArray;
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
    private static final String TAG = DrawerAdapter.class.getSimpleName();
    private final int[] CATEGORY_INDEX = new int[]{0, 5};

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
        String[] titles = context.getResources().getStringArray(R.array.item_menu);
        switch (type) {
            case Group:
                view = mInflater.inflate(R.layout.drawer_nav_item_group, viewGroup, false);
                TextView txtTitle = (TextView) view.findViewById(R.id.txt_group_item_title);
                txtTitle.setText(titles[i]);
                return view;
            case Normal:
                view = mInflater.inflate(R.layout.drawer_nav_item, viewGroup, false);
                TextView txtTitleNormal = (TextView) view.findViewById(R.id.txt_normal_title);
                ImageView image = (ImageView) view.findViewById(R.id.img_icon);
                View span = (View) view.findViewById(R.id.span);
                hideSpan(span, i, titles);
                txtTitleNormal.setText(titles[i]);
                image.setImageResource(imgs.getResourceId(i, -1));
                return view;
        }
        return null;
    }

    private enum ItemType {
        Group, Normal
    }

    private static class NormalItemHolder {
        TextView txtTitle, txtNormal;
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

    private void hideSpan(View span, int position, String[] titles) {
        if (span != null) {
            for (int i = 0; i < CATEGORY_INDEX.length; i++) {
                if((CATEGORY_INDEX[i]> 0 && position == CATEGORY_INDEX[i] - 1) || position == titles.length - 1){
                    span.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}