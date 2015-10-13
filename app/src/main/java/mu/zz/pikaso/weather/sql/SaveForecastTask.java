package mu.zz.pikaso.weather.sql;

import java.util.List;

import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 13.10.2015.
 */
public class SaveForecastTask extends Thread {
    private DataBaseHelper db;
    private List<Weather> forecast;
    private long cityID;
    private IActionUI context;

    public SaveForecastTask(List<Weather> forecast, long cityID, DataBaseHelper db, IActionUI context){
        this.forecast = forecast;
        this.db = db;
        this.cityID = cityID;
        this.context = context;
    }

    @Override
    public void run() {
        //delete prev.
        db.Weather.deleteByCity(cityID);
        //add new
        for (Weather w: forecast) {
            db.Weather.insert(w, cityID);
        }
        db.City.updateFLU(cityID);
        //signal MainActivity
        context.savedForecast(cityID);
    }

}
