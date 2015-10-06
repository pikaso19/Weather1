package mu.zz.pikaso.weather;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import mu.zz.pikaso.weather.adapters.WeatherAdapter;
import mu.zz.pikaso.weather.representations.Weather;
import mu.zz.pikaso.weather.ui.IActionUI;


public class WeatherFragment extends Fragment {

    private static final String ARG_PARAM1 = "city";
    private String mParam1;

    ImageButton menu;
    ImageButton refresh;
    TextView title;
    RecyclerView recyclerView;

    public static WeatherFragment newInstance(String cityName) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, cityName);
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
        title = (TextView) getView().findViewById(R.id.cityname);
        menu = (ImageButton) getView().findViewById(R.id.btnMenu);
        refresh = (ImageButton) getView().findViewById(R.id.btnResfresh);
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        WeatherAdapter weatherAdapter = new WeatherAdapter();
        ArrayList<Weather> weathers = new ArrayList<Weather>(1);

        Weather weatherToday = new Weather();
        weatherToday.setCurrentTemperature(+17.2);
        weatherToday.setDate(new java.sql.Date(2015,03,12));
        weatherToday.setDescription("some clouds");
        weatherToday.setFlag(R.drawable.umbrella);
        weatherToday.setId(0);
        weatherToday.setImage(R.drawable.umbrella);
        weatherToday.setMaxTemperature(23);
        weatherToday.setMinTemperature(15);
        weatherToday.setName("Lviv");

        weathers.add(weatherToday);

        weatherAdapter.setWeatherDataset(weathers);
        recyclerView.setAdapter(weatherAdapter);

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
                    ((IActionUI) getActivity()).onClickRefresh();
                } catch (ClassCastException e) {
                    throw new ClassCastException(getActivity().toString()
                            + " must implement IActionUI");
                }
            }
        });
    }


}
