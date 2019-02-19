package com.jtech.cloudtorrentmaster.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jtech.cloudtorrentmaster.R;
import com.jtechlib2.util.ImageUtils;
import com.jtechlib2.view.adapter.RecyclerAdapter;
import com.jtechlib2.view.recycler.RecyclerHolder;

import java.util.Map;

import androidx.annotation.NonNull;

/**
 * 服务器图标适配器
 */
public class ServerLogoAdapter extends RecyclerAdapter<Map.Entry<String, Uri>> {
    private OnServerLogoListener listener;
    private int selectedPosition = 0;
    private boolean isExpand = false;

    public ServerLogoAdapter(Context context) {
        super(context);
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public ServerLogoAdapter setListener(OnServerLogoListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 设置展开状态
     *
     * @param expand
     * @return
     */
    public ServerLogoAdapter setExpand(boolean expand) {
        isExpand = expand;
        return this;
    }

    /**
     * 设置选择状态
     *
     * @param key
     * @return
     */
    public ServerLogoAdapter setSelected(@NonNull String key) {
        int lastSelectedPosition = selectedPosition;
        for (int i = 0; i < getItemCount(); i++) {
            if (key.equals(getItem(i).getKey())) {
                this.selectedPosition = i;
                notifyItemChanged(selectedPosition);
                notifyItemChanged(lastSelectedPosition);
                return this;
            }
        }
        return this;
    }

    /**
     * 获得选中的对象
     *
     * @return
     */
    public Map.Entry<String, Uri> getSelectItem() {
        return getItem(selectedPosition);
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
        return layoutInflater.inflate(R.layout.view_server_logo_item, viewGroup, false);
    }

    @Override
    protected void convert(RecyclerHolder holder, Map.Entry<String, Uri> model, int position) {
        ImageView imageView = holder.getImageView(R.id.imageview_server_logo);
        //显示logo
        ImageUtils.showImage(getContext(), model.getValue(), imageView);
        //设置选择状态
        imageView.setSelected(position == selectedPosition);
        //设置文本描述
        holder.setText(R.id.textview_server_logo, model.getKey());
        //设置是否展示描述
        holder.setViewVisible(R.id.textview_server_logo, isExpand);
        //设置点击状态
        holder.setClickListener(R.id.imageview_server_logo, v -> {
            if (null != listener) {
                listener.onItemSelected(model);
            }
        });
    }

    /**
     * 服务器logo监听
     */
    public interface OnServerLogoListener {
        void onItemSelected(Map.Entry<String, Uri> entry);
    }
}