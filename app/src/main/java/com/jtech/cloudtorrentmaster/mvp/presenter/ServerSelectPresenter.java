package com.jtech.cloudtorrentmaster.mvp.presenter;

import android.content.Context;
import android.os.Bundle;

import com.jtech.cloudtorrentmaster.mvp.contract.ServerSelectContract;

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
}