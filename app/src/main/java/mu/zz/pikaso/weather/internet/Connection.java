package mu.zz.pikaso.weather.internet;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mu.zz.pikaso.weather.pojo.CurrentWeather;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;



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

    /*
     *  Get current weather
     */
    public CurrentWeather getCurrentWeather(int cityID){
        WeatherAPI openWeatherService = retrofit.create(WeatherAPI.class);
        Call<CurrentWeather> weatherCall = openWeatherService.getCurrentWeather(cityID);
        try {
            Response<CurrentWeather> currentWeather = weatherCall.execute();
            return currentWeather.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     *  Search and Get available cities in openweather
     */
    public List<City> getCitiesList(String cityPattern){
        List<City> citiesMatch = new ArrayList<City>();
        WeatherAPI openWeatherService = retrofit.create(WeatherAPI.class);
        Call<JsonObject> citiesJSON = openWeatherService.getAvailableCities(cityPattern);
        try {
            Response jsonObjectResponse = citiesJSON.execute();
            JsonArray jArr = ((JsonObject) jsonObjectResponse.body()).getAsJsonArray("list");
            for (int i=0; i < jArr.size(); i++) {
                JsonObject obj = jArr.get(i).getAsJsonObject();
                City city = new City();
                Integer id = obj.get("id").getAsInt();
                city.setId(id);
                String name = obj.get("name").getAsString();
                city.setName(name);
                JsonObject sys = obj.get("sys").getAsJsonObject();
                String country = sys.get("country").getAsString();
                city.setCountry(country);

                citiesMatch.add(city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return citiesMatch;
    }


    public List<Weather> getWeatherForecast(int cityID){
        List<Weather> forecast = new ArrayList<Weather>();
        WeatherAPI openWeatherService = retrofit.create(WeatherAPI.class);
        Call<JsonObject> forecastJSON = openWeatherService.getForecast16(cityID);
        try {
            Response jsonObjectResponse = forecastJSON.execute();
            Log.d("0k19vej5ug",jsonObjectResponse.raw().request().urlString());
            JsonArray jArr = ((JsonObject) jsonObjectResponse.body()).getAsJsonArray("list");
            for(int i=0;i<jArr.size();i++){
                Weather weather = new Weather();
                JsonObject obj = jArr.get(i).getAsJsonObject();
                long dates = obj.get("dt").getAsLong();
                JsonObject tempObj = obj.get("temp").getAsJsonObject();
                double day = tempObj.get("day").getAsDouble();
                double min = tempObj.get("min").getAsDouble();
                double max = tempObj.get("max").getAsDouble();
                double night = tempObj.get("night").getAsDouble();
                double eve = tempObj.get("eve").getAsDouble();
                double morn = tempObj.get("morn").getAsDouble();
                JsonArray weatherArr = obj.getAsJsonArray("weather");
                String description = weatherArr.get(0).getAsJsonObject().get("description").getAsString();
                String icon = weatherArr.get(0).getAsJsonObject().get("icon").getAsString();

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(dates*1000L);
                weather.setDate(calendar);
                weather.setDay(day);
                weather.setMin(min);
                weather.setMax(max);
                weather.setNight(night);
                weather.setEve(eve);
                weather.setMorn(morn);
                weather.setDescription(description);
                weather.setImage(icon);

                forecast.add(weather);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return forecast;
    }
}

