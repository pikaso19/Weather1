package mu.zz.pikaso.weather;


import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import java.util.List;

import mu.zz.pikaso.weather.tasks.DeleteCityThread;
import mu.zz.pikaso.weather.tasks.SearchCityTask;
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
    private boolean BigDisplay = false;

    private boolean isRefreshAllRunning = false;
    private boolean isRefreshRunning = false;
    private boolean isSearchRunning = false;

    FrameLayout frame1;
    FrameLayout frame2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frame1 = (FrameLayout) findViewById(R.id.fragment);
        frame2 = (FrameLayout) findViewById(R.id.fragment2);

        //deleteDatabase(DataBaseHelper.DATABASE_NAME); // delete DataBase
        try {
            dataBase = new DataBaseHelper(getApplicationContext());
        }catch (Exception e) {
            deleteDatabase(DataBaseHelper.DATABASE_NAME); // delete DataBase
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //changeFragmentsDisplay();
    }

    private void changeFragmentsDisplay(){
        //get Display size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("z1z1z1", "W:" + width + " H:" + height);
        //change settings
        if(width>320){
            BigDisplay = true;
            int widthNew = width/3;
            frame1.getLayoutParams().width = widthNew;
            frame2.setVisibility(View.VISIBLE);
            frame2.getLayoutParams().width = width - widthNew;

            weatherFragment = WeatherFragment.newInstance(null,0);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment2, weatherFragment).commit();

        }else{
            BigDisplay = false;
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
            frame1.setLayoutParams(lp);
            frame2.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
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
            deleteDatabase(DataBaseHelper.DATABASE_NAME); // delete DataBase
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
        if(!isRefreshAllRunning) {
            new RefreshAllTask(dataBase, this).execute();
            isRefreshAllRunning = true; //block
        }
    }

    @Override
    public void onClickAddCity() {
        // V
        dialogFragment = new AddCityFragmentDialog();
        dialogFragment.show(getSupportFragmentManager().beginTransaction(), null);
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
        builder.setTitle("Remove "+city.getName()+"?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Start delete task!
                new DeleteCityThread(dataBase, city, context).start();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
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
    public void RefreshAllCompleted(boolean success) {
        // V
        if(success)
            Toast.makeText(this,"Refresh completed!", Toast.LENGTH_SHORT).show();

        isRefreshAllRunning = false; //un-block
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
            if(!isRefreshRunning){
                RefreshWeatherTask task = new RefreshWeatherTask(dataBase, cityID, this);
                task.execute();
                isRefreshRunning = true;
            }
        } else {
            Toast.makeText(this.getApplicationContext(), "NO INTERNET connection available!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Weather was Updated!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Can't get forecast+weather!", Toast.LENGTH_SHORT).show();
        }
        isRefreshRunning = false;

    }




    /*
                                                    FRAGMENT 3
    */

    @Override
    public void searchCity(String pattern) {
        if(!isSearchRunning) {
            SearchCityTask task = new SearchCityTask(pattern, this);
            task.execute();
            isSearchRunning = true;
        }
    }

    @Override
    public void foundCitiesDisplay(List<City> cities) {
        dialogFragment.fillListCities(cities);
        isSearchRunning = false;
    }

    @Override
    public void onCityAdd(City city) {
        if(city != null){
            if(city.getName() != null && city.getId() > 0){
                if(!city.getName().isEmpty()) {
                    //save City in DB
                    SaveCityTask thread = new SaveCityTask(city, dataBase, this);
                    thread.execute();
                }else{
                    Toast.makeText(this, "Weather is available only for settlements", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Retry search with analogue", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Press find and choose city from list", Toast.LENGTH_SHORT).show();
        }

    }

    public void CitySaved(City city){
        if(city != null) {
            if (city.getName() != null) {
                menuFragment.addCity(city);
                dialogFragment.dismiss();
            }
        }else{
            Toast.makeText(this, "This city already is in your Favourites", Toast.LENGTH_SHORT).show();
        }
    }





}
