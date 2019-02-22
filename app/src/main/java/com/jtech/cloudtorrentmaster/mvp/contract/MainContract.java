package com.jtech.cloudtorrentmaster.mvp.contract;

import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtechlib2.contract.BaseContract;

import java.io.File;
import java.util.List;

/**
 * 主页
 */
public interface MainContract {
    interface Presenter extends BaseContract.Presenter {
        ServerInfoModel getServerInfo();

        List<ServerInfoModel> loadServerInfoList(boolean withoutCurrent);

        void addMagnetTask(String magnet);

        void addTorrentTask(File file);

        void initServer();
    }

    interface View extends BaseContract.View {
        void addTaskSuccess();

        void addTaskFail(String error);
    }
}