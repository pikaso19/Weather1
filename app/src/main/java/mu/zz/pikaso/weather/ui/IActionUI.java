package mu.zz.pikaso.weather.ui;

import android.support.v7.widget.RecyclerView;

import mu.zz.pikaso.weather.representations.City;

/**
 * Created by pikaso on 30.09.2015.
 */
public interface IActionUI {
    //fragment 1
    void onClickRefreshALL();
    void onClickAddCity();
    void onClickExit();

    void onCitySelected(City city);
    void onCityDelete(City city);

    //fragment 2
    void onClickRefresh(RecyclerView rw, int id);

    //fragment 3
    void onCityAdd(City city);
}
