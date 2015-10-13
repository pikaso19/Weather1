package mu.zz.pikaso.weather.representations;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pikaso on 03.10.2015.
 */
public class Weather implements Comparable<Weather>{
    private Calendar date;

    private double day;
    private double min;
    private double max;
    private double night;
    private double eve;
    private double morn;

    private String description;
    private String image;                  //image file name "weather state"


    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return "http://openweathermap.org/img/w/"+image+".png";
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(Weather another) {
        return getDate().getTime().compareTo(another.getDate().getTime());
    }

    public static int daysDifference(Date now, Date before){
        long endDay = now.getTime() / 1000 / 60 / 60 / 24;
        long startDay = before.getTime() / 1000 / 60 / 60 / 24;
        long daysBetween = endDay - startDay;
        Log.d("0k19vej5ug", now.toString() + " vs " + before.toString() + " = "+daysBetween + " days");
        return (int)daysBetween;
    }

    public static long hoursDifference(Date now, Date before){
        long endHour = now.getTime() / 1000 / 60 / 60;
        long startHour = before.getTime() / 1000 / 60 / 60;
        long hoursBetween = endHour - startHour;
        Log.d("0k19vej5ug", now.toString() + " vs " + before.toString() + " = "+hoursBetween + " hours");
        return (int)hoursBetween;
    }
}
