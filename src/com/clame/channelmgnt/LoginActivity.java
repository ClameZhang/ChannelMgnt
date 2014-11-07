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

import android.R.id;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	EditText userName;
	EditText password;
	TextView errorInfo;
	Button btnLogin;
	String sUN = "";
	UserBean userBean = new UserBean();
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<LimitBean> limitList = new ArrayList<LimitBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	ArrayList<SerialBean> serialList = new ArrayList<SerialBean>();
	boolean isGoodBean = false;
	boolean isLevelBean = false;
	boolean isLimitBean = false;
	boolean isSerialBean = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		userName = (EditText) findViewById(R.id.tv_content_name);
		password = (EditText) findViewById(R.id.tv_content_password);
		errorInfo = (TextView) findViewById(R.id.tv_content_errorinfo);
		btnLogin = (Button) findViewById(R.id.btn_login);

		if (!Helper.isNetworkConnected(LoginActivity.this)) {
			errorInfo.setText(getResources()
					.getString(R.string.error_nonetwork));
			return;
		}

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				errorInfo.setText("");

				// TODO Auto-generated method stub
				sUN = userName.getText().toString();
				if ("".equals(sUN)) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_no_username));
					return;
				}

				String sPWD = Helper.md5(password.getText().toString());
				if ("".equals(sPWD)) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_no_password));
					return;
				}

				userBean.setUserName(sUN);
				userBean.setPassword(sPWD);

				String url = "py_w/2001";
				JSONObject login = new JSONObject();
				String ip = "";
				if (Helper.getLocalIpAddress(LoginActivity.this) != null
						&& !"".equals(Helper
								.getLocalIpAddress(LoginActivity.this))) {
					ip = Helper.getLocalIpAddress(LoginActivity.this);
				} else {
					ip = Helper.getIpAddress();
				}
				String os = Helper.getSystemVersion();
				try {
					login.put("username", sUN);
					login.put("passwd", sPWD);
					login.put("ip", ip);
					login.put("os", os);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				StringEntity entity = null;
				try {
					entity = new StringEntity(login.toString());
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				RequestAPIClient.post(LoginActivity.this, url, entity,
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

									while (true) {
										if (isGoodBean & isSerialBean
												& isLevelBean && isLimitBean) {
											break;
										}
									}

									if (userBean.isInit) {
										Intent initIntent = new Intent(
												LoginActivity.this,
												InitActivity.class);
										initIntent.putExtra("name", sUN);

										Bundle bundle = new Bundle();
										bundle.putSerializable("GOODBEANS",
												goodList);
										bundle.putSerializable("LEVELBEANS",
												levelList);
										bundle.putSerializable("LIMITBEANS",
												limitList);
										bundle.putSerializable("SERIALBEANS",
												serialList);
										initIntent.putExtra("BUNDLE", bundle);

										LoginActivity.this
												.startActivity(initIntent);
									} else {
										JSONTokener beanParser = new JSONTokener(
												msg);
										JSONObject beanObj = (JSONObject) beanParser
												.nextValue();
										String uLevel = beanObj
												.getString("ulevel");
										String serialID = beanObj
												.getString("s_id");
										userBean.setuLevel(uLevel);
										userBean.setSerialID(serialID);

										Intent mainIntent = new Intent(
												LoginActivity.this,
												MainActivity.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("USERBEAN",
												userBean);
										bundle.putSerializable("GOODBEANS",
												goodList);
										bundle.putSerializable("LEVELBEANS",
												levelList);
										bundle.putSerializable("LIMITBEANS",
												limitList);
										bundle.putSerializable("SERIALBEANS",
												serialList);
										mainIntent.putExtra("BUNDLE", bundle);
										LoginActivity.this
												.startActivity(mainIntent);
									}
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

		String url2 = "py_r/2001/GETGOODSBASE";
		RequestAPIClient.get(url2, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] response) {
				if (response == null) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					return;
				}

				String responseStr = "";
				try {
					responseStr = new String(response, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated
					// catch block
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					e.printStackTrace();
				}

				try {
					JSONTokener jsonParser = new JSONTokener(responseStr);
					JSONObject userObj = (JSONObject) jsonParser.nextValue();
					// 接下来的就是JSON对象的操作了
					String code = userObj.getString("code");
					String msg = userObj.getString("msg");

					if (code.equals(RequestAPIClient.STATUS_FAIL)) {
						errorInfo.setText(msg);
						return;
					}

					// {"04":"白金橙花匀亮修护隐形面膜","01":"酵素","02":"乳酸菌","03":"牛樟菇"}
					msg = msg.substring(1);
					msg = msg.substring(0, msg.length() - 1);
					String[] goodsListTmp = msg.split(",");
					for (int i = 0; i < goodsListTmp.length; i++) {
						GoodBean goodBean = new GoodBean();
						String[] info = goodsListTmp[i].split(":");
						String ID = info[0];
						ID = ID.substring(1);
						ID = ID.substring(0, ID.length() - 1);
						String Name = info[1];
						Name = Name.substring(1);
						Name = Name.substring(0, Name.length() - 1);
						goodBean.setgID(ID);
						goodBean.setgName(Name);
						goodList.add(goodBean);
					}

					isGoodBean = true;
				} catch (JSONException ex) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					return;
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				errorInfo.setText(getResources().getString(
						R.string.error_server));
				return;
			}
		});

		String url3 = "py_r/2001/GETSERIASID";
		RequestAPIClient.get(url3, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] response) {
				if (response == null) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					return;
				}

				String responseStr = "";
				try {
					responseStr = new String(response, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated
					// catch block
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					e.printStackTrace();
				}

				try {
					JSONTokener jsonParser = new JSONTokener(responseStr);
					JSONObject userObj = (JSONObject) jsonParser.nextValue();
					// 接下来的就是JSON对象的操作了
					String code = userObj.getString("code");
					String msg = userObj.getString("msg");

					if (code.equals(RequestAPIClient.STATUS_FAIL)) {
						errorInfo.setText(msg);
						return;
					}

					// {"A2":"米亚妮亚","A1":"一三一素"}
					msg = msg.substring(1);
					msg = msg.substring(0, msg.length() - 1);
					String[] serialsListTmp = msg.split(",");
					for (int i = 0; i < serialsListTmp.length; i++) {
						SerialBean serialBean = new SerialBean();
						String[] info = serialsListTmp[i].split(":");
						String ID = info[0];
						ID = ID.substring(1);
						ID = ID.substring(0, ID.length() - 1);
						String Name = info[1];
						Name = Name.substring(1);
						Name = Name.substring(0, Name.length() - 1);						
						serialBean.setsID(ID);
						serialBean.setsName(Name);						
						serialList.add(serialBean);
					}

					isSerialBean = true;
				} catch (JSONException ex) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					return;
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				errorInfo.setText(getResources().getString(
						R.string.error_server));
				return;
			}
		});

		String url4 = "py_r/2001/GETLEVEL";
		RequestAPIClient.get(url4, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] response) {
				if (response == null) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					return;
				}

				String responseStr = "";
				try {
					responseStr = new String(response, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated
					// catch block
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					e.printStackTrace();
				}

				try {
					JSONTokener jsonParser = new JSONTokener(responseStr);
					JSONObject userObj = (JSONObject) jsonParser.nextValue();
					// 接下来的就是JSON对象的操作了
					String code = userObj.getString("code");
					String msg = userObj.getString("msg");

					if (code.equals(RequestAPIClient.STATUS_FAIL)) {
						errorInfo.setText(msg);
						return;
					}

					// {"0":"呈煌","1":"生产商装箱员","2":"生产商发货员","3":"省级代理","4":"一级代理商","5":"二级代理商","6":"正品销售商","7":"正品销售商3"}
					msg = msg.substring(1);
					msg = msg.substring(0, msg.length() - 1);
					String[] levelListTmp = msg.split(",");
					for (int i = 0; i < levelListTmp.length; i++) {
						LevelBean levelBean = new LevelBean();
						String[] info = levelListTmp[i].split(":");
						String ID = info[0];
						ID = ID.substring(1);
						ID = ID.substring(0, ID.length() - 1);
						String Name = info[1];
						Name = Name.substring(1);
						Name = Name.substring(0, Name.length() - 1);					
						levelBean.setlID(ID);
						levelBean.setlName(Name);
						levelList.add(levelBean);
					}

					isLevelBean = true;
				} catch (JSONException ex) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					return;
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				errorInfo.setText(getResources().getString(
						R.string.error_server));
				return;
			}
		});

		String url5 = "py_r/2001/GETLIMIT";
		RequestAPIClient.get(url5, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] response) {
				if (response == null) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					return;
				}

				String responseStr = "";
				try {
					responseStr = new String(response, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated
					// catch block
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					e.printStackTrace();
				}

				try {
					JSONTokener jsonParser = new JSONTokener(responseStr);
					JSONObject userObj = (JSONObject) jsonParser.nextValue();
					// 接下来的就是JSON对象的操作了
					String code = userObj.getString("code");
					String msg = userObj.getString("msg");

					if (code.equals(RequestAPIClient.STATUS_FAIL)) {
						errorInfo.setText(msg);
						return;
					}

					// {"01":"20","02":"20","03":"20","04":"54"}
					msg = msg.substring(1);
					msg = msg.substring(0, msg.length() - 1);
					String[] limitListTmp = msg.split(",");
					for (int i = 0; i < limitListTmp.length; i++) {
						LimitBean limitBean = new LimitBean();
						String[] info = limitListTmp[i].split(":");
						String ID = info[0];
						ID = ID.substring(1);
						ID = ID.substring(0, ID.length() - 1);
						String Num = info[1];
						Num = Num.substring(1);
						Num = Num.substring(0, Num.length() - 1);
						limitBean.setlID(ID);
						limitBean.setlNum(Num);
						limitList.add(limitBean);
					}

					isLimitBean = true;
				} catch (JSONException ex) {
					errorInfo.setText(getResources().getString(
							R.string.login_error_request));
					return;
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				errorInfo.setText(getResources().getString(
						R.string.error_server));
				return;
			}
		});
	}
}
