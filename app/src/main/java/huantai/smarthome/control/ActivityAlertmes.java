package huantai.smarthome.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import huantai.smarthome.adapter.AlertmesAdapter;
import huantai.smarthome.bean.Alertinfo;
import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.bean.HomeItem;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.DateUtil;

public class ActivityAlertmes extends Activity {

    private ListView alert_info_listview;
    private TextView tv_nodata;
    private AlertmesAdapter adapter;
    ProgressDialog progressDialog;
    public static final String DEVICECHANGE = "状态改变";
    private List<Alertinfo> alertinfoLists;
    private TextView tv_clear;
    private GizWifiDevice device;
    private ImageView iv_video_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertmes);
        progressDialog = new ProgressDialog(this);
        initview();

    }

    public void setProgressDialog(String text) {
        progressDialog.setMessage(text);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        setProgressDialog("获取数据中...");
        progressDialog.show();
        initdata();
        initlist();
        initevent();
        progressDialog.cancel();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initview() {
        alert_info_listview = (ListView) findViewById(R.id.alert_info_listview);
        tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        iv_video_back = (ImageView) findViewById(R.id.iv_video_back);
//		alert_info_listview.setAdapter(adapter);
    }

    private void initdata() {
        device = MainActivity.commandevice;
        List<HomeItem> homeDeviceLists = Select.from(HomeItem.class).where(Condition.prop("content").eq(DEVICECHANGE)).list();
        if (homeDeviceLists.size() != 0) {

            for (HomeItem homeItem : homeDeviceLists) {
                Alertinfo alertinfo = new Alertinfo();
                alertinfo.setAlertcontent(homeItem.getName());
                alertinfo.setAlerttime(DateUtil.getCurrentTime());
                alertinfo.setBindgiz(device.getMacAddress());
                SugarRecord.save(alertinfo);
            }
        }

        alertinfoLists = Select.from(Alertinfo.class)
                .where(Condition.prop("bindgiz").eq(device.getMacAddress()))
                .list();;
        if (alertinfoLists.size() != 0) {
            adapter = new AlertmesAdapter(ActivityAlertmes.this, alertinfoLists);
            adapter.notifyDataSetChanged();
            tv_nodata.setVisibility(View.GONE);
        } else {
            tv_nodata.setVisibility(View.VISIBLE);
        }


    }

    private void initlist() {
        alert_info_listview.setAdapter(adapter);
    }

    private void initevent() {
        alert_info_listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                new AlertDialog.Builder(ActivityAlertmes.this)
                        .setTitle(R.string.prompt)
                        .setMessage("确定要删除此记录吗")
                        .setPositiveButton(R.string.besure,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        //删除item
                                        adapter.DeleteItem(position);
                                        //发送AlertAdapter界面更新广播
                                        Intent intent = new Intent(ConstAction.alertnotifyaction);
                                        sendBroadcast(intent);

                                        Toast.makeText(getApplicationContext(),
                                                "删除完毕", Toast.LENGTH_SHORT)
                                                .show();
                                        setProgressDialog("刷新数据中...");
                                        progressDialog.show();
                                        progressDialog.cancel();
                                    }
                                }).setNegativeButton("取消", null).show();
            }
        });
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ActivityAlertmes.this)
                        .setTitle(R.string.prompt)
                        .setMessage("确定要清空记录么？")
                        .setPositiveButton(R.string.besure,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        //删除所有
                                        adapter.DeleteALl();
                                        //发送AlertAdapter界面更新广播
                                        Intent intent = new Intent(ConstAction.alertnotifyaction);
                                        sendBroadcast(intent);

                                        Toast.makeText(getApplicationContext(),
                                                "删除完毕", Toast.LENGTH_SHORT)
                                                .show();
                                        setProgressDialog("刷新数据中...");
                                        progressDialog.show();
                                        progressDialog.cancel();
                                        tv_nodata.setVisibility(View.VISIBLE);
                                    }
                                }).setNegativeButton("取消", null).show();
            }
        });
        iv_video_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
