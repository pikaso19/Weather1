package mu.zz.pikaso.weather.sql;

import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 13.10.2015.
 */
public class SaveCurrentWeatherTask extends Thread {
    private long cityID;
    private Weather weather;
    private DataBaseHelper db;
    private IActionUI context;

    public SaveCurrentWeatherTask(Weather current, long cityID, DataBaseHelper db, IActionUI context){
        this.weather = current;
        this.cityID = cityID;
        this.db = db;
        this.context = context;

    }

    @Override
    public void run() {
        //find today weather id
        long id = db.Weather.getTodayWeatherID(cityID);
        if(id > 0){
            //save current data
            db.Weather.updateByCurrent(weather,id);
            //update WLU in City
            db.City.updateWLU(cityID);
            //signal MainActivity
            context.savedWeather();
        }

    }


}
