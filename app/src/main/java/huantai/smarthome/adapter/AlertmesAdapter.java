package huantai.smarthome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import huantai.smarthome.bean.Alertinfo;
import huantai.smarthome.initial.R;


public class AlertmesAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Alertinfo> mList;
	
	
	
	public AlertmesAdapter(Context mContext, ArrayList<Alertinfo> mList) {
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
		
		return convertView;
	}

	class ViewHolder{
		private TextView alert_name;
		private TextView alert_time;
	}
}
