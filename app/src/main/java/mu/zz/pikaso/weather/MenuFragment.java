package mu.zz.pikaso.weather;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import mu.zz.pikaso.weather.adapters.CitiesListAdapter;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuFragment extends ListFragment {
    private CitiesListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu,
                container, false);

        final Button btnRefreshAll = (Button) rootView.findViewById(R.id.btnRefreshAll);
        final Button btnAddCity = (Button) rootView.findViewById(R.id.btnAddCity);
        final Button btnExit = (Button) rootView.findViewById(R.id.btnExit);
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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: read favourites cities from sql!
        if(adapter == null)
        {
            adapter=new CitiesListAdapter(getActivity(), android.R.id.list);
            adapter.add(new City(702550, "Lviv", "UA"));//hardcode
            setListAdapter(adapter);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        try{
            ((IActionUI) getActivity()).onCitySelected( adapter.getItem(position) );
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement IActionUI");
        }
    }

    public void addCity(City city){
        if(city != null){
            adapter.add(city);
            adapter.notifyDataSetChanged();
        }
    }

    public void delCity(City city){
        if(city != null) {
            adapter.remove(city);
            adapter.notifyDataSetChanged();
        }
    }

}
