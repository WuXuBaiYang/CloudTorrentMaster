package com.jtech.cloudtorrentmaster.mvp.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.jtech.cloudtorrentmaster.manager.IconsManager;
import com.jtech.cloudtorrentmaster.manager.ParamsCacheManager;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.mvp.contract.ServerSelectContract;

import java.util.List;
import java.util.Map;

/**
 * 服务器选择页面
 */
public class ServerSelectPresenter implements ServerSelectContract.Presenter {
    private Context context;
    private ServerSelectContract.View view;

    public ServerSelectPresenter(Context context, ServerSelectContract.View view, Bundle bundle) {
        this.context = context;
        this.view = view;
    }

    @Override
    public List<ServerInfoModel> loadServerInfoList() {
        return ParamsCacheManager.get(context).getServerInfoList();
    }

    @Override
    public void setupSerInfoList(List<ServerInfoModel> models) {
        ParamsCacheManager.get(context)
                .setServerInfoList(models);
    }

    @Override
    public void addOrUpdateServerInfo(ServerInfoModel model, int index) {
        ParamsCacheManager.get(context).addOrUpdateServerInfo(model, index);
    }

    @Override
    public List<Map.Entry<String, Uri>> loadServerLogoList() {
        return IconsManager.get(context).getLogoIcons();
    }
}