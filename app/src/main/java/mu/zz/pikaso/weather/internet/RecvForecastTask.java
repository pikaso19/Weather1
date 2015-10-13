package mu.zz.pikaso.weather.internet;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;


/**
 * Created by pikaso on 08.10.2015.
 */
public class RecvForecastTask extends AsyncTask<Void,Void,Void> {
    private List<Weather> forecast;
    private IActionUI context;
    private long cityId;

    public RecvForecastTask(long id, IActionUI context){
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
        context.readyForecast(forecast, cityId);
    }
}
