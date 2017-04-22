package com.cn.yitu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.yitu.bean.FerryCarBean;
import com.cn.yitu.ui.R;

import java.util.List;


public class FerryCarAdapter extends BaseAdapter{
	private List<FerryCarBean> list;
	private Context context;

	public FerryCarAdapter(List<FerryCarBean> list, Context context) {
		this.list = list;
		this.context = context;
	}

	public void setList(List<FerryCarBean> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.ferry_car_item, parent, false);
			holder.ferry_car_pic = (ImageView) convertView.findViewById(R.id.ferry_car_pic);
			holder.ferry_car_name = (TextView)convertView.findViewById(R.id.ferry_car_name);
			holder.ferry_car_state = (TextView)convertView.findViewById(R.id.ferry_car_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ferry_car_name.setText("摆渡车"+list.get(position).getFerry_push_id());
		holder.ferry_car_state.setText(list.get(position).getFerry_push_state());
		holder.ferry_car_pic.setBackgroundResource(R.mipmap.edc);
		return convertView;
	}
	class ViewHolder {
		ImageView ferry_car_pic;
		TextView ferry_car_name;
		TextView ferry_car_state;
	}
}
