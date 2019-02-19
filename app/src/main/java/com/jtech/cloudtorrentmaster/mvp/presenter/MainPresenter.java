package com.jtech.cloudtorrentmaster.mvp.presenter;

import android.content.Context;
import android.os.Bundle;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.net.API;
import com.jtech.cloudtorrentmaster.view.activity.MainActivity;

/**
 * 主页
 */
public class MainPresenter implements MainContract.Presenter {
    private Context context;
    private MainContract.View view;

    private ServerInfoModel model;

    public MainPresenter(Context context, MainContract.View view, Bundle bundle) {
        this.context = context;
        this.view = view;
        if (null != bundle) {
            this.model = (ServerInfoModel) bundle.getSerializable(MainActivity.KEY_SERVER_INFO);
        }
        //初始化离线服务器接口
        API.get().initCloudTorrentApi(
                String.format(context.getString(R.string.server_base_url), model.getIp(), model.getPort()));
    }

    @Override
    public ServerInfoModel getServerInfo() {
        return model;
    }
}