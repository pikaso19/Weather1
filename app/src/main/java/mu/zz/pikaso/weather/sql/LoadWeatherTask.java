package mu.zz.pikaso.weather.sql;

import android.os.AsyncTask;

import java.util.List;


import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 12.10.2015.
 */
public class LoadWeatherTask extends AsyncTask<Void,Void,Void> {
    private DataBaseHelper dataBaseHelper;
    private IActionUI context;
    private List<Weather> forecast;
    private long cityID;

    public LoadWeatherTask(DataBaseHelper dataBaseHelper, long cityID, IActionUI context){
        this.dataBaseHelper = dataBaseHelper;
        this.context = context;
        this.cityID = cityID;
    }
    @Override
    protected Void doInBackground(Void... params) {
        forecast = dataBaseHelper.Weather.selectAll(cityID);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.displayForecast(forecast);
    }
}
