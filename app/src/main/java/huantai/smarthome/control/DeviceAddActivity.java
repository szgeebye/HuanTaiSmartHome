package huantai.smarthome.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import huantai.smarthome.bean.AddSwitchDevice;
import huantai.smarthome.bean.ConstantData;
import huantai.smarthome.initial.R;
import huantai.smarthome.popWindow.InputPopup;

/**
 * description:设备添加Activity
 * auther：xuewenliao
 * time：2017/10/1 11:10
 */

public class DeviceAddActivity extends Activity {

    private ImageView iv_back;
    private ListView lv_device_add;
    private DeviceAddActivity.myAdapeter myAdapeter;
    private List<AddSwitchDevice> addSwitchDeviceLists;
    private AddSwitchDevice addSwitchDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_set);

        initData();
        initView();
        setEvent();
    }

    private void initData() {
        addSwitchDeviceLists = new ArrayList<AddSwitchDevice>();
        for (int i = 0; i < 4; i++) {

            addSwitchDevice = new AddSwitchDevice();
            addSwitchDevice.setDevicesort(ConstantData.devicename[i]);
            addSwitchDevice.setPicture(i);
            addSwitchDeviceLists.add(addSwitchDevice);

        }
    }

    private void setEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_device_add.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputPopup inputPopup = new InputPopup(DeviceAddActivity.this);
                inputPopup.showPopupWindow();
            }
        });

    }


    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        lv_device_add = (ListView) findViewById(R.id.lv_device_add);

        myAdapeter = new myAdapeter();
        lv_device_add.setAdapter(myAdapeter);
    }

    class myAdapeter extends BaseAdapter {
        @Override
        public int getCount() {
            return addSwitchDeviceLists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            Holder holder;

            if (view == null) {
                view = View.inflate(DeviceAddActivity.this, R.layout.device_add_item_list, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            holder.getDeviceaddName().setText(addSwitchDeviceLists.get(position).getDevicesort());
            holder.getDeviceaddIcon().setImageResource(R.drawable.device_images);//获取映射图片的资源文件
            holder.getDeviceaddIcon().setImageLevel(addSwitchDeviceLists.get(position).getPicture());

            return view;
        }
    }

    public myAdapeter getMyAdapeter(){
      return myAdapeter;
    };
}

class Holder {
    View view;
    private TextView tv_deviceaddName;
    private ImageView iv_deviceaddicon;

    public Holder(View view) {
        this.view = view;
    }

    public TextView getDeviceaddName() {
        tv_deviceaddName = (TextView) view.findViewById(R.id.tv_deviceaddName);
        return tv_deviceaddName;
    }

    public ImageView getDeviceaddIcon() {
        iv_deviceaddicon = (ImageView) view.findViewById(R.id.iv_deviceaddicon);
        return iv_deviceaddicon;
    }

}
