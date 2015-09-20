package mu.zz.pikaso.weather.pojo;

/**
 * Created by pikaso on 20.09.2015.
 */
import java.util.HashMap;
import java.util.Map;

public class Clouds {

    private int all;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The all
     */
    public int getAll() {
        return all;
    }

    /**
     *
     * @param all
     * The all
     */
    public void setAll(int all) {
        this.all = all;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
