package com.clame.channelmgnt;

import java.util.ArrayList;
import java.util.Calendar;

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
public class FragmentDeliveryHistory extends Fragment {

	ImageView iv_user;
	TextView tv_title;
	DatePicker dp_start;
	DatePicker dp_end;
	Button btn_search;

	public FragmentDeliveryHistory() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_delivery_history,
				container, false);

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentDeliveryHistory.this.getActivity())
						.setTitle("��ʾ")
						.setMessage("ȷ���˳�?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentDeliveryHistory.this.getActivity()
												.setResult(-1);// ȷ����ť�¼�
										FragmentDeliveryHistory.this.getActivity()
												.finish();
									}
								})
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// ȡ����ť�¼�
									}
								}).show();
			}
		});

		tv_title = (TextView) layout.findViewById(R.id.tv_title);
		tv_title.setText(getResources().getText(R.string.main_title_history));
		
		final Calendar now = Calendar.getInstance();
		int mYear = now.get(Calendar.YEAR);
		int mMonth = now.get(Calendar.MONTH);
		int mDay = now.get(Calendar.DAY_OF_MONTH);
		
		dp_start = (DatePicker) layout.findViewById(R.id.dp_start);
		dp_start.init(mYear, mMonth, mDay, null);
		
		dp_end = (DatePicker) layout.findViewById(R.id.dp_end);
		dp_end.init(mYear, mMonth, mDay, null);
		
		btn_search = (Button) layout.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int dayStart = dp_start.getDayOfMonth();
				int monthStart = dp_start.getMonth() + 1;
				int yearStart = dp_start.getYear();
				Calendar start = Calendar.getInstance();
				start.set(yearStart, monthStart, dayStart);
				
				int dayEnd = dp_end.getDayOfMonth();
				int monthEnd = dp_end.getMonth() + 1;
				int yearEnd = dp_end.getYear();
				Calendar end = Calendar.getInstance();
				end.set(yearEnd, monthEnd, dayEnd);
				
				if (start.compareTo(end) > 0) {
					new AlertDialog.Builder(FragmentDeliveryHistory.this.getActivity())
					.setTitle("����")
					.setMessage("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣡")
					.setIcon(android.R.drawable.ic_secure)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {}
							}).show();
					return;
				}
				
				Bundle bundle = new Bundle();
				bundle.putString("start", String.valueOf(yearStart) + "," + String.valueOf(monthStart) + "," + String.valueOf(dayStart));
				bundle.putString("end", String.valueOf(yearEnd) + "," + String.valueOf(monthEnd) + "," + String.valueOf(dayEnd));				
				FragmentDeliveryHistoryResult fHistoryResult = new FragmentDeliveryHistoryResult();  
		        FragmentManager fm = getFragmentManager();  
		        FragmentTransaction tx = fm.beginTransaction();  
		        fHistoryResult.setArguments(bundle);
		        tx.add(R.id.main_details, fHistoryResult);  
		        tx.addToBackStack(null);  
		        tx.commit(); 
			}
		});

		return layout;
	}
}