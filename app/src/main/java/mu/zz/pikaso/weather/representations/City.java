package mu.zz.pikaso.weather.representations;

/**
 * Created by pikaso on 03.10.2015.
 */
public class City {
    private int id;
    private String name;
    private String country;

    public City(){}


    public City(int id, String name, String country){
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlagURL() {
        return "http://openweathermap.org/images/flags/"+country.toLowerCase()+".png";
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
