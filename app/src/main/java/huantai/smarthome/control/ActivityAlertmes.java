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

import huantai.smarthome.adapter.AlertmesAdapter;
import huantai.smarthome.initial.R;

public class ActivityAlertmes extends Activity {

	private ListView alert_info_listview;
	private TextView tv_nodata;
	private AlertmesAdapter adapter;
	String MacAddress;
//	ArrayList<Alertinfo> mlist = new ArrayList<Alertinfo>();
	ActionBar actionBar;
	// MyReceiver receiver;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alertmes);
		setActionBar(true, true, "警报记录");
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

	public void setActionBar(Boolean HBE, Boolean DSHE, CharSequence Title) {

		actionBar = getActionBar();// 初始化ActionBar
		actionBar.setHomeButtonEnabled(HBE);
		actionBar.setIcon(R.drawable.back_bt);
		actionBar.setTitle(Title);
		actionBar.setDisplayShowHomeEnabled(DSHE);
	}

	private void initview() {
		alert_info_listview = (ListView) findViewById(R.id.alert_info_listview);
		tv_nodata = (TextView) findViewById(R.id.tv_nodata);
	}

	private void initdata() {


	}

	int alertmesid;

	private void initlist() {
//		adapter = new AlertmesAdapter(ActivityAlertmes.this, mlist);
//		alert_info_listview.setAdapter(adapter);
	}

	private void initevent() {
		alert_info_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
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
										Toast.makeText(getApplicationContext(),
												"删除完毕", Toast.LENGTH_SHORT)
												.show();
										setProgressDialog("刷新数据中...");
										progressDialog.show();
										initdata();
										initlist();
										progressDialog.cancel();
									}
								}).setNegativeButton("取消", null).show();
			}
		});
	}


}
