package mu.zz.pikaso.weather.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;

import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.sql.DataBaseHelper;

/**
 * Created by pikaso on 14.10.2015.
 */
public abstract class Conditions {
    public static final boolean isForecastUpToDate(DataBaseHelper db, long cityID){
        Calendar a = db.City.getForecastLastUpdate(cityID);
        if(a==null){
            return false;
        }else{
            Calendar cal = Calendar.getInstance();
            int days = Weather.daysDifference(cal.getTime(), a.getTime());
            if(days>0){
                return false;
            }
        }
        return true;
    }

    public static final boolean isWeatherUpToDate(DataBaseHelper db, long cityID){
        Calendar a = db.City.getWeatherLastUpdate(cityID);
        if(a==null){
            return false;
        }else{
            Calendar cal = Calendar.getInstance();
            int days = Weather.daysDifference(cal.getTime(), a.getTime());
            if(days>0){
                return false;
            }
        }
        return true;
    }

    public static boolean isInternetAvailable(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
