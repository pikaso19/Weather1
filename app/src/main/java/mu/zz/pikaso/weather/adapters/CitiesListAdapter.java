package mu.zz.pikaso.weather.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.R;
import mu.zz.pikaso.weather.representations.City;

/**
 * Created by pikaso on 02.10.2015.
 */
public class CitiesListAdapter extends ArrayAdapter<City> {
    private Context context;
    private List<City> cities;

    @Override
    public void add(City object) {
        cities.add(object);
    }

    @Override
    public void remove(City object) {
        cities.remove(object);
    }

    @Override
    public void clear() {
        cities.clear();
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public City getItem(int position) {
        return cities.get(position);
    }

    public CitiesListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        // TODO Auto-generated constructor stub
        this.context = context;
        cities = new ArrayList<City>();

    }


    private class ViewHolder{
        ImageView flag;
        TextView city;
    }

    public View getView(int position,View convertView,ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            // inflate item layout
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_citylist, parent, false);

            // initialize the view holder
            viewHolder = new ViewHolder();
            viewHolder.flag = (ImageView) convertView.findViewById(R.id.flag);
            viewHolder.city = (TextView) convertView.findViewById(R.id.city);
            convertView.setTag(viewHolder);
        } else {
            // recycle the already inflated view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.city.setText(cities.get(position).getName());

        Glide.with(context).load(cities.get(position).getFlagURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.noflag)//TODO: change to no_image
                .into(viewHolder.flag);

        return convertView;
    };

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        context = null;
        cities = null;
    }
}
