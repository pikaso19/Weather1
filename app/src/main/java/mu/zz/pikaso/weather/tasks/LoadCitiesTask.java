package mu.zz.pikaso.weather.tasks;

import android.os.AsyncTask;

import java.util.List;

import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 12.10.2015.
 */
public class LoadCitiesTask extends AsyncTask<Void,Void,List<City>> {
    private DataBaseHelper dataBaseHelper;
    private IActionUI context;

    public LoadCitiesTask(DataBaseHelper dataBaseHelper, IActionUI context){
        this.dataBaseHelper = dataBaseHelper;
        this.context = context;
    }
    @Override
    protected List<City> doInBackground(Void... params) {
        List<City> cities = dataBaseHelper.City.selectAll();
        return cities;
    }

    @Override
    protected void onPostExecute(List<City> cities) {
        super.onPostExecute(cities);
        context.displayCities(cities);
    }
}
