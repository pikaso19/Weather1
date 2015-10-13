package mu.zz.pikaso.weather.other;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.internet.RecvWeatherTask;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 13.10.2015.
 */
public class WeatherUpdateTask  extends Thread {
    private DataBaseHelper db;
    private long cityID;
    private Context context;

    public WeatherUpdateTask(long cityID, DataBaseHelper db, Context context){
        this.cityID = cityID;
        this.db = db;
        this.context = context;
    }

    @Override
    public void run() {
        Log.d("0k19vej5ug", "getWeatherLastUpdate!");
        Calendar a = db.City.getWeatherLastUpdate(cityID);
        if(a==null){
            if(Connection.isInternetAvailable(context)){
                Log.d("0k19vej5ug", "trying to start RecvWeatherTask!");
                RecvWeatherTask task = new RecvWeatherTask(cityID,(IActionUI)context);
                task.execute();
            }
        }else{
            Calendar cal = Calendar.getInstance();
            int days = Weather.daysDifference(cal.getTime(), a.getTime());
            if(days>0){
                Log.d("0k19vej5ug", "trying to start RecvWeatherTask!");
                RecvWeatherTask task = new RecvWeatherTask(cityID,(IActionUI)context);
                task.execute();
            }
        }
    }

}
