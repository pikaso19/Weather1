package mu.zz.pikaso.weather.other;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.internet.RecvForecastTask;
import mu.zz.pikaso.weather.internet.RecvWeatherTask;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 13.10.2015.
 */
public class ForecastUpdateThread extends Thread {
    private DataBaseHelper db;
    private long cityID;
    private Context context;

    public ForecastUpdateThread(long cityID, DataBaseHelper db, Context context){
        this.cityID = cityID;
        this.db = db;
        this.context = context;
    }

    @Override
    public void run() {
        Log.d("0k19vej5ug", "getForecastLastUpdate!");
        Calendar a = db.City.getForecastLastUpdate(cityID);
        if(a==null){
            if(Connection.isInternetAvailable(context)){
                Log.d("0k19vej5ug", "trying to start RecvForecastTask!");
                RecvForecastTask task = new RecvForecastTask(cityID,(IActionUI)context);
                task.execute();
            }
        }else{
            Calendar cal = Calendar.getInstance();
            int days = Weather.daysDifference(cal.getTime(), a.getTime());
            if(days>0){
                Log.d("0k19vej5ug", "trying to start RecvForecastTask!");
                RecvForecastTask task = new RecvForecastTask(cityID,(IActionUI)context);
                task.execute();
            }
        }
    }

}
