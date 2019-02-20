package com.jtech.cloudtorrentmaster.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jtech.cloudtorrentmaster.R;
import com.jtech.cloudtorrentmaster.model.ServerInfoModel;
import com.jtechlib2.util.ImageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 服务器选择列表popup适配器
 */
public class ServerSelectListAdapter extends BaseAdapter {
    private List<ServerInfoModel> list;
    private Context context;

    public ServerSelectListAdapter(Context context) {
        this.context = context;
    }

    /**
     * 设置数据集合
     *
     * @param list
     * @return
     */
    public ServerSelectListAdapter setDatas(List<ServerInfoModel> list) {
        this.list = list;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public int getCount() {
        return null != list ? list.size() : 0;
    }

    @Override
    public ServerInfoModel getItem(int position) {
        return null != list ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return null != list ? list.get(position).getId().hashCode() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.view_server_select_popup_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ServerInfoModel model = getItem(position);
        //显示logo
        ImageUtils.showImage(context, model.getIconUri(), viewHolder.imageViewLogo);
        //设置服务器名称
        viewHolder.textViewLabel.setText(model.getLabel());
        //设置服务器地址
        viewHolder.textViewIPAddress.setText(model.getIpAddress());
        return convertView;
    }

    /**
     * 视图持有
     */
    class ViewHolder {
        @BindView(R.id.imageview_server_select_popup_logo)
        ImageView imageViewLogo;
        @BindView(R.id.textview_server_select_popup_label)
        TextView textViewLabel;
        @BindView(R.id.textview_server_select_popup_ip_address)
        TextView textViewIPAddress;

        View itemView;

        ViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}