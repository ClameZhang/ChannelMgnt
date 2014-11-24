package com.clame.channelmgnt;

import java.util.ArrayList;

import com.clame.channelmgnt.bean.GoodBean;
import com.clame.channelmgnt.helper.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentPackageTwo extends Fragment implements OnTouchListener {

	ImageView iv_return;
	ImageView iv_user;
	TextView tv_title;
	TextView tv_msg;
	TextView tv_scan_no;
	TextView tv_unscan_no;
	Button btn_next;
	ArrayList<String> flagIDList;

	String goodName;
	String goodID;
	int reqCount;
	String userName;
	String serialID;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();

	public FragmentPackageTwo() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		final FragmentRecorder app = (FragmentRecorder)this.getActivity().getApplication();
		app.setFragmentname("FragmentPackageTwo");

		Bundle bundle = getArguments();
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		goodName = bundle.getString("goodName");
		goodID = bundle.getString("goodID");
		userName = bundle.getString("userName");
		serialID = bundle.getString("serialID");
		reqCount = Integer.parseInt(bundle.getString("goodLimit"));

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_package_two,
				container, false);

		iv_return = (ImageView) layout.findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();
				tx.remove(FragmentPackageTwo.this);
				tx.commit();
			}
		});

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentPackageTwo.this.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentPackageTwo.this.getActivity()
												.setResult(-1);// 确定按钮事件
										FragmentPackageTwo.this.getActivity()
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
		msg = msg.replace("XX", goodName);
		msg = msg.replace("AA", String.valueOf(reqCount));
		tv_msg.setText(msg);

		flagIDList = new ArrayList<String>();

		tv_scan_no = (TextView) layout.findViewById(R.id.tv_scan_no);
		tv_scan_no.setText("0");

		tv_unscan_no = (TextView) layout.findViewById(R.id.tv_unscan_no);
		tv_unscan_no.setText(String.valueOf(reqCount));

		btn_next = (Button) layout.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!"0".equals(tv_unscan_no.getText().toString())) {

					new AlertDialog.Builder(FragmentPackageTwo.this
							.getActivity())
							.setTitle("提示")
							.setMessage("你还没有扫满一箱，请继续扫描")
							.setIcon(android.R.drawable.ic_dialog_info)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
										}
									}).show();
					return;
				}

				Bundle bundle = new Bundle();
				bundle.putSerializable("GOODBEANS", goodList);
				bundle.putString("goodName", goodName);
				bundle.putString("goodID", goodID);
				bundle.putString("reqCount", String.valueOf(reqCount));
				bundle.putString("userName", userName);
				bundle.putString("serialID", serialID);
				bundle.putSerializable("ID_LIST", flagIDList);
				FragmentPackageThree fThree = new FragmentPackageThree();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();
				fThree.setArguments(bundle);
				tx.add(R.id.main_details, fThree, "FragmentPackageThree");
				tx.addToBackStack(null);
				tx.commit();
			}
		});

		return layout;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view.setOnTouchListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	public void update(String flagID, String nfcContent) {
		String isContentOK = Helper.checkDelSmallBoxTag(nfcContent);

		if (!"SUCC".equals(isContentOK)) {
			new AlertDialog.Builder(FragmentPackageTwo.this.getActivity())
					.setTitle("提示")
					.setMessage(isContentOK)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).show();
			return;
		}

		int unscanCount = Integer.parseInt(tv_unscan_no.getText().toString());
		if (unscanCount == 0) {
			new AlertDialog.Builder(FragmentPackageTwo.this.getActivity())
					.setTitle("提示")
					.setMessage("请点击下一步")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).show();
			return;
		}

		if (flagIDList.indexOf(flagID) < 0 && unscanCount > 0) {
			flagIDList.add(flagID);
			tv_scan_no.setText(String.valueOf(flagIDList.size()));
			tv_unscan_no.setText(String.valueOf(reqCount - flagIDList.size()));
		}
	}
}
