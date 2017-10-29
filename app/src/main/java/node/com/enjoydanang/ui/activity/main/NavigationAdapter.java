package node.com.enjoydanang.ui.activity.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import node.com.enjoydanang.R;
import node.com.enjoydanang.model.NavigationItem;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class NavigationAdapter extends BaseAdapter {

    private Context mContext;
    private List<NavigationItem> lstItem;
    private final int TYPE_ITEM = 2;


    public NavigationAdapter(Context mContext, List<NavigationItem> lstItem) {
        this.mContext = mContext;
        this.lstItem = lstItem;
    }


    @Override
    public int getCount() {
        return CollectionUtils.isNotEmpty(lstItem) ? lstItem.size() : 0;
    }

    @Override
    public NavigationItem getItem(int position) {
        return lstItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isHeader() ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_ITEM;
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isHeader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        NavigationItem menuItem = lstItem.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            int layout = menuItem.isHeader() ? R.layout.drawer_nav_item_group : R.layout.drawer_nav_item;
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, viewGroup, false);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
            holder.txtTitleMenu = (TextView) convertView.findViewById(R.id.txt_title);
            holder.span = (View) convertView.findViewById(R.id.span);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.txtTitleMenu != null) {
            holder.txtTitleMenu.setText(menuItem.getTitle());
        }
        if (holder.imgIcon != null) {
            if (menuItem.getIcon() != 0) {
                holder.imgIcon.setImageResource(menuItem.getIcon());
            }
        }
        if(holder.span != null){
            if(position == lstItem.size() - 1){
                holder.span.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        public ImageView imgIcon;
        public TextView txtTitleMenu;
        public View span;
    }

}
