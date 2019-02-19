package com.jtech.cloudtorrentmaster.mvp.contract;

import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtechlib2.contract.BaseContract;

/**
 * 主页
 */
public interface MainContract {
    interface Presenter extends BaseContract.Presenter {
        ServerInfoModel getServerInfo();
    }

    interface View extends BaseContract.View {
    }
}