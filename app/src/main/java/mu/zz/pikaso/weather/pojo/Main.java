package mu.zz.pikaso.weather.pojo;

/**
 * Created by pikaso on 20.09.2015.
 */
import java.util.HashMap;
import java.util.Map;

public class Main {

    private double temp;
    private int pressure;
    private int humidity;
    private double temp_min;
    private double temp_max;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The temp
     */
    public double getTemp() {
        return temp;
    }

    /**
     *
     * @param temp
     * The temp
     */
    public void setTemp(double temp) {
        this.temp = temp;
    }

    /**
     *
     * @return
     * The pressure
     */
    public int getPressure() {
        return pressure;
    }

    /**
     *
     * @param pressure
     * The pressure
     */
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    /**
     *
     * @return
     * The humidity
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     *
     * @param humidity
     * The humidity
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    /**
     *
     * @return
     * The temp_min
     */
    public double getTemp_Min() {
        return temp_min;
    }

    /**
     *
     * @param temp_min
     * The temp_min
     */
    public void setTemp_Min(double temp_min) {
        this.temp_min = temp_min;
    }

    /**
     *
     * @return
     * The temp_max
     */
    public double getTemp_Max() {
        return temp_max;
    }

    /**
     *
     * @param temp_max
     * The temp_max
     */
    public void setTemp_Max(double temp_max) {
        this.temp_max = temp_max;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
