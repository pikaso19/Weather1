package mu.zz.pikaso.weather.internet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.pojo.CurrentWeather;
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

    /*
     *  Search and Get available cities in openweather
     */
    public List<String> getCitiesList(String cityPattern){
        List<String> citiesMatch = new ArrayList<String>();
        WeatherAPI weatherService = retrofit.create(WeatherAPI.class);
        Call<JsonObject> citiesJSON = weatherService.getAvailableCities(cityPattern);
        try {
            Response jsonObjectResponse = citiesJSON.execute();
            JsonArray jArr = ((JsonObject) jsonObjectResponse.body()).getAsJsonArray("list");
            for (int i=0; i < jArr.size(); i++) {
               String city = new String();
                JsonObject obj = jArr.get(i).getAsJsonObject();

                String name = obj.get("name").getAsString();
                JsonObject sys = obj.get("sys").getAsJsonObject();
                String country = sys.get("country").getAsString();
                city = name +", " + country;

                citiesMatch.add(city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return citiesMatch;
    }


}

