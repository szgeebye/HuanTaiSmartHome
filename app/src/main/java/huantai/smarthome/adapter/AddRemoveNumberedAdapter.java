package huantai.smarthome.adapter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.bean.HomeItem;
import huantai.smarthome.control.HomeFragment;
import huantai.smarthome.initial.R;
import huantai.smarthome.view.TextViewHolder;

public class AddRemoveNumberedAdapter extends RecyclerView.Adapter<TextViewHolder>{
  private static final int ITEM_VIEW_TYPE_ITEM = 0;
  private static final int ITEM_VIEW_TYPE_ADD = 1;

  private List<HomeItem> homeItemLists;
  private Context context;

  public AddRemoveNumberedAdapter(List<HomeItem> homeItemLists, Context context) {
    this.homeItemLists=homeItemLists;
    this.context = context;
  }

  //更新数据
  public void setData(List<HomeItem> homeItemLists) {
        this.homeItemLists=homeItemLists;
  }


  @Override
  public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(
            viewType == ITEM_VIEW_TYPE_ADD ? R.layout.item_add : R.layout.activity_item, parent, false);
    return new TextViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final TextViewHolder holder, final int position) {

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
    if (homeItemLists != null){

      holder.tv_title.setText(homeItemLists.get(position).getName());
      holder.tv_content.setText(homeItemLists.get(position).getContent());
      holder.iv_icon.setImageLevel(homeItemLists.get(position).getPicture());
      holder.iv_edit.setEnabled(false);
    }

    holder.itemLayout.setOnLongClickListener(new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
//        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
//        vibrator.vibrate(20);
        holder.iv_edit.setVisibility(View.VISIBLE);
        holder.iv_edit.setEnabled(true);
        //发震动广播
        Intent intent = new Intent(ConstAction.vibratoraction);
        context.sendBroadcast(intent);


        return true;
      }
    });

//    holder.tv_title.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        removeItem(holder.getPosition());
//      }
//    });
  }

  private void addItem() {
    if (homeItemLists.size() >=1){
      int lastItem = homeItemLists.size() - 1;
      notifyItemInserted(lastItem + 2);
    } else {
      notifyItemInserted(0);
    }

  }

  private void removeItem(int position) {
    homeItemLists.remove(position);
    notifyItemRemoved(position);
  }

  @Override
  public int getItemViewType(int position) {
    return position == homeItemLists.size() ? ITEM_VIEW_TYPE_ADD : ITEM_VIEW_TYPE_ITEM;
  }

  @Override
  public int getItemCount() {
    return homeItemLists.size()+1;
  }

}

