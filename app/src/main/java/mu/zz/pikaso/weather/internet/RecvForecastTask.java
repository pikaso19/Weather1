package mu.zz.pikaso.weather.internet;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.adapters.WeatherAdapter;
import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;


/**
 * Created by pikaso on 08.10.2015.
 */
public class RecvForecastTask extends AsyncTask<Void,Void,Void> {
    private List<Weather> forecast;
    private IActionUI context;
    private int cityId;

    public RecvForecastTask(int id, IActionUI context){
        super();
        this.context = context;
        forecast = new ArrayList<Weather>(16);
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
        //activate event
        context.readyWeather(forecast,cityId);
    }
}
