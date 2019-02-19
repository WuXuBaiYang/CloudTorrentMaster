package com.jtech.cloudtorrentmaster.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.manager.ActivityGoManager;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.mvp.contract.ServerSelectContract;
import com.jtech.cloudtorrentmaster.mvp.presenter.ServerSelectPresenter;
import com.jtech.cloudtorrentmaster.utils.ToastUtils;
import com.jtech.cloudtorrentmaster.utils.Utils;
import com.jtech.cloudtorrentmaster.view.adapter.ServerInfoAdapter;
import com.jtech.cloudtorrentmaster.view.adapter.ServerLogoAdapter;
import com.jtech.cloudtorrentmaster.view.weight.TitleView;
import com.jtechlib2.listener.OnItemViewMoveListener;
import com.jtechlib2.listener.OnItemViewSwipeListener;
import com.jtechlib2.util.ImageUtils;
import com.jtechlib2.view.activity.BaseActivity;
import com.jtechlib2.view.recycler.JRecyclerView;
import com.jtechlib2.view.recycler.RecyclerHolder;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 服务器选择页面
 */
public class ServerSelectActivity extends BaseActivity implements ServerSelectContract.View,
        View.OnClickListener, OnItemViewMoveListener, OnItemViewSwipeListener, ServerInfoAdapter.OnServerInfoListener,
        ServerLogoAdapter.OnServerLogoListener, TextView.OnEditorActionListener {
    private ServerSelectContract.Presenter presenter;

    @BindView(R.id.jrecyclerview_server_select)
    JRecyclerView jRecyclerView;
    @BindView(R.id.fab_server_select)
    FloatingActionButton fabAdd;
    @BindView(R.id.cardview_server_select_create_sheet)
    CardView cardViewSheet;
    @BindView(R.id.imageview_server_select_logo_selected)
    ImageView imageViewLogoSelected;
    @BindView(R.id.jrecyclerview_server_select_logo)
    JRecyclerView jRecyclerViewLogo;
    @BindView(R.id.textinputlayout_server_select_label)
    TextInputLayout textInputLayoutLabel;
    @BindView(R.id.textinputlayout_server_select_ip)
    TextInputLayout textInputLayoutIP;
    @BindView(R.id.textinputlayout_server_select_port)
    TextInputLayout textInputLayoutPort;

    private BottomSheetBehavior bottomSheetBehavior;
    private ServerInfoAdapter serverInfoAdapter;
    private ServerLogoAdapter serverLogoAdapter;
    private String editServerInfoId = "";

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
        //初始化底部的创建视图
        bottomSheetBehavior = BottomSheetBehavior.from(cardViewSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallBack());
        bottomSheetBehavior.setHideable(true);
        //设置适配器
        jRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serverInfoAdapter = new ServerInfoAdapter(getActivity());
        jRecyclerView.setAdapter(serverInfoAdapter);
        serverInfoAdapter.setListener(this);
        //设置拖动监听
        jRecyclerView.setMoveUpDown(false, this);
        //设置滑动删除监听
        jRecyclerView.setSwipeStart(true, this);
        //设置图标默认选中状态
        imageViewLogoSelected.setSelected(true);
        //设置图标列表适配器
        jRecyclerViewLogo.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.HORIZONTAL, false));
        serverLogoAdapter = new ServerLogoAdapter(getActivity());
        jRecyclerViewLogo.setAdapter(serverLogoAdapter);
        serverLogoAdapter.setListener(this);
        //添加输入框操作监听
        Objects.requireNonNull(textInputLayoutPort.getEditText())
                .setOnEditorActionListener(this);
        Objects.requireNonNull(textInputLayoutPort.getEditText())
                .addTextChangedListener(new OnTextWatcher(textInputLayoutPort));
        Objects.requireNonNull(textInputLayoutIP.getEditText())
                .addTextChangedListener(new OnTextWatcher(textInputLayoutIP));
        Objects.requireNonNull(textInputLayoutLabel.getEditText())
                .addTextChangedListener(new OnTextWatcher(textInputLayoutLabel));
    }

    @Override
    protected void loadData() {
        //设置服务器数据集合
        serverInfoAdapter.setDatas(presenter.loadServerInfoList());
        //如果存在服务器信息，则默认隐藏创建sheet
        if (serverInfoAdapter.getItemCount() > 0) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        //设置服务器logo集合
        serverLogoAdapter.setDatas(presenter.loadServerLogoList());
        //设置默认选中
        setDefaultLogoSelect();
    }

    /**
     * 提交服务器信息
     */
    private boolean commitServerInfo() {
        //获取选择的logo
        Map.Entry<String, Uri> selectLogo = serverLogoAdapter.getSelectItem();
        //判断标签是否符合条件
        String label = Objects.requireNonNull(
                textInputLayoutLabel.getEditText()).getText().toString();
        if (TextUtils.isEmpty(label)) {
            textInputLayoutLabel.setError(getString(R.string.input_error_empty));
            return false;
        }
        if (label.length() > textInputLayoutLabel.getCounterMaxLength()) {
            textInputLayoutLabel.setError(getString(R.string.input_error_label_over_limit));
            return false;
        }
        //判断ip地址是否符合条件
        String ip = Objects.requireNonNull(
                textInputLayoutIP.getEditText()).getText().toString();
        if (TextUtils.isEmpty(ip)) {
            textInputLayoutIP.setError(getString(R.string.input_error_empty));
            return false;
        }
        if (!Utils.isIPAddress(ip)) {
            textInputLayoutIP.setError(getString(R.string.input_error_ip_format_error));
            return false;
        }
        //判断端口号是否符合条件
        if (0 == Objects.requireNonNull(
                textInputLayoutPort.getEditText()).length()) {
            textInputLayoutPort.setError(getString(R.string.input_error_empty));
            return false;
        }
        int port = Integer.parseInt(Objects.requireNonNull(
                textInputLayoutPort.getEditText()).getText().toString());
        if (port > 65535) {
            textInputLayoutPort.setError(getString(R.string.input_error_port_over_limit));
            return false;
        }
        //封装服务器信息对象
        ServerInfoModel model = new ServerInfoModel()
                .setId(TextUtils.isEmpty(editServerInfoId)
                        ? UUID.randomUUID().toString() : editServerInfoId)
                .setIconName(selectLogo.getKey())
                .setIconUri(selectLogo.getValue().toString())
                .setLabel(label)
                .setIp(ip)
                .setPort(port);
        presenter.addOrUpdateServerInfo(model, 0);
        //更新到服务器列表
        serverInfoAdapter.addOrUpdateItem(model, 0);
        setupCreateSheet(null);
        return true;
    }

    /**
     * 设置创建服务器信息sheet参数
     *
     * @param model
     */
    private void setupCreateSheet(ServerInfoModel model) {
        if (null != model) {
            //设置已有参数
            serverLogoAdapter.setSelected(model.getIconName());
            ImageUtils.showImage(getActivity(), Uri.parse(model.getIconUri()), imageViewLogoSelected);
            Objects.requireNonNull(textInputLayoutLabel.getEditText()).setText(model.getLabel());
            Objects.requireNonNull(textInputLayoutIP.getEditText()).setText(model.getIp());
            Objects.requireNonNull(textInputLayoutPort.getEditText()).setText(String.valueOf(model.getPort()));
        } else {
            //清空已输入内容
            setDefaultLogoSelect();
            Objects.requireNonNull(textInputLayoutLabel.getEditText()).setText("");
            Objects.requireNonNull(textInputLayoutIP.getEditText()).setText("");
            Objects.requireNonNull(textInputLayoutPort.getEditText()).setText("");
        }
    }

    /**
     * 设置默认选中
     */
    private void setDefaultLogoSelect() {
        if (serverLogoAdapter.getItemCount() > 0) {
            Map.Entry<String, Uri> entry = serverLogoAdapter.getItem(0);
            ImageUtils.showImage(getActivity(), entry.getValue(), imageViewLogoSelected);
            serverLogoAdapter.setSelected(entry.getKey());
        }
    }

    @Override
    public void onItemClick(ServerInfoModel model) {
        //跳转到主页
        ActivityGoManager.goMainPage(getActivity(), model);
        //关闭sheet并关闭当前页面
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        onBackPressed();
    }

    @Override
    public void onItemDragClick(RecyclerHolder holder) {
        jRecyclerView.startDrag(holder);
    }

    @Override
    public void onItemEditClick(ServerInfoModel model) {
        this.editServerInfoId = model.getId();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        //设置已有参数
        setupCreateSheet(model);
    }

    @Override
    public void onItemSelected(Map.Entry<String, Uri> entry) {
        //显示选中的图片
        ImageUtils.showImage(getActivity(), entry.getValue(), imageViewLogoSelected);
        //设置选中状态
        serverLogoAdapter.setSelected(entry.getKey());
    }

    @OnClick({R.id.fab_server_select})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_server_select://展开添加页面
                int state = bottomSheetBehavior.getState();
                if (state == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (commitServerInfo()) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
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
        ToastUtils.showLong(jRecyclerView, String.format(getString(R.string.server_select_delete_toast), model.getLabel()))
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //执行提交操作
        if (actionId == EditorInfo.IME_ACTION_DONE && commitServerInfo()) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        return false;
    }

    /**
     * 输入变化监听
     */
    private class OnTextWatcher implements TextWatcher {
        private TextInputLayout textInputLayout;

        public OnTextWatcher(TextInputLayout textInputLayout) {
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textInputLayout.setError("");
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 底部sheet回调
     */
    private class BottomSheetCallBack extends BottomSheetBehavior.BottomSheetCallback {
        @Override
        public void onStateChanged(@NonNull View view, int state) {
            if (state == BottomSheetBehavior.STATE_HIDDEN) {
                //关闭输入法
                Utils.hideSoftInput(getActivity(), textInputLayoutPort);
                //设置为添加按钮
                fabAdd.setImageResource(R.drawable.ic_fab_add);
                //重置参数
                setupCreateSheet(null);
            } else {
                fabAdd.setImageResource(R.drawable.ic_fab_done);
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {

        }
    }

    @Override
    public void onBackPressed() {
        //如果创建sheet没有隐藏，则隐藏
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }
}