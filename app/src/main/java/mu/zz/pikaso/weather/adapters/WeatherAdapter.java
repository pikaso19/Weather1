package mu.zz.pikaso.weather.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



import mu.zz.pikaso.weather.R;
import mu.zz.pikaso.weather.representations.Weather;

/**
 * Created by pikaso on 04.10.2015.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {
    private ArrayList<Weather> weatherDataset;

    public void setWeatherDataset(ArrayList<Weather> weatherDataset){
        this.weatherDataset = weatherDataset;
    }

    public static class WeatherHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView date;
        private TextView text1;
        private TextView text2;
        private TextView text3;

        public WeatherHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.txtDate);
            image = (ImageView) itemView.findViewById(R.id.imgState);
            text1 = (TextView) itemView.findViewById(R.id.txtTemperature1);
            text2 = (TextView) itemView.findViewById(R.id.txtTemperature2);
            text3 = (TextView) itemView.findViewById(R.id.txtDescription);

        }
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weatherlist_row,parent,false);

        WeatherHolder weatherHolder = new WeatherHolder(view);
        return weatherHolder;
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        TextView date = holder.date;
        ImageView image = holder.image;
        TextView text1 = holder.text1;
        TextView text2 = holder.text2;
        TextView text3 = holder.text3;

        SimpleDateFormat iso = new SimpleDateFormat(
                "dd-MM-yy");
        date.setText(iso.format(weatherDataset.get(position).getDate()));
        image.setImageResource(weatherDataset.get(position).getImage());
        text1.setText(String.valueOf(weatherDataset.get(position).getCurrentTemperature()) + " °C");
        text2.setText("from " + String.valueOf(weatherDataset.get(position).getMinTemperature()) +" to "+ String.valueOf(weatherDataset.get(position).getMaxTemperature()) +" °C");
        text3.setText(weatherDataset.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return weatherDataset.size();
    }

}
