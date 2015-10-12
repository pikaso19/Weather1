package mu.zz.pikaso.weather.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        db.execSQL(DataBaseContract.Query.CREATE_TABLEMAIN);
        Log.d("0k19vej5ug", "DataBase tables created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Delete database on upgrade and create empty new db
        db.execSQL(DataBaseContract.Query.DROP_TABLEMAIN);
        db.execSQL(DataBaseContract.Query.DROP_TABLECITY);
        db.execSQL(DataBaseContract.Query.DROP_TABLEWEATHER);
        Log.d("0k19vej5ug", "DataBase deleted!");

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
                Log.d("0k19vej5ug", "DataBase insert City");
            }else{
                Log.d("0k19vej5ug", "DataBase can't insert the City exsist");
            }
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

                Log.d("0k19vej5ug", "DataBase get City");
                return city;
            }

            Log.d("0k19vej5ug", "DataBase can't get the City");
            return null;
        }

        private boolean isCityExsist(long code){
            SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
            Cursor c = db.rawQuery(DataBaseContract.Query.GET_CITY_BY_ID + code, null);
            if (c.getCount() > 0) {
                Log.d("0k19vej5ug","SQL: Record exsist");
                c.close();
                return true;
            }
            Log.d("0k19vej5ug","SQL: New record");
            c.close();
            return false;
        }
    }

    public class TableWeather{
        private TableWeather(){}

        public long insert(Weather weather){
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
            // insert row
            long id = db.insert(DataBaseContract.TableWeather.TABLE_NAME, null, values);
            Log.d("0k19vej5ug", "DataBase insert City");
            return id;
        }
    }

    public Weather select(long id){
        SQLiteDatabase db = DataBaseHelper.this.getReadableDatabase();
        // fill data
        Cursor c = db.rawQuery(DataBaseContract.Query.GET_WEATHER_BY_ID+id, null);
        if (c != null)
            c.moveToFirst();

        if(c.getCount() > 0) {
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
            Log.d("0k19vej5ug", "DataBase get City");
            return weather;
        }
        Log.d("0k19vej5ug", "DataBase can't get the City");
        return null;
    }


}
