package com.jtech.torrentmaster.view.adapter;

import android.content.Context;
import android.text.Html;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jtech.torrentmaster.R;
import com.jtech.torrentmaster.model.ServerTorrentModel;
import com.jtechlib2.view.adapter.RecyclerAdapter;
import com.jtechlib2.view.recycler.RecyclerHolder;

/**
 * 浏览文件目录列表适配器
 */
public class ViewFolderAdapter extends RecyclerAdapter<ServerTorrentModel.FileModel> {
    public ViewFolderAdapter(Context context) {
        super(context);
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, int viewType) {
        return layoutInflater.inflate(R.layout.view_view_folder_item, viewGroup, false);
    }

    @Override
    protected void convert(RecyclerHolder holder, ServerTorrentModel.FileModel model, int position) {
        //设置文件信息
        String fileSize = Formatter.formatFileSize(getContext(), model.getSize());
        String fileInfo = String.format(getContext().getString(R.string.view_folder_file_info),
                model.getPath(), fileSize, String.valueOf(model.getPercent()));
        holder.setText(R.id.textview_view_folder, Html.fromHtml(fileInfo));
    }
}