package mu.zz.pikaso.weather.ui;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.pojo.CurrentWeather;

/**
 * Created by pikaso on 20.09.2015.
 */
public class ShowWeather extends AsyncTask<Void,Void,CurrentWeather> {
    private EditText editText;
    public ShowWeather(EditText editText) {
        this.editText = editText;
    }

    @Override
    protected CurrentWeather doInBackground(Void... params) {
        CurrentWeather b = new CurrentWeather();
        try {
            Connection a = new Connection();
            b = a.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    protected void onPostExecute(CurrentWeather currentWeather) {
        super.onPostExecute(currentWeather);
        editText.setText("City: " + currentWeather.getName());
        editText.append("\nWind: " + currentWeather.getWind().getSpeed() + "m/s");
        editText.append("\nClouds: " + currentWeather.getClouds().getAll() + "%");
        double cur = currentWeather.getMain().getTemp() - 273.15;
        double min = currentWeather.getMain().getTemp_Min() - 273.15;
        double max = currentWeather.getMain().getTemp_Max() - 273.15;
        editText.append("\nTemperature: " + cur + "°C (from "+ min +" to "+ max +")");
        editText.append("\nState: " + currentWeather.getWeather().get(0).getDescription());
    }
}
