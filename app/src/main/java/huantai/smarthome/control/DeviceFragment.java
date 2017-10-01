package huantai.smarthome.control;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    protected static final int DEVICEDELETE = 99;//删除设备
    protected static final int DEVICERENAME = 100;//设备改名
    private TextView tv_rename;
    private ImageView bt_device_add;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DEVICERENAME:
                    tv_rename = (TextView) msg.obj;
                    setDeviceInfo();
                    break;
            }
        }
    };


    public DeviceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device, container, false);
        initView();
        initStatusListener();
        return view;
    }

    @Override
    public void initStatusListener() {

        bt_device_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DeviceAddActivity.class);
                startActivity(intent);
            }
        });

        lv_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    public void initView() {
        bt_device_add = (ImageView) view.findViewById(R.id.bt_device_add);

        lv_device = (SlideListView) view.findViewById(R.id.lv_device);
        //设定策划模式
        lv_device.initSlideMode(SlideListView.MOD_RIGHT);
        deviceShowAdapter = new DeviceShowAdapter(switchInfoList, getActivity());
        deviceShowAdapter.setHandler(handler);
        lv_device.setAdapter(deviceShowAdapter);

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

    private void setDeviceInfo() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(new EditText(getActivity())).create();
        final TextView re = tv_rename;
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.alert_devicefragment_set_device_info);

        final EditText et_rename = (EditText) window.findViewById(R.id.et_rename);
        LinearLayout ll_device_no = (LinearLayout) window.findViewById(R.id.ll_device_no);
        LinearLayout ll_device_sure = (LinearLayout) window.findViewById(R.id.ll_device_sure);

        ll_device_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re.setText(et_rename.getText());
                dialog.cancel();
            }
        });

        ll_device_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

}
