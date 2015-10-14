package mu.zz.pikaso.weather;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;


import mu.zz.pikaso.weather.adapters.CitiesListAdapter;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.ui.IActionUI;


public class MenuFragment extends ListFragment {
    private CitiesListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu,
                container, false);

        final Button btnRefreshAll = (Button) rootView.findViewById(R.id.btnRefreshAll);
        final Button btnAddCity = (Button) rootView.findViewById(R.id.btnAddCity);
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

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TODO: read favourites cities from sql!
        if(adapter == null)
        {
            adapter=new CitiesListAdapter(getActivity(), android.R.id.list);
            setListAdapter(adapter);
            // LOAD CITIES FROM SQL
            ((IActionUI) getActivity()).loadCities();

        }

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ((IActionUI) getActivity()).onCityDelete( adapter.getItem(position) );
                return true;
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((IActionUI) getActivity()).onCitySelected( adapter.getItem(position) );
            }
        });

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.clear();
        adapter = null;
    }
}
