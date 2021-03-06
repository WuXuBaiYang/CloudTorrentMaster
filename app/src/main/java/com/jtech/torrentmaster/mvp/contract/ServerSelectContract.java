package com.jtech.torrentmaster.mvp.contract;

import android.net.Uri;

import com.jtech.torrentmaster.model.ServerInfoModel;
import com.jtechlib2.contract.BaseContract;

import java.util.List;
import java.util.Map;

/**
 * 服务器选择页面
 */
public interface ServerSelectContract {
    interface Presenter extends BaseContract.Presenter {
        List<ServerInfoModel> loadServerInfoList();

        void setupSerInfoList(List<ServerInfoModel> models);

        void addOrUpdateServerInfo(ServerInfoModel model, int index);

        List<Map.Entry<String, Uri>> loadServerLogoList();
    }

    interface View extends BaseContract.View {
    }
}