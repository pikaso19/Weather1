package mu.zz.pikaso.weather;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.adapters.CitiesListAdapter;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment {
    private Button btnRefreshAll = null;
    private Button btnAddCity = null;
    private Button btnExit = null;

    public MainActivityFragment() {
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnRefreshAll = (Button) getView().findViewById(R.id.btnRefreshAll);
        btnAddCity = (Button) getView().findViewById(R.id.btnAddCity);
        btnExit = (Button) getView().findViewById(R.id.btnExit);

/*      // how i get current weather
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new ShowWeather(editText1)).execute();
            }
        });
*/

        /* -------- BUTTONS CLICK LISTENERS -------- */
        btnRefreshAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActionUI) getActivity()).onClickRefreshALL();
            }
        });

        btnAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActionUI) getActivity()).onClickAddCity();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActionUI) getActivity()).onClickExit();
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, null, true);
    }

    List<City> cities = new ArrayList<City>();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: read favourites cities from sql!
        cities.add(new City(0,"Lviv",R.drawable.noflag));
        cities.add(new City(1,"Kyiv",R.drawable.noflag));
        cities.add(new City(3,"London",R.drawable.noflag));

        CitiesListAdapter adapter=new CitiesListAdapter(getActivity(), cities);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try{
            ((IActionUI) getActivity()).onCitySelected(cities.get(position).getName());
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement IActionUI");
        }

    }
}
