package mu.zz.pikaso.weather.ui;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.adapters.WeatherAdapter;
import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;


/**
 * Created by pikaso on 08.10.2015.
 */
public class RecvForecastTask extends AsyncTask<Void,Void,Void> {
    private RecyclerView recyclerView;
    private List<Weather> forecast;
    private int cityId;

    public RecvForecastTask(RecyclerView recyclerView, int id){
        super();
        this.recyclerView = recyclerView;
        forecast = new ArrayList<Weather>();
        cityId = id;
    }


    @Override
    protected Void doInBackground(Void... params) {
        Connection connection = new Connection();
        forecast = connection.getWeatherForecast(cityId);//hardcode
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //UPDATE
        if(forecast.size()>0){
            
            ((WeatherAdapter)recyclerView.getAdapter()).setWeatherDataset(forecast);
        }
        ((WeatherAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
    }
}
