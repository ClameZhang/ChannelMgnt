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
import com.clame.channelmgnt.bean.UserBean;
import com.clame.channelmgnt.bean.UserInfoBean;
import com.clame.channelmgnt.communication.RequestAPIClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.clame.channelmgnt.helper.Helper;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentDeliveryOne extends Fragment {

	ImageView iv_user;
	TextView tv_title;
	Spinner spinner_condition;
	ArrayAdapter<String> adapter;
	ArrayList<String> condition1List = new ArrayList<String>();
	EditText et_condition;
	Button btn_search;
	// CheckBox checkbox;

	UserBean userBean;
	StringEntity entity;
	ArrayAdapter<String> adapter1;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<LimitBean> limitList = new ArrayList<LimitBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	ArrayList<SerialBean> serialList = new ArrayList<SerialBean>();
	ArrayList<UserInfoBean> userInfoList = new ArrayList<UserInfoBean>();
	ArrayList<String> downIDList = new ArrayList<String>();

	public FragmentDeliveryOne() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		final FragmentRecorder app = (FragmentRecorder) this.getActivity()
				.getApplication();
		app.setFragmentname("FragmentDeliveryOne");

		Bundle bundle = getArguments();
		userBean = (UserBean) bundle.getSerializable("USERBEAN");
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		limitList = (ArrayList<LimitBean>) bundle.getSerializable("LIMITBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LEVELBEANS");
		serialList = (ArrayList<SerialBean>) bundle
				.getSerializable("SERIALBEANS");
		userInfoList = (ArrayList<UserInfoBean>) bundle
				.getSerializable("USERINFOBEANS");

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_delivery_one,
				container, false);

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentDeliveryOne.this.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentDeliveryOne.this.getActivity()
												.setResult(-1);// 确定按钮事件
										FragmentDeliveryOne.this.getActivity()
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
		tv_title.setText(getResources().getText(R.string.main_title_delivery));

		spinner_condition = (Spinner) layout
				.findViewById(R.id.spinner_senior_search_condition);
		condition1List.add("授权号");
		condition1List.add("用户名");
		adapter1 = new ArrayAdapter<String>(
				FragmentDeliveryOne.this.getActivity(),
				android.R.layout.simple_spinner_item, condition1List);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_condition.setAdapter(adapter1);
		spinner_condition.setVisibility(View.VISIBLE);

		et_condition = (EditText) layout.findViewById(R.id.et_search_content);

		String url = "py_r/2015";
		JSONObject searchObj = new JSONObject();
		try {
			searchObj.put("username", userBean.getUserName());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			entity = new StringEntity(searchObj.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestAPIClient.post(FragmentDeliveryOne.this.getActivity(), url,
				entity, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						String errStr = "请求服务器失败";
						new AlertDialog.Builder(FragmentDeliveryOne.this
								.getActivity())
								.setTitle("提示")
								.setMessage(errStr)
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
								String errStr = "请求服务器失败";
								new AlertDialog.Builder(
										FragmentDeliveryOne.this.getActivity())
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
								String msg = userObj.getString("msg");
								msg = msg.substring(1);
								msg = msg.substring(0, msg.length() - 1);
								String[] idListTmp = msg.split(":");
								String ids = idListTmp[1];
								ids = ids.substring(1);
								ids = ids.substring(0, ids.length() - 1);
								String[] downIDs = ids.split(",");
								for (int i = 0; i < downIDs.length; i++) {
									if (!"".equals(downIDs[i])) {
										downIDList.add(downIDs[i]);
									}
								}
							}
						} catch (JSONException ex) {
							String errStr = "请求服务器失败";
							new AlertDialog.Builder(FragmentDeliveryOne.this
									.getActivity())
									.setTitle("提示")
									.setMessage(errStr)
									.setIcon(android.R.drawable.ic_dialog_info)
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
					}
				});

		btn_search = (Button) layout.findViewById(R.id.btn_next);
		// checkbox = (CheckBox) layout.findViewById(R.id.ckb_sendtoend);

		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String DID = "";
				String DNAME = "";
				String DLEVEL = "";

