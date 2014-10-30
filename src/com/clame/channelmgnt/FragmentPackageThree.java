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
public class FragmentPackageThree extends Fragment {

	ImageView iv_return;
	ImageView iv_user;
	TextView tv_title;
	TextView tv_msg;
	TextView tv_scan_status;
	Button btn_commit;

	public FragmentPackageThree() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		Bundle bundle = getArguments();
		String goodName = bundle.getString("goodName");
		String reqCount = bundle.getString("reqCount");;

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_package_three,
				container, false);

		iv_return = (ImageView) layout.findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        FragmentManager fm = getFragmentManager(); 
		        FragmentTransaction tx = fm.beginTransaction();
		        tx.remove(FragmentPackageThree.this);
		        tx.commit();
			}
		});

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentPackageThree.this.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentPackageThree.this.getActivity()
												.setResult(-1);// 确定按钮事件
										FragmentPackageThree.this.getActivity()
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
		
		tv_msg = (TextView) layout.findViewById(R.id.tv_msg);
		String msg = tv_msg.getText().toString();
		int a = msg.indexOf("XX");
		int b = msg.indexOf("20");
		msg = msg.replace("XX", goodName);
		msg = msg.replace("AA", String.valueOf(reqCount));
		tv_msg.setText(msg);

		tv_scan_status = (TextView) layout.findViewById(R.id.tv_scan_status);
		
		btn_commit = (Button) layout.findViewById(R.id.btn_next);
		btn_commit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("未扫描".equals(tv_scan_status.getText().toString())) {

					new AlertDialog.Builder(FragmentPackageThree.this.getActivity())
							.setTitle("提示")
							.setMessage("你还没有扫描包装大箱NFC标签")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int whichButton) {
										}
									}).show();
					return;
				}
			}
		});

		return layout;
	}
}
