package com.jtech.cloudtorrentmaster.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.manager.ServerConnectManager;
import com.jtech.cloudtorrentmaster.model.ServerTorrentModel;
import com.jtech.cloudtorrentmaster.model.event.ServerConfigEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerConnectEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerDownloadsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerSearchEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerStatsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerTorrentsEvent;
import com.jtech.cloudtorrentmaster.model.event.ServerUsersEvent;
import com.jtech.cloudtorrentmaster.mvp.contract.ServerSelectContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.ServerSelectPresenter;
import com.jtechlib2.view.activity.BaseActivity;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

/**
 * 服务器选择页面
 */
public class ServerSelectActivity extends BaseActivity implements ServerSelectContract.View {
    private ServerSelectContract.Presenter presenter;

    @Override
    protected void initVariables(Bundle bundle) {
        //绑定P类
        presenter = new ServerSelectPresenter(getActivity(), this, bundle);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_server_select);
    }

    @Override
    protected void loadData() {
        ServerConnectManager.connectServer("198.13.44.42", 63000);
    }

    @BindView(R.id.textview)
    TextView textView;

    @Subscribe
    public void a(ServerConfigEvent event) {
        textView.append(new Gson().toJson(event.getModel()));
        textView.append("\n\n");
        Log.i(TAG, "a: ");
    }

    @Subscribe
    public void a(ServerDownloadsEvent event) {
        textView.append(new Gson().toJson(event.getModel()));
        textView.append("\n\n");
        Log.i(TAG, "a: ");
    }

    @Subscribe
    public void a(ServerTorrentsEvent event) {
        textView.append(new Gson().toJson(event.getModels(), new TypeToken<List<ServerTorrentModel>>() {
        }.getType()));
        textView.append("\n\n");
        Log.i(TAG, "a: ");
    }

    @Subscribe
    public void a(ServerSearchEvent event) {
        textView.append(new Gson().toJson(event.getModels(), new TypeToken<List<ServerTorrentModel>>() {
        }.getType()));
        textView.append("\n\n");
        Log.i(TAG, "a: ");
    }

    @Subscribe
    public void a(ServerUsersEvent event) {
        textView.append(new Gson().toJson(event.getModels(), new TypeToken<List<ServerTorrentModel>>() {
        }.getType()));
        textView.append("\n\n");
        Log.i(TAG, "a: ");
    }

    @Subscribe
    public void a(ServerStatsEvent event) {
        textView.append(new Gson().toJson(event.getModel()));
        textView.append("\n\n");
        Log.i(TAG, "a: ");
    }

    @Subscribe
    public void a(ServerConnectEvent event) {
        textView.append("连接服务器状态：" + event.isConnected());
        textView.append("\n\n");
        Log.i(TAG, "a: ");
    }
}