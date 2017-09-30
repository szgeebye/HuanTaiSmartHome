package huantai.smarthome.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import huantai.smarthome.initial.R;

/**
 * Created by lj_xwl on 2017/9/19.
 */

public class DeviceHolder {
    //    Context context;
    View view;

    public DeviceHolder(View view) {
//        this.context = context;
        this.view = view;
    }

    private TextView tv_delete;//删除按钮
    private TextView tv_rename;//改名按钮
    private TextView tv_add;//添加按钮
    private TextView tv_deviceName;//设备名称
    private ImageView iv_icon;//设置图片

    public TextView getTv_delete() {
        if (null == tv_delete) {
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        }
        return tv_delete;
    }

    public TextView getTv_rename() {
        if (null == tv_rename) {
            tv_rename = (TextView) view.findViewById(R.id.tv_rename);
        }
        return tv_rename;
    }

    public TextView getTv_deviceName() {
        if (null == tv_deviceName) {
            tv_deviceName = (TextView) view.findViewById(R.id.tv_deviceName);
        }
        return tv_deviceName;
    }

    public TextView getTv_add() {
        if (null == tv_add) {
            tv_add = (TextView) view.findViewById(R.id.tv_add);
        }
        return tv_add;
    }

    public ImageView getTv_icon() {
        if (null == iv_icon) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        }
        return iv_icon;
    }


}
