package huantai.smarthome.control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;

import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.initial.CommonModule.GosConstant;
import huantai.smarthome.initial.R;
import huantai.smarthome.initial.SettingsModule.GosAboutActivity;
import huantai.smarthome.popWindow.ExitPopup;
import huantai.smarthome.utils.ShareDeviceUtils;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MineFragment extends Fragment {

    private LinearLayout ll_share,llAbout,llexit;
    private View view;
    private Switch sw_sf;
    private GizWifiDevice device;
    private SharedPreferences spf;
    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine,container,false);
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
        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),SharedDeviceListAcitivity.class);
//                startActivity(intent);
                String address = device.getMacAddress();
                ShareDeviceUtils.makeQRCode(address,getContext());
            }
        });

        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        GosAboutActivity.class);
                intent.putExtra("title", "关于");
                startActivity(intent);
            }
        });

        llexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitPopup exitPopup = new ExitPopup(getActivity());
                exitPopup.showPopupWindow();
//                Intent eintent = new Intent();
//                eintent.setAction(ConstAction.exitappnotifyaction);
//                getActivity().sendBroadcast(eintent);
            }
        });
        sw_sf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sw_sf.isChecked()){
                    spf.edit().putBoolean("issafe", true).commit();

                    Toast.makeText(getActivity(), "监听状态，可接收警报信息",Toast.LENGTH_SHORT ).show();
                }else{
                    spf.edit().putBoolean("issafe", false).commit();
                    Toast.makeText(getActivity(), "撤防状态，不再接收警报信息",Toast.LENGTH_SHORT ).show();
                }
                Intent intent = new Intent(ConstAction.notifyfinishaction);
                getContext().sendBroadcast(intent);
            }
        });
    }

    private void initView() {
        spf = getActivity().getSharedPreferences(GosConstant.SPF_Name, Context.MODE_PRIVATE);
        ll_share = (LinearLayout) view.findViewById(R.id.ll_share);
        llAbout = (LinearLayout) view.findViewById(R.id.llAbout);
        llexit = (LinearLayout) view.findViewById(R.id.llexit);
        sw_sf=(Switch) view.findViewById(R.id.sw_sf);
        sw_sf.setChecked(spf.getBoolean("issafe", true));
    }


}
