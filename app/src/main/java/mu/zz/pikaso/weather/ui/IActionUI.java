package mu.zz.pikaso.weather.ui;

/**
 * Created by pikaso on 30.09.2015.
 */
public interface IActionUI {
    //fragment 1
    void onClickRefreshALL();
    void onClickAddCity();
    void onClickExit();

    void onCitySelected(String city);
    void onCityDelete(String city);

    //fragment 2
    void onClickMenu();
    void onClickRefresh();

    //fragment 3
    void onCityAdd(String city);
}
