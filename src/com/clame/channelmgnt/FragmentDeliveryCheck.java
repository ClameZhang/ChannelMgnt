package com.clame.channelmgnt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.clame.channelmgnt.bean.GoodBean;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentDeliveryCheck extends Fragment {

	ImageView iv_user;
	TextView tv_title;
	TextView tv_check_status;
	TextView tv_check_info;
	TextView tv_suggest;
	UserBean userBean;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<UserInfoBean> userInfoList = new ArrayList<UserInfoBean>();

	public FragmentDeliveryCheck() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		final FragmentRecorder app = (FragmentRecorder)this.getActivity().getApplication();
		app.setFragmentname("FragmentDeliveryCheck");

		Bundle bundle = getArguments();
		userBean = (UserBean) bundle.getSerializable("USERBEAN");
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		userInfoList = (ArrayList<UserInfoBean>) bundle
				.getSerializable("USERINFOBEANS");

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_delivary_check,
				container, false);

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentDeliveryCheck.this
						.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentDeliveryCheck.this
												.getActivity().setResult(-1);// 确定按钮事件
										FragmentDeliveryCheck.this
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

		tv_check_info = (TextView) layout.findViewById(R.id.tv_check_info);
		tv_check_info.setVisibility(View.GONE);

		tv_suggest = (TextView) layout.findViewById(R.id.tv_suggest);
		tv_suggest.setVisibility(View.GONE);

		return layout;
	}

	public void update(String flagID, final String mCurrentContent) {
		String isContentOK = Helper.checkDelBigBoxTag(mCurrentContent);

		if (!"SUCC".equals(isContentOK)) {
			new AlertDialog.Builder(FragmentDeliveryCheck.this.getActivity())
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
		
		String url = "py_r/2014";
		JSONObject pkg = new JSONObject();
		try {
			pkg.put("username", userBean.getUserName());
			pkg.put("stime", "");
			pkg.put("etime", "9999-11-30 12:00:00");
			pkg.put("nfc_id", flagID);
			pkg.put("expgoods", "");
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

		RequestAPIClient.post(FragmentDeliveryCheck.this.getActivity(), url,
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
							JSONObject msg = userObj.getJSONObject("msg");

							if (code.equals(RequestAPIClient.STATUS_FAIL)) {
								String errStr = "非法的标签，请联系总部进行查证";
								new AlertDialog.Builder(
										FragmentDeliveryCheck.this
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
								// {"recv_lid":"5","recv_name":"1","alname":"01","dist_time":"2014-11-10T16:51:12.000Z","par_id":"0442d37abe3480","send_lid":"2","send_name":"97"}
								String sendID = msg.getString("send_name");
								String recvID = msg.getString("recv_name");
								String goodID = mCurrentContent
										.substring(10, 12);

								String sendName = Helper.getUserNameByID(
										userInfoList, sendID);
								String recvName = Helper.getUserNameByID(
										userInfoList, recvID);
								if ("8".equals(recvID)) {
									recvName = "消费者";
								}
								String goodName = Helper.getGoodName(goodList, goodID);

								String info = tv_check_info.getText().toString();
								info = info.replace("XX", goodName);
								info = info.replace("AA", sendName);
								info = info.replace("BB", recvName);
								tv_check_info.setText(info);
								tv_check_info.setVisibility(View.VISIBLE);
								
								if (recvID.equals(userBean.getUserName())) {
									tv_check_status
											.setText(getResources().getString(
													R.string.fragment_scan_success));
								} else {
									tv_check_status
									.setText(getResources().getString(
											R.string.fragment_scan_fail));
									tv_suggest.setVisibility(View.VISIBLE);
								}
								
								return;
							}
						} catch (JSONException ex) {
							String errStr = "非法的标签，请联系总部进行查证";
							new AlertDialog.Builder(
									FragmentDeliveryCheck.this
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
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						new AlertDialog.Builder(FragmentDeliveryCheck.this
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
