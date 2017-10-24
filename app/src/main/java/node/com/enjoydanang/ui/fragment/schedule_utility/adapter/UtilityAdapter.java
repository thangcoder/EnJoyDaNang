package node.com.enjoydanang.ui.fragment.schedule_utility.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Utility;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Author: Tavv
 * Created on 24/10/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class UtilityAdapter extends BaseAdapter {

    private Context context;
    private List<Utility> lstUtilities;

    public UtilityAdapter(Context context, List<Utility> lstUtilities) {
        this.context = context;
        this.lstUtilities = lstUtilities;
    }


    @Override
    public int getCount() {
        return CollectionUtils.isEmpty(lstUtilities) ? 0 : lstUtilities.size();
    }

    @Override
    public Utility getItem(int i) {
        return CollectionUtils.isEmpty(lstUtilities) ? null : lstUtilities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return CollectionUtils.isEmpty(lstUtilities) ? 0 : lstUtilities.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_utility, viewGroup, false);
            holder = new ViewHolder();
            holder.txtUtilityName = (TextView) view.findViewById(R.id.txtUtility);
            holder.imgIcon = (ImageView) view.findViewById(R.id.icUtility);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Utility utility = lstUtilities.get(position);
        holder.txtUtilityName.setText(utility.getName());
        ImageUtils.loadImageNoRadius(context, holder.imgIcon, utility.getIcon());
        return view;
    }

    static class ViewHolder {
        public TextView txtUtilityName;
        public ImageView imgIcon;
    }
}