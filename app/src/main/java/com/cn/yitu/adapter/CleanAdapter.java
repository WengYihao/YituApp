package com.cn.yitu.adapter;

import java.util.List;

import com.cn.yitu.bean.CleanBean;
import com.cn.yitu.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CleanAdapter extends BaseAdapter{
	private List<CleanBean> list;
	private Context context;
	
	public CleanAdapter(List<CleanBean> list, Context context) {
		this.list = list;
		this.context = context;
	}

	public void setList(List<CleanBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.clean_item, parent, false);
			holder.clean_pic = (ImageView) convertView.findViewById(R.id.clean_pic);
			holder.clean_name = (TextView)convertView.findViewById(R.id.clean_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.clean_name.setText("区域"+list.get(position).getCleaning_area_id());
		holder.clean_pic.setBackgroundResource(R.mipmap.edc);
		return convertView;
	}
	class ViewHolder {
		ImageView clean_pic;
		TextView clean_name;
	}
}
