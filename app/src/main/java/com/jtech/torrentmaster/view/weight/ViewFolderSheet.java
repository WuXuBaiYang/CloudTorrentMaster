package com.jtech.torrentmaster.view.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jtech.torrentmaster.R;
import com.jtech.torrentmaster.model.ServerTorrentModel;
import com.jtech.torrentmaster.view.adapter.ViewFolderAdapter;
import com.jtechlib2.view.recycler.JRecyclerView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件夹目录浏览sheet
 */
public class ViewFolderSheet extends BottomSheetDialog {
    @BindView(R.id.jrecyclerview_view_folder)
    JRecyclerView jRecyclerView;

    private ViewFolderAdapter viewFolderAdapter;

    private ViewFolderSheet(@NonNull Context context) {
        super(context);
        //绑定视图注解
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(context)
                .inflate(R.layout.view_view_folder_sheet, null);
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        //设置列表
        jRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.viewFolderAdapter = new ViewFolderAdapter(context);
        jRecyclerView.setAdapter(viewFolderAdapter);
    }

    /**
     * 构造对象
     *
     * @param context
     * @return
     */
    public static ViewFolderSheet build(Context context) {
        return new ViewFolderSheet(context);
    }

    /**
     * 设置文件夹目录集合
     *
     * @param models
     * @return
     */
    public ViewFolderSheet setFolderData(List<ServerTorrentModel.FileModel> models) {
        if (null != viewFolderAdapter) {
            this.viewFolderAdapter.setDatas(models);
        }
        return this;
    }

    @OnClick(R.id.imageview_view_folder_close)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_view_folder_close://关闭
                dismiss();
                break;
        }
    }
}