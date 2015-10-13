package mu.zz.pikaso.weather.sql;

import android.app.ActionBar;
import android.provider.BaseColumns;

/**
 * Created by pikaso on 11.10.2015.
 */
public final class DataBaseContract {
    public DataBaseContract(){}

    /* Inner class that defines the table contents */
    public static abstract class TableCity implements BaseColumns {
        public static final String TABLE_NAME = "City";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_COUNTRY = "country";
        public static final String COLUMN_NAME_WUPDATE = "wupdate"; // current weather update
        public static final String COLUMN_NAME_FUPDATE = "fupdate"; // forecast update

    }

    /* Inner class that defines the table contents */
    public static abstract class TableWeather implements BaseColumns {
        public static final String TABLE_NAME = "Weather";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DAY = "day";
        public static final String COLUMN_NAME_MIN = "min";
        public static final String COLUMN_NAME_MAX = "max";
        public static final String COLUMN_NAME_NIGHT = "night";
        public static final String COLUMN_NAME_MORNING = "morn";
        public static final String COLUMN_NAME_EVENING = "eve";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_ICON = "icon";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CITYID = "city_id";
    }

    public static abstract class Rest{
        public static final String CREATE_TABLE = "CREATE TABLE ";
        public static final String COMMA = ",";

        public static final String TYPE_INTEGER = " INTEGER";
        public static final String TYPE_TEXT = " TEXT";
        public static final String TYPE_DATE = " DATE";
        public static final String TYPE_REAL = " REAL";

        public static final String PRIMARY_KEY = " PRIMARY KEY";
        public static final String FOREIGN_KEY = " FOREIGN KEY ";
        public static final String REFERENCES = " REFERENCES ";
        public static final String NOT_NULL = " NOT NULL ";

        public static final String DROP_TABLE = "DROP TABLE IF EXIST ";
    }

    public static abstract class Query{
        public static final String CREATE_TABLECITY = Rest.CREATE_TABLE + TableCity.TABLE_NAME + " (" +
                TableCity.COLUMN_NAME_ID + Rest.TYPE_INTEGER + Rest.PRIMARY_KEY + Rest.COMMA +
                TableCity.COLUMN_NAME_NAME + Rest.TYPE_TEXT + Rest.COMMA +
                TableCity.COLUMN_NAME_COUNTRY + Rest.TYPE_TEXT + Rest.COMMA +
                TableCity.COLUMN_NAME_WUPDATE + Rest.TYPE_DATE + Rest.COMMA +
                TableCity.COLUMN_NAME_FUPDATE + Rest.TYPE_DATE + ")";

        public static final String CREATE_TABLEWEATHER = Rest.CREATE_TABLE + TableWeather.TABLE_NAME + " (" +
                TableWeather.COLUMN_NAME_ID + Rest.TYPE_INTEGER + Rest.PRIMARY_KEY + Rest.COMMA +
                TableWeather.COLUMN_NAME_DAY + Rest.TYPE_REAL + Rest.COMMA +
                TableWeather.COLUMN_NAME_MIN + Rest.TYPE_REAL + Rest.COMMA +
                TableWeather.COLUMN_NAME_MAX + Rest.TYPE_REAL + Rest.COMMA +
                TableWeather.COLUMN_NAME_NIGHT + Rest.TYPE_REAL + Rest.COMMA +
                TableWeather.COLUMN_NAME_MORNING + Rest.TYPE_REAL + Rest.COMMA +
                TableWeather.COLUMN_NAME_EVENING + Rest.TYPE_REAL + Rest.COMMA +
                TableWeather.COLUMN_NAME_DESCRIPTION + Rest.TYPE_REAL + Rest.COMMA +
                TableWeather.COLUMN_NAME_ICON + Rest.TYPE_TEXT + Rest.COMMA +
                TableWeather.COLUMN_NAME_DATE + Rest.TYPE_DATE + Rest.COMMA +
                TableWeather.COLUMN_NAME_CITYID + Rest.TYPE_INTEGER + Rest.COMMA +
                Rest.FOREIGN_KEY + "(" + TableWeather.COLUMN_NAME_CITYID + ")" +
                Rest.REFERENCES + TableCity.TABLE_NAME + "(" + TableCity.COLUMN_NAME_ID + "))";



        public static final String DROP_TABLECITY = Rest.DROP_TABLE + TableCity.TABLE_NAME;
        public static final String DROP_TABLEWEATHER = Rest.DROP_TABLE + TableWeather.TABLE_NAME;



        public static final String GET_CITY_BY_ID = "SELECT * FROM "+ TableCity.TABLE_NAME + " WHERE " +
                TableCity.COLUMN_NAME_ID + " = ";
        public static final String GET_ALL_CITIES = "SELECT * FROM " + TableCity.TABLE_NAME;
        public static final String GET_WUPDATE = "SELECT (wupdate) FROM " + TableCity.TABLE_NAME + " WHERE " +
                TableCity.COLUMN_NAME_ID + " = ";
        public static final String GET_FUPDATE = "SELECT (fupdate) FROM " + TableCity.TABLE_NAME+ " WHERE " +
                TableCity.COLUMN_NAME_ID + " = ";
        public static final String GET_WEATHER_BY_ID = "SELECT * FROM "+TableWeather.TABLE_NAME + " WHERE " +
                TableWeather.COLUMN_NAME_ID + " = ";
        public static final String GET_FORECAST = "SELECT * FROM " + TableWeather.TABLE_NAME + " WHERE " +
                TableWeather.COLUMN_NAME_CITYID + " = ";



        public static final String UPDATE_WLU = TableCity.COLUMN_NAME_ID + " = ";
        public static final String UPDATE_FLU = TableCity.COLUMN_NAME_ID + " = ";
        public static final String UPDATE_CURRENT_WEATHER = TableWeather.COLUMN_NAME_ID + " = ";



        public static final String DELETE_CITY_WHERE_ID = TableCity.COLUMN_NAME_ID + " = ";
        public static final String DELETE_WEATHER_WHERE_ID = TableWeather.COLUMN_NAME_ID + " = ";
        public static final String DELETE_WEATHER_WHERE_CITYID = TableWeather.COLUMN_NAME_CITYID + " = ";








    }


}
