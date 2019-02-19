package com.jtech.cloudtorrentmaster.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtechlib2.util.ImageUtils;
import com.jtechlib2.view.adapter.RecyclerSwipeAdapter;
import com.jtechlib2.view.recycler.RecyclerHolder;

import androidx.annotation.NonNull;

/**
 * 服务器信息列表适配器
 */
public class ServerInfoAdapter extends RecyclerSwipeAdapter<ServerInfoModel> {
    private final static int MIN_SWIPE_DISTANCE = 300;

    private OnServerInfoListener listener;
    private boolean isDrag = false;

    public ServerInfoAdapter(Context context) {
        super(context);
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public ServerInfoAdapter setListener(OnServerInfoListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 添加或更新服务器列表
     *
     * @param model
     * @param index
     */
    public void addOrUpdateItem(@NonNull ServerInfoModel model, int index) {
        for (int i = 0; i < getItemCount(); i++) {
            if (model.getId().equals(getItem(i).getId())) {
                updataItem(i, model);
                return;
            }
        }
        //如果未更新则添加数据对象
        addData(index, model);
    }

    @Override
    public void clearView(RecyclerHolder recyclerHolder) {
        if (isDrag && null != listener) listener.onDragFinish();
        this.isDrag = false;
        //显示拖动按钮
        recyclerHolder.setViewVisible(R.id.imageview_server_info_drag, true);
        //设置背景为透明
        recyclerHolder.getView(R.id.textview_server_info_background).setAlpha(0);
    }

    @Override
    public View getSwipeView(RecyclerHolder recyclerHolder) {
        return isDrag ? recyclerHolder.itemView :
                recyclerHolder.getView(R.id.cardview_server_info);
    }

    @Override
    public void onSwipe(RecyclerHolder recyclerHolder, int direction, float dx) {
    }

    @Override
    public void onSwipeStart(RecyclerHolder recyclerHolder, float dx) {
        //隐藏拖拽按钮
        recyclerHolder.setViewVisibleInvisible(R.id.imageview_server_info_drag, false);
        float alpha = 1;
        if (Math.abs(dx) < MIN_SWIPE_DISTANCE) {
            alpha = Math.abs(dx) / MIN_SWIPE_DISTANCE;
        }
        //设置地测的alpha值
        recyclerHolder.getView(R.id.textview_server_info_background)
                .setAlpha(alpha);
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
        return layoutInflater.inflate(R.layout.view_server_info_item, viewGroup, false);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void convert(RecyclerHolder holder, ServerInfoModel model, int position) {
        //显示国家图标
        ImageUtils.showImage(getContext(), Uri.parse(model.getIconUri()),
                holder.getImageView(R.id.imageview_server_info_country));
        //显示标签
        holder.setText(R.id.textview_server_info_label, model.getLabel());
        //显示ip地址
        holder.setText(R.id.textview_server_info_ip, model.getIpAddress());
        //设置拖动按钮的点击事件
        holder.getView(R.id.imageview_server_info_drag)
                .setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN
                            && null != listener) {
                        isDrag = true;
                        listener.onItemDragClick(holder);
                        return true;
                    }
                    return false;
                });
        //设置点击事件
        holder.setClickListener(R.id.cardview_server_info, v -> {
            if (null != listener) {
                listener.onItemClick(model);
            }
        });
        //设置编辑按钮点击事件
        holder.setClickListener(R.id.imageview_server_info_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemEditClick(model);
                }
            }
        });
    }

    /**
     * 服务器信息监听
     */
    public interface OnServerInfoListener {
        void onItemDragClick(RecyclerHolder holder);

        void onItemEditClick(ServerInfoModel model);

        void onItemClick(ServerInfoModel model);

        void onDragFinish();
    }
}
