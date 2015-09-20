package mu.zz.pikaso.weather.internet;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import rx.Observable;

import mu.zz.pikaso.weather.pojo.CurrentWeather;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Subscriber;

/**
 * Created by pikaso on 18.09.2015.
 */


public class Connection  {
    private Retrofit retrofit;

    public Connection() {

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public CurrentWeather test(){
        WeatherAPI weatherService = retrofit.create(WeatherAPI.class);
        Call<CurrentWeather> weatherCall = weatherService.getCurrentWeather("Lviv");
        try {
            Response<CurrentWeather> currentWeather = weatherCall.execute();
            return currentWeather.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}

