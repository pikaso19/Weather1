package mu.zz.pikaso.weather.ui;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;

/**
 * Created by pikaso on 30.09.2015.
 */
public interface IActionUI {
    //fragment 1
    void onClickRefreshALL();
    void onClickAddCity();
    void onClickExit();

    void loadCities();
    void displayCities(List<City> cities);

    void onCitySelected(City city);
    void onCityDelete(City city);

    //fragment 2
    void onClickRefresh(RecyclerView rw, int id);
    void displayForecast(List<Weather> forecast);

    //fragment 3
    void onCityAdd(City city);

    // events
    void readyWeather(List<Weather> forecast, long cityID);

    //SQL

}
