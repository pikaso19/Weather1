package mu.zz.pikaso.weather.internet;

import android.os.AsyncTask;

import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 13.10.2015.
 */
public class RecvWeatherTask extends AsyncTask<Void,Void,Weather> {
    private IActionUI context;
    private long cityID;

    public RecvWeatherTask(long cityID, IActionUI context){
        super();
        this.context = context;
        this.cityID = cityID;
    }


    @Override
    protected Weather doInBackground(Void... params) {
        Weather current;
        Connection connection = new Connection();
        current = connection.getCurrentWeather(cityID);//hardcode
        return current;
    }

    @Override
    protected void onPostExecute(Weather w) {
        super.onPostExecute(w);
        //activate event
        context.readyWeather(w, cityID);
    }
}
