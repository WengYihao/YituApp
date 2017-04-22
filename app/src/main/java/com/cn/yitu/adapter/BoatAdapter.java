package com.cn.yitu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.yitu.bean.BoatBean;
import com.cn.yitu.ui.R;

import java.util.List;


public class BoatAdapter extends BaseAdapter{
	private List<BoatBean> list;
	private Context context;

	public BoatAdapter(List<BoatBean> list, Context context) {
		this.list = list;
		this.context = context;
	}

	public void setList(List<BoatBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.boat_item, parent, false);
			holder.boat_pic = (ImageView) convertView.findViewById(R.id.boat_pic);
			holder.boat_name = (TextView)convertView.findViewById(R.id.boat_name);
			holder.boat_state = (TextView)convertView.findViewById(R.id.boat_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.boat_name.setText("游船"+list.get(position).getPleasure_boat_id());
		holder.boat_state.setText(list.get(position).getPleasure_boat_state());
		holder.boat_pic.setBackgroundResource(R.mipmap.edc);
		return convertView;
	}
	class ViewHolder {
		ImageView boat_pic;
		TextView boat_name;
		TextView boat_state;
	}
}
