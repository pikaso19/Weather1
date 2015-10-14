package mu.zz.pikaso.weather.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 13.10.2015.
 */
public class RefreshAllTask extends AsyncTask<Void,Void,Boolean> {
    private DataBaseHelper db;
    private Context context;

    public RefreshAllTask(DataBaseHelper db, Context context){
        this.db = db;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<City> cities = db.City.selectAll();
        boolean success = false;
        // 1. clear table
        db.Weather.deleteWhole();
        if(cities != null){
            for(int i=0;i<cities.size();i++){
                // 2. download forecast & weather
                Connection connection = new Connection();
                List<Weather> forecast = connection.getWeatherForecast(cities.get(i).getId());
                Weather current = connection.getCurrentWeather(cities.get(i).getId());
                if(forecast != null && current != null){
                    if(!forecast.isEmpty()){
                        forecast.get(0).setDescription(current.getDescription());
                        forecast.get(0).setImage(current.getImage());
                        forecast.get(0).setDay(current.getDay());
                    }
                    for(int j=0;j<forecast.size();j++){
                        db.Weather.insert(forecast.get(j), cities.get(i).getId());
                        success = true;
                    }
                    db.City.updateWLU(cities.get(i).getId());
                    db.City.updateFLU(cities.get(i).getId());
                }
            }
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        ((IActionUI)context).RefreshAllCompleted(success);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        db = null;
        context = null;
    }
}
