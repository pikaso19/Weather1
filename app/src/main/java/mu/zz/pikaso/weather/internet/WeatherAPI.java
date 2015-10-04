package mu.zz.pikaso.weather.internet;


import mu.zz.pikaso.weather.pojo.CurrentWeather;
import retrofit.Call;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by pikaso on 20.09.2015.
 */
public interface WeatherAPI {

    @GET("/data/2.5/weather?APPID=0500035c707563f90fcdd99cf6b1009a&")
    Call<CurrentWeather> getCurrentWeather(@Query("q") String city);
}
