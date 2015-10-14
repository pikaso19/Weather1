package mu.zz.pikaso.weather.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;

/**
 * Created by pikaso on 11.10.2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Weather.db";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public TableCity City;
    public TableWeather Weather;

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        City = new TableCity();
        Weather = new TableWeather();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create db tables
        db.execSQL(DataBaseContract.Query.CREATE_TABLECITY);
        db.execSQL(DataBaseContract.Query.CREATE_TABLEWEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete database on upgrade and create empty new db
        db.execSQL(DataBaseContract.Query.DROP_TABLECITY);
        db.execSQL(DataBaseContract.Query.DROP_TABLEWEATHER);

        onCreate(db);
    }

    public class TableCity{
        private TableCity(){}

        public boolean insert(City city){
            if(!isCityExsist(city.getId())){
                SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
                // fill data
                ContentValues values = new ContentValues();
                values.put(DataBaseContract.TableCity.COLUMN_NAME_NAME, city.getName());
                values.put(DataBaseContract.TableCity.COLUMN_NAME_ID, city.getId());
                values.put(DataBaseContract.TableCity.COLUMN_NAME_COUNTRY, city.getCountry());
                // insert row
                db.insert(DataBaseContract.TableCity.TABLE_NAME, null, values);
                return true;
            }
            return false;
        }

        public void updateWLU(long cityID){
            //hourly update from Internet available
            if(isCityExsist(cityID)){
                SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
                // fill data
                ContentValues values = new ContentValues();
                SimpleDateFormat iso = new SimpleDateFormat(DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                values.put(DataBaseContract.TableCity.COLUMN_NAME_WUPDATE, iso.format(calendar.getTime()));
                // update row
                db.update(DataBaseContract.TableCity.TABLE_NAME, values, DataBaseContract.Query.UPDATE_WLU + cityID, null);
            }
        }

        public void updateFLU(long cityID){
            //daily update from Internet available
            if(isCityExsist(cityID)){
                SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
                // fill data
                ContentValues values = new ContentValues();
                SimpleDateFormat iso = new SimpleDateFormat(DATE_FORMAT);
                Calendar calendar = Calendar.getInstance();
                values.put(DataBaseContract.TableCity.COLUMN_NAME_FUPDATE, iso.format(calendar.getTime()));
                // update row
                db.update(DataBaseContract.TableCity.TABLE_NAME, values, DataBaseContract.Query.UPDATE_FLU+cityID, null);
                Log.d("SQLTEST[C]", "DataBase WLU set");
            }else{
                Log.d("SQLTEST[C]", "No city with id = "+cityID);
            }
        }

        public List<City> selectAll(){
            List<City> cities = new ArrayList<City>();
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_ALL_CITIES, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        City city = new City();
                        city.setId(c.getInt(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_ID)));
                        city.setName(c.getString(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_NAME)));
                        city.setCountry(c.getString(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_COUNTRY)));
                        cities.add(city);
                    } while (c.moveToNext());
                }
                if(c!=null)
                    if(!c.isClosed())
                        c.close();
            }

            return cities;
        }

        public City select(long code){
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_CITY_BY_ID+code, null);
            if (c != null) {
                c.moveToFirst();
                if (c.getCount() > 0) {
                    City city = new City();
                    city.setId(c.getInt(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_ID)));
                    city.setName(c.getString(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_NAME)));
                    city.setCountry(c.getString(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_COUNTRY)));
                    if(c!=null)
                        if(!c.isClosed())
                            c.close();
                    return city;
                }
                if(c!=null)
                    if(!c.isClosed())
                        c.close();
            }
            return null;
        }

        public void delete(long code){
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            // fill data
            db.delete(DataBaseContract.TableCity.TABLE_NAME, DataBaseContract.Query.DELETE_CITY_WHERE_ID + code, null);
            db.delete(DataBaseContract.TableWeather.TABLE_NAME, DataBaseContract.Query.DELETE_WEATHER_WHERE_CITYID + code, null);
        }

        private boolean isCityExsist(long code){
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_CITY_BY_ID + code, null);
            if (c.getCount() > 0) {
                if(c!=null)
                    if(!c.isClosed())
                        c.close();
                return true;
            }
            if(c!=null)
                if(!c.isClosed())
                    c.close();
            return false;
        }

        public Calendar getWeatherLastUpdate(long id){
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_WUPDATE+id, null);
            if (c != null) {
                c.moveToFirst();
                if (c.getCount() > 0) {
                    String strDate = c.getString(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_WUPDATE));
                    if(strDate == null){
                        if(c!=null)
                            if(!c.isClosed())
                                c.close();
                        return null;
                    }
                    SimpleDateFormat iso = new SimpleDateFormat(DATE_FORMAT);
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(iso.parse(strDate));
                        if(c!=null)
                            if(!c.isClosed())
                                c.close();
                        return calendar;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(c!=null)
                        if(!c.isClosed())
                            c.close();
                    return null;
                }
            }
            if(c!=null)
                if(!c.isClosed())
                    c.close();
            return null;
        }

        public Calendar getForecastLastUpdate(long id){
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_FUPDATE+id, null);
            if (c != null) {
                c.moveToFirst();
                if (c.getCount() > 0) {
                    String strDate = c.getString(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_FUPDATE));
                    if(strDate == null){
                        if(c!=null)
                            if(!c.isClosed())
                                c.close();
                        return null;
                    }
                    SimpleDateFormat iso = new SimpleDateFormat(DATE_FORMAT);
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(iso.parse(strDate));
                        if(c!=null)
                            if(!c.isClosed())
                                c.close();
                        return calendar;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(c!=null)
                        if(!c.isClosed())
                            c.close();
                    return null;
                }
            }
            if(c!=null)
                if(!c.isClosed())
                    c.close();
            return null;
        }

    }

    public class TableWeather {
        private TableWeather() {}

        public long insert(Weather weather, long cityID) {
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            // fill data
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_DAY, weather.getDay());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_MIN, weather.getMin());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_MAX, weather.getMax());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_NIGHT, weather.getNight());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_MORNING, weather.getMorn());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_EVENING, weather.getEve());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_DESCRIPTION, weather.getDescription());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_ICON, weather.getImage());
            SimpleDateFormat iso = new SimpleDateFormat(DATE_FORMAT);
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_DATE, iso.format(weather.getDate().getTime()));
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_CITYID, cityID);
            // insert row
            long id = db.insert(DataBaseContract.TableWeather.TABLE_NAME, null, values);
            return id;
        }

        public List<Weather> selectAll(long cityID){
            List<Weather> forecast = new ArrayList<Weather>();
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_FORECAST + cityID, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Weather weather = new Weather();
                        weather.setDay(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_DAY)));
                        weather.setMin(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_MIN)));
                        weather.setMax(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_MAX)));
                        weather.setNight(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_NIGHT)));
                        weather.setMorn(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_MORNING)));
                        weather.setEve(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_EVENING)));
                        weather.setDescription(c.getString(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_DESCRIPTION)));
                        weather.setImage(c.getString(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_ICON)));
                        String strDate = c.getString(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_DATE));
                        SimpleDateFormat iso = new SimpleDateFormat(DATE_FORMAT);
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(iso.parse(strDate));
                            weather.setDate(calendar);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        forecast.add(weather);
                    } while (c.moveToNext());
                }
                if(c!=null)
                    if(!c.isClosed())
                        c.close();
            }
            if(c!=null)
                if(!c.isClosed())
                    c.close();
            return forecast;
        }

        public Weather select(long id) {
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_WEATHER_BY_ID + id, null);
            if (c != null)
            {
                c.moveToFirst();
                if (c.getCount() > 0) {
                    Weather weather = new Weather();
                    weather.setDay(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_DAY)));
                    weather.setMin(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_MIN)));
                    weather.setMax(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_MAX)));
                    weather.setNight(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_NIGHT)));
                    weather.setMorn(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_MORNING)));
                    weather.setEve(c.getDouble(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_EVENING)));
                    weather.setDescription(c.getString(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_DESCRIPTION)));
                    weather.setImage(c.getString(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_ICON)));
                    String strDate = c.getString(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_DATE));
                    SimpleDateFormat iso = new SimpleDateFormat(DATE_FORMAT);
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(iso.parse(strDate));
                        weather.setDate(calendar);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(c!=null)
                        if(!c.isClosed())
                            c.close();
                    return weather;
                }
                if(c!=null)
                    if(!c.isClosed())
                        c.close();
            }
            return null;
        }

        public void updateByCurrent(Weather current, long id){
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            // fill data
            ContentValues values = new ContentValues();
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_DESCRIPTION, current.getDescription());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_ICON, current.getImage());
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_DAY, current.getDay());
            // update row
            db.update(DataBaseContract.TableWeather.TABLE_NAME, values, DataBaseContract.Query.UPDATE_CURRENT_WEATHER + id, null);
        }

        public long getTodayWeatherID(long cityID){
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            Calendar today = Calendar.getInstance();
            long id = -1;
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_FORECAST+cityID, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        id = c.getLong(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_ID));
                        String strDate = c.getString(c.getColumnIndex(DataBaseContract.TableWeather.COLUMN_NAME_DATE));
                        SimpleDateFormat iso = new SimpleDateFormat(DATE_FORMAT);
                        try {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(iso.parse(strDate));
                            int daydiff = mu.zz.pikaso.weather.representations.Weather.daysDifference(today.getTime(), calendar.getTime());
                            if (daydiff == 0) {
                                if(c!=null)
                                    if(!c.isClosed())
                                        c.close();
                                return id;
                            }else{
                                id = -1;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } while (c.moveToNext());
                }
                if(c!=null)
                    if(!c.isClosed())
                        c.close();
            }
            return id;
        }

        public void delete(long id){
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            db.delete(DataBaseContract.TableWeather.TABLE_NAME, DataBaseContract.Query.DELETE_WEATHER_WHERE_ID + id, null);
        }

        public void deleteByCity(long cityID){
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            db.delete(DataBaseContract.TableWeather.TABLE_NAME, DataBaseContract.Query.DELETE_WEATHER_WHERE_CITYID + cityID, null);
        }

        public void deleteWhole(){
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            db.delete(DataBaseContract.TableWeather.TABLE_NAME, null, null);
        }

        public void deleteOldWeather(){
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            Calendar today = Calendar.getInstance();
            SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
            db.delete(DataBaseContract.TableWeather.TABLE_NAME, "strftime('%Y-%m-%d', date) < strftime('%Y-%m-%d', '"+iso.format(today.getTime())+"')", null);
        }

    }



}
