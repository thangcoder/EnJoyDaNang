package node.com.enjoydanang.ui.fragment.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Category;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Created by chien on 10/8/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    List<Category> partners;
    Context mContext;
    public ProductAdapter(Context context, List<Category> partners) {
        mContext = context;
        this.partners = partners;
    }
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_product, parent, false);
        ProductAdapter.ViewHolder vh = new ProductAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        Category datum = partners.get(position);
        holder.tvTitle.setText(datum.getName());
        ImageUtils.loadImageNoRadius(mContext,holder.imgPhoto,datum.getPicture());
    }

    @Override
    public int getItemCount() {
        return partners.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
//        public SimpleDraweeView imgPhoto;
        public final View mView;
        @BindView(R.id.img_photo)
        ImageView imgPhoto;
        @BindView(R.id.tv_title)
         TextView tvTitle;
        @BindView(R.id.tv_meta)
         TextView tvMeta;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            ButterKnife.bind(this, mView);
        }
    }
}