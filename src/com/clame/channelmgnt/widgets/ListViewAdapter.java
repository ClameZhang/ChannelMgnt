package com.clame.channelmgnt.widgets;

import java.util.List;
import java.util.Map;

import com.clame.channelmgnt.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	private List<Map<String, Object>> data;
	private LayoutInflater layoutInflater;
	private Context context;

	public ListViewAdapter(Context context, List<Map<String, Object>> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(this.context);
	}

	public final class Zujian {
		public TextView tv_date;
		public TextView tv_good;
		public TextView tv_receiver;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Zujian zujian = null;
		if (convertView == null) {
			zujian = new Zujian();
			// 获得组件，实例化组件
			convertView = layoutInflater.inflate(R.layout.mylistview, null);
			zujian.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			zujian.tv_good = (TextView) convertView.findViewById(R.id.tv_good);
			zujian.tv_receiver = (TextView) convertView
					.findViewById(R.id.tv_receiver);
			convertView.setTag(zujian);
		} else {
			zujian = (Zujian) convertView.getTag();
		}
		// 绑定数据
		zujian.tv_date.setText((String) data.get(position).get("tv_date"));
		zujian.tv_good.setText((String) data.get(position).get("tv_good"));
		if (data.get(position).get("tv_receiver") == null) {
			zujian.tv_receiver.setVisibility(View.GONE);
		} else {
			zujian.tv_receiver.setText((String) data.get(position).get(
					"tv_receiver"));
		}
		return convertView;
	}
}
