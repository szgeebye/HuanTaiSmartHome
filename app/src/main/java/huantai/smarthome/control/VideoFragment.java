package huantai.smarthome.control;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmDevice;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmMgrConnectStateChangeListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

import java.util.ArrayList;
import java.util.List;

import huantai.smarthome.initial.R;
import huantai.smarthome.utils.ActivityManager;
import huantai.smarthome.video.AddDeviceUserEnsure;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class VideoFragment extends Fragment {

    private IXmSystem xmSystem;
    private XmAccount account;
    private ListView listView;
    private MyAdapter adapter;
    private List<XmDevice> mlist;
    private View view;
    private ImageView iv_vedio_add;
    private ImageView iv_video_back;
    private ProgressDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getContext(), "监控视频加载成功！", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getContext(), "监控视频加载失败！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    private List<String> nameLists;

    public VideoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, container, false);
//        init();
//        loadVideo();
        init();
        initview();
        initData();
        setEvent();
        return view;
    }

    //创建Fragment时回调，只会调用一次
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        init();
//        loadVideo();
//        init();
//        initData();
        System.out.println(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        xmSystem.xmLogout();
        System.out.println(2);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    ActivityManager manager;

    private void init() {
        xmSystem = XmSystem.getInstance();
        account = MainActivity.account;
        xmSystem.registerOnMgrConnectChangeListener(onXmMgrConnectStateChangeListener);
        loginMgr();
    }

    private void loginMgr() {
        xmSystem.xmMgrSignin(new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                Log.v("loginMgr", info.discribe);
            }

            @Override
            public void onSuc() {
                Log.v("loginMgr", "MgrSignin suc!");
            }
        });
    }

    OnXmMgrConnectStateChangeListener onXmMgrConnectStateChangeListener = new OnXmMgrConnectStateChangeListener() {
        @Override
        public void onChange(boolean connectState) {
            Log.i("ConnectState", "OnXmMgrConnectStateChangeListener onChange is " + connectState);
        }
    };

    private void initview() {
        iv_vedio_add = (ImageView) view.findViewById(R.id.iv_vedio_add);
        iv_video_back = (ImageView) view.findViewById(R.id.iv_video_back);
        listView = (ListView) view.findViewById(R.id.lv_vedio_list);
        mlist = new ArrayList<XmDevice>();
        adapter = new MyAdapter(getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemListener);
    }

    private void initData() {
        getDevices();
    }

    private void getDevices() {
        Log.v("AAAAA", "getDevices");
        xmSystem.xmGetDeviceList(new OnXmListener<List<XmDevice>>() {
            @Override
            public void onErr(XmErrInfo info) {
                Log.v("AAAAA", "get devices fail");
            }

            @Override
            public void onSuc(List<XmDevice> info) {
                Log.v("AAAAA", "getDevices  onSuc");
                mlist = info;
                adapter.notifyDataSetChanged();
            }
        });
        nameLists = new ArrayList<>();
        for (int i = 0;i < mlist.size();i++) {
            String videoName = "摄像头"+i+"号";
            nameLists.add(videoName);
        }
    }

    private void setEvent() {
        iv_vedio_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice();
            }
        });
        iv_video_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    //增加设备
    private void addDevice() {
        Intent in = new Intent(getActivity(), AddDeviceUserEnsure.class);
        in.putExtra("isDemo", account.isDemo());
        if (!account.isDemo())
            in.putExtra("username", account.getmUsername());
        startActivity(in);
    }


    //设备选择事件
    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int cameraId = mlist.get(position).getmCameraId();
            Intent in = new Intent(getActivity(), VideoPlayActivity.class);
            in.putExtra("cameraId",cameraId);
            in.putExtra("device",getActivity().getIntent().getParcelableExtra("GizWifiDevice"));//传网关设备
            startActivity(in);
        }
    };

    class MyAdapter extends BaseAdapter {

        Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoler holer = null;
            if (convertView == null) {
                holer = new ViewHoler();
                convertView = LayoutInflater.from(context).inflate(R.layout.vedio_list_item, null);
                holer.tv = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holer);
            } else {
                holer = (ViewHoler) convertView.getTag();
            }
//            holer.tv.setText(mlist.get(position).getmName());
            holer.tv.setText(nameLists.get(position));
            return convertView;
        }

        class ViewHoler {
            TextView tv;
        }
    }

    //加载视频提示
    public void showLoadingDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("请稍后...");
        dialog.show();
    }

    public void closeLoadingDialog() {
        dialog.dismiss();
    }
}
