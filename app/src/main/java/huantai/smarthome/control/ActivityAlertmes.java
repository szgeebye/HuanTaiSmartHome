package huantai.smarthome.control;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	String MacAddress;
//	ArrayList<Alertinfo> mlist = new ArrayList<Alertinfo>();
	ActionBar actionBar;
	// MyReceiver receiver;
	ProgressDialog progressDialog;
	public static final String DEVICECHANGE = "状态改变";
	private List<Alertinfo> alertinfoLists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alertmes);
		Intent intent = getIntent();
		MacAddress = intent.getStringExtra("MacAddress");
		progressDialog = new ProgressDialog(this);
		// receiver=new MyReceiver();
		// IntentFilter filter=new IntentFilter();
		// filter.addAction("com.way.tabui.actity.GizService");
		// AlertmesActivity.this.registerReceiver(receiver, filter);
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
		// TODO Auto-generated method stub
		// AlertmesActivity.this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	private void initview() {
		alert_info_listview = (ListView) findViewById(R.id.alert_info_listview);
		tv_nodata = (TextView) findViewById(R.id.tv_nodata);
//		alert_info_listview.setAdapter(adapter);
	}

	private void initdata() {
		List<HomeItem> homeDeviceLists = Select.from(HomeItem.class).where(Condition.prop("content").eq(DEVICECHANGE)).list();
		if (homeDeviceLists.size() != 0) {

			for (HomeItem homeItem : homeDeviceLists) {
				Alertinfo alertinfo = new Alertinfo();
				alertinfo.setAlertcontent(homeItem.getName());
				alertinfo.setAlerttime(DateUtil.getCurrentTime());
				SugarRecord.save(alertinfo);
			}
		}

		alertinfoLists = SugarRecord.listAll(Alertinfo.class);
		if (alertinfoLists.size() != 0) {
			adapter = new AlertmesAdapter(ActivityAlertmes.this, alertinfoLists);
			adapter.notifyDataSetChanged();
		}

	}

	int alertmesid;

	private void initlist() {
		alert_info_listview.setAdapter(adapter);
	}

	private void initevent() {
		alert_info_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									final int position, long id) {
				// TODO Auto-generated method stub
//				alertmesid = mlist.get(position).getId();
				new AlertDialog.Builder(ActivityAlertmes.this)
						.setTitle(R.string.prompt)
						.setMessage("确定要删除此记录吗")
						.setPositiveButton(R.string.besure,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
//										dbAdapter.deleteAlert(alertmesid);

										Alertinfo alertinfo = alertinfoLists.get(position);
										SugarRecord.delete(alertinfo);
										//发送AlertAdapter界面更新广播
										Intent intent = new Intent(ConstAction.alertnotifyaction);
										sendBroadcast(intent);

										Toast.makeText(getApplicationContext(),
												"删除完毕", Toast.LENGTH_SHORT)
												.show();
										setProgressDialog("刷新数据中...");
										progressDialog.show();
//										initdata();
//										initlist();
										progressDialog.cancel();
									}
								}).setNegativeButton("取消", null).show();
			}
		});
	}


}
