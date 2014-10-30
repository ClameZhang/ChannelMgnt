package com.clame.channelmgnt;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentPackageOne extends Fragment {

	ImageView iv_user;
	TextView tv_title;
	Spinner spinner_goods;
	ArrayAdapter<String> adapter;
	ArrayList<String> goodsList;
	Button btn_next;

	public FragmentPackageOne() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_package_one,
				container, false);

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentPackageOne.this.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentPackageOne.this.getActivity()
												.setResult(-1);// 确定按钮事件
										FragmentPackageOne.this.getActivity()
												.finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// 取消按钮事件
									}
								}).show();
			}
		});

		tv_title = (TextView) layout.findViewById(R.id.tv_title);
		tv_title.setText(getResources().getText(R.string.main_title_package));

		spinner_goods = (Spinner) layout.findViewById(R.id.spinner_goods);
		goodsList = new ArrayList<String>();
		goodsList.add("酵素");
		goodsList.add("乳酸菌");
		goodsList.add("牛樟菇");
		adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, goodsList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_goods.setAdapter(adapter);
		spinner_goods.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner_goods.setVisibility(View.VISIBLE);
		
		btn_next = (Button) layout.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String goodName = spinner_goods.getSelectedItem().toString();
				Bundle bundle = new Bundle();  
                bundle.putString("goodName", goodName);  
				FragmentPackageTwo fTwo = new FragmentPackageTwo();  
		        FragmentManager fm = getFragmentManager();  
		        FragmentTransaction tx = fm.beginTransaction();  
		        fTwo.setArguments(bundle);
		        tx.add(R.id.main_details, fTwo);  
		        tx.addToBackStack(null);  
		        tx.commit(); 
			}
		});

		return layout;
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
