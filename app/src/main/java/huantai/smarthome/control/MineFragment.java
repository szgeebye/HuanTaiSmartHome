package huantai.smarthome.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gizwits.gizwifisdk.api.GizWifiDevice;

import huantai.smarthome.initial.R;
import huantai.smarthome.utils.ShareDeviceUtils;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MineFragment extends Fragment {

    private Button bt_share;
    private View view;
    private GizWifiDevice device;
    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine,container,false);
//        TextView txt_content = (TextView) view.findViewById(R.id.txt_mine);
//        txt_content.setText("MineFragment");
        initView();
        initDevice();
        initEvent();

        return view;
    }

    private void initDevice() {
        Intent intent = getActivity().getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
    }

    private void initEvent() {
        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),SharedDeviceListAcitivity.class);
//                startActivity(intent);
                String address = device.getMacAddress();
                ShareDeviceUtils.makeQRCode(address,getContext());
            }
        });
    }

    private void initView() {
        bt_share = (Button) view.findViewById(R.id.bt_share);
    }


}
