package com.jtech.cloudtorrentmaster.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.model.ServerTorrentModel;
import com.jtechlib2.view.adapter.RecyclerAdapter;
import com.jtechlib2.view.recycler.RecyclerHolder;

/**
 * 种子任务列表适配器
 */
public class TorrentsAdapter extends RecyclerAdapter<ServerTorrentModel> {
    public TorrentsAdapter(Context context) {
        super(context);
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
        return layoutInflater.inflate(R.layout.view_torrents_item, viewGroup, false);
    }

    @Override
    protected void convert(RecyclerHolder holder, ServerTorrentModel model, int position) {

    }
}