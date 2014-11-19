package com.clame.channelmgnt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.clame.channelmgnt.bean.GoodBean;
import com.clame.channelmgnt.bean.ManagementChainBean;

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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentManagementHistoryResultDetail extends Fragment {

	ImageView iv_return;
	ImageView iv_user;
	TextView tv_title;
	ArrayList<ManagementChainBean> mgntChainList = new ArrayList<ManagementChainBean>();

	public FragmentManagementHistoryResultDetail() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		Bundle bundle = getArguments();
		mgntChainList = (ArrayList<ManagementChainBean>) bundle.getSerializable("CHAINBEANs");

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_management_historyresultdetail,
				container, false);

		iv_return = (ImageView) layout.findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        FragmentManager fm = getFragmentManager(); 
		        FragmentTransaction tx = fm.beginTransaction();
		        tx.remove(FragmentManagementHistoryResultDetail.this);
		        tx.commit();
			}
		});

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentManagementHistoryResultDetail.this.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentManagementHistoryResultDetail.this.getActivity()
												.setResult(-1);// 确定按钮事件
										FragmentManagementHistoryResultDetail.this.getActivity()
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
		tv_title.setText(getResources().getText(R.string.main_title_history));

		return layout;
	}
}
