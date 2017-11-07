package huantai.smarthome.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.SugarRecord;

import java.util.List;

import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.bean.HomeItem;
import huantai.smarthome.control.ActivityAlertmes;
import huantai.smarthome.initial.R;
import huantai.smarthome.view.TextViewHolder;

public class AddRemoveNumberedAdapter extends RecyclerView.Adapter<TextViewHolder> implements View.OnClickListener {
    private static final int ITEM_VIEW_TYPE_ITEM = 0;
    private static final int ITEM_VIEW_TYPE_ADD = 1;

    private List<HomeItem> homeItemLists;
    private Context context;
    //删除按钮是否隐藏
    private boolean iv_delete_gone = true;
    //声明监听接口对象
    private OnItemClickListener mOnItemClickListener = null;
    private Intent vintent;
    public static final String DEVICENOMAL = "正常";
    public static final String DEVICECHANGE = "状态改变";
    public AddRemoveNumberedAdapter(List<HomeItem> homeItemLists, Context context) {
        this.homeItemLists = homeItemLists;
        this.context = context;
    }

    //更新数据
    public void setData(List<HomeItem> homeItemLists) {
        this.homeItemLists = homeItemLists;
    }


    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                viewType == ITEM_VIEW_TYPE_ADD ? R.layout.item_add : R.layout.activity_item, parent, false);

        TextViewHolder textViewHolder = new TextViewHolder(view);
        //为每一个item设置点击监听
//    textViewHolder.itemLayout.setOnClickListener(this);
        view.setOnClickListener(this);

        return textViewHolder;
    }

    @Override
    public void onBindViewHolder(final TextViewHolder holder, final int position) {

        vintent = new Intent(ConstAction.vibratoraction);

        if (position == homeItemLists.size()) {
            holder.tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//          addItem();
                }
            });
            return;
        }
        //获取图片资源文件
        holder.iv_icon.setImageResource(R.drawable.home_images);
        if (homeItemLists != null) {

            holder.tv_title.setText(homeItemLists.get(position).getName());
            holder.tv_content.setText(homeItemLists.get(position).getContent());
            holder.iv_icon.setImageLevel(homeItemLists.get(position).getPicture());
            holder.itemLayout.setTag(position);

            if (DEVICECHANGE.equals(homeItemLists.get(position).getContent())) {
                holder.llf_itemLayout.setBackgroundResource(R.drawable.home_item_sel_change);
                context.sendBroadcast(vintent);
            } else if (DEVICENOMAL.equals(homeItemLists.get(position).getContent())) {
                //(160,244,247,245)
                holder.llf_itemLayout.setBackgroundResource(R.drawable.home_item_sel_original);
            } else {
                holder.llf_itemLayout.setBackgroundResource(R.drawable.home_item_sel_original);
            }

            //注册界面更新广播接收者
            IntentFilter filter = new IntentFilter(ConstAction.notifyfinishaction);
            context.registerReceiver(notifyfinishbroadcast, filter);


            if (iv_delete_gone) {
                holder.iv_edit.setVisibility(View.GONE);
                holder.iv_edit.setEnabled(false);
            } else {
                holder.iv_edit.setVisibility(View.VISIBLE);
                holder.iv_edit.setEnabled(true);
                holder.iv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(holder.getPosition());
                    }
                });
            }

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/11/5 tag用来判断item点击的位置，若为最后一个跳转到报警表。
                    if ((int)v.getTag() == homeItemLists.size()-1) {
                        Intent intent = new Intent(context, ActivityAlertmes.class);
                        context.startActivity(intent);
                    }
                    System.out.println("click"+(int)v.getTag());
                }
            });
            //将position保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(position);
        }


        holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {



            @Override
            public boolean onLongClick(View v) {
                //显示删除按钮
                iv_delete_gone = false;
                //刷新数据
                notifyDataSetChanged();
                //发震动广播

                context.sendBroadcast(vintent);

                //发删除完成广播
                Intent dintent = new Intent(ConstAction.deletefinishaction);
                context.sendBroadcast(dintent);

                return true;
            }
        });
    }

    //实现界面更新广播内容
    private BroadcastReceiver notifyfinishbroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            iv_delete_gone = true;
            //刷新数据
            notifyDataSetChanged();
        }
    };


    private void addItem() {
        if (homeItemLists.size() >= 1) {
            int lastItem = homeItemLists.size() - 1;
            notifyItemInserted(lastItem + 2);
        } else {
            notifyItemInserted(0);
        }

    }

    private void removeItem(int position) {
        HomeItem homeItem = SugarRecord.findById(HomeItem.class, homeItemLists.get(position).getId());
        homeItem.setDelete(true);
        SugarRecord.save(homeItem);
        homeItemLists.remove(position);
        Log.i("datadelete", SugarRecord.listAll(HomeItem.class).toString());
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position == homeItemLists.size() ? ITEM_VIEW_TYPE_ADD : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return homeItemLists.size();
    }

    //接口暴露监听
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }
}

