package com.clame.channelmgnt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.clame.channelmgnt.bean.GoodBean;
import com.clame.channelmgnt.bean.LevelBean;
import com.clame.channelmgnt.bean.LimitBean;
import com.clame.channelmgnt.bean.SerialBean;
import com.clame.channelmgnt.bean.UserBean;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentDeliveryOneNormal extends Fragment {

	ImageView iv_user;
	TextView tv_title;
	Spinner spinner_level;
	Spinner spinner_name;
	ArrayAdapter<String> adapter;
	ArrayList<String> level1List;
	ArrayList<String> nameList;
	Button btn_next;
	UserBean userBean;
	StringEntity entity;
	ArrayAdapter<String> adapter1;
	ArrayAdapter<String> adapter2;
	String name;
	String level;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<LimitBean> limitList = new ArrayList<LimitBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	ArrayList<SerialBean> serialList = new ArrayList<SerialBean>();

	public FragmentDeliveryOneNormal() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		Bundle bundle = getArguments();
		userBean = (UserBean) bundle.getSerializable("USERBEAN");
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		limitList = (ArrayList<LimitBean>) bundle.getSerializable("LEVELBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LIMITBEANS");
		serialList = (ArrayList<SerialBean>) bundle
				.getSerializable("SERIALBEANS");

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_delivery_one_normal,
				container, false);

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentDeliveryOneNormal.this
						.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentDeliveryOneNormal.this
												.getActivity().setResult(-1);// 确定按钮事件
										FragmentDeliveryOneNormal.this
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

		spinner_level = (Spinner) layout
				.findViewById(R.id.spinner_senior_name1);

		spinner_name = (Spinner) layout.findViewById(R.id.spinner_senior_name2);

		String url = "py_r/2005";
		JSONObject userObj = new JSONObject();
		try {
			userObj.put("username", userBean.getUserName());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			entity = new StringEntity(userObj.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestAPIClient.post(FragmentDeliveryOneNormal.this.getActivity(),
				url, entity, new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
						String errStr = "请求服务器失败";
						new AlertDialog.Builder(FragmentDeliveryOneNormal.this
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
										FragmentDeliveryOneNormal.this
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
								JSONArray downArray = userObj
										.getJSONArray("msg");

								level1List = new ArrayList<String>();

								for (int i = 0; i < downArray.length(); i++) {
									JSONObject oj = downArray.getJSONObject(i);
									String downName = Helper.getLevelName(levelList, oj.getString("down_id"));
									level1List.add(downName);
								}

								adapter1 = new ArrayAdapter<String>(
										FragmentDeliveryOneNormal.this
												.getActivity(),
										android.R.layout.simple_spinner_item,
										level1List);
								adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
								spinner_level.setAdapter(adapter1);
								spinner_level.setVisibility(View.VISIBLE);

								spinner_level
										.setOnItemSelectedListener(new OnItemSelectedListener() {

											@Override
											public void onItemSelected(
													AdapterView<?> arg0,
													View arg1, int arg2,
													long arg3) {
												// TODO Auto-generated method
												// stub
												String url = "py_r/2013";
												JSONObject userObj = new JSONObject();
												try {

													userObj.put(
															"username",
															userBean.getUserName());
													userObj.put(
															"down_id",
															Helper.getLevelIDByName(levelList, adapter1.getItem(arg2)));
												} catch (JSONException e1) {
													// TODO Auto-generated catch
													// block
													e1.printStackTrace();
												}

												try {
													entity = new StringEntity(
															userObj.toString());
												} catch (UnsupportedEncodingException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
												RequestAPIClient
														.post(FragmentDeliveryOneNormal.this
																.getActivity(),
																url,
																entity,
																new AsyncHttpResponseHandler() {

																	@Override
																	public void onSuccess(
																			int arg0,
																			Header[] arg1,
																			byte[] response) {
																		if (response == null) {
																			return;
																		}

																		String responseStr = "";
																		try {
																			responseStr = new String(
																					response,
																					"UTF-8");
																		} catch (UnsupportedEncodingException e) {
																			// TODO
																			// Auto-generated
																			// catch
																			// block
																			e.printStackTrace();
																		}

																		try {
																			JSONTokener jsonParser = new JSONTokener(
																					responseStr);
																			JSONObject userObj = (JSONObject) jsonParser
																					.nextValue();
																			// 接下来的就是JSON对象的操作了
																			String code = userObj
																					.getString("code");

																			if (code.equals(RequestAPIClient.STATUS_FAIL)) {
																				String errStr = "请求服务器失败";
																				new AlertDialog.Builder(
																						FragmentDeliveryOneNormal.this
																								.getActivity())
																						.setTitle(
																								"提示")
																						.setMessage(
																								errStr)
																						.setIcon(
																								android.R.drawable.ic_dialog_info)
																						.setPositiveButton(
																								"确定",
																								new DialogInterface.OnClickListener() {
																									public void onClick(
																											DialogInterface dialog,
																											int whichButton) {
																									}
																								})
																						.show();
																				return;
																			} else {
																				JSONArray downArray = userObj
																						.getJSONArray("msg");

																				nameList = new ArrayList<String>();

																				for (int i = 0; i < downArray
																						.length(); i++) {
																					JSONObject oj = downArray
																							.getJSONObject(i);
																					nameList
																							.add(oj.getString("down_name"));
																				}

																				adapter2 = new ArrayAdapter<String>(
																						FragmentDeliveryOneNormal.this
																								.getActivity(),
																						android.R.layout.simple_spinner_item,
																						nameList);
																				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
																				spinner_name
																						.setAdapter(adapter2);
																				spinner_name
																						.setOnItemSelectedListener(new SpinnerSelectedListener());
																				spinner_name
																						.setVisibility(View.VISIBLE);
																			}
																		} catch (JSONException ex) {
																			String errStr = "请求服务器失败";
																			new AlertDialog.Builder(
																					FragmentDeliveryOneNormal.this
																							.getActivity())
																					.setTitle(
																							"提示")
																					.setMessage(
																							errStr)
																					.setIcon(
																							android.R.drawable.ic_dialog_info)
																					.setPositiveButton(
																							"确定",
																							new DialogInterface.OnClickListener() {
																								public void onClick(
																										DialogInterface dialog,
																										int whichButton) {
																								}
																							})
																					.show();
																			return;
																		}
																	}

																	@Override
																	public void onFailure(
																			int statusCode,
																			Header[] headers,
																			byte[] responseBody,
																			Throwable error) {
																		String errStr = "请求服务器失败";
																		new AlertDialog.Builder(
																				FragmentDeliveryOneNormal.this
																						.getActivity())
																				.setTitle(
																						"提示")
																				.setMessage(
																						errStr)
																				.setIcon(
																						android.R.drawable.ic_dialog_info)
																				.setPositiveButton(
																						"确定",
																						new DialogInterface.OnClickListener() {
																							public void onClick(
																									DialogInterface dialog,
																									int whichButton) {
																							}
																						})
																				.show();
																		return;
																	}
																});
											}

											@Override
											public void onNothingSelected(
													AdapterView<?> arg0) {
												// TODO Auto-generated method
												// stub
											}
										});
							}
						} catch (JSONException ex) {
							String errStr = "请求服务器失败";
							new AlertDialog.Builder(
									FragmentDeliveryOneNormal.this
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

		btn_next = (Button) layout.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = "";
				String level = "";
				if (spinner_name.getSelectedItem() != null) {
					name = spinner_name.getSelectedItem().toString();
				}
				if (spinner_level.getSelectedItem() != null) {
					level = Helper.getLevelIDByName(levelList, spinner_level.getSelectedItem().toString());
				}
				Bundle bundle = new Bundle();
				bundle.putString("name", name);
				bundle.putString("level", level);
				bundle.putString("userName", userBean.getUserName());
				bundle.putString("userLevel", userBean.getuLevel());
				bundle.putString("SerialID", userBean.getSerialID());
				bundle.putSerializable("GOODBEANS", goodList);			
				bundle.putSerializable("LEVELBEANS", levelList);
				bundle.putSerializable("LIMITBEANS", limitList);
				bundle.putSerializable("SERIALBEANS", serialList);
				
				FragmentDeliveryTwo fTwo = new FragmentDeliveryTwo();
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
