package huantai.smarthome.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import huantai.smarthome.adapter.DeviceShowAdapter;
import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.bean.ControlDataible;
import huantai.smarthome.bean.SwitchInfo;
import huantai.smarthome.initial.R;
import huantai.smarthome.popWindow.PopupCurtain;
import huantai.smarthome.popWindow.PopupSwitch;
import huantai.smarthome.utils.ControlUtils;
import huantai.smarthome.utils.ToastUtil;
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
    protected static final int SENDSUCCESS = 101;//数据发送成功
    private TextView tv_rename;
    private ImageView bt_device_add;
    private GizWifiDevice device;//机智云设备
    public static String address;//点击条目的当前地址

    private LinearLayout ll_add_device;
    private List<SwitchInfo> initItemLists;
    private IntentFilter updateFilter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DEVICERENAME:
                    tv_rename = (TextView) msg.obj;
                    setDeviceInfo();
                    break;
                case SENDSUCCESS:
                    ToastUtil.ToastShow(getActivity(), "数据发送成功");
                    break;
            }
        }
    };


    public DeviceFragment() {
    }

    public void setSwitchInfoList(List<SwitchInfo> switchInfoList) {
        this.switchInfoList = switchInfoList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device, container, false);
        return view;
    }

    /*
    * onStart在onCreateView后面执行，保证第一次添加数据能立即显示
    */
    @Override
    public void onStart() {
        super.onStart();
        initView();
        initDevice();
        initData();
        initStatusListener();
        initBroadreceive();

    }

    private void initData() {

        //初始化显示未删除的设备
        initItemLists = Select.from(SwitchInfo.class)
                .where(Condition.prop("isdelete").eq(0),Condition.prop("bindgiz").eq(device.getMacAddress()))
                .list();
        deviceShowAdapter = new DeviceShowAdapter(initItemLists, getContext());
        // FIXME: 2017/11/30 两次设置？？？
        deviceShowAdapter.setData(initItemLists);
        if (initItemLists.size() != 0) {
            //更新数据
            //通知适配器更新视图
            deviceShowAdapter.notifyDataSetChanged();
            ll_add_device.setVisibility(View.GONE);

        } else {
            ll_add_device.setVisibility(View.VISIBLE);
        }
        deviceShowAdapter.setHandler(handler);
        lv_device.setAdapter(deviceShowAdapter);

    }

    @Override
    public void initStatusListener() {

        bt_device_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeviceAddActivity.class);
                startActivity(intent);
            }
        });


        ll_add_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeviceAddActivity.class);
                startActivity(intent);
            }
        });

        lv_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                address = deviceShowAdapter.getSwitchInfoLists().get(position).getAddress(); //获取所点击设备的当前地址

                if (deviceShowAdapter.getSwitchInfoLists().get(position).getType() == 6) {//如果是空调设备

                    Intent intent = new Intent(getActivity(),SmartAirConditionActivity.class);

                    intent.putExtra("name", deviceShowAdapter.getSwitchInfoLists().get(position).getName());
                    intent.putExtra("brand", deviceShowAdapter.getSwitchInfoLists().get(position).getStatus1());
                    intent.putExtra("temperature", deviceShowAdapter.getSwitchInfoLists().get(position)
                            .getStatus2());
                    intent.putExtra("mod", deviceShowAdapter.getSwitchInfoLists().get(position).getStatus3());
                    intent.putExtra("speed", deviceShowAdapter.getSwitchInfoLists().get(position).getStatus4());
                    intent.putExtra("direction", deviceShowAdapter.getSwitchInfoLists().get(position).getStatus5());
                    intent.putExtra("opcl", deviceShowAdapter.getSwitchInfoLists().get(position).getFlag());
                    intent.putExtra("device_id", deviceShowAdapter.getSwitchInfoLists().get(position).getAddress());
                    Log.i("getAddress",deviceShowAdapter.getSwitchInfoLists().get(position).getAddress());
                    startActivity(intent);

                } else if (deviceShowAdapter.getSwitchInfoLists().get(position).getType() == 5) {//如果是窗帘
                    PopupCurtain popupCurtain = new PopupCurtain(getActivity(),address);
                    popupCurtain.showPopupWindow();

                } else {
                    PopupSwitch popupSwitch = new PopupSwitch(getActivity(),deviceShowAdapter.getSwitchInfoLists().get(position));
                    //popup初始化事件
                    popupSwitch.init();
                    popupSwitch.showPopupWindow();
                }


            }
        });

    }

    @Override
    public void initView() {
        ll_add_device = (LinearLayout) view.findViewById(R.id.ll_add_device);
        bt_device_add = (ImageView) view.findViewById(R.id.bt_device_add);
        lv_device = (SlideListView) view.findViewById(R.id.lv_device);
        //设定策划模式
        lv_device.initSlideMode(SlideListView.MOD_RIGHT);

    }

    @Override
    public void initBroadreceive() {
        //注册界面更新广播接收者
        updateFilter = new IntentFilter(ConstAction.devicenotifyfinishaction);
        getContext().registerReceiver(notifyfinishbroadcast, updateFilter);
        Intent sendDataBroadcastIntent;
        sendDataBroadcastIntent = new Intent(ConstAction.senddeviceaction);
        //状态同步广播
        sendDataBroadcastIntent.putExtra("value",ControlUtils.STATUS_UP_DATA);
        getContext().sendBroadcast(sendDataBroadcastIntent);
    }

    private BroadcastReceiver notifyfinishbroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新数据
            deviceShowAdapter.UpdateData();
        }
    };


    @Override
    public void initDevice() {
        Intent intent = getActivity().getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        System.out.print(1);
    }

    @Override
    public void sendJson(String key, Object value) throws JSONException {
//        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
//        hashMap.put(key, value);
//        device.write(hashMap, 0);
//        Log.i("==", hashMap.toString());


    }

    //修改设备信息
    private void setDeviceInfo() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(new EditText(getActivity())).create();
        final TextView re = tv_rename;
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.alert_devicefragment_set_device_info);

        final EditText et_rename = (EditText) window.findViewById(R.id.et_rename);
        final EditText et_readdress = (EditText) window.findViewById(R.id.et_readdress);

        LinearLayout ll_device_no = (LinearLayout) window.findViewById(R.id.ll_device_no);
        LinearLayout ll_device_sure = (LinearLayout) window.findViewById(R.id.ll_device_sure);

        final List<SwitchInfo> findSwitchList = Select.from(SwitchInfo.class).where(Condition.prop("name").eq(re.getText()),Condition.prop("bindgiz").eq(device.getMacAddress())).list();
        final SwitchInfo switchInfo = findSwitchList.get(0);
        et_readdress.setText(switchInfo.getAddress());//显示当前设备Mac地址

        ll_device_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switchInfo.setName(String.valueOf(et_rename.getText()));
                switchInfo.setAddress(String.valueOf(et_readdress.getText()));
                Log.i("rename", switchInfo.toString());
                SugarRecord.save(switchInfo);
                re.setText(et_rename.getText());
                ToastUtil.ToastShow(getActivity(), "修改成功");
                dialog.cancel();

                //通知DeviceShowAdapter更新界面
                //发送DeviceShowAdapter界面更新广播
                Intent intent = new Intent(ConstAction.devicenotifyfinishaction);
                getContext().sendBroadcast(intent);
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
