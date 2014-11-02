package com.clame.channelmgnt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.clame.channelmgnt.communication.RequestAPIClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
import android.widget.Button;
import android.widget.ImageView;
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
	String boxID;
	ArrayList<String> idList = new ArrayList<String>();
	String userName;
	String goodID;
	String serialID;

	public FragmentPackageThree() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		Bundle bundle = getArguments();
		String goodName = bundle.getString("goodName");
		String reqCount = bundle.getString("reqCount");
		userName = bundle.getString("userName");
		serialID = bundle.getString("serialID");
		goodID = bundle.getString("goodID");
		idList = (ArrayList<String>) bundle.getSerializable("ID_LIST");

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

					new AlertDialog.Builder(FragmentPackageThree.this
							.getActivity())
							.setTitle("提示")
							.setMessage("你还没有扫描包装大箱NFC标签")
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

				String url = "py_r/2003";
				JSONObject pkg = new JSONObject();
				try {
					String idStr = "";
					for (int i = 0; i < idList.size(); i++) {
						if ("".equals(idStr)) {
							idStr = idStr + idList.get(i);
						} else {
							idStr = idStr + "," + idList.get(i);
						}
					}
					pkg.put("username", userName);
					pkg.put("son_id", idStr);
					pkg.put("par_id", boxID);
					pkg.put("expgoods", goodID);
					pkg.put("channel_id", serialID);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				StringEntity entity = null;
				try {
					entity = new StringEntity(pkg.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				RequestAPIClient.post(FragmentPackageThree.this.getActivity(),
						url, entity, new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] response) {
								if (response == null) {
									return;
								}

								String responseStr = "";
								try {
									responseStr = new String(response, "UTF-8");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								try {
									JSONTokener jsonParser = new JSONTokener(
											responseStr);
									JSONObject userObj = (JSONObject) jsonParser
											.nextValue();
									// 接下来的就是JSON对象的操作了
									String code = userObj.getString("code");
									String msg = userObj.getString("msg");

									if (code.equals(RequestAPIClient.STATUS_FAIL)) {
										String errStr = "";
										if (msg.indexOf("有商品不匹配") >= 0) {
											errStr = "有商品不匹配";
										} else {
											errStr = "有重复ID";
										}
										new AlertDialog.Builder(
												FragmentPackageThree.this
														.getActivity())
												.setTitle("提示")
												.setMessage(errStr)
												.setIcon(
														android.R.drawable.ic_dialog_info)
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
															}
														}).show();
										return;
									} else {
										String errStr = "装箱成功";
										new AlertDialog.Builder(
												FragmentPackageThree.this
														.getActivity())
												.setTitle("提示")
												.setMessage(errStr)
												.setIcon(
														android.R.drawable.ic_dialog_info)
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
															}
														}).show();
										return;
									}
								} catch (JSONException ex) {
									return;
								}
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] responseBody,
									Throwable error) {
								new AlertDialog.Builder(
										FragmentPackageThree.this.getActivity())
										.setTitle("提示")
										.setMessage("服务器请求失败")
										.setIcon(
												android.R.drawable.ic_dialog_info)
										.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
													}
												}).show();
								return;
							}
						});
			}
		});

		return layout;
	}

	public void update(String flagID) {
		if (getResources().getString(R.string.fragment_unscan).equals(
				tv_scan_status.getText().toString())) {
			tv_scan_status.setText(getResources().getString(
					R.string.fragment_scan));
			boxID = flagID;
		}
	}
}
