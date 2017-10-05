package huantai.smarthome.control;

import android.content.Intent;
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

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import huantai.smarthome.adapter.DeviceShowAdapter;
import huantai.smarthome.bean.ControlDataible;
import huantai.smarthome.bean.SwitchInfo;
import huantai.smarthome.initial.R;
import huantai.smarthome.popWindow.PopupSwitch;
import huantai.smarthome.utils.ToastUtil;
import huantai.smarthome.view.SlideListView;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class DeviceFragment extends Fragment implements ControlDataible {

    private View view;
    private SlideListView lv_device;
    private static List<SwitchInfo> switchInfoList = new ArrayList<SwitchInfo>();
    private DeviceShowAdapter deviceShowAdapter;

    protected static final int DEVICEDELETE = 99;//删除设备
    protected static final int DEVICERENAME = 100;//设备改名
    private TextView tv_rename;
    private ImageView bt_device_add;
//    private ImageView bt_device_refresh;

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

    public static void setSwitchInfoList(List<SwitchInfo> switchInfoList) {
        DeviceFragment.switchInfoList = switchInfoList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device, container, false);

        initView();
        initData();
        initStatusListener();
        return view;
    }

    private void initData() {

//        switchInfoList = InputPopup.getSwitchLists();
        //初始化显示未删除的设备
        List<SwitchInfo> initItemLists = Select.from(SwitchInfo.class)
                .where(Condition.prop("isdelete").eq(0))
                .list();
        if (initItemLists.size() != 0) {
            deviceShowAdapter = new DeviceShowAdapter(initItemLists, getContext());
            //更新数据
            deviceShowAdapter.setData(initItemLists);
            //通知适配器更新视图
            deviceShowAdapter.notifyDataSetChanged();
        } else {
            deviceShowAdapter = new DeviceShowAdapter(switchInfoList, getContext());
            deviceShowAdapter.setData(switchInfoList);
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


        lv_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int type = deviceShowAdapter.getSwitchInfoLists().get(position).getType();
                PopupSwitch popupSwitch = new PopupSwitch(getActivity(), deviceShowAdapter.getSwitchInfoLists().get(position).getType(), deviceShowAdapter.getSwitchInfoLists().get(position).getAddress());
                //popup初始化事件
                popupSwitch.init();
                popupSwitch.showPopupWindow();

            }
        });

//        bt_device_refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                deviceShowAdapter.notifyDataSetChanged();
//                //发送DeviceShowAdapter界面更新广播
//                Intent intent = new Intent(ConstAction.devicenotifyfinishaction);
//                getContext().sendBroadcast(intent);
//            }
//        });

    }

    @Override
    public void initView() {
//        bt_device_refresh = (ImageView) view.findViewById(R.id.bt_device_refresh);
        bt_device_add = (ImageView) view.findViewById(R.id.bt_device_add);
        lv_device = (SlideListView) view.findViewById(R.id.lv_device);
        //设定策划模式
        lv_device.initSlideMode(SlideListView.MOD_RIGHT);

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
                List<SwitchInfo> findSwitchList =  Select.from(SwitchInfo.class).where(Condition.prop("name").eq(re.getText())).list();;
                SwitchInfo switchInfo = findSwitchList.get(0);
                switchInfo.setName(String.valueOf(et_rename.getText()));
                Log.i("rename",switchInfo.toString());
                SugarRecord.save(switchInfo);
                re.setText(et_rename.getText());
                ToastUtil.ToastShow(getActivity(),"改名成功");
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
