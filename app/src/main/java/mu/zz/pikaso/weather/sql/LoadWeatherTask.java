package mu.zz.pikaso.weather.sql;

import android.os.AsyncTask;

import java.util.List;


import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 12.10.2015.
 */
public class LoadWeatherTask extends AsyncTask<Void,Void,List<Weather>> {
    private DataBaseHelper dataBaseHelper;
    private IActionUI context;
    private long cityID;

    public LoadWeatherTask(DataBaseHelper dataBaseHelper, long cityID, IActionUI context){
        this.dataBaseHelper = dataBaseHelper;
        this.context = context;
        this.cityID = cityID;
    }
    @Override
    protected List<Weather> doInBackground(Void... params) {
        List<Weather> forecast = dataBaseHelper.Weather.selectAll(cityID);
        return forecast;
    }

    @Override
    protected void onPostExecute(List<Weather> forecast) {
        super.onPostExecute(forecast);
        context.displayForecast(forecast, cityID);
    }
}
