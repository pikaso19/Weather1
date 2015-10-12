package mu.zz.pikaso.weather.sql;

import android.os.AsyncTask;

import java.util.List;

import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 12.10.2015.
 */
public class LoadCitiesTask extends AsyncTask<Void,Void,Void> {
    private DataBaseHelper dataBaseHelper;
    private IActionUI context;
    private List<City> cities;

    public LoadCitiesTask(DataBaseHelper dataBaseHelper, IActionUI context){
        this.dataBaseHelper = dataBaseHelper;
        this.context = context;
    }
    @Override
    protected Void doInBackground(Void... params) {
        cities = dataBaseHelper.City.selectAll();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        context.displayCities(cities);
    }
}
