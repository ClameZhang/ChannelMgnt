package com.clame.channelmgnt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.clame.channelmgnt.bean.LevelBean;
import com.clame.channelmgnt.bean.ManagementChainBean;
import com.clame.channelmgnt.bean.UserBean;
import com.clame.channelmgnt.bean.UserInfoBean;
import com.clame.channelmgnt.communication.RequestAPIClient;
import com.clame.channelmgnt.helper.Helper;
import com.clame.channelmgnt.widgets.ListViewAdapter;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentManagementHistoryResult extends Fragment {

	ImageView iv_return;
	ImageView iv_user;
	TextView tv_title;
	ListView listView;
	ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	UserBean userBean;
	ArrayList<UserInfoBean> userInfoList = new ArrayList<UserInfoBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	ArrayList<ManagementChainBean> mgntChainList = new ArrayList<ManagementChainBean>();

	public FragmentManagementHistoryResult() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		Bundle bundle = getArguments();
		list = (ArrayList<Map<String, Object>>) bundle
				.getSerializable("historylist");
		userInfoList = (ArrayList<UserInfoBean>) bundle
				.getSerializable("USERINFOBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LEVELBEANS");
		userBean = (UserBean) bundle.getSerializable("USERBEAN");

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_management_historyresult,
				container, false);

		iv_return = (ImageView) layout.findViewById(R.id.iv_return);
		iv_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        FragmentManager fm = getFragmentManager(); 
		        FragmentTransaction tx = fm.beginTransaction();
		        tx.remove(FragmentManagementHistoryResult.this);
		        tx.commit();
			}
		});

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentManagementHistoryResult.this.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentManagementHistoryResult.this.getActivity()
												.setResult(-1);// 确定按钮事件
										FragmentManagementHistoryResult.this.getActivity()
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

		listView = (ListView) layout.findViewById(R.id.list_history);
		List<Map<String, Object>> listData = list;
		listView.setAdapter(new ListViewAdapter(this.getActivity(), listData));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Map<String, Object> obj = list.get(arg2);
				String flagIDStr = obj.get("tv_nfc").toString();

				flagIDStr = "048FCBFA463D80";
				String url = "py_r/2018";
				JSONObject pkg = new JSONObject();
				try {
					pkg.put("username", userBean.getUserName());
					pkg.put("stime", "2014-01-01 12:00:00");
					pkg.put("etime", "9999-11-30 12:00:00");
					pkg.put("par_id", flagIDStr);
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

				RequestAPIClient.post(FragmentManagementHistoryResult.this.getActivity(), url,
						entity, new AsyncHttpResponseHandler() {

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
										String errStr = userObj.getString("msg");
										if ("".equals(errStr)) {
											errStr = "非法的标签";
										}
										new AlertDialog.Builder(
												FragmentManagementHistoryResult.this
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
										JSONArray msgArray = userObj.getJSONArray("msg");
										// {"recv_lid":"5","recv_name":"1","alname":"01","dist_time":"2014-11-10T16:51:12.000Z","par_id":"0442d37abe3480","send_lid":"2","send_name":"97"}
										for (int i = 0; i < msgArray.length(); i++) {
											JSONObject oj = msgArray.getJSONObject(i);
											ManagementChainBean chainBean = new ManagementChainBean();
											
											String time = oj.getString("dist_time");
											String sendLID = oj.getString("send_lid");
											String sendID = oj.getString("send_name");
											String sendLName = Helper.getLevelName(levelList, sendLID);
											String sendName = Helper.getUserNameByID(userInfoList, sendID);
											String recvLID = oj.getString("recv_lid");
											String recvID = oj.getString("recv_name");
											String recvLName = Helper.getLevelName(levelList, recvLID);
											String recvName = Helper.getUserNameByID(userInfoList, recvID);
											
											chainBean.setTime(time);
											chainBean.setSendLID(sendLID);
											chainBean.setSendID(sendID);
											chainBean.setSendLName(sendLName);
											chainBean.setSendName(sendName);
											chainBean.setRecvLID(recvLID);
											chainBean.setRecvID(recvID);
											chainBean.setRecvLName(recvLName);
											chainBean.setRecvName(recvName);
											mgntChainList.add(chainBean);
										}
										
										Bundle bundle = new Bundle();
										bundle.putSerializable("CHAINBEANs", mgntChainList);

										FragmentManagementHistoryResultDetail fResult = new FragmentManagementHistoryResultDetail();
										FragmentManager fm = getFragmentManager();
										FragmentTransaction tx = fm.beginTransaction();
										fResult.setArguments(bundle);
										tx.add(R.id.main_details, fResult);
										tx.addToBackStack(null);
										tx.commit();
									}
								} catch (JSONException ex) {
									String errStr = "非法的标签";
									new AlertDialog.Builder(
											FragmentManagementHistoryResult.this.getActivity())
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

							@Override
							public void onFailure(int statusCode, Header[] headers,
									byte[] responseBody, Throwable error) {
								new AlertDialog.Builder(FragmentManagementHistoryResult.this
										.getActivity())
										.setTitle("提示")
										.setMessage("服务器请求失败")
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
						});
			
			}
		});

		return layout;
	}
}
