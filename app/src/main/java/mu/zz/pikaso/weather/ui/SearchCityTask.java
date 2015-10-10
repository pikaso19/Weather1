package mu.zz.pikaso.weather.ui;


import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.representations.City;

/**
 * Created by pikaso on 06.10.2015.
 */
public class SearchCityTask extends AsyncTask<Void,Void,Void> {
    private ListView listView ;
    private List<City> cities;
    private String pattern;

    public void setPattern(String pattern){
        this.pattern = pattern;
    }

    public SearchCityTask(ListView listView){
        super();
        cities = new ArrayList<City>();
        this.listView = listView;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Connection connection = new Connection();
        cities = connection.getCitiesList(pattern);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //UPDATE
        ((ArrayAdapter<String>)listView.getAdapter()).clear();
        if(cities.size()>0){
            for (int i=0;i<cities.size();i++) {
                ((ArrayAdapter<City>)listView.getAdapter()).insert(cities.get(i), i);
            }
        }else{
            ((ArrayAdapter<String>)listView.getAdapter()).insert("Nothing Found", 0);
        }
        ((ArrayAdapter<String>)listView.getAdapter()).notifyDataSetChanged();
    }
}
