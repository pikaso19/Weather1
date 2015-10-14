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
 * Created by pikaso on 14.10.2015.
 */
public class RefreshWeatherTask extends AsyncTask<Void,Void,List<Weather>> {
    private DataBaseHelper dataBaseHelper;
    private IActionUI context;
    private long cityID;
    private boolean isUpdated;

    public RefreshWeatherTask(DataBaseHelper dataBaseHelper, long cityID, IActionUI context){
        this.dataBaseHelper = dataBaseHelper;
        this.context = context;
        this.cityID = cityID;
        isUpdated = false;
    }

    @Override
    protected List<Weather> doInBackground(Void... params) {
        List<Weather> forecast;
//        boolean isUPDATED = Conditions.isForecastUpToDate(dataBaseHelper, cityID);
//        if(isUPDATED){
            // load data from DB
//            isUPDATED = Conditions.isWeatherUpToDate(dataBaseHelper, cityID);
//            if(isUPDATED){
//                forecast = dataBaseHelper.Weather.selectAll(cityID);
//            }else{
//                //download weather from internet
//                Connection connection = new Connection();
//                Weather current = connection.getCurrentWeather(cityID);
//                if(current != null){
//                   long id = dataBaseHelper.Weather.getTodayWeatherID(cityID);
//                    dataBaseHelper.Weather.updateByCurrent(current,id);
//                    dataBaseHelper.City.updateWLU(cityID);
//                }
//                forecast = dataBaseHelper.Weather.selectAll(cityID);
//            }
//        }else{
//            Log.d("0k19vej5ug", "Conditions.isInternet beg");
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
                    isUpdated = true;
                }
            }else{
                //TODO: display user that internet is not available
                forecast = dataBaseHelper.Weather.selectAll(cityID);
            }
//        }
        return forecast;
    }

    @Override
    protected void onPostExecute(List<Weather> forecast) {
        super.onPostExecute(forecast);
        context.displayForecast(forecast, cityID, isUpdated);
    }
}
