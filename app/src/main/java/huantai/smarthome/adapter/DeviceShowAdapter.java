package huantai.smarthome.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import huantai.smarthome.bean.SwitchInfo;
import huantai.smarthome.initial.R;
import huantai.smarthome.view.DeviceHolder;

/**
 * Created by lj_xwl on 2017/9/19.
 */

public class DeviceShowAdapter extends BaseAdapter{
    private List<SwitchInfo> switchInfoList;
    private Context context;

    Handler handler = new Handler();
    protected static final int DEVICEDELETE = 99;//删除设备
    protected static final int DEVICERENAME = 100;//设备改名

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public DeviceShowAdapter(List<SwitchInfo> switchInfoList, Context context) {
        this.switchInfoList = switchInfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
//        return switchInfoList.size();
        return 5;
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
        final DeviceHolder holder;
//        final  SwitchInfo switchInfo = switchInfoList.get(position);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.device_item_list, null);
            holder = new DeviceHolder(view);
            view.setTag(holder);

//            holder.getTv_deviceName().setText(switchInfoList.get(position).getName());
        } else {
            holder = (DeviceHolder) view.getTag();
        }

//        view.findViewById(R.id.tv_deviceName);


        holder.getTv_rename().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = DEVICERENAME;
//                msg.obj = switchInfo;
                msg.obj = holder.getTv_deviceName();
                handler.sendMessage(msg);
            }
        });
        holder.getTv_delete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
