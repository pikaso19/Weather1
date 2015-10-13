package mu.zz.pikaso.weather.representations;

import mu.zz.pikaso.weather.tools.CountryCodes;

/**
 * Created by pikaso on 03.10.2015.
 */
public class City {
    private long id;
    private String name;
    private String country;


    public City(){}


    public City(int id, String name, String country){
        this.id = id;
        this.name = name;
        if(country.length()>2){
            CountryCodes a = new CountryCodes();
            this.country = a.getCode(country);
        }else{
            this.country = country;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlagURL() {
        String s = "";
        if(country.length()>2){
            CountryCodes a = new CountryCodes();
            s = a.getCode(country);
        }else{
            s = country;
        }
        return "http://openweathermap.org/images/flags/"+s.toLowerCase()+".png";//hardcode!
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return name+","+country;
    }
}
