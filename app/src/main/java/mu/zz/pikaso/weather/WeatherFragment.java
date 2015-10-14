package mu.zz.pikaso.weather;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.adapters.WeatherAdapter;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;


public class WeatherFragment extends Fragment {
    private static final String ARG_PARAM1 = "city";
    private static final String ARG_PARAM2 = "id";
    private String mParam1;                              //city name
    private long mParam2;                                //city id

    TextView title;
    ImageButton menu;
    ImageButton refresh;
    RecyclerView recyclerView;

    public static WeatherFragment newInstance(String cityName, long id) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, cityName);
        args.putLong(ARG_PARAM2, id);
        fragment.setArguments(args);
        return fragment;
    }

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getLong(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        try {
            title = (TextView) getView().findViewById(R.id.cityname);
            menu = (ImageButton) getView().findViewById(R.id.btnMenu);
            refresh = (ImageButton) getView().findViewById(R.id.btnResfresh);
            recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
            recyclerView.setLayoutManager(layoutManager);

            WeatherAdapter weatherAdapter = new WeatherAdapter();
            weatherAdapter.setWeatherDataset(new ArrayList<Weather>());
            weatherAdapter.setActivity(getActivity());
            recyclerView.setAdapter(weatherAdapter);
            //when adapter is set signal MainActivity
            if(mParam2>0){
                try {
                    ((IActionUI) getActivity()).loadWeather(mParam2);
                    refresh.setEnabled(false);
                } catch (ClassCastException e) {
                    throw new ClassCastException(getActivity().toString()
                            + " must implement IActionUI");
                }
            }

            if(mParam1 != null)
                title.setText(mParam1);

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });

            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ((IActionUI) getActivity()).onClickRefresh(recyclerView, mParam2);
                        refresh.setEnabled(false);
                    } catch (ClassCastException e) {
                        throw new ClassCastException(getActivity().toString()
                                + " must implement IActionUI");
                    }
                }
            });
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    void DisplayWeather(List<Weather> forecast){
        refresh.setEnabled(true);
        if(forecast.size()>0){
            ((WeatherAdapter)recyclerView.getAdapter()).setWeatherDataset(forecast);
        }
        ((WeatherAdapter)recyclerView.getAdapter()).notifyDataSetChanged();
    }

}
