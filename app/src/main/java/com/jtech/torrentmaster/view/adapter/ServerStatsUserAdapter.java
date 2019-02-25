package com.jtech.torrentmaster.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jtech.torrentmaster.R;
import com.jtech.torrentmaster.model.ServerUserModel;
import com.jtechlib2.view.adapter.RecyclerAdapter;
import com.jtechlib2.view.recycler.RecyclerHolder;

/**
 * 服务器状态已连接用户列表
 */
public class ServerStatsUserAdapter extends RecyclerAdapter<ServerUserModel> {
    public ServerStatsUserAdapter(Context context) {
        super(context);
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
        return layoutInflater.inflate(R.layout.view_server_stats_user_item, viewGroup, false);
    }

    @Override
    protected void convert(RecyclerHolder holder, ServerUserModel model, int position) {
        //设置用户信息
        holder.setText(R.id.textview_server_stats_user, model.getIpAddress());
    }
}