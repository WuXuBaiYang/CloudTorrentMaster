package com.jtech.cloudtorrentmaster.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.model.ServerTorrentModel;
import com.jtechlib2.view.adapter.RecyclerAdapter;
import com.jtechlib2.view.recycler.RecyclerHolder;

/**
 * 种子任务列表适配器
 */
public class TorrentsAdapter extends RecyclerAdapter<ServerTorrentModel> {
    private OnTorrentsListener listener;

    public TorrentsAdapter(Context context) {
        super(context);
    }

    public TorrentsAdapter setListener(OnTorrentsListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
        return layoutInflater.inflate(R.layout.view_torrents_item, viewGroup, false);
    }

    @Override
    protected void convert(RecyclerHolder holder, ServerTorrentModel model, int position) {
        //设置任务名称
        holder.setText(R.id.textview_torrents_task_name, model.getName());
        //设置任务下载进度
        NumberProgressBar progressBar = holder.getView(R.id.progressbar_torrents_item);
        progressBar.setProgress((int) model.getPercent());
        //设置按钮状态
        //按钮已加载并且未作废并且下载进度未满则可以对下载进行操作
        holder.setViewVisible(R.id.imageview_torrents_option_download,
                model.isLoaded() && !model.isDropped() && model.getPercent() < 100);
        //任务未加载或未开始则可以出现移除操作
        holder.setViewVisible(R.id.imageview_torrents_option_task,
                !model.isLoaded() || !model.isStarted());
        //设置按钮状态
        holder.setSelected(R.id.imageview_torrents_option_download, model.isStarted());
        holder.setSelected(R.id.imageview_torrents_option_task, model.isLoaded());
        //设置操作按钮点击事件
        holder.setClickListener(R.id.imageview_torrents_folder, v -> {
            if (null != listener) {
                listener.openFolder(model);
            }
        });
        holder.setClickListener(R.id.imageview_torrents_option_download, v -> {
            if (null != listener) {
                listener.modifyTorrent((model.isStarted() ?
                        "stop:" : "start:") + model.getInfoHash());
            }
        });
        holder.setClickListener(R.id.imageview_torrents_option_task, v -> {
            if (null != listener) {
                listener.modifyTorrent("delete:" + model.getInfoHash());
            }
        });
    }

    /**
     * 服务器任务列表监听
     */
    public interface OnTorrentsListener {
        void openFolder(ServerTorrentModel model);

        void modifyTorrent(String optional);
    }
}