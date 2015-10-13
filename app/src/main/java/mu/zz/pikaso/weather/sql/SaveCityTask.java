package mu.zz.pikaso.weather.sql;

import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 13.10.2015.
 */
public class SaveCityTask extends Thread {
    private DataBaseHelper db;
    private City city;

    public SaveCityTask(City city, DataBaseHelper db){
        this.city = city;
        this.db = db;
    }

    @Override
    public void run() {
        db.City.insert(city);
    }

}
