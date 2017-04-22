package com.cn.yitu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.yitu.bean.SecurityBean;
import com.cn.yitu.ui.R;

import java.util.List;


public class SecurityAdapter extends BaseAdapter{
	private List<SecurityBean> list;
	private Context context;

	public SecurityAdapter(List<SecurityBean> list, Context context) {
		this.list = list;
		this.context = context;
	}

	public void setList(List<SecurityBean> list) {
		if (list != null) {
			this.list = list;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.security_item, parent, false);
			holder.security_pic = (ImageView) convertView.findViewById(R.id.security_pic);
			holder.security_name = (TextView)convertView.findViewById(R.id.security_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.security_name.setText("路线"+list.get(position).getThe_security_line_id());
		holder.security_pic.setBackgroundResource(R.mipmap.edc);
		return convertView;
	}
	class ViewHolder {
		ImageView security_pic;
		TextView security_name;
	}
}
