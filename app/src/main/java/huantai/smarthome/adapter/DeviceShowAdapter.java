package huantai.smarthome.adapter;

import android.content.Context;
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

public class DeviceShowAdapter extends BaseAdapter {
    private List<SwitchInfo> switchInfoList;
    private Context context;


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
        DeviceHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.device_item_list, null);
            holder = new DeviceHolder(view);
            view.setTag(holder);
        } else {
            holder = (DeviceHolder) view.getTag();
        }

        return view;
    }
}
