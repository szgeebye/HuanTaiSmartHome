package huantai.smarthome.control;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import huantai.smarthome.adapter.AddRemoveNumberedAdapter;
import huantai.smarthome.adapter.MyFragmentPagerAdapter;
import huantai.smarthome.bean.ConstantData;
import huantai.smarthome.bean.ControlDataible;
import huantai.smarthome.bean.HomeItem;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.ConvertUtil;
import huantai.smarthome.utils.MarginDecoration;

/**
 * description:home界面
 * auther：xuewenliao
 * time：2017/9/8 9:06
 */
public class HomeFragment extends Fragment implements ControlDataible {

    private View view;
    /**
     * The GizWifiDevice device
     */
    private GizWifiDevice device;
    private List<HomeItem> homeItemLists = new ArrayList<HomeItem>();
    private AddRemoveNumberedAdapter addRemoveNumberedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_recycler_view, container, false);

        initData();
        addRemoveNumberedAdapter = new AddRemoveNumberedAdapter(homeItemLists);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(addRemoveNumberedAdapter);
//        recyclerView.setAdapter(new AddRemoveNumberedAdapter(4,homeItemLists));

        initDevice();
        initStatusListener();
        return view;
    }

    private void initData() {
        // FIXME: 2017/9/8 修改成从数据库读取
        HomeItem item = new HomeItem();
        //添加名称
        item.setName("hehe");
        //添加数据
        item.setContent("123");
        //添加图片
        item.setPicture(1);
//        homeItemLists.clear();
        homeItemLists.add(item);
    }


    @Override
    public void initDevice() {
        Intent intent = getActivity().getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        Log.i("Apptest", device.getDid());
    }

    @Override
    public void initStatusListener() {
        device.setListener(mListener);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initBroadreceive() {

    }

    @Override
    public void sendJson(String key, Object value) throws JSONException {

    }

    private GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
        @Override
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 已定义的设备数据点，有布尔、数值和枚举型数据
                if (dataMap.get("data") != null) {
                    //除报警外所以数据
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
                    //报警数据
                    ConcurrentHashMap<String, Object> alertmap = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");

                    /**
                         * description:获取报警外所以数据
                         * auther：xuewenliao
                         * time：2017/9/8 17:31
                         */
                    //清空数据
                    homeItemLists.clear();
                    //Home展示的item集合
                    //"size()-3"表示除去扩展和3个未实现的功能（总共9个字段）
                    for (int i = 0; i < map.size() - 3; i++) {
                        HomeItem item = new HomeItem();
                        //获取数据
                        String content = String.valueOf(map.get(ConstantData.key[i]));//获取温度
//                        String content =  map.get(ConstantData.key[i]);//获取温度
                        //添加名称
                        item.setName(ConstantData.name[i]);
                        //添加数据
                        item.setContent(content);
                        //添加图片
                        item.setPicture(i);
                        homeItemLists.add(item);

                    }

                    /**
                         * description:获取警报数据
                         * auther：xuewenliao
                         * time：2017/9/8 17:26
                         */
                    HomeItem item = new HomeItem();
                    //获取数据
                    String content = String.valueOf(alertmap.get(ConstantData.key[8]));//获取温度
//                        String content =  map.get(ConstantData.key[i]);//获取温度
                    //添加名称
                    item.setName(ConstantData.name[8]);
                    //添加数据
                    item.setContent(content);
                    //添加图片
                    item.setPicture(6);
                    homeItemLists.add(item);

                    //更新数据
                    addRemoveNumberedAdapter.setData(homeItemLists);
                    //通知适配器更新视图
                    addRemoveNumberedAdapter.notifyDataSetChanged();

//                    String Humidity = String.valueOf(map.get("Humidity"));//获取湿度
//                    String smoke1 = (String) map.get("smoke1");//获取烟雾
//                    String gas1 = (String) map.get("gas1");//获取气体
//                    String Alert_1 = (String) map.get("Alert_1"); //获取报警
//                    String gate1 = (String) map.get("gate1"); //门
//                    String body1 = (String) map.get("body1"); //人体感应
//                    String LED_OnOff = (String) map.get("LED_OnOff"); //灯
//                    Integer Send_com = (Integer) map.get("Send_com");//Send_com


                }
            }

        }

    };
}
