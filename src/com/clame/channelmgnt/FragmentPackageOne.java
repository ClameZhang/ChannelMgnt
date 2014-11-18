package com.clame.channelmgnt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
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
public class FragmentPackageOne extends Fragment {

	ImageView iv_user;
	TextView tv_title;
	Spinner spinner_goods;
	ArrayAdapter<String> adapter;
	ArrayList<String> goodsList;
	Button btn_next;
	UserBean userBean;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<LimitBean> limitList = new ArrayList<LimitBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	ArrayList<SerialBean> serialList = new ArrayList<SerialBean>();

	public FragmentPackageOne() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		
		final FragmentRecorder app = (FragmentRecorder)this.getActivity().getApplication();
		app.setFragmentname("FragmentPackageOne");

		Bundle bundle = getArguments();
		userBean = (UserBean) bundle.getSerializable("USERBEAN");
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		limitList = (ArrayList<LimitBean>) bundle.getSerializable("LIMITBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LEVELBEANS");
		serialList = (ArrayList<SerialBean>) bundle.getSerializable("SERIALBEANS");

		LayoutInflater myInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = myInflater.inflate(R.layout.fragment_package_one,
				container, false);

		iv_user = (ImageView) layout.findViewById(R.id.iv_user);
		iv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(FragmentPackageOne.this.getActivity())
						.setTitle("提示")
						.setMessage("确定退出?")
						.setIcon(R.drawable.ic_return)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										FragmentPackageOne.this.getActivity()
												.setResult(-1);// 确定按钮事件
										FragmentPackageOne.this.getActivity()
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

		String url = "py_r/2002/" + userBean.getSerialID();
		goodsList = new ArrayList<String>();
		spinner_goods = (Spinner) layout.findViewById(R.id.spinner_goods);
		RequestAPIClient.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				if (arg0 == 200) {
					String response = "";
					try {
						response = new String(arg2, "UTF-8");
					} catch (UnsupportedEncodingException e) {

						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						JSONTokener jsonParser = new JSONTokener(response);
						JSONObject object = (JSONObject) jsonParser.nextValue();
						JSONArray productArray = object.getJSONArray("msg");
						
						for(int i = 0; i < productArray.length(); i++) {
			                JSONObject oj = productArray.getJSONObject(i);
			                goodsList.add(oj.getString("name"));
			            }

						adapter = new ArrayAdapter<String>(
								FragmentPackageOne.this.getActivity(),
								android.R.layout.simple_spinner_item, goodsList);
						adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spinner_goods.setAdapter(adapter);
						spinner_goods
								.setOnItemSelectedListener(new SpinnerSelectedListener());
						spinner_goods.setVisibility(View.VISIBLE);
					} catch (JSONException ex) {
						return;
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});

		btn_next = (Button) layout.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String goodName = spinner_goods.getSelectedItem().toString();
				String goodLimit = Helper.getLimit(limitList, goodList, goodName);
				String goodID = Helper.getGoodID(goodList, goodName);
				Bundle bundle = new Bundle();
				bundle.putSerializable("GOODBEANS", goodList);
				bundle.putString("userName", userBean.getUserName());
				bundle.putString("serialID", userBean.getSerialID());
				bundle.putString("goodName", goodName);
				bundle.putString("goodID", goodID);
				bundle.putString("goodLimit", goodLimit);
				FragmentPackageTwo fTwo = new FragmentPackageTwo();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction tx = fm.beginTransaction();
				fTwo.setArguments(bundle);
				tx.add(R.id.main_details, fTwo, "FragmentPackageTwo");
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
