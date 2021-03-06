package mu.zz.pikaso.weather;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mu.zz.pikaso.weather.tools.Conditions;
import mu.zz.pikaso.weather.representations.City;
import mu.zz.pikaso.weather.tasks.SearchCityTask;
import mu.zz.pikaso.weather.ui.IActionUI;

/**
 * Created by pikaso on 06.10.2015.
 */
public class AddCityFragmentDialog extends DialogFragment {
    private City selectedCity;
    private ListView listCities;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        selectedCity = null;
        getDialog().setCanceledOnTouchOutside(true);

        View v = inflater.inflate(R.layout.fragment_addcity, null);

        listCities = (ListView) v.findViewById(R.id.listCities);
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<City>());
        listCities.setAdapter(adapter);

        final EditText edt = (EditText) v.findViewById(R.id.edDSearch);
        final ImageButton search = (ImageButton) v.findViewById(R.id.btnDSearch);;

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Conditions.isInternetAvailable(getContext())) {
                    //before doing something check internet connection
                    if (edt.getText().toString().length() > 2) {
                        ((IActionUI) getActivity()).searchCity(edt.getText().toString());
                    }
                }else{
                    Toast.makeText(getContext(),"NO INTERNET connection available!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        listCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCity = (City) listCities.getItemAtPosition(position);
                listCities.setItemChecked(position, true);
            }
        });

        v.findViewById(R.id.btnDAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((IActionUI)getActivity()).onCityAdd(selectedCity);
            }
        });

        v.findViewById(R.id.btnDBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });


        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    public void fillListCities(List<City> cities){
        ((ArrayAdapter<City>)listCities.getAdapter()).clear();
        if(cities.size()>0){
            for (int i=0;i<cities.size();i++) {
                ((ArrayAdapter<City>)listCities.getAdapter()).insert(cities.get(i), i);
            }
        }else{
            ((ArrayAdapter<String>)listCities.getAdapter()).insert("Nothing Found", 0);
        }
        ((ArrayAdapter<String>)listCities.getAdapter()).notifyDataSetChanged();
    }

}
