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

import java.util.List;

import huantai.smarthome.bean.Alertinfo;
import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.initial.R;


public class AlertmesAdapter extends BaseAdapter {
	private Context mContext;
	private List<Alertinfo> mList;
	
	
	
	public AlertmesAdapter(Context mContext, List<Alertinfo> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
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
			notifyDataSetChanged();
		}
	};


	class ViewHolder{
		private TextView alert_name;
		private TextView alert_time;
	}
}
