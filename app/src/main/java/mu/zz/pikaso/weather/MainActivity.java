package mu.zz.pikaso.weather;


import android.app.Activity;
import android.app.Application;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


import mu.zz.pikaso.weather.ui.IActionUI;

public class MainActivity extends AppCompatActivity implements IActionUI{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }
            // Create a new Fragment to be placed in the activity layout
            MainActivityFragment firstFragment = new MainActivityFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, firstFragment).commit();
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

        //TODO: delete line for changing fragment
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new WeatherFragment()).addToBackStack(null).commit();


    }

    @Override
    public void onClickAddCity() {
        AddCityFragmentDialog dialog = new AddCityFragmentDialog();
        dialog.show(getSupportFragmentManager().beginTransaction(), null);
    }

    @Override
    public void onClickExit() {
        finish();
        System.exit(0);
    }

    @Override
    public void onCitySelected(String city) {
        //TODO: call weather fragment
        WeatherFragment wf = WeatherFragment.newInstance("Lviv");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, wf).addToBackStack(null).commit();
    }

    @Override
    public void onCityDelete(String city) {
        //TODO: delete city on long press
    }

    /*
                                                    FRAGMENT 2
    */

    @Override
    public void onClickRefresh() {

    }

    @Override
    public void onClickMenu() {

    }

    /*
                                                    FRAGMENT 3
    */

    @Override
    public void onCityAdd(String city) {
        Log.d("0k19vej5ug", "Selected city: "+city+"\n");
    }
}
