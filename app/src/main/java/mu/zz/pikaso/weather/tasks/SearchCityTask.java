package mu.zz.pikaso.weather.tasks;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 06.10.2015.
 */
public class SearchCityTask extends AsyncTask<Void,Void,List<City>> {
    private String pattern;
    private IActionUI context;


    public SearchCityTask(String pattern, IActionUI context){
        super();
        this.context = context;
        this.pattern = pattern;
    }

    @Override
    protected List<City> doInBackground(Void... params) {
        Connection connection = new Connection();
        List<City> cities = connection.getCitiesList(pattern);
        return cities;
    }

    @Override
    protected void onPostExecute(List<City> cities) {
        super.onPostExecute(cities);
        //UPDATE
        context.foundCitiesDisplay(cities);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        pattern = null;
        context = null;
    }
}
