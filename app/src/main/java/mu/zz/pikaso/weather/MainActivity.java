package mu.zz.pikaso.weather;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.internet.Connection;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.sql.LoadCitiesTask;
import mu.zz.pikaso.weather.sql.LoadWeatherTask;
import mu.zz.pikaso.weather.ui.IActionUI;
import mu.zz.pikaso.weather.internet.RecvForecastTask;

public class MainActivity extends FragmentActivity implements IActionUI{
    private WeatherFragment weatherFragment;
    private MenuFragment menuFragment;
    private AddCityFragmentDialog dialogFragment;
    private DataBaseHelper dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            dataBase = new DataBaseHelper(getApplicationContext());
        }catch (Exception e) {
            Log.d("0k19vej5ug", "[FORCE] Database delete");
            deleteDatabase(DataBaseHelper.DATABASE_NAME); // delete DataBase
            Log.d("0k19vej5ug", "[FORCE] Database create!");
            dataBase = new DataBaseHelper(getApplicationContext());
        }

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            menuFragment = new MenuFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            menuFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, menuFragment).commit();

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
                                                FRAGMENT 1
    */
    @Override
    public void onClickRefreshALL (){
        //TODO: Get weather forecast for favourites cities

        this.deleteDatabase(DataBaseHelper.DATABASE_NAME); // delete DataBase
        Log.d("0k19vej5ug", "Database deleted!");
    }

    @Override
    public void onClickAddCity() {
        dialogFragment = new AddCityFragmentDialog();
        dialogFragment.show(getSupportFragmentManager().beginTransaction(), null);
    }

    @Override
    public void onClickExit() {
        finish();
    }

    @Override
    public void onCitySelected(City city) {
        weatherFragment = WeatherFragment.newInstance(city.getName(),city.getId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, weatherFragment).addToBackStack(null).commit();
        //now load async forecast
        LoadWeatherTask task = new LoadWeatherTask(dataBase, city.getId(), this);
        task.execute();
    }

    @Override
    public void onCityDelete(City city) {
        //TODO: delete city on long press
    }

    @Override
    public void loadCities(){
        LoadCitiesTask task = new LoadCitiesTask(dataBase, this);
        task.execute();
    }

    @Override
    public void displayCities(List<City> cities) {
        if(!cities.isEmpty()){
            for (City city: cities) {
                menuFragment.addCity(city);
            }
        }
    }

    /*
                                                    FRAGMENT 2
    */

    @Override
    public void onClickRefresh(RecyclerView rw, int id) {
        //TODO: refresh weather for current city
        if(Connection.isInternetAvailable(this)) {
            //before doing something check internet connection
            Log.d("0k19vej5ug","Internet available!");
            RecvForecastTask task = new RecvForecastTask(id, this);
            task.execute();
        }else{
            Log.d("0k19vej5ug", "Internet unreachable!");
            Toast.makeText(this.getApplicationContext(), "There NO INTERNET connection available!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void readyWeather(List<Weather> forecast, long cityID){
        if(!forecast.isEmpty()){
            weatherFragment.DisplayWeather(forecast);

            //TODO: seperate with task
            List<Long> weatherIDs = new ArrayList<Long>(16);
            //delete prev.
            dataBase.Weather.deleteByCity(cityID);
            //add new
            for (Weather w: forecast) {
                weatherIDs.add(dataBase.Weather.insert(w,cityID));
            }
            //TODO: update WLU & FLU in CityTable
        }
    }

    @Override
    public void displayForecast(List<Weather> forecast) {
        if(forecast != null){
            if(!forecast.isEmpty()){
                for (Weather w: forecast) {
                    //TODO: change to single item ading
                    //weatherFragment.add(w);
                }
            }
        }
        //after refactor delete this
        weatherFragment.DisplayWeather(forecast);
    }

    /*
                                                    FRAGMENT 3
    */

    @Override
    public void onCityAdd(City city) {
        menuFragment.addCity(city);

        dataBase.City.insert(city);
        City a = dataBase.City.select(city.getId());
        Log.d("0k19vej5ug","Read["+city.getId()+"]: "+a.getName()+a.getCountry()+a.getId());

        dialogFragment.dismiss();
    }






}
