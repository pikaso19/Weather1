package mu.zz.pikaso.weather.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;


import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.tools.Conditions;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 12.10.2015.
 */
public class LoadWeatherTask extends AsyncTask<Void,Void,List<Weather>> {
    private DataBaseHelper dataBaseHelper;
    private IActionUI context;
    private long cityID;
    private boolean isUpdated;

    public LoadWeatherTask(DataBaseHelper dataBaseHelper, long cityID, IActionUI context){
        this.dataBaseHelper = dataBaseHelper;
        this.context = context;
        this.cityID = cityID;
        isUpdated = false;
    }
    @Override
    protected List<Weather> doInBackground(Void... params) {
        List<Weather> forecast;
        boolean isUPDATED = Conditions.isForecastUpToDate(dataBaseHelper, cityID);
        if(isUPDATED){
            // load data from DB
            forecast = dataBaseHelper.Weather.selectAll(cityID);
        }else{
            boolean isOnline = Conditions.isInternetAvailable((Context) context);
            if(isOnline) {
                //load data from Internet
                Connection connection = new Connection();
                forecast = connection.getWeatherForecast(cityID);
                Weather current = connection.getCurrentWeather(cityID);
                //save data to DB
                dataBaseHelper.Weather.deleteByCity(cityID);
                if (forecast != null) {
                    //update with current weather
                    if(!forecast.isEmpty() && current != null){
                        forecast.get(0).setDescription(current.getDescription());
                        forecast.get(0).setImage(current.getImage());
                        forecast.get(0).setDay(current.getDay());
                    }
                    //save data to BD
                    for (int i=0;i<forecast.size();i++) {
                        dataBaseHelper.Weather.insert(forecast.get(i), cityID);
                    }
                    dataBaseHelper.City.updateFLU(cityID);
                    dataBaseHelper.City.updateWLU(cityID);
                    isUpdated = true;
                }

            }else{
                dataBaseHelper.Weather.deleteOldWeather();  //  delete old Weather data
                forecast = dataBaseHelper.Weather.selectAll(cityID);
            }
        }
        return forecast;
    }

    @Override
    protected void onPostExecute(List<Weather> forecast) {
        super.onPostExecute(forecast);
        context.displayForecast(forecast, cityID,isUpdated);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        dataBaseHelper = null;
        context = null;
    }
}
