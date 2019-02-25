package com.jtech.cloudtorrentmaster.mvp.presenter;

import android.content.Context;
import android.os.Bundle;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.manager.ParamsCacheManager;
import com.jtech.cloudtorrentmaster.manager.ServerConnectManager;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.mvp.contract.MainContract;
import com.jtech.cloudtorrentmaster.net.API;
import com.jtech.cloudtorrentmaster.view.activity.MainActivity;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    }

    @Override
    public void initServer() {
        if (null != model) {
            //初始化离线服务器接口
            API.get().initCloudTorrentApi(
                    String.format(context.getString(R.string.server_base_url), model.getIp(), model.getPort()));
            //启动长链接服务
            ServerConnectManager.connectServer(model.getIp(), model.getPort());
        }
    }

    @Override
    public ServerInfoModel getServerInfo() {
        return model;
    }

    @Override
    public List<ServerInfoModel> loadServerInfoList(boolean withoutCurrent) {
        List<ServerInfoModel> list = ParamsCacheManager.get(context).getServerInfoList();
        //移除当前服务器
        if (withoutCurrent && null != model) {
            String currentId = model.getId();
            for (ServerInfoModel item : list) {
                if (currentId.equals(item.getId())) {
                    list.remove(item);
                    break;
                }
            }
        }
        return list;
    }

    @Override
    public void addMagnetTask(String magnet) {
        API.get().cloudTorrentApi()
                .addMagnetTask(RequestBody.create(
                        MediaType.parse("application/json;charset=UTF-8"), magnet))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> view.addTaskSuccess(), throwable ->
                        view.addTaskFail(throwable.getMessage()))
                .isDisposed();
    }

    @Override
    public void addTorrentTask(File file) {
        API.get().cloudTorrentApi()
                .addTorrentTask(RequestBody.create(
                        MediaType.parse("application/json;charset=UTF-8"), file))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> view.addTaskSuccess(), new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.addTaskFail(throwable.getMessage());
                    }
                })
                .isDisposed();
    }

    @Override
    public void modifyTorrentTask(String operation) {
        API.get().cloudTorrentApi()
                .modifyTask(RequestBody.create(MediaType
                        .parse("application/json;charset=UTF-8"), operation))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> view.modifySuccess(),
                        throwable -> view.modifyFail(throwable.getMessage()))
                .isDisposed();
    }
}