package com.jtech.cloudtorrentmaster.view.activity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.manager.ActivityGoManager;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.mvp.contract.ServerSelectContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.ServerSelectPresenter;
import com.jtech.cloudtorrentmaster.utils.ToastUtils;
import com.jtech.cloudtorrentmaster.view.adapter.ServerInfoAdapter;
import com.jtech.cloudtorrentmaster.view.weight.CreateServerInfoSheet;
import com.jtech.cloudtorrentmaster.view.weight.TitleView;
import com.jtechlib2.listener.OnItemViewMoveListener;
import com.jtechlib2.listener.OnItemViewSwipeListener;
import com.jtechlib2.view.activity.BaseActivity;
import com.jtechlib2.view.recycler.JRecyclerView;
import com.jtechlib2.view.recycler.RecyclerHolder;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 服务器选择页面
 */
public class ServerSelectActivity extends BaseActivity implements ServerSelectContract.View,
        View.OnClickListener, OnItemViewMoveListener, OnItemViewSwipeListener,
        ServerInfoAdapter.OnServerInfoListener {
    private ServerSelectContract.Presenter presenter;

    @BindView(R.id.jrecyclerview_server_select)
    JRecyclerView jRecyclerView;

    private CreateServerInfoSheet createServerInfoSheet;
    private ServerInfoAdapter serverInfoAdapter;

    @Override
    protected void initVariables(Bundle bundle) {
        //绑定P类
        presenter = new ServerSelectPresenter(getActivity(), this, bundle);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_server_select);
        //设置标题栏
        TitleView.build(getActivity())
                .setTitle(R.string.server_select_title)
                .setLeftButton(R.drawable.ic_title_close, this);
        //设置适配器
        jRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serverInfoAdapter = new ServerInfoAdapter(getActivity());
        jRecyclerView.setAdapter(serverInfoAdapter);
        serverInfoAdapter.setListener(this);
        //设置拖动监听
        jRecyclerView.setMoveUpDown(false, this);
        //设置滑动删除监听
        jRecyclerView.setSwipeStart(true, this);
    }

    @Override
    protected void loadData() {
        //设置服务器数据集合
        serverInfoAdapter.setDatas(presenter.loadServerInfoList());
    }

    /**
     * 获取服务器信息编辑创建sheet
     *
     * @return
     */
    private CreateServerInfoSheet getServerInfoSheet() {
        if (null == createServerInfoSheet) {
            this.createServerInfoSheet = CreateServerInfoSheet.build(getActivity())
                    .setServerLogos(presenter.loadServerLogoList())
                    .setListener(model -> {
                        //提交服务器信息
                        presenter.addOrUpdateServerInfo(model, 0);
                        //更新到服务器列表
                        serverInfoAdapter.addOrUpdateItem(model, 0);
                    });
        }
        return this.createServerInfoSheet;
    }

    @Override
    public void onItemClick(ServerInfoModel model) {
        //跳转到主页
        ActivityGoManager.goMainPage(getActivity(), model);
        //关闭sheet并关闭当前页面
        getServerInfoSheet().dismiss();
        onBackPressed();
    }

    @Override
    public void onItemEditClick(ServerInfoModel model) {
        getServerInfoSheet().setEditServerInfo(model)
                .show();
    }

    @Override
    public void onItemDragClick(RecyclerHolder holder) {
        jRecyclerView.startDrag(holder);
    }

    @OnClick({R.id.fab_server_select})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_server_select://展开添加页面
                getServerInfoSheet().show();
                break;
            default://默认为关闭按钮
                onBackPressed();
                break;
        }
    }

    @Override
    public boolean onItemViewMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //移动数据
        serverInfoAdapter.moveData(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onDragFinish() {
        //存储移动后的数据
        presenter.setupSerInfoList(serverInfoAdapter.getRealDatas());
    }

    @Override
    public void onItemViewSwipe(RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        final ServerInfoModel model = serverInfoAdapter.getItem(position);
        //删除对应数据
        serverInfoAdapter.removeData(position);
        //弹出撤销操作提示
        ToastUtils.makeLong(jRecyclerView, String.format(getString(R.string.server_select_delete_toast), model.getLabel()))
                .setAction(R.string.server_select_undo, v -> {
                    //将撤销的数据还原回去
                    serverInfoAdapter.addData(position, model);
                })
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        presenter.setupSerInfoList(serverInfoAdapter.getRealDatas());
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        //如果创建sheet没有隐藏，则隐藏
        if (getServerInfoSheet().isShowing()) {
            getServerInfoSheet().dismiss();
            return;
        }
        super.onBackPressed();
    }
}