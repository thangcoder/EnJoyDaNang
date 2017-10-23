package node.com.enjoydanang.ui.fragment.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Created by chien on 10/8/17.
 */

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categories;
    private static LayoutInflater inflater = null;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        inflater = (LayoutInflater) context.
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
    public long getItemId(int position) {
        return CollectionUtils.isNotEmpty(categories) ? categories.get(position).getId() : 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_home_menu, viewGroup, false);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        Category category = categories.get(i);
        holder.tvName.setText(category.getName());
        ImageUtils.loadImageNoRadius(context, holder.imgCategory, category.getIcon());
        return view;
    }

    static final class Holder {
        @BindView(R.id.tv_name)
        TextView tvName;

        @BindView(R.id.img_icon)
        ImageView imgCategory;

        Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}