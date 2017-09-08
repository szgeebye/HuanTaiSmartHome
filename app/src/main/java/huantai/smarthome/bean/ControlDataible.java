package huantai.smarthome.bean;

import org.json.JSONException;

/**
     * description:数据控制接口
     * auther：xuewenliao
     * time：2017/9/7 19:33
     */

public interface ControlDataible {
    void initStatusListener();//设置设备状态监听
    void initView();
    void initBroadreceive();//发数据广播
    void initDevice();//初始化设备
    void sendJson(String key, Object value) throws JSONException;//发数据
}
