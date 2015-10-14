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

    public LoadWeatherTask(DataBaseHelper dataBaseHelper, long cityID, IActionUI context){
        this.dataBaseHelper = dataBaseHelper;
        this.context = context;
        this.cityID = cityID;
    }
    @Override
    protected List<Weather> doInBackground(Void... params) {
        List<Weather> forecast;
        boolean isUPDATED = Conditions.isForecastUpToDate(dataBaseHelper, cityID);
        if(isUPDATED){
            // load data from DB
            forecast = dataBaseHelper.Weather.selectAll(cityID);
        }else{
            Log.d("0k19vej5ug", "Conditions.isInternet beg");
            boolean isOnline = Conditions.isInternetAvailable((Context) context);
            Log.d("0k19vej5ug", "Conditions.isInternet res = "+isOnline);
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
                }

            }else{
                //TODO: display user that internet is not available
                forecast = dataBaseHelper.Weather.selectAll(cityID);
            }
        }
        return forecast;
    }

    @Override
    protected void onPostExecute(List<Weather> forecast) {
        super.onPostExecute(forecast);
        context.displayForecast(forecast, cityID);
    }
}