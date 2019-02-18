package com.jtech.cloudtorrentmaster.mvp.contract;

import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtechlib2.contract.BaseContract;

import java.util.List;

/**
 * 服务器选择页面
 */
public interface ServerSelectContract {
    interface Presenter extends BaseContract.Presenter {
        List<ServerInfoModel> loadServerInfoList();

        void setupSerInfoList(List<ServerInfoModel> models);
    }

    interface View extends BaseContract.View {
    }
}