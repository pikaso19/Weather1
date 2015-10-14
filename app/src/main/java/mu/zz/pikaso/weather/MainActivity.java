package mu.zz.pikaso.weather;


import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.List;

import mu.zz.pikaso.weather.tasks.DeleteCityThread;
import mu.zz.pikaso.weather.tools.Conditions;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.tasks.LoadCitiesTask;
import mu.zz.pikaso.weather.tasks.LoadWeatherTask;
import mu.zz.pikaso.weather.tasks.RefreshAllTask;
import mu.zz.pikaso.weather.tasks.RefreshWeatherTask;
import mu.zz.pikaso.weather.tasks.SaveCityTask;
import mu.zz.pikaso.weather.ui.IActionUI;

public class MainActivity extends FragmentActivity implements IActionUI{
    private WeatherFragment weatherFragment;
    private MenuFragment menuFragment;
    private AddCityFragmentDialog dialogFragment;
    private DataBaseHelper dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //deleteDatabase(DataBaseHelper.DATABASE_NAME); // delete DataBase
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
        // V
        //answer => MainActivity.RefreshAllCompleted()
        new RefreshAllTask(dataBase, this).execute();
    }

    @Override
    public void onClickAddCity() {
        // V
        dialogFragment = new AddCityFragmentDialog();
        dialogFragment.show(getSupportFragmentManager().beginTransaction(), null);
    }

    @Override
    public void onClickExit() {
        // V
        //TODO: get out this button
        deleteDatabase(DataBaseHelper.DATABASE_NAME); // delete DataBase
        finish();
    }

    @Override
    public void onCitySelected(City city) {
        // V
        weatherFragment = WeatherFragment.newInstance(city.getName(),city.getId());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, weatherFragment).addToBackStack(null).commit();
    }



    @Override
    public void onCityDelete(final City city) {
        // V
        final IActionUI context = this;
        //TODO: delete city on long press
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Start delete task!
                new DeleteCityThread(dataBase, city, context).start();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void CityDeleted(final City city){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuFragment.delCity(city);
            }
        });
    }


    @Override
    public void loadCities(){
        // V
        //MenuFragment is loaded => here
        // here => MainActivity.displayCities(List..)
        LoadCitiesTask task = new LoadCitiesTask(dataBase, this);
        task.execute();
    }

    @Override
    public void displayCities(List<City> cities) {
        // V
        if(!cities.isEmpty()){
            for (City city: cities) {
                menuFragment.addCity(city);
            }
        }
    }

    @Override
    public void RefreshAllCompleted() {
        // V
        Toast.makeText(this,"Refresh completed!", Toast.LENGTH_SHORT).show();
    }

    /*
                                                    FRAGMENT 2
    */

    @Override
    public void loadWeather(long cityID) {
        // V
        //=> from WeatherFragment after adapter is set
        //to => MainActivity.displayForecast(List..);
        LoadWeatherTask task = new LoadWeatherTask(dataBase, cityID, this);
        task.execute();
    }

    @Override
    public void onClickRefresh(RecyclerView rw, long cityID) {
        // V
        // task => MainActivity.displayForecast(List..)
        if(Conditions.isInternetAvailable(this)) {
            //before doing something check internet connection
            RefreshWeatherTask task = new RefreshWeatherTask(dataBase, cityID, this);
            task.execute();
        } else {
            Toast.makeText(this.getApplicationContext(), "There NO INTERNET connection available!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void displayForecast(List<Weather> forecast, long cityID, boolean isUpdated) {
        // V
        if(forecast != null){
            if(!forecast.isEmpty()){
                weatherFragment.DisplayWeather(forecast);
                //TODO: change to single item ading
                if(isUpdated)
                    Toast.makeText(this, "Weather was Updated!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Can't get forecast+weather!", Toast.LENGTH_LONG).show();
        }

    }




    /*
                                                    FRAGMENT 3
    */

    @Override
    public void onCityAdd(City city) {
        if(city != null){
            if(city.getName() != null && city.getId() > 0){
                if(!city.getName().isEmpty()) {
                    //save City in DB
                    SaveCityTask thread = new SaveCityTask(city, dataBase, this);
                    thread.execute();
                }else{
                    Toast.makeText(this, "Weather is available only for settlements", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "Retry search with analogue", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Press find and choose city from list", Toast.LENGTH_LONG).show();
        }

    }

    public void CitySaved(City city){
        if(city != null) {
            if (city.getName() != null) {
                menuFragment.addCity(city);
                dialogFragment.dismiss();
            }
        }
    }






}
