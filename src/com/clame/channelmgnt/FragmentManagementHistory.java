package com.clame.channelmgnt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.clame.channelmgnt.bean.DeliveryHistoryBean;
import com.clame.channelmgnt.bean.GoodBean;
import com.clame.channelmgnt.bean.LevelBean;
import com.clame.channelmgnt.bean.ManagementHistoryBean;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @ClassName FragmentMine.java
 * @author Clame
 * 
 */
public class FragmentManagementHistory extends Fragment {

	ImageView iv_user;
	TextView tv_title;
	DatePicker dp_start;
	DatePicker dp_end;
	Button btn_search;
	UserBean userBean;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<UserInfoBean> userInfoList = new ArrayList<UserInfoBean>();
	ArrayList<ManagementHistoryBean> managementHistoryList = new ArrayList<ManagementHistoryBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();

	public FragmentManagementHistory() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		Bundle bundle = getArguments();
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		userInfoList = (ArrayList<UserInfoBean>) bundle
				.getSerializable("USERINFOBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LEVELBEANS");
		userBean = (UserBean) bundle.getSerializable("USERBEAN");

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_management_history,
				container, false);

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentManagementHistory.this
						.getActivity())
						.setTitle("��ʾ")
						.setMessage("ȷ���˳�?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentManagementHistory.this
												.getActivity().setResult(-1);// ȷ����ť�¼�
										FragmentManagementHistory.this
												.getActivity().finish();
									}
								})
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										// ȡ����ť�¼�
									}
								}).show();
			}
		});

		tv_title = (TextView) layout.findViewById(R.id.tv_title);
		tv_title.setText(getResources().getText(R.string.main_title_history));

		final Calendar now = Calendar.getInstance();
		int mYear = now.get(Calendar.YEAR);
		int mMonth = now.get(Calendar.MONTH);
		int mDay = now.get(Calendar.DAY_OF_MONTH);

		dp_start = (DatePicker) layout.findViewById(R.id.dp_start);
		dp_start.init(mYear, mMonth, mDay, null);

		dp_end = (DatePicker) layout.findViewById(R.id.dp_end);
		dp_end.init(mYear, mMonth, mDay, null);

		btn_search = (Button) layout.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int dayStart = dp_start.getDayOfMonth();
				int monthStart = dp_start.getMonth() + 1;
				int yearStart = dp_start.getYear();
				Calendar start = Calendar.getInstance();
				start.set(yearStart, monthStart, dayStart);
				String startStr = String.valueOf(yearStart) + "-"
						+ String.valueOf(monthStart) + "-"
						+ String.valueOf(dayStart) + " 00:00:00";

				int dayEnd = dp_end.getDayOfMonth();
				int monthEnd = dp_end.getMonth() + 1;
				int yearEnd = dp_end.getYear();
				Calendar end = Calendar.getInstance();
				end.set(yearEnd, monthEnd, dayEnd);
				String endStr = String.valueOf(yearEnd) + "-"
						+ String.valueOf(monthEnd) + "-"
						+ String.valueOf(dayEnd) + " 23:59:59";

				if (start.compareTo(end) > 0) {
					new AlertDialog.Builder(FragmentManagementHistory.this
							.getActivity())
							.setTitle("����")
							.setMessage("��ʼʱ�䲻�ܴ��ڽ���ʱ�䣡")
							.setIcon(android.R.drawable.ic_secure)
							.setPositiveButton("ȷ��",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
										}
									}).show();
					return;
				}

				String url = "py_r/2010";
				JSONObject del = new JSONObject();
				try {
					del.put("username", userBean.getUserName());
					del.put("stime", startStr);
					del.put("etime", endStr);
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

				RequestAPIClient.post(
						FragmentManagementHistory.this.getActivity(), url,
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
									// �������ľ���JSON����Ĳ�����
									String code = userObj.getString("code");
									JSONArray msgArray = userObj
											.getJSONArray("msg");
									for (int i = 0; i < msgArray.length(); i++) {
										JSONObject oj = msgArray
												.getJSONObject(i);
										ManagementHistoryBean historyBean = new ManagementHistoryBean();

										// {"client_ip":"999","client_ua":"undefined","type":"ADMINCHECK","qrcode":"NULL","nfc":"048fcbfa463d80","verify_at":"2014-11-13T00:08:09.000Z","result":"SUCC"}

										String time = oj.getString("verify_at");
										String nfcID = oj.getString("nfc");
										String goodName = oj.getString("nfc");
										historyBean.setTime(time);
										historyBean.setGoodName(goodName);
										historyBean.setNfc(nfcID);
										managementHistoryList.add(historyBean);
									}

									if (code.equals(RequestAPIClient.STATUS_FAIL)) {
										String errStr = "��ȡ������Ϣʧ��";
										new AlertDialog.Builder(
												FragmentManagementHistory.this
														.getActivity())
												.setTitle("��ʾ")
												.setMessage(errStr)
												.setIcon(
														android.R.drawable.ic_dialog_info)
												.setPositiveButton(
														"ȷ��",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
															}
														}).show();
										return;
									} else {
										Bundle bundle = new Bundle();

										ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
										for (int i = 0; i < managementHistoryList
												.size(); i++) {
											Map<String, Object> map = new HashMap<String, Object>();
											String time = managementHistoryList
													.get(i).getTime();
											time = time.substring(0, 10) + " "
													+ time.substring(11, 19);
											map.put("tv_date", "���ʱ�䣺" + time);
											map.put("tv_good", "��Ʒ��"
													+ managementHistoryList
															.get(i).getGoodName());
											map.put("tv_nfc", managementHistoryList.get(i).getNfc());
											list.add(map);
										}
										bundle.putSerializable("historylist",
												list);
										bundle.putSerializable("USERBEAN", userBean);
										bundle.putSerializable("LEVELBEANS",
												levelList);
										bundle.putSerializable("USERINFOBEANS",
												userInfoList);

										FragmentManagementHistoryResult fHistoryResult = new FragmentManagementHistoryResult();
										FragmentManager fm = getFragmentManager();
										FragmentTransaction tx = fm
												.beginTransaction();
										fHistoryResult.setArguments(bundle);
										tx.add(R.id.main_details,
												fHistoryResult);
										tx.addToBackStack(null);
										tx.commit();
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
										FragmentManagementHistory.this
												.getActivity())
										.setTitle("��ʾ")
										.setMessage("��ȡ������Ϣʧ��")
										.setIcon(
												android.R.drawable.ic_dialog_info)
										.setPositiveButton(
												"ȷ��",
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
