package com.jtech.cloudtorrentmaster.view.weight;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;

import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.view.adapter.ServerSelectListAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;

/**
 * 服务器选择列表popup
 */
public class ServerSelectPopup extends ListPopupWindow implements AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener {
    private OnServerSelectPopupListener listener;
    private ServerSelectListAdapter listAdapter;

    public ServerSelectPopup(@NonNull Context context) {
        super(context);
        setOnItemClickListener(this);
        setOnDismissListener(this);
        //实例化适配器
        this.listAdapter = new ServerSelectListAdapter(context);
        setAdapter(listAdapter);
    }

    /**
     * 构造对象
     *
     * @param context
     * @return
     */
    public static ServerSelectPopup build(Context context) {
        return new ServerSelectPopup(context);
    }

    /**
     * s设置数据集合
     *
     * @param models
     * @return
     */
    public ServerSelectPopup setDatas(List<ServerInfoModel> models) {
        if (null != listAdapter) {
            listAdapter.setDatas(models);
        }
        return this;
    }

    /**
     * 显示
     *
     * @param parent
     */
    public void show(@NonNull View parent) {
        setAnchorView(parent);
        super.show();
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public ServerSelectPopup setListener(OnServerSelectPopupListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void onDismiss() {
        if (null != listener) {
            listener.onDismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != listener && null != listAdapter) {
            listener.onItemSelect(listAdapter.getItem(position));
        }
        dismiss();
    }

    /**
     * 服务器选择列表监听
     */
    public interface OnServerSelectPopupListener {
        void onItemSelect(ServerInfoModel model);

        void onDismiss();
    }
}