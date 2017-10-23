package node.com.enjoydanang.ui.fragment.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import node.com.enjoydanang.R;
import node.com.enjoydanang.model.Weather;
import node.com.enjoydanang.utils.ImageUtils;

/**
 * Author: Tavv
 * Created on 23/10/2017
 * Project Name: EnjoyDaNang
 * Version 1.0
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<Weather> lstWeather;
    private Context context;


    public WeatherAdapter(List<Weather> lstWeather, Context context) {
        this.lstWeather = lstWeather;
        this.context = context;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        Weather weather = lstWeather.get(position);
        if (weather != null) {
            holder.txtWeatherDay.setText(weather.getDay());
            holder.txtTemperature.setText(weather.getTemperature());
            ImageUtils.loadImageNoRadius(context, holder.imgWeather, weather.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return lstWeather.size() > 0 ? lstWeather.size() : 0;
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtWeatherDay)
        TextView txtWeatherDay;
        @BindView(R.id.imgWeather)
        ImageView imgWeather;
        @BindView(R.id.txtTemperature)
        TextView txtTemperature;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
