package node.com.enjoydanang.ui.fragment.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.event.OnItemClickListener;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Author: Tavv
 * Created on 28/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder> {

    private Context context;

    private List<Partner> lstResult;

    private OnItemClickListener onItemClick;

    public SearchResultAdapter(Context context, List<Partner> lstResult, OnItemClickListener onItemClick) {
        this.lstResult = lstResult;
        this.context = context;
        this.onItemClick = onItemClick;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, final int position) {
        Partner partner = lstResult.get(position);
        if (partner != null) {
            holder.txtPartnerName.setText(partner.getName());
            ImageUtils.loadImageNoRadius(context, holder.imgPartner, partner.getPicture());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick.onClick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(lstResult) ? 0 : lstResult.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgPartner)
        ImageView imgPartner;

        @BindView(R.id.txtPartnerName)
        TextView txtPartnerName;

        View view;

        public SearchViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }

    public void updateResult(List<Partner> lstPartner){
        int oldSize = 0;
        if(CollectionUtils.isNotEmpty(lstResult)){
            oldSize = lstResult.size();
            lstResult.clear();
        }
        notifyItemRangeRemoved(0, oldSize);
        lstResult.addAll(lstPartner);
        notifyItemRangeChanged(0, lstResult.size());
        notifyDataSetChanged();
    }

    public void clearAllItem(){
        if(CollectionUtils.isNotEmpty(lstResult)){
            int size = lstResult.size();
            lstResult.clear();
            notifyItemRangeChanged(0, size);
            notifyDataSetChanged();
        }
    }
}
