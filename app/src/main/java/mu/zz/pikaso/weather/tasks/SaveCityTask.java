package mu.zz.pikaso.weather.tasks;

import android.os.AsyncTask;

import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 13.10.2015.
 */
public class SaveCityTask extends AsyncTask<Void,Void,City> {
    private DataBaseHelper db;
    private City city;
    private IActionUI context;

    public SaveCityTask(City city, DataBaseHelper db, IActionUI context){
        this.city = city;
        this.db = db;
        this.context = context;
    }

    @Override
    protected City doInBackground(Void... params) {
        boolean saved = db.City.insert(city);
        if(!saved) city = null;
        return city;
    }

    @Override
    protected void onPostExecute(City city) {
        super.onPostExecute(city);
        context.CitySaved(city);
    }

}
