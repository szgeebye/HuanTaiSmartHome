package huantai.smarthome.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import huantai.smarthome.adapter.DeviceShowAdapter;
import huantai.smarthome.bean.ControlDataible;
import huantai.smarthome.bean.SwitchInfo;
import huantai.smarthome.initial.R;
import huantai.smarthome.view.SlideListView;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class DeviceFragment extends Fragment implements ControlDataible {

    private View view;
    private SlideListView lv_device;
    private List<SwitchInfo> switchInfoList = new ArrayList<SwitchInfo>();
    private DeviceShowAdapter deviceShowAdapter;

    public DeviceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device, container, false);
        initView();
        return view;
    }

    @Override
    public void initStatusListener() {

    }

    @Override
    public void initView() {
        lv_device = (SlideListView) view.findViewById(R.id.lv_device);
        //设定策划模式
        lv_device.initSlideMode(SlideListView.MOD_RIGHT);
        deviceShowAdapter = new DeviceShowAdapter(switchInfoList,getActivity());
        lv_device.setAdapter(deviceShowAdapter);
        lv_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void initBroadreceive() {

    }

    @Override
    public void initDevice() {

    }

    @Override
    public void sendJson(String key, Object value) throws JSONException {

    }
}
