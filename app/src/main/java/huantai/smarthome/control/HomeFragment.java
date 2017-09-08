package huantai.smarthome.control;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;

import org.json.JSONException;

import java.util.concurrent.ConcurrentHashMap;

import huantai.smarthome.adapter.AddRemoveNumberedAdapter;
import huantai.smarthome.adapter.MyFragmentPagerAdapter;
import huantai.smarthome.bean.ControlDataible;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.ConvertUtil;
import huantai.smarthome.utils.MarginDecoration;

/**
     * description:home界面
     * auther：xuewenliao
     * time：2017/9/8 9:06
     */
public class HomeFragment extends Fragment implements ControlDataible{

    private View view;
    /** The GizWifiDevice device */
    private GizWifiDevice device;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_recycler_view,container, false);


        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(new AddRemoveNumberedAdapter(4));

        initDevice();
        initStatusListener();
        return view;
    }


    @Override
    public void initDevice() {
        Intent intent = getActivity().getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        Log.i("Apptest", device.getDid());
    }

    @Override
    public void initStatusListener() {
        device.setListener(mListener);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initBroadreceive() {

    }

    @Override
    public void sendJson(String key, Object value) throws JSONException {

    }

    private GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
        @Override
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 已定义的设备数据点，有布尔、数值和枚举型数据
                if (dataMap.get("data") != null) {
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
                    // 获得kuozhan类型数据
//                    String msg = ConvertUtil.byteStringToHexString((byte[]) map.get("kuozhan"));

                    //获取温度
                    Integer Temperature = (Integer) map.get("Temperature");
                    //获取湿度
                    Integer Humidity = (Integer) map.get("Humidity");
                    //获取烟雾
                    String smoke1 = (String) map.get("smoke1");
                    //获取气体
                    String gas1 = (String) map.get("gas1");
                    //获取报警
                    String Alert_1 = (String) map.get("Alert_1");


                    //门
//                    String gate1 = (String) map.get("gate1");
                    //人体感应
//                    String body1 = (String) map.get("body1");
                    //灯
//                    String LED_OnOff = (String) map.get("LED_OnOff");
                    //Send_com
                    Integer Send_com = (Integer) map.get("Send_com");


                }
            }

        }

    };
}
