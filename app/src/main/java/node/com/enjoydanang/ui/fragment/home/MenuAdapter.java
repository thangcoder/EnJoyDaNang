package node.com.enjoydanang.ui.fragment.home;

import android.content.Context;
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
import node.com.enjoydanang.model.MenuItem;

/**
 * Created by chien on 10/8/17.
 */

public class MenuAdapter extends BaseAdapter {
    Context context;
    List<MenuItem> menuItems;
    private static LayoutInflater inflater=null;

    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int i) {
        return menuItems.get(i);
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
        MenuItem menuItem = menuItems.get(i);
        holder.tvName.setText(menuItem.getName());
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
