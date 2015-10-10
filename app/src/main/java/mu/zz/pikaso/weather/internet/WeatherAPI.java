package mu.zz.pikaso.weather.internet;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import mu.zz.pikaso.weather.pojo.CurrentWeather;
import retrofit.Call;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by pikaso on 20.09.2015.
 */
public interface WeatherAPI {

    @GET("/data/2.5/weather?APPID=0500035c707563f90fcdd99cf6b1009a&units=metric")
    Call<CurrentWeather> getCurrentWeather(@Query("id") int cityID);

    @GET("/data/2.5/find?mode=json&cnt=10&type=like")
    Call<JsonObject> getAvailableCities(@Query("q") String city);

    @GET("/data/2.5/forecast/daily?APPID=0500035c707563f90fcdd99cf6b1009a&units=metric&cnt=16")
    Call<JsonObject> getForecast16(@Query("id") int cityID);
}
