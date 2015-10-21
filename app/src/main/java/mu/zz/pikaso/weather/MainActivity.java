package mu.zz.pikaso.weather;


import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;

import mu.zz.pikaso.weather.tasks.*;
import mu.zz.pikaso.weather.representations.*;
import mu.zz.pikaso.weather.tools.Conditions;
import mu.zz.pikaso.weather.sql.DataBaseHelper;
import mu.zz.pikaso.weather.ui.IActionUI;

public class MainActivity extends FragmentActivity implements IActionUI{
    /* fragments */
    private WeatherFragment weatherFragment;
    private MenuFragment menuFragment;
    private AddCityFragmentDialog dialogFragment;

    /* DataBase manager */
    private DataBaseHelper dataBase;

    /* change view for display with big width */
    private boolean isBigDisplay = false;
    private boolean isFullScreenWeather = false;

    /* for init in portrait mode */
    private boolean isAppLoaded = false;
    private boolean isAppLoadingAsLandScape = false;

    /* lock/un-lock buttons, while task running */
    private boolean isRefreshAllRunning = false;
    private boolean isRefreshRunning = false;
    private boolean isSearchRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            isAppLoadingAsLandScape = true;
        //Orientation must be PORTRAIT while app loading
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(isAppLoaded)
            ChangeFragmentsDisplay();
    }
    



    /*
                                                FRAGMENT 1
    */
    @Override
    public void onClickRefreshALL (){
        //answer => MainActivity.RefreshAllCompleted()
        if(!isRefreshAllRunning) {
            new RefreshAllTask(dataBase, this).execute();
            isRefreshAllRunning = true; //block
        }
    }

    @Override
    public void onClickAddCity() {
        dialogFragment = new AddCityFragmentDialog();
        dialogFragment.show(getSupportFragmentManager().beginTransaction(), null);
    }

    @Override
    public void onCitySelected(City city) {
        if(isBigDisplay){
            weatherFragment.setArgs(city.getName(), city.getId());
        }else{
            weatherFragment = WeatherFragment.newInstance(city.getName(),city.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, weatherFragment).addToBackStack(null).commit();
        }

    }

    @Override
    public void onCityDelete(final City city) {
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
    public void CityDeleted(final City city) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                menuFragment.delCity(city);
            }
        });
    }

    @Override
    public void loadCities(){
        //MenuFragment is loaded => here
        // here => MainActivity.displayCities(List..)
        LoadCitiesTask task = new LoadCitiesTask(dataBase, this);
        task.execute();

        //INITIALIZATION
        if(!isAppLoaded){
            if(isAppLoadingAsLandScape) {
                //draw landscape view
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                ChangeFragmentsDisplay();
            }
            //allow orientation change
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            isAppLoaded = true;
        }

    }

    @Override
    public void displayCities(List<City> cities) {
        if(!cities.isEmpty()){
            for (City city: cities) {
                menuFragment.addCity(city);
            }
        }
    }

    @Override
    public void RefreshAllCompleted(boolean success) {
        if(success)
            Toast.makeText(this.getApplicationContext(),"Refresh completed!", Toast.LENGTH_SHORT).show();

        isRefreshAllRunning = false; //un-block
    }

    /*
                                                    FRAGMENT 2
    */

    @Override
    public void loadWeather(long cityID) {
        //=> from WeatherFragment after adapter is set
        //to => MainActivity.displayForecast(List..);
        LoadWeatherTask task = new LoadWeatherTask(dataBase, cityID, this);
        task.execute();
    }

    @Override
    public void onClickRefresh(RecyclerView rw, long cityID) {
        // task => MainActivity.displayForecast(List..)
        if(cityID > 0) {
            if (Conditions.isInternetAvailable(this)) {
                //before doing something check internet connection
                if (!isRefreshRunning) {
                    RefreshWeatherTask task = new RefreshWeatherTask(dataBase, cityID, this);
                    task.execute();
                    isRefreshRunning = true;
                }
            } else {
                Toast.makeText(this.getApplicationContext(), "NO INTERNET connection available!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this.getApplicationContext(), "Choose city before!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayForecast(List<Weather> forecast, long cityID, boolean isUpdated) {
        if(forecast != null){
            if(!forecast.isEmpty()){
                weatherFragment.DisplayWeather(forecast);
                if(isUpdated)
                    Toast.makeText(this.getApplicationContext(), "Weather was Updated!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this.getApplicationContext(), "Can't get forecast+weather!", Toast.LENGTH_SHORT).show();
        }
        isRefreshRunning = false;

    }

    @Override
    public void onMenuClick() {

        if(isBigDisplay){
            //landscape
            FrameLayout frame1 = (FrameLayout) findViewById(R.id.fragment);
            FrameLayout frame2 = (FrameLayout) findViewById(R.id.fragment2);
            if(isFullScreenWeather){
                //half-screen weather
                isFullScreenWeather = false;
                int width = getDisplayWidth();
                frame1.setVisibility(View.VISIBLE);
                frame2.getLayoutParams().width = width-width/3;
                //show menu
                getSupportFragmentManager().beginTransaction().show(menuFragment).commit();
            }else{
                //full-screen weather
                isFullScreenWeather = true;
                int width = getDisplayWidth();
                frame1.setVisibility(View.GONE);
                frame2.getLayoutParams().width = width;
                //hide menu fragment
                getSupportFragmentManager().beginTransaction().hide(menuFragment).commit();
            }
        }else{
            //portrait
            this.onBackPressed();
        }
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
                    Toast.makeText(this.getApplicationContext(), "Weather is available only for settlements", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this.getApplicationContext(), "Retry search with analogue", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this.getApplicationContext(), "Press find and choose city from list", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void CitySaved(City city){
        if(city != null) {
            if (city.getName() != null) {
                menuFragment.addCity(city);
                dialogFragment.dismiss();
            }
        }else{
            Toast.makeText(this.getApplicationContext(), "This city already is in your Favourites", Toast.LENGTH_SHORT).show();
        }
    }


    /*
                                    OTHER FUNCTIONS
    */
    private int getDisplayWidth(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private void ChangeFragmentsDisplay(){
        FrameLayout frame1 = (FrameLayout) findViewById(R.id.fragment);
        FrameLayout frame2 = (FrameLayout) findViewById(R.id.fragment2);
        //get Display size
        int width = getDisplayWidth();
        /*          LANDSCAPE           */
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            if(width >= 800) {
                int widthNew = width / 3;
                frame1.getLayoutParams().width = widthNew;
                frame2.setVisibility(View.VISIBLE);
                frame2.getLayoutParams().width = width - widthNew;

                if(weatherFragment!= null){
                    if(weatherFragment.isAdded()){
                        long id = weatherFragment.getCityID();
                        String city = weatherFragment.getCityName();

                        if(!isBigDisplay)
                            this.onBackPressed();

                        weatherFragment = WeatherFragment.newInstance(city,id);
                    }else{
                        weatherFragment = WeatherFragment.newInstance(null,0);
                    }
                }else{
                    weatherFragment = WeatherFragment.newInstance(null,0);
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment2, weatherFragment).commit();
                menuFragment.changeOrientationToLandscape();
                isBigDisplay = true;
            }
        /*          PORTRAIT           */
        }else{
            if(isBigDisplay) {
                if (isFullScreenWeather)
                    onMenuClick();

                frame1.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                frame2.setVisibility(View.GONE);
                if (weatherFragment != null) {
                    weatherFragment.setArgs(null, 0);
                    getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
                }
                menuFragment.changeOrientationToPortrait();
                isBigDisplay = false;
            }
        }
    }


}
