package mu.zz.pikaso.weather;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;


import mu.zz.pikaso.weather.adapters.CitiesListAdapter;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.ui.IActionUI;


public class MenuFragment extends ListFragment {
    private CitiesListAdapter adapter;
    private Button btnRefreshAll;
    private Button btnAddCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu,
                container, false);

        btnRefreshAll = (Button) rootView.findViewById(R.id.btnRefreshAll);
        btnAddCity = (Button) rootView.findViewById(R.id.btnAddCity);
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
                ((IActionUI) getActivity()).onCityDelete(adapter.getItem(position));
                return true;
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((IActionUI) getActivity()).onCitySelected(adapter.getItem(position));
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

    public void changeOrientationToLandscape(){
        btnRefreshAll.setText("");
        btnAddCity.setText("Add");
        LinearLayout titleLayout = (LinearLayout) getActivity().findViewById(R.id.TitleMenu);
        LinearLayout mainLayout = (LinearLayout) getActivity().findViewById(R.id.MainMenu);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mainLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shadow) );
        } else {
            mainLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shadow));
        }

        //hide title
        for(int i=0;i<titleLayout.getChildCount();i++){
            titleLayout.getChildAt(i).setVisibility(View.GONE);
        }

        mainLayout.removeView(btnRefreshAll);
        mainLayout.removeView(btnAddCity);

        titleLayout.addView(btnRefreshAll);
        titleLayout.addView(btnAddCity);

        btnRefreshAll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        btnAddCity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
    }

    public void changeOrientationToPortrait(){
        btnRefreshAll.setText("Refresh all");
        btnAddCity.setText("Add city");

        LinearLayout titleLayout = (LinearLayout) getActivity().findViewById(R.id.TitleMenu);
        LinearLayout mainLayout = (LinearLayout) getActivity().findViewById(R.id.MainMenu);
        mainLayout.setBackgroundColor(Color.TRANSPARENT);
        //hide title
        titleLayout.removeView(btnRefreshAll);
        titleLayout.removeView(btnAddCity);
        for(int i=0;i<titleLayout.getChildCount();i++){
            titleLayout.getChildAt(i).setVisibility(View.VISIBLE);
        }

        final ListView lv = getListView();
        mainLayout.removeView(lv);
        mainLayout.addView(btnRefreshAll);
        mainLayout.addView(lv);
        mainLayout.addView(btnAddCity);

        btnRefreshAll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f));
        btnAddCity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.0f));
    }





}
