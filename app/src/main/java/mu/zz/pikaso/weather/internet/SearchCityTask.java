package mu.zz.pikaso.weather.internet;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pikaso on 06.10.2015.
 */
public class SearchCityTask extends AsyncTask<Void,Void,Void> {
    private ListView listView ;
    private List<String> cities;
    private String pattern;

    public void setPattern(String pattern){
        this.pattern = pattern;
    }

    public SearchCityTask(ListView listView){
        super();
        cities = new ArrayList<String>();
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
        for (int i=0;i<cities.size();i++) {
            ((ArrayAdapter<String>)listView.getAdapter()).insert(cities.get(i), i);
        }
        ((ArrayAdapter<String>)listView.getAdapter()).notifyDataSetChanged();
    }
}