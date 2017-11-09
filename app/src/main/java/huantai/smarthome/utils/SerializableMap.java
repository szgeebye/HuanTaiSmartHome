package huantai.smarthome.utils;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lj_xwl on 2017/11/9.
 */

public class SerializableMap implements Serializable {
    private ConcurrentHashMap map;
    public ConcurrentHashMap getMap() {
        return map;
    }
    public void setMap(ConcurrentHashMap map) {
        this.map= map;
    }
}
