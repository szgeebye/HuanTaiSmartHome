package huantai.smarthome.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import huantai.smarthome.adapter.AddRemoveNumberedAdapter;
import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.bean.ConstantData;
import huantai.smarthome.bean.ControlDataible;
import huantai.smarthome.bean.HomeItem;
import huantai.smarthome.initial.CommonModule.GosConstant;
import huantai.smarthome.initial.R;
import huantai.smarthome.popWindow.ListPopup;
import huantai.smarthome.utils.MarginDecoration;
import huantai.smarthome.utils.SerializableMap;
import huantai.smarthome.utils.ToastUtil;

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
    private RecyclerView recyclerView;
    private TextView tv_delete_finish;
    private ImageView iv_person_image;
    private ImageView bt_pop;
    private ListPopup mListPopup;
    private List<HomeItem> allLists = new ArrayList<HomeItem>();//popList展示所有数据
    private SharedPreferences spf;
    public static final String DEVICENOMAL = "正常";
    public static final String DEVICECHANGE = "状态改变";
    public static final String ALERT = "安防监听已启用";
    public static final String NOALERT = "安防监听未启用";
    private ConcurrentHashMap<String, Object> dataMap;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(addRemoveNumberedAdapter);

        initView();
        initDevice();
        initStatusListener();
        initBroadreceive();
        setEvent();
        return view;
    }

    private void initData() {
        //从数据库读取初始化数据
        HomeItem item = new HomeItem();
        List<HomeItem> initItemLists = SugarRecord.listAll(HomeItem.class);
        if (initItemLists.size() != 0) {
            addRemoveNumberedAdapter = new AddRemoveNumberedAdapter(initItemLists, getContext());
            //更新数据
            addRemoveNumberedAdapter.setData(initItemLists);
            //通知适配器更新视图
            addRemoveNumberedAdapter.notifyDataSetChanged();
        } else {
            addRemoveNumberedAdapter = new AddRemoveNumberedAdapter(homeItemLists, getContext());
        }
    }


    @Override
    public void initDevice() {
        Intent intent = getActivity().getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        Log.i("Apptest", device.getDid());
    }

    @Override
    public void initStatusListener() {
//        device.setListener(mListener);
    }

    @Override
    public void initView() {
        spf = getActivity().getSharedPreferences(GosConstant.SPF_Name, Context.MODE_PRIVATE);
        tv_delete_finish = (TextView) view.findViewById(R.id.tv_delete_finish);
        iv_person_image = (ImageView) view.findViewById(R.id.iv_person_image);

        iv_person_image.setVisibility(View.VISIBLE);
        bt_pop = (ImageView) view.findViewById(R.id.bt_pop);

    }

    @Override
    public void initBroadreceive() {

        MyReceiver receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstAction.deletefinishaction);
        filter.addAction(ConstAction.servicedatanotifyaction);
        getContext().registerReceiver(receiver, filter);
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //实现删除完成广播内容
            if (action.equals(ConstAction.deletefinishaction)) {
                tv_delete_finish.setVisibility(View.VISIBLE);
                tv_delete_finish.setEnabled(true);
                iv_person_image.setVisibility(View.INVISIBLE);
            } else if (action.equals(ConstAction.servicedatanotifyaction)) {
                Bundle bundle = intent.getExtras();
                SerializableMap serializableMap = (SerializableMap) bundle.getSerializable("deviceMap");
                dataMap = serializableMap.getMap();
//                dataMap = (ConcurrentHashMap<String, Object>) intent.getSerializableExtra("deviceMap");
                analyseData();
            }
        }
    }

    private void analyseData() {
        String content;
        if (dataMap != null) {
            //除报警外所以数据
            ConcurrentHashMap<String, Object> map = dataMap;
            //报警数据
            ConcurrentHashMap<String, Object> alertmap = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");

            homeItemLists = SugarRecord.listAll(HomeItem.class);
            if (homeItemLists.isEmpty()) {

                Log.i("dataAll", homeItemLists.toString());
                //Home展示的item集合
                //6、7为“开关”和“空调”没有接收的数据，“扩展”占一个字段（总共9个字段）
                for (int i = 0; i < map.size(); i++) {
                    HomeItem item = new HomeItem();
                    switch (i) {


                        case 6:
                        case 7:
                            continue;
                        case 8:
                            //获取报警数据
                            content = ALERT;//获取温度
//                                    content = String.valueOf(alertmap.get(ConstantData.key[i]));//获取温度
                            //添加名称
                            item.setName(ConstantData.name[i]);
                            //添加数据
                            item.setContent(content);
                            //添加图片
                            item.setPicture(i);
                            SugarRecord.save(item);

                            break;
                        case 2:
                        case 3:
                        case 4:
                        case 5:
//                                    if ("true".equals((String) map.get(ConstantData.key[i]))) {
//                                        //如果报警
////                                        ConstantData.name[i];
//                                    }
                        case 0:
                        case 1:
                            //获取温度
                            content = String.valueOf(map.get(ConstantData.key[i]));
                            //添加名称
                            item.setName(ConstantData.name[i]);
                            //添加数据
                            item.setContent(content);
                            //添加图片
                            item.setPicture(i);
                            SugarRecord.save(item);
                            break;


                    }
                }

            } else {

                Log.i("dataAll", homeItemLists.toString());
                //Home展示的item集合
                //6、7为“开关”和“空调”没有接收的数据，“扩展”占一个字段（总共9个字段）
                for (int i = 0; i < map.size(); i++) {
                    //LED和空调字段暂时不接数据
                    if (i == 6 || i == 7) {
                        continue;
                    }


                    for (HomeItem homeItem : homeItemLists) {
                        if (homeItem.getName().equals(ConstantData.name[i])) {
                            if (i == 8) {
                                if (spf.getBoolean("issafe", true)) {//如果开启报警记录监听

                                    content = ALERT;//可查看
                                } else {
                                    content = NOALERT;
                                }
//                                        content = String.valueOf(alertmap.get(ConstantData.key[i]));//获取警报
//                                        continue;
                            } else {

                                if ("true".equals(String.valueOf(map.get(ConstantData.key[i])))) {
//                                            content = "状态改变";
                                    content = DEVICECHANGE;


                                } else if ("false".equals(String.valueOf(map.get(ConstantData.key[i])))) {
                                    content = DEVICENOMAL;
//                                            content = "正常";
                                } else {
                                    content = String.valueOf(map.get(ConstantData.key[i]));//获取除报警外数据
                                }

                            }
                            homeItem.setContent(content);
                            SugarRecord.save(homeItem);
                            Log.i("update", homeItem.toString());

                        }
                    }
                }
            }

            homeItemLists = SugarRecord.listAll(HomeItem.class);
            homeItemLists = Select.from(HomeItem.class)
                    .where(Condition.prop("isdelete").eq(0))
                    .list();
            Log.i("dataAll", homeItemLists.toString());

            //更新数据
            addRemoveNumberedAdapter.setData(homeItemLists);
            //通知适配器更新视图
            addRemoveNumberedAdapter.notifyDataSetChanged();


        }
    }

    /**
     * description:发送AddRemoveAdapter界面更新广播
     * auther：xuewenliao
     * time：2017/9/10 16:57
     */
    private void setEvent() {
        tv_delete_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送AddRemoveAdapter界面更新广播
                Intent intent = new Intent(ConstAction.notifyfinishaction);
                getContext().sendBroadcast(intent);

                tv_delete_finish.setVisibility(View.INVISIBLE);
                tv_delete_finish.setEnabled(false);
                iv_person_image.setVisibility(View.VISIBLE);
            }
        });

        bt_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allLists = SugarRecord.listAll(HomeItem.class);
                Log.i("dataPoplist", allLists.toString());
                bindPopWindowEvent();
                mListPopup.showPopupWindow();
            }
        });

    }


    @Override
    public void sendJson(String key, Object value) throws JSONException {

    }


    public static final int TAG_CREATE = 0x01;
    int i;

    private void bindPopWindowEvent() {
        ListPopup.Builder builder = new ListPopup.Builder(getActivity());
        for (i = 0; i < allLists.size(); i++) {
            builder.addItem(TAG_CREATE, allLists.get(i).getName());
        }
        mListPopup = builder.build();

        mListPopup.setOnListPopupItemClickListener(new ListPopup.OnListPopupItemClickListener() {
            @Override
            public void onItemClick(int what, int position) {
                HomeItem homeItem = allLists.get(position);
                if (homeItem.isdelete() == false) {
                    ToastUtil.ToastShow(getActivity(), "设备已存在，无法添加到主界面");
                } else if (homeItem.isdelete() == true) {
                    homeItem.setDelete(false);
                    SugarRecord.save(homeItem);

                    List<HomeItem> addItemLists;
                    addItemLists = Select.from(HomeItem.class).where(Condition.prop("isdelete").eq(0)).list();
                    //更新数据
                    addRemoveNumberedAdapter.setData(addItemLists);
                    //通知适配器更新视图
                    addRemoveNumberedAdapter.notifyDataSetChanged();
                    ToastUtil.ToastShow(getActivity(), "添加成功");
                    mListPopup.dismiss();
                }


            }
        });
    }

}
