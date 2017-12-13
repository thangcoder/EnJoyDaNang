package node.com.enjoydanang.ui.fragment.search;

import android.content.Context;
import android.location.Address;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Partner;
import node.com.enjoydanang.utils.ImageUtils;
import node.com.enjoydanang.utils.event.OnItemClickListener;
import node.com.enjoydanang.utils.helper.LocationHelper;

/**
 * Author: Tavv
 * Created on 12/12/2017.
 * Project Name: EnJoyDaNang
 * Version : 1.0
 */

public class SearchPartnerResultAdapter extends RecyclerView.Adapter<SearchPartnerResultAdapter.SearchPartnerResultVH> {

    private List<Partner> lstPartners;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private LocationHelper mLocationHelper;


    public SearchPartnerResultAdapter(List<Partner> lstPartners, Context context,
                                      OnItemClickListener onItemClickListener,
                                      LocationHelper mLocationHelper) {
        this.lstPartners = lstPartners;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.mLocationHelper = mLocationHelper;
    }

    @Override
    public SearchPartnerResultVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_partner, parent, false);
        return new SearchPartnerResultVH(view);
    }

    @Override
    public void onBindViewHolder(SearchPartnerResultVH holder, final int position) {
        Partner model = lstPartners.get(position);
        holder.setIsRecyclable(false);
        if (model != null) {
            holder.txtPartnerName.setText(model.getName());
            holder.txtAddress.setText(getAddress(model));
            ImageUtils.loadImageNoRadius(context, holder.imgPartner, model.getPicture());
            if (StringUtils.isNotBlank(model.getDistance()) &&
                    !StringUtils.equals(model.getDistance().trim(), "km") &&
                    model.isDisplayDistance()) {
                holder.txtDistance.setText(model.getDistance());
            } else {
                holder.txtDistance.setVisibility(View.GONE);
            }

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.isEmpty(lstPartners) ? 0 : lstPartners.size();
    }

    public static class SearchPartnerResultVH extends RecyclerView.ViewHolder {

        @BindView(R.id.imgPartner)
        ImageView imgPartner;

        @BindView(R.id.txtPartnerName)
        TextView txtPartnerName;

        @BindView(R.id.txtDistance)
        TextView txtDistance;

        @BindView(R.id.txtAddress)
        TextView txtAddress;

        public View view;

        public SearchPartnerResultVH(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void removeItemAtPosition(ArrayList<Integer> pos) {
        for (int j = pos.size() - 1; j >= 0; j--) {
            int position = pos.get(j);
            lstPartners.remove(position);
            notifyDataSetChanged();
        }
    }

    public void removeAt(int position) {
        lstPartners.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lstPartners.size());
    }

    private String getAddress(Partner partner) {
        try {
            double lat = Double.parseDouble(partner.getGeoLat());
            double lng = Double.parseDouble(partner.getGeoLng());
            Address address = mLocationHelper.getAddress(lat, lng);
            return mLocationHelper.getFullInfoAddress(address);
        } catch (Exception e) {
            return "";
        }
    }
}
