package huantai.smarthome.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import huantai.smarthome.initial.R;

public class TextViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_title;
    public TextView tv_content;
    public ImageView iv_icon;
    public ImageView iv_edit;
    public LinearLayout itemLayout;


    public TextViewHolder(View itemView) {
        super(itemView);
        itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);
        tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);

    }
}