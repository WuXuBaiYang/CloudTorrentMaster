package com.jtech.cloudtorrentmaster.view.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtech.cloudtorrentmaster.utils.Utils;
import com.jtech.cloudtorrentmaster.view.adapter.ServerLogoAdapter;
import com.jtechlib2.util.ImageUtils;
import com.jtechlib2.view.recycler.JRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;

/**
 * 创建服务器信息sheet
 */
public class CreateServerInfoSheet extends BottomSheetDialog implements ServerLogoAdapter.OnServerLogoListener,
        View.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener {
    @BindView(R.id.imageview_server_select_sheet_logo)
    ImageView imageViewLogoSelected;
    @BindView(R.id.jrecyclerview_server_select_sheet_logo)
    JRecyclerView jRecyclerViewLogo;
    @BindView(R.id.textinputlayout_server_select_sheet_label)
    TextInputLayout textInputLayoutLabel;
    @BindView(R.id.textinputlayout_server_select_sheet_ip)
    TextInputLayout textInputLayoutIP;
    @BindView(R.id.textinputlayout_server_select_sheet_port)
    TextInputLayout textInputLayoutPort;
    @BindView(R.id.imageview_server_select_sheet_forward)
    ImageView imageViewForward;

    private OnCreateServerInfoSheetListener listener;
    private List<TextInputEditText> inputEditTexts;
    private ServerLogoAdapter serverLogoAdapter;
    private int currentInputFocusIndex = -1;
    private String editServerInfoId = "";

    private CreateServerInfoSheet(@NonNull Context context, int theme) {
        super(context, theme);
        //设置dismiss监听
        setOnDismissListener(this);
        //设置显示监听
        setOnShowListener(this);
        //初始化主视图
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(context)
                .inflate(R.layout.view_server_select_sheet, null);
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        //设置图标默认选中状态
        imageViewLogoSelected.setSelected(true);
        //设置图标列表适配器
        jRecyclerViewLogo.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false));
        serverLogoAdapter = new ServerLogoAdapter(context);
        jRecyclerViewLogo.setAdapter(serverLogoAdapter);
        serverLogoAdapter.setListener(this);
        //添加输入框操作监听
        Objects.requireNonNull(textInputLayoutLabel.getEditText())
                .addTextChangedListener(new OnTextWatcher(textInputLayoutLabel));
        Objects.requireNonNull(textInputLayoutIP.getEditText())
                .addTextChangedListener(new OnTextWatcher(textInputLayoutIP));
        Objects.requireNonNull(textInputLayoutPort.getEditText())
                .addTextChangedListener(new OnTextWatcher(textInputLayoutPort));
    }

    /**
     * 构造当前对象
     *
     * @param context
     * @return
     */
    public static CreateServerInfoSheet build(Context context) {
        return new CreateServerInfoSheet(context, R.style.BottomSheetDialog);
    }

    /**
     * 设置监听
     *
     * @param listener
     * @return
     */
    public CreateServerInfoSheet setListener(OnCreateServerInfoSheetListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 设置服务器logo集合
     *
     * @param list
     * @return
     */
    public CreateServerInfoSheet setServerLogos(List<Map.Entry<String, Uri>> list) {
        //设置服务器logo集合
        serverLogoAdapter.setDatas(list);
        //设置默认选中
        setDefaultLogoSelect();
        return this;
    }

    /**
     * 设置需要编辑的服务器信息
     *
     * @param model
     * @return
     */
    public CreateServerInfoSheet setEditServerInfo(ServerInfoModel model) {
        this.editServerInfoId = model.getId();
        //设置已有参数
        setupCreateSheet(model);
        return this;
    }

    /**
     * 跳到下一个输入框
     */
    private void nextInput() {
        //焦点下标自增，如果依然小于0，则置0
        if (++currentInputFocusIndex < 0) currentInputFocusIndex = 0;
        setInputFocusOn(currentInputFocusIndex);
    }

    /**
     * 设置输入框焦点并弹出键盘
     *
     * @param index
     */
    private void setInputFocusOn(int index) {
        //初始化输入框焦点控制集合
        if (null == inputEditTexts) {
            this.inputEditTexts = new ArrayList<>();
            inputEditTexts.add(findViewById(
                    R.id.textinputedittext_server_select_sheet_label));
            inputEditTexts.add(findViewById(
                    R.id.textinputedittext_server_select_sheet_ip));
            inputEditTexts.add(findViewById(
                    R.id.textinputedittext_server_select_sheet_port));
        }
        //如果超出0~集合最大值则无效
        if (index < 0 || index > inputEditTexts.size() - 1) return;
        //获取目标输入框对象并弹出软键盘获取焦点
        showSoftInput(inputEditTexts.get(index));
    }

    /**
     * 获取焦点并弹出软键盘
     *
     * @param editText
     */
    private void showSoftInput(@NonNull EditText editText) {
        showSoftInput(editText, 0);
    }

    /**
     * 获取焦点并弹出软键盘
     *
     * @param editText
     * @param delayMillis
     */
    private void showSoftInput(@NonNull EditText editText, long delayMillis) {
        //延时启动
        new Handler().postDelayed(() -> {
            //获取目标输入框对象并弹出软键盘获取焦点
            editText.setFocusable(true);
            editText.requestFocus();
            //弹出软键盘
            Utils.showSoftInput(getContext(), editText);
        }, delayMillis);
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
            ImageUtils.showImage(getContext(), model.getIconUri(), imageViewLogoSelected);
            Objects.requireNonNull(textInputLayoutLabel.getEditText()).setText(model.getLabel());
            Objects.requireNonNull(textInputLayoutIP.getEditText()).setText(model.getIp());
            Objects.requireNonNull(textInputLayoutPort.getEditText()).setText(String.valueOf(model.getPort()));
        } else {
            //清空已输入内容
            setDefaultLogoSelect();
            Objects.requireNonNull(textInputLayoutLabel.getEditText()).setText("");
            textInputLayoutLabel.getEditText().clearFocus();
            Objects.requireNonNull(textInputLayoutIP.getEditText()).setText("");
            textInputLayoutIP.getEditText().clearFocus();
            Objects.requireNonNull(textInputLayoutPort.getEditText()).setText("");
            textInputLayoutPort.getEditText().clearFocus();
            //重置状态
            imageViewForward.setSelected(false);
            currentInputFocusIndex = -1;
            editServerInfoId = "";
        }
    }

    /**
     * 确认要提交的信息
     */
    private boolean commitServerInfo() {
        //获取选择的logo
        Map.Entry<String, Uri> selectLogo = serverLogoAdapter.getSelectItem();
        //判断标签是否符合条件
        String label = Objects.requireNonNull(
                textInputLayoutLabel.getEditText()).getText().toString();
        if (TextUtils.isEmpty(label)) {
            textInputLayoutLabel.setError(getContext().getString(R.string.input_error_empty));
            showSoftInput(textInputLayoutLabel.getEditText());
            return false;
        }
        if (label.length() > textInputLayoutLabel.getCounterMaxLength()) {
            textInputLayoutLabel.setError(getContext().getString(R.string.input_error_label_over_limit));
            showSoftInput(textInputLayoutLabel.getEditText());
            return false;
        }
        //判断ip地址是否符合条件
        String ip = Objects.requireNonNull(
                textInputLayoutIP.getEditText()).getText().toString();
        if (TextUtils.isEmpty(ip)) {
            textInputLayoutIP.setError(getContext().getString(R.string.input_error_empty));
            showSoftInput(textInputLayoutIP.getEditText());
            return false;
        }
        if (!Utils.isIPAddress(ip)) {
            textInputLayoutIP.setError(getContext().getString(R.string.input_error_ip_format_error));
            showSoftInput(textInputLayoutIP.getEditText());
            return false;
        }
        //判断端口号是否符合条件
        EditText editText = Objects.requireNonNull(textInputLayoutPort.getEditText());
        if (0 == editText.length()) {
            textInputLayoutPort.setError(getContext().getString(R.string.input_error_empty));
            showSoftInput(Objects.requireNonNull(textInputLayoutPort.getEditText()), 80);
            return false;
        }
        int port = Integer.parseInt(editText.getText().toString());
        if (port > 65535 || port <= 0) {
            textInputLayoutPort.setError(getContext().getString(R.string.input_error_port_over_limit));
            showSoftInput(textInputLayoutPort.getEditText(), 80);
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
        //置空当前sheet已填写的内容
        setupCreateSheet(null);
        //回调要提交的服务器信息
        if (null != listener) {
            listener.commitServerInfo(model);
        }
        return true;
    }

    /**
     * 设置默认选中
     */
    private void setDefaultLogoSelect() {
        if (serverLogoAdapter.getItemCount() > 0) {
            Map.Entry<String, Uri> entry = serverLogoAdapter.getItem(0);
            ImageUtils.showImage(getContext(), entry.getValue(), imageViewLogoSelected);
            serverLogoAdapter.setSelected(entry.getKey());
        }
    }

    @Override
    public void onItemSelected(Map.Entry<String, Uri> entry) {
        //显示选中的图片
        ImageUtils.showImage(getContext(), entry.getValue(), imageViewLogoSelected);
        //设置选中状态
        serverLogoAdapter.setSelected(entry.getKey());
    }

    @Override
    public void onShow(DialogInterface dialog) {
        showSoftInput(Objects.requireNonNull(
                textInputLayoutLabel.getEditText()), 200);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        //关闭输入法
        Utils.hideSoftInput(getContext(), textInputLayoutPort);
        //重置参数
        setupCreateSheet(null);
    }

    @OnClick({R.id.imageview_server_select_sheet_close,
            R.id.imageview_server_select_sheet_forward})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_server_select_sheet_close:
                dismiss();
                break;
            case R.id.imageview_server_select_sheet_forward:
                if (imageViewForward.isSelected()) {
                    //如果为结束模式，则关闭当前sheet
                    if (commitServerInfo()) dismiss();
                } else {
                    //切换焦点至下一个输入框
                    nextInput();
                }
                break;
        }
    }

    /**
     * 端口输入框输入动作监听
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @OnEditorAction(R.id.textinputedittext_server_select_sheet_port)
    boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //执行提交操作
        if (actionId == EditorInfo.IME_ACTION_DONE && commitServerInfo()) {
            //关闭输入法以及当前sheet
            Utils.hideSoftInput(getContext(), textInputLayoutPort);
            dismiss();
            return true;
        }
        return false;
    }

    /**
     * 焦点切换监听
     *
     * @param v
     * @param hasFocus
     */
    @OnFocusChange({R.id.textinputedittext_server_select_sheet_label,
            R.id.textinputedittext_server_select_sheet_ip,
            R.id.textinputedittext_server_select_sheet_port})
    void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) return;
        switch (v.getId()) {
            case R.id.textinputedittext_server_select_sheet_label://标签
                imageViewForward.setSelected(false);
                this.currentInputFocusIndex = 0;
                break;
            case R.id.textinputedittext_server_select_sheet_ip://ip地址
                imageViewForward.setSelected(false);
                this.currentInputFocusIndex = 1;
                break;
            case R.id.textinputedittext_server_select_sheet_port://端口号
                imageViewForward.setSelected(true);
                this.currentInputFocusIndex = 2;
                break;
        }
    }

    /**
     * 输入变化回调
     */
    private class OnTextWatcher implements TextWatcher {
        private TextInputLayout textInputLayout;

        OnTextWatcher(TextInputLayout textInputLayout) {
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
     * 监听
     */
    public interface OnCreateServerInfoSheetListener {
        void commitServerInfo(ServerInfoModel model);
    }
}