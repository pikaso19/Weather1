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
        Log.d("SQLTEST", "DataBase tables created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete database on upgrade and create empty new db
        db.execSQL(DataBaseContract.Query.DROP_TABLECITY);
        db.execSQL(DataBaseContract.Query.DROP_TABLEWEATHER);
        Log.d("SQLTEST", "DataBase deleted!");

        onCreate(db);
    }

    public class TableCity{
        private TableCity(){}

        public void insert(City city){
            if(!isCityExsist(city.getId())){
                SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
                // fill data
                ContentValues values = new ContentValues();
                values.put(DataBaseContract.TableCity.COLUMN_NAME_NAME, city.getName());
                values.put(DataBaseContract.TableCity.COLUMN_NAME_ID, city.getId());
                values.put(DataBaseContract.TableCity.COLUMN_NAME_COUNTRY, city.getCountry());
                // insert row
                db.insert(DataBaseContract.TableCity.TABLE_NAME, null, values);
                Log.d("SQLTEST[C]", "DataBase insert City");
            }else{
                Log.d("SQLTEST[C]", "DataBase can't insert the City exsist");
            }
        }

        public List<City> selectAll(){
            List<City> cities = new ArrayList<City>();
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Log.d("0k19vej5ug", "SALL: "+DataBaseContract.Query.GET_ALL_CITIES);
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
                if (c != null && !c.isClosed()) {
                    c.close();
                }
            }

            //LOG FOR (delete after test)
            Log.d("SQLTEST[C]","selectALL():");
            for(int i=0;i<cities.size();i++){
                Log.d("SQLTEST[C]","City["+(i)+"] = "+cities.get(i));
            }

            return cities;
        }

        public City select(long code){
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_CITY_BY_ID+code, null);
            if (c != null)
                c.moveToFirst();

            if(c.getCount() > 0) {
                City city = new City();
                city.setId(c.getInt(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_ID)));
                city.setName(c.getString(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_NAME)));
                city.setCountry(c.getString(c.getColumnIndex(DataBaseContract.TableCity.COLUMN_NAME_COUNTRY)));

                Log.d("SQLTEST[C]", "DataBase get City");
                return city;
            }

            Log.d("SQLTEST[C]", "DataBase can't get the City");
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
                Log.d("SQLTEST[C]","SQL: Record exsist");
                c.close();
                return true;
            }
            Log.d("SQLTEST[C]", "SQL: New record");
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
                    SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(iso.parse(strDate));
                        Log.d("SQLTEST[C]", "DataBase get wlu");
                        if(c!=null && !c.isClosed())
                            c.close();
                        return calendar;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(c!=null && !c.isClosed())
                        c.close();
                    Log.d("SQLTEST[C]", "DataBase can't parse wlu");
                    return null;
                }
            }
            if(c!=null && !c.isClosed())
                c.close();
            Log.d("SQLTEST[C]", "DataBase can't get the wlu");
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
                    SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(iso.parse(strDate));
                        Log.d("SQLTEST[C]", "DataBase get flu");
                        if(c!=null && !c.isClosed())
                            c.close();
                        return calendar;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(c!=null && !c.isClosed())
                        c.close();
                    Log.d("SQLTEST[C]", "DataBase can't parse flu");
                    return null;
                }
            }
            if(c!=null && !c.isClosed())
                c.close();
            Log.d("SQLTEST[C]", "DataBase can't get the flu");
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
            SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_DATE, iso.format(weather.getDate().getTime()));
            values.put(DataBaseContract.TableWeather.COLUMN_NAME_CITYID, cityID);
            // insert row
            long id = db.insert(DataBaseContract.TableWeather.TABLE_NAME, null, values);
            Log.d("SQLTEST[W]", "DataBase insert Weather "+id);
            return id;
        }

        public List<Weather> selectAll(long cityID){
            List<Weather> forecast = new ArrayList<Weather>();
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Log.d("0k19vej5ug", "WALL: " + DataBaseContract.Query.GET_FORECAST+cityID);
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
                        SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                if (c != null && !c.isClosed()) {
                    c.close();
                }
            }

            //LOG FOR (delete after test)
            Log.d("SQLTEST[W]","selectALL():");
            for(int i=0;i<forecast.size();i++){
                Log.d("SQLTEST[W]","Forecast["+(i)+"] = "+forecast.get(i));
            }

            return forecast;
        }

        public Weather select(long id) {
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            // fill data
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_WEATHER_BY_ID + id, null);
            if (c != null)
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
                SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(iso.parse(strDate));
                    weather.setDate(calendar);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("SQLTEST[W]", "DataBase get Weather " + weather.getDescription());
                return weather;
            }
            Log.d("SQLTEST[W]", "DataBase can't get the Weather");
            return null;
        }

        public void delete(long id){
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            // fill data
            Log.d("SQLTEST[W]", "delete(" + id + ")");
            db.delete(DataBaseContract.TableWeather.TABLE_NAME, DataBaseContract.Query.DELETE_WEATHER_WHERE_ID + id, null);
        }

        public void deleteByCity(long cityID){
            SQLiteDatabase db = DataBaseHelper.this.getWritableDatabase();
            // fill data
            Log.d("SQLTEST[W]", "deleteByCity(" + cityID + ")");
            db.delete(DataBaseContract.TableWeather.TABLE_NAME, DataBaseContract.Query.DELETE_WEATHER_WHERE_CITYID + cityID, null);
        }

    }



}
