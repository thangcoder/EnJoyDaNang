package node.com.enjoydanang.ui.fragment.home;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.model.Data;
import node.com.enjoydanang.model.Datum;
import node.com.enjoydanang.model.MenuItem;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Created by chien on 10/8/17.
 */

public class MenuAdapter extends BaseAdapter {
    Context context;
    List<Datum> categories;
    private static LayoutInflater inflater=null;

    public MenuAdapter(Context context, List<Datum> menuItems) {
        this.context = context;
        this.categories = menuItems;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if(view==null){
            view = inflater.inflate(R.layout.item_home_menu, null);
            holder = new Holder(view);
            view.setTag(holder);
        }else {
            holder=(Holder)view.getTag();
        }
        Datum data = categories.get(i);
        holder.tvName.setText(data.getName());
        ImageUtils.loadImageNoRadius(context,holder.imgIcon,data.getPicture());
        return view;
    }
    static final class  Holder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.img_icon)
        ImageView imgIcon;

        Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}