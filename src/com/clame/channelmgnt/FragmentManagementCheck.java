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
import com.clame.channelmgnt.bean.ManagementChainBean;
import com.clame.channelmgnt.bean.UserBean;
import com.clame.channelmgnt.bean.UserInfoBean;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentManagementCheck extends Fragment {

	ImageView iv_user;
	TextView tv_title;
	TextView tv_check_status;
	LinearLayout ll_check_info;
	TextView tv_check_info_title;
	TextView tv_check_info;
	Button btn_show_detail;
	UserBean userBean;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<UserInfoBean> userInfoList = new ArrayList<UserInfoBean>();
	ArrayList<ManagementChainBean> mgntChainList = new ArrayList<ManagementChainBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	boolean isJump = false;

	public FragmentManagementCheck() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		final FragmentRecorder app = (FragmentRecorder) this.getActivity()
				.getApplication();
		app.setFragmentname("FragmentManagementCheck");

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_management_check,
				container, false);

		Bundle bundle = getArguments();
		userBean = (UserBean) bundle.getSerializable("USERBEAN");
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		userInfoList = (ArrayList<UserInfoBean>) bundle.getSerializable("USERINFOBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LEVELBEANS");

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentManagementCheck.this
						.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentManagementCheck.this
												.getActivity().setResult(-1);// 确定按钮事件
										FragmentManagementCheck.this
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
		tv_title.setText(getResources().getText(R.string.main_title_check));

		tv_check_status = (TextView) layout.findViewById(R.id.tv_check_status);

		ll_check_info = (LinearLayout) layout.findViewById(R.id.ll_check_info);

		tv_check_info_title = (TextView) layout
				.findViewById(R.id.tv_check_info_title);
		tv_check_info = (TextView) layout.findViewById(R.id.tv_check_info);
		btn_show_detail = (Button) layout.findViewById(R.id.btn_show_detail);

		btn_show_detail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				while(!isJump) {
					continue;
				}

				isJump = false;
				ArrayList<ManagementChainBean> mgntChainListFinal = new ArrayList<ManagementChainBean>();
				for (int i = 2; i < 8; i++) {
					boolean isMatch = false;
					for (int j = 0; j < mgntChainList.size(); j++) {
						if (mgntChainList.get(j).getSendLID().equals(String.valueOf(i))) {
							mgntChainListFinal.add(mgntChainList.get(j));
							isMatch = true;
							break;
						}
					}
					if (!isMatch) {
						ManagementChainBean bean = new ManagementChainBean();
						bean.setIsEmpty(true);
						bean.setSendLID(String.valueOf(i));
						mgntChainListFinal.add(bean);
					}
				}
				
				ManagementChainBean lastSendBean = new ManagementChainBean();
				for (int k = 0; k < mgntChainListFinal.size(); k++) {
					if (!mgntChainListFinal.get(k).isEmpty) {
						lastSendBean = mgntChainListFinal.get(k);
					}
				}
				
				int lastRecvLid = Integer.parseInt(lastSendBean.getRecvLID());
				mgntChainListFinal.get(lastRecvLid - 2).setIsEmpty(false);
				mgntChainListFinal.get(lastRecvLid - 2).setSendID(lastSendBean.getRecvID());
				mgntChainListFinal.get(lastRecvLid - 2).setSendLID(lastSendBean.getRecvLID());
				mgntChainListFinal.get(lastRecvLid - 2).setSendName(lastSendBean.getRecvName());
				mgntChainListFinal.get(lastRecvLid - 2).setSendLName(lastSendBean.getRecvLName());

				for (int l = mgntChainListFinal.size() - 1; l > lastRecvLid - 2; l--) {
					mgntChainListFinal.remove(mgntChainListFinal.size() - 1);
				}
				
				for (int m = mgntChainListFinal.size() - 1; m >= 0; m--) {
					ManagementChainBean tmpBean = mgntChainListFinal.get(m);
					if (tmpBean.isEmpty) {
						ManagementChainBean tmpBeanChild = mgntChainListFinal.get(m + 1);
						String childUid = tmpBeanChild.getSendID();
					}
				}
				
				while(!isJump) {
					continue;
				}
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("CHAINBEANs", mgntChainListFinal);

				FragmentManagementHistoryResultDetail fResult = new FragmentManagementHistoryResultDetail();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();
				fResult.setArguments(bundle);
				tx.add(R.id.main_details, fResult);
				tx.addToBackStack(null);
				tx.commit();
			}
		});

		if (getResources().getString(R.string.fragment_unscan).equals(
				tv_check_status.getText().toString())) {
			ll_check_info.setVisibility(View.GONE);
		}

		return layout;
	}

	public void update(String flagID, String mCurrentContent) {
		boolean isFail = false;
		String goodName = "";

		String flagIDStr = "048FCBFA463D80";

		String checkResult = Helper.checkDelBigBoxTag(mCurrentContent);
		if (!checkResult.equals("SUCC")) {
			String errStr = "无效的箱子专用标签";
			new AlertDialog.Builder(FragmentManagementCheck.this.getActivity())
					.setTitle("提示")
					.setMessage(errStr)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).show();
			isFail = true;
		}

		if (!isFail) {
			String goodID = mCurrentContent.substring(10, 12);
			goodName = Helper.getGoodName(goodList, goodID);
			if ("".equals(goodName)) {
				String errStr = "无效的箱子专用标签";
				new AlertDialog.Builder(
						FragmentManagementCheck.this.getActivity())
						.setTitle("提示")
						.setMessage(errStr)
						.setIcon(android.R.drawable.ic_dialog_info)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).show();
				isFail = true;
			}
		}

		tv_check_status.setText(getResources()
				.getString(R.string.fragment_scan));

		ll_check_info.setVisibility(View.VISIBLE);

		String checkInfoTitle = getResources().getString(
				R.string.fragment_mgnt_check_info_title);
		if (isFail) {
			checkInfoTitle = "无效的箱子专用标签";
			btn_show_detail.setVisibility(View.GONE);
		} else {
			checkInfoTitle = checkInfoTitle.replace("XX", goodName);
			btn_show_detail.setVisibility(View.VISIBLE);
		}
		tv_check_info_title.setText(checkInfoTitle);

		String checkInfo = getResources().getString(
				R.string.fragment_mgnt_check_info_id);
		checkInfo = checkInfo.replace("NN", flagID);
		tv_check_info.setText(checkInfo);
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

		RequestAPIClient.post(FragmentManagementCheck.this.getActivity(), url,
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
										FragmentManagementCheck.this
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
									isJump = true;
								}
							}
						} catch (JSONException ex) {
							String errStr = "非法的标签";
							new AlertDialog.Builder(
									FragmentManagementCheck.this.getActivity())
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
						new AlertDialog.Builder(FragmentManagementCheck.this
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
}
