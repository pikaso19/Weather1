package mu.zz.pikaso.weather.representations;

/**
 * Created by pikaso on 03.10.2015.
 */
public class City {
    private int id;
    private String name;
    private Integer flag;

    public City(int id, String name, Integer flag){
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

}
