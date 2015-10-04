package mu.zz.pikaso.weather.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import mu.zz.pikaso.weather.R;
import mu.zz.pikaso.weather.representations.City;

/**
 * Created by pikaso on 02.10.2015.
 */
public class CitiesListAdapter extends BaseAdapter {
    private final Context context;
    private final List<City> cities;

    public CitiesListAdapter(Context context, List<City> cities ) {
        this.context = context;
        this.cities=cities;
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cities.get(position).getId();
    }

    public View getView(int position,View convertView,ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.citylist_row, null);
        }

        ImageView flag = (ImageView) convertView.findViewById(R.id.flag);
        TextView city = (TextView) convertView.findViewById(R.id.city);

        city.setText(cities.get(position).getName());
        flag.setImageResource(cities.get(position).getFlag());

        return convertView;
    };
}
