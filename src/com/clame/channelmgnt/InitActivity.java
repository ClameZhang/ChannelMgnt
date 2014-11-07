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
import com.clame.channelmgnt.communication.RequestAPIClient;
import com.clame.channelmgnt.helper.Helper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InitActivity extends Activity {
	EditText newPwd;
	EditText confirmPwd;
	TextView errorInfo;
	Button btnConfirm;
	String name = "";
	boolean isLogin = false;
	UserBean userBean = new UserBean();
	String url = "py_w/2003";
	StringEntity entity = null;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<LimitBean> limitList = new ArrayList<LimitBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	ArrayList<SerialBean> serialList = new ArrayList<SerialBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);

		Intent i = getIntent();
		name = i.getStringExtra("name");
		userBean.setUserName(name);
		Bundle bundle = i.getBundleExtra("BUNDLE");
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		limitList = (ArrayList<LimitBean>) bundle.getSerializable("LEVELBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LIMITBEANS");
		serialList = (ArrayList<SerialBean>) bundle
				.getSerializable("SERIALBEANS");

		newPwd = (EditText) findViewById(R.id.tv_content_pwd);
		confirmPwd = (EditText) findViewById(R.id.tv_content_confirm);
		errorInfo = (TextView) findViewById(R.id.tv_content_errorinfo);
		btnConfirm = (Button) findViewById(R.id.btn_confirm);

		if (!Helper.isNetworkConnected(InitActivity.this)) {
			errorInfo.setText(getResources()
					.getString(R.string.error_nonetwork));
			return;
		}

		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorInfo.setText("");

				// TODO Auto-generated method stub
				String sPWD = newPwd.getText().toString();
				if ("".equals(sPWD)) {
					errorInfo.setText(getResources().getString(
							R.string.init_error_no_password));
					return;
				}

				if (sPWD.length() < 6) {
					errorInfo.setText(getResources().getString(
							R.string.init_error_wrong_password));
					return;
				}

				String sConfirm = confirmPwd.getText().toString();
				if ("".equals(sConfirm) || !sConfirm.equals(sPWD)) {
					errorInfo.setText(getResources().getString(
							R.string.init_error_unmatch_password));
					return;
				}

				sPWD = Helper.md5(sConfirm);

				userBean.setPassword(sPWD);

				JSONObject init = new JSONObject();
				String ip = "";
				if (Helper.getLocalIpAddress(InitActivity.this) != null
						&& !"".equals(Helper
								.getLocalIpAddress(InitActivity.this))) {
					ip = Helper.getLocalIpAddress(InitActivity.this);
				} else {
					ip = Helper.getIpAddress();
				}
				String os = Helper.getSystemVersion();
				try {
					init.put("username", name);
					init.put("passwd", sPWD);
					init.put("ip", ip);
					init.put("os", os);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					entity = new StringEntity(init.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				RequestAPIClient.post(InitActivity.this, url, entity,
						new AsyncHttpResponseHandler() {

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] response) {
								if (response == null) {
									errorInfo.setText(getResources().getString(
											R.string.login_error_request));
									return;
								}

								String responseStr = "";
								try {
									responseStr = new String(response, "UTF-8");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									errorInfo.setText(getResources().getString(
											R.string.login_error_request));
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
										errorInfo.setText(msg);
										return;
									} else {
										isLogin = true;
									}
									
									if (!isLogin) {
										return;
									}

									String url2 = "py_w/2001";
									RequestAPIClient.post(InitActivity.this, url2, entity,
											new AsyncHttpResponseHandler() {

												@Override
												public void onSuccess(int arg0, Header[] arg1,
														byte[] response) {
													if (response == null) {
														errorInfo.setText(getResources().getString(
																R.string.login_error_request));
														return;
													}

													String responseStr = "";
													try {
														responseStr = new String(response, "UTF-8");
													} catch (UnsupportedEncodingException e) {
														// TODO Auto-generated catch block
														errorInfo.setText(getResources().getString(
																R.string.login_error_request));
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
															if (!msg.equals(getResources()
																	.getString(
																			R.string.login_error_init))) {
																errorInfo.setText(msg);
																return;
															} else {
																userBean.setInit(true);
															}
														}

														JSONTokener beanParser = new JSONTokener(
																msg);
														JSONObject beanObj = (JSONObject) beanParser
																.nextValue();
														String uLevel = beanObj.getString("ulevel");
														String serialID = beanObj.getString("s_id");
														userBean.setuLevel(uLevel);
														userBean.setSerialID(serialID);

														Intent mainIntent = new Intent(
																InitActivity.this,
																MainActivity.class);
														Bundle bundle = new Bundle();
														bundle.putSerializable("USERBEAN", userBean);
														bundle.putSerializable("GOODBEANS",
																goodList);
														bundle.putSerializable("LEVELBEANS",
																levelList);
														bundle.putSerializable("LIMITBEANS",
																limitList);
														bundle.putSerializable("SERIALBEANS",
																serialList);
														mainIntent.putExtra("BUNDLE", bundle);
														InitActivity.this.startActivity(mainIntent);													
													} catch (JSONException ex) {
														errorInfo.setText(getResources().getString(
																R.string.login_error_request));
														return;
													}
												}

												@Override
												public void onFailure(int statusCode,
														Header[] headers, byte[] responseBody,
														Throwable error) {
													errorInfo.setText(getResources().getString(
															R.string.error_server));
													return;
												}
											});
								} catch (JSONException ex) {
									errorInfo.setText(getResources().getString(
											R.string.login_error_request));
									return;
								}
							}

							@Override
							public void onFailure(int statusCode,
									Header[] headers, byte[] responseBody,
									Throwable error) {
								errorInfo.setText(getResources().getString(
										R.string.error_server));
								return;
							}
						});
			}
		});
	}
}
