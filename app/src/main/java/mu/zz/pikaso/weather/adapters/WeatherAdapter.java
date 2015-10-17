package mu.zz.pikaso.weather.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;
import java.util.List;

import mu.zz.pikaso.weather.R;
import mu.zz.pikaso.weather.representations.Weather;

/**
 * Created by pikaso on 04.10.2015.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {
    private List<Weather> weatherDataset;
    private Activity activity;

    public void setWeatherDataset(List<Weather> weatherDataset){
        this.weatherDataset = weatherDataset;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
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
            text1 = (TextView) itemView.findViewById(R.id.wlText1);
            text2 = (TextView) itemView.findViewById(R.id.wlText2);
            text3 = (TextView) itemView.findViewById(R.id.wlText3);
        }
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_weatherlist,parent,false);

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

        SimpleDateFormat iso = new SimpleDateFormat("EEE, d MMM yyyy");
        date.setText(iso.format(weatherDataset.get(position).getDate().getTime()));

        Glide.with(activity).load(weatherDataset.get(position).getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.umbrella)//TODO: change to no_image
                .into(image);

        text1.setText(String.valueOf(weatherDataset.get(position).getDay()) + "° ("+weatherDataset.get(position).getMin()+"°|"+weatherDataset.get(position).getMax()+"°)");
        text2.setText("Night: " + weatherDataset.get(position).getNight() +"°; Morning: "+ weatherDataset.get(position).getMorn() +"°; Evening: "+weatherDataset.get(position).getEve()+"°");
        text3.setText(weatherDataset.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return weatherDataset.size();
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        activity = null;
        weatherDataset = null;
        Log.d("MEMORY","WeatherAdapter finalized");
    }
}
