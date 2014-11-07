package com.clame.channelmgnt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.clame.channelmgnt.bean.GoodBean;
import com.clame.channelmgnt.bean.LevelBean;
import com.clame.channelmgnt.bean.LimitBean;
import com.clame.channelmgnt.bean.SerialBean;
import com.clame.channelmgnt.communication.RequestAPIClient;
import com.clame.channelmgnt.helper.Helper;
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
public class FragmentDeliveryThree extends Fragment {

	ImageView iv_return;
	ImageView iv_user;
	TextView tv_title;
	TextView tv_msg;
	TextView tv_scan_num;
	Button btn_commit;
	ArrayList<String> flagIDList;

	String goodName;
	String goodID;
	String level;
	String name;
	String userName;
	String userLevel;
	String SerialID;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<LimitBean> limitList = new ArrayList<LimitBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	ArrayList<SerialBean> serialList = new ArrayList<SerialBean>();

	public FragmentDeliveryThree() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		Bundle bundle = getArguments();
		name = bundle.getString("name");
		level = bundle.getString("level");
		userName = bundle.getString("userName");
		userLevel = bundle.getString("userLevel");
		name = bundle.getString("name");
		goodName = bundle.getString("goodName");
		SerialID = bundle.getString("SerialID");
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		limitList = (ArrayList<LimitBean>) bundle.getSerializable("LEVELBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LIMITBEANS");
		serialList = (ArrayList<SerialBean>) bundle
				.getSerializable("SERIALBEANS");
		goodID = Helper.getGoodID(goodList, goodName);

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_delivary_three,
				container, false);

		iv_return = (ImageView) layout.findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();
				tx.remove(FragmentDeliveryThree.this);
				tx.commit();
			}
		});

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentDeliveryThree.this
						.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentDeliveryThree.this
												.getActivity().setResult(-1);// 确定按钮事件
										FragmentDeliveryThree.this
												.getActivity().finish();
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
		tv_title.setText(getResources().getText(R.string.main_title_delivery));

		tv_msg = (TextView) layout.findViewById(R.id.tv_msg);
		String msg = tv_msg.getText().toString();
		msg = msg.replace("XX", goodName);
		tv_msg.setText(msg);

		flagIDList = new ArrayList<String>();

		tv_scan_num = (TextView) layout.findViewById(R.id.tv_scan_no);

		btn_commit = (Button) layout.findViewById(R.id.btn_next);
		btn_commit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("0".equals(tv_scan_num.getText().toString())) {

					new AlertDialog.Builder(FragmentDeliveryThree.this
							.getActivity())
							.setTitle("提示")
							.setMessage("你还没有扫任何大箱标签，请继续扫描")
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

				String url = "py_r/2008";
				JSONObject del = new JSONObject();
				try {
					String idStr = "";
					for (int i = 0; i < flagIDList.size(); i++) {
						if ("".equals(idStr)) {
							idStr = idStr + flagIDList.get(i);
						} else {
							idStr = idStr + "," + flagIDList.get(i);
						}
					}
					del.put("username", userName);
					del.put("par_id", idStr);
					del.put("cv_name", name);
					del.put("expgoods", Helper.getGoodID(goodList, goodName));
					del.put("sn_id", userLevel);
					del.put("cv_id", level);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				StringEntity entity = null;
				try {
					entity = new StringEntity(del.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				RequestAPIClient.post(FragmentDeliveryThree.this.getActivity(),
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

									if (code.equals(RequestAPIClient.STATUS_FAIL)) {
										String errStr = "有商品不匹配";
										new AlertDialog.Builder(
												FragmentDeliveryThree.this
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
										String errStr = "发货成功";
										new AlertDialog.Builder(
												FragmentDeliveryThree.this
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
										FragmentDeliveryThree.this
												.getActivity())
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

	public void update(String flagID, String nfcContent) {
		String isContentOK = Helper.checkBigBoxTag(nfcContent, SerialID,
				goodID, "AA");

		if (!"SUCC".equals(isContentOK)) {
			new AlertDialog.Builder(FragmentDeliveryThree.this.getActivity())
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
		
		if (flagIDList.indexOf(flagID) < 0) {
			flagIDList.add(flagID);
			tv_scan_num.setText(String.valueOf(flagIDList.size()));
		}
	}
}
