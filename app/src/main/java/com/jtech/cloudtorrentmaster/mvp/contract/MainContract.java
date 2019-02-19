package com.jtech.cloudtorrentmaster.mvp.contract;

import com.jtechlib2.contract.BaseContract;

/**
 * 主页
 */
public interface MainContract {
    interface Presenter extends BaseContract.Presenter {
        String getServerName();

        String getIPAddress();
    }

    interface View extends BaseContract.View {
    }
}