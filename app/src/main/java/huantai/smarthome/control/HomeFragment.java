package huantai.smarthome.control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
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
import huantai.smarthome.initial.R;
import huantai.smarthome.popWindow.ListPopup;
import huantai.smarthome.utils.MarginDecoration;
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
    private Button bt_pop;
    private ListPopup mListPopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_recycler_view, container, false);

        initData();
//        addRemoveNumberedAdapter = new AddRemoveNumberedAdapter(homeItemLists, getContext());
//        addRemoveNumberedAdapter = new AddRemoveNumberedAdapter(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(addRemoveNumberedAdapter);
        addRemoveNumberedAdapter.setOnItemClickListener(new AddRemoveNumberedAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                ToastUtil.ToastShow(getActivity(),""+position);
            }
        });
        initView();
        initDevice();
        initStatusListener();
        initBroadreceive();
        bindPopWindowEvent();
        setEvent();
        return view;
    }

    private void initData() {
        // FIXME: 2017/9/8 修改成从数据库读取
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
            //添加名称
//            item.setName("请添加");
//            //添加数据
//            item.setContent("服务器未启用");
//            //添加图片
//            item.setPicture(1);
////        homeItemLists.clear();
//            homeItemLists.add(item);
        }

//        //添加名称
//        item.setName("hehe");
//        //添加数据
//        item.setContent("123");
//        //添加图片
//        item.setPicture(1);
////        homeItemLists.clear();
//        homeItemLists.add(item);
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
        tv_delete_finish = (TextView) view.findViewById(R.id.tv_delete_finish);
        iv_person_image = (ImageView) view.findViewById(R.id.iv_person_image);

        iv_person_image.setVisibility(View.VISIBLE);
        bt_pop = (Button) view.findViewById(R.id.bt_pop);

    }

    @Override
    public void initBroadreceive() {
        IntentFilter filter = new IntentFilter(ConstAction.deletefinishaction);
        getContext().registerReceiver(deletefinishbroadcast, filter);
    }

    /**
     * description:实现删除完成广播内容
     * auther：xuewenliao
     * time：2017/9/10 16:57
     */
    private BroadcastReceiver deletefinishbroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tv_delete_finish.setVisibility(View.VISIBLE);
            tv_delete_finish.setEnabled(true);
            iv_person_image.setVisibility(View.INVISIBLE);
        }
    };

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
                mListPopup.showPopupWindow();
            }
        });

    }


    @Override
    public void sendJson(String key, Object value) throws JSONException {

    }

    private GizWifiDeviceListener mListener = new GizWifiDeviceListener() {

        private String content;

        @Override
        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                // 已定义的设备数据点，有布尔、数值和枚举型数据
                if (dataMap.get("data") != null) {
                    //除报警外所以数据
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
                    //报警数据
                    ConcurrentHashMap<String, Object> alertmap = (ConcurrentHashMap<String, Object>) dataMap.get("alerts");

                    homeItemLists = SugarRecord.listAll(HomeItem.class);
                    if (homeItemLists.isEmpty()) {

                        Log.i("dataAll", homeItemLists.toString());
                        //Home展示的item集合
                        //"size()-3"表示除去扩展和3个未实现的功能（总共9个字段）
                        for (int i = 0; i < map.size(); i++) {
                            HomeItem item = new HomeItem();
                            //LED和空调字段暂时不接数据
                            if (i == 6 || i == 7) {
                                continue;
                            }
                            if (i == 8) {
                                //获取报警数据
                                content = String.valueOf(alertmap.get(ConstantData.key[i]));//获取温度
                                //添加名称
                                item.setName(ConstantData.name[i]);
                                //添加数据
                                item.setContent(content);
                                //添加图片
                                item.setPicture(i);
                                SugarRecord.save(item);
                            } else {
                                //获取温度
                                content = String.valueOf(map.get(ConstantData.key[i]));
                                //添加名称
                                item.setName(ConstantData.name[i]);
                                //添加数据
                                item.setContent(content);
                                //添加图片
                                item.setPicture(i);
//                            homeItemLists.add(item);
                                SugarRecord.save(item);
                            }
                        }

                    } else {

                        Log.i("dataAll", homeItemLists.toString());
                        //Home展示的item集合
                        //"size()-3"表示除去扩展和3个未实现的功能（总共9个字段）
                        for (int i = 0; i < map.size(); i++) {
                            //LED和空调字段暂时不接数据
                            if (i == 6 || i == 7) {
                                continue;
                            }
                            for (HomeItem homeItem : homeItemLists) {
                                if (homeItem.getName().equals(ConstantData.name[i])) {
                                    if (i == 8) {
                                        content = String.valueOf(alertmap.get(ConstantData.key[i]));//获取温度
                                    } else {
                                        content = String.valueOf(map.get(ConstantData.key[i]));//获取温度

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
                            .where(Condition.prop("name").eq("温度"))
                            .list();


//                    homeItemLists= Select.from(HomeItem.class)
//                         .where(Condition.prop("is_delete").eq(0))
//                         .list();

                    homeItemLists = Select.from(HomeItem.class)
                            .where(Condition.prop("isdelete").eq(0))
                            .list();
//                    homeItemLists = Select.from(HomeItem.class).where(Condition.prop("isDelete").eq(0)).list();
//                    SugarRecord.findWithQuery()
                    Log.i("dataAll", homeItemLists.toString());

                    //更新数据
                    addRemoveNumberedAdapter.setData(homeItemLists);
                    //通知适配器更新视图
                    addRemoveNumberedAdapter.notifyDataSetChanged();


                }
            }

        }

    };


//    public static final int TAG_CREATE = 0x01;
//    public static final int TAG_DELETE = 0x02;
//    public static final int TAG_MODIFY = 0x03;

    private void bindPopWindowEvent() {
        ListPopup.Builder builder = new ListPopup.Builder(getActivity());



//        builder.addItem(TAG_CREATE, "Create-01");
//        builder.addItem(TAG_MODIFY, "Modify-01");
//        builder.addItem(TAG_CREATE, "Create-02");
//        builder.addItem(TAG_DELETE, "Delete-01");
//        builder.addItem(TAG_MODIFY, "Modify-02");
//        builder.addItem(TAG_CREATE, "Create-03");
//        builder.addItem(TAG_DELETE, "Delete-02");
//        builder.addItem(TAG_MODIFY, "Modify-03");
//        builder.addItem(TAG_DELETE, "Delete-03");
//        builder.addItem(TAG_MODIFY, "Modify-04");
//        builder.addItem(TAG_DELETE, "Delete-04");
//        builder.addItem(TAG_CREATE, "Create-04");
        mListPopup = builder.build();

        mListPopup.setOnListPopupItemClickListener(new ListPopup.OnListPopupItemClickListener() {
            @Override
            public void onItemClick(int what) {
//                switch (what) {
//                    case TAG_CREATE:
//                        Toast.makeText(getContext(), "click create", Toast.LENGTH_LONG).show();
//                        break;
//                    case TAG_DELETE:
//                        Toast.makeText(getContext(), "click delete", Toast.LENGTH_LONG).show();
//                        break;
//                    case TAG_MODIFY:
//                        Toast.makeText(getContext(), "click modify", Toast.LENGTH_LONG).show();
//                        break;
//                    default:
//                        break;
//                }
            }
        });
    }

}
