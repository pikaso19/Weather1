package mu.zz.pikaso.weather.tasks;

import android.os.AsyncTask;

import java.util.List;

import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 14.10.2015.
 */
public class DeleteCityThread extends Thread {
    private DataBaseHelper db;
    private IActionUI context;
    private City city;

    public DeleteCityThread(DataBaseHelper db, City city, IActionUI context){
        this.db = db;
        this.context = context;
        this.city = city;
    }

    @Override
    public void run() {
        super.run();
        db.City.delete(city.getId());
        //Signal MainActivity
        context.CityDeleted(city);
    }
}