//				if (checkbox.isChecked()) {
//					DID = "8";
//					DNAME = "消费者";
//					DLEVEL = "8";
//				} else {
//					String condition = "";
//					if (spinner_condition.getSelectedItem() != null) {
//						condition = spinner_condition.getSelectedItem()
//								.toString();
//					}
//
//					String content = "";
//					if ("".equals(et_condition.getText().toString())) {
//						new AlertDialog.Builder(FragmentDeliveryOne.this
//								.getActivity())
//								.setTitle("提示")
//								.setMessage("检索内容不能为空，请输入检索内容")
//								.setIcon(R.drawable.ic_return)
//								.setPositiveButton("确定",
//										new DialogInterface.OnClickListener() {
//											public void onClick(
//													DialogInterface dialog,
//													int whichButton) {
//											}
//										}).show();
//						return;
//					} else {
//						content = et_condition.getText().toString();
//					}
//
//					if (downIDList.size() == 0) {
//						new AlertDialog.Builder(FragmentDeliveryOne.this
//								.getActivity())
//								.setTitle("提示")
//								.setMessage("从服务器获取数据失败")
//								.setIcon(R.drawable.ic_return)
//								.setPositiveButton("确定",
//										new DialogInterface.OnClickListener() {
//											public void onClick(
//													DialogInterface dialog,
//													int whichButton) {
//											}
//										}).show();
//						return;
//					}
//
//					boolean isUserExist = false;
//					if ("授权号".equals(condition)) {
//						if (downIDList.indexOf(content) > -1) {
//							isUserExist = true;
//							DID = content;
//							DNAME = Helper.getUserNameByID(userInfoList, DID);
//						}
//					} else if ("用户名".equals(condition)) {
//						DNAME = content;
//						DID = Helper.getIDByUserName(userInfoList, DNAME);
//						if (downIDList.indexOf(DID) > -1) {
//							isUserExist = true;
//						}
//					}
//					DLEVEL = Helper.getUserLevelByID(userInfoList, DID);
//
//					if (!isUserExist) {
//						new AlertDialog.Builder(FragmentDeliveryOne.this
//								.getActivity())
//								.setTitle("提示")
//								.setMessage("该用户不存在")
//								.setIcon(R.drawable.ic_return)
//								.setPositiveButton("确定",
//										new DialogInterface.OnClickListener() {
//											public void onClick(
//													DialogInterface dialog,
//													int whichButton) {
//											}
//										}).show();
//						return;
//					}
//				}
				
				String condition = "";
				if (spinner_condition.getSelectedItem() != null) {
					condition = spinner_condition.getSelectedItem().toString();
				}

				String content = "";
				if ("".equals(et_condition.getText().toString())) {
					new AlertDialog.Builder(FragmentDeliveryOne.this
							.getActivity())
							.setTitle("提示")
							.setMessage("检索内容不能为空，请输入检索内容")
							.setIcon(R.drawable.ic_return)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
										}
									}).show();
					return;
				} else {
					content = et_condition.getText().toString();
				}

				if (downIDList.size() == 0) {
					new AlertDialog.Builder(FragmentDeliveryOne.this
							.getActivity())
							.setTitle("提示")
							.setMessage("从服务器获取数据失败")
							.setIcon(R.drawable.ic_return)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
										}
									}).show();
					return;
				}

				boolean isUserExist = false;
				if ("授权号".equals(condition)) {
					if (downIDList.indexOf(content) > -1) {
						isUserExist = true;
						DID = content;
						DNAME = Helper.getUserNameByID(userInfoList, DID);
					}
				} else if ("用户名".equals(condition)) {
					DNAME = content;
					DID = Helper.getIDByUserName(userInfoList, DNAME);
					if (downIDList.indexOf(DID) > -1) {
						isUserExist = true;
					}
				}
				DLEVEL = Helper.getUserLevelByID(userInfoList, DID);

				if (!isUserExist) {
					new AlertDialog.Builder(FragmentDeliveryOne.this
							.getActivity())
							.setTitle("提示")
							.setMessage("该用户不存在")
							.setIcon(R.drawable.ic_return)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
										}
									}).show();
					return;
				}

				if (DID.equals(userBean.getUserName())) {
					new AlertDialog.Builder(FragmentDeliveryOne.this
							.getActivity())
							.setTitle("提示")
							.setMessage("不能选择自己作为发货人")
							.setIcon(R.drawable.ic_return)
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
				bundle.putString("DID", DID);
				bundle.putString("DNAME", DNAME);
				bundle.putString("DLEVEL", DLEVEL);
				bundle.putSerializable("USERBEAN", userBean);
				bundle.putSerializable("GOODBEANS", goodList);
				bundle.putSerializable("LEVELBEANS", levelList);
				bundle.putSerializable("LIMITBEANS", limitList);
				bundle.putSerializable("SERIALBEANS", serialList);

				FragmentDeliveryOneResult fResult = new FragmentDeliveryOneResult();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();
				fResult.setArguments(bundle);
				tx.add(R.id.main_details, fResult);
				tx.addToBackStack(null);
				tx.commit();
			}
		});

//		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				// TODO Auto-generated method stub
//				if (isChecked) {
//					spinner_condition.setEnabled(false);
//					et_condition.setText("");
//					et_condition.setEnabled(false);
//					btn_search.setText("下一步");
//				} else {
//					spinner_condition.setEnabled(true);
//					et_condition.setEnabled(true);
//					btn_search.setText("检索");
//				}
//			}
//		});

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
