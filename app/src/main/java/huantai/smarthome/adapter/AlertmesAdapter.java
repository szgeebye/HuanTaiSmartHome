package huantai.smarthome.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import huantai.smarthome.bean.Alertinfo;
import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.control.MainActivity;
import huantai.smarthome.initial.R;


public class AlertmesAdapter extends BaseAdapter {
	private Context mContext;
	private List<Alertinfo> mList;
	private GizWifiDevice device;
	
	
	public AlertmesAdapter(Context mContext, List<Alertinfo> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
	}

	public void UpdateData() {
		device = MainActivity.commandevice;
//        this.switchInfoList = Select.from(SwitchInfo.class).where(Condition.prop("bindgiz").eq(device.getMacAddress())).list();
//		this.mList = Select.from(Alertinfo.class)
//				.where(Condition.prop("bindgiz").eq(device.getMacAddress()))
//				.list();
		this.mList = Select.from(Alertinfo.class)
				.where(Condition.prop("bindgiz").eq(device.getMacAddress()))
				.list();
		notifyDataSetChanged();
	}

	public boolean DeleteItem(int position) {
		Alertinfo alertinfo = mList.get(position);
		boolean delete = SugarRecord.delete(alertinfo);
		return delete;
	}

	public int DeleteALl() {
//		Alertinfo alertinfo = mList.get(position);
		int delete = SugarRecord.deleteAll(Alertinfo.class);
		return delete;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView= LinearLayout.inflate(mContext,R.layout.item_alert_info , null);
			viewHolder=new ViewHolder();
			viewHolder.alert_name =(TextView) convertView.
					findViewById(R.id.alert_name);
			viewHolder.alert_time =(TextView) convertView.
					findViewById(R.id.alert_time);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.alert_name.setText(mList.get(position).getAlertcontent());
		
		viewHolder.alert_time.setText(mList.get(position).getAlerttime());


		//注册界面更新广播接收者
		IntentFilter filter = new IntentFilter(ConstAction.alertnotifyaction);
		mContext.registerReceiver(notifyfinishbroadcast, filter);

		return convertView;
	}

	//实现界面更新广播内容
	private BroadcastReceiver notifyfinishbroadcast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//刷新数据
			UpdateData();
//			notifyDataSetChanged();
		}
	};


	class ViewHolder{
		private TextView alert_name;
		private TextView alert_time;
	}
}
