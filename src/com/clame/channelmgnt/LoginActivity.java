package com.clame.channelmgnt;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.clame.channelmgnt.bean.UserBean;
import com.clame.channelmgnt.communication.RequestAPIClient;
import com.clame.channelmgnt.helper.Helper;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		userName = (EditText) findViewById(R.id.tv_content_name);
		password = (EditText) findViewById(R.id.tv_content_password);
		errorInfo = (TextView) findViewById(R.id.tv_content_errorinfo);
		btnLogin = (Button) findViewById(R.id.btn_login);
		
		if (!Helper.isNetworkConnected(LoginActivity.this)) {
			errorInfo.setText(getResources().getString(R.string.error_nonetwork));
			return;
		}
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				errorInfo.setText("");
				
				// TODO Auto-generated method stub
				String sUN = userName.getText().toString();
				if ("".equals(sUN)) {
					errorInfo.setText(getResources().getString(R.string.login_error_no_username));
					return;
				}
				
				String sPWD = password.getText().toString();
				if ("".equals(sPWD)) {
					errorInfo.setText(getResources().getString(R.string.login_error_no_password));
					return;
				}
				

//				RequestAPIClient.get("", new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(String response) {
//						if (response == null) {
//							errorInfo.setText(getResources().getString(R.string.error_server));
//							return;
//						}
//
//						UserBean userBean = new UserBean();
//						if (userBean.isInit) {
//							
//						} else {
//							
//						}
//						
//						try {  
//						    JSONTokener jsonParser = new JSONTokener(response);
//						    JSONObject product = (JSONObject) jsonParser.nextValue();  
//						    // 接下来的就是JSON对象的操作了  
//						    bean.setProd_place(product.getString("prod_place"));
//						    bean.setImage(product.getString("image"));
//						    bean.setName(product.getString("name"));
//						    bean.setDist_place(product.getString("dist_place"));
//						    bean.setSerial(product.getString("serial"));
//						} catch (JSONException ex) {
//							Intent failedIntent = new Intent(MainActivity.this,
//									FailedActivity.class);
//							MainActivity.this.startActivity(failedIntent);
//							return;
//						}
//
//						Intent successIntent = new Intent(MainActivity.this,
//								SuccessActivity.class);
//						successIntent.putExtra("name", bean.getName());
//						successIntent.putExtra("makeLocation", bean.getProd_place());
//						successIntent.putExtra("saleLocation", bean.getDist_place());
//						successIntent.putExtra("sequence", bean.getSerial());
//						successIntent.putExtra("imgURL", bean.getImage());
//						MainActivity.this.startActivity(successIntent);
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] responseBody, Throwable error) {
//						errorInfo.setText(getResources().getString(R.string.error_server));
//						return;
//					}
//				});
				
				UserBean userBean = new UserBean();
				if (userBean.isInit) {
					Intent initIntent = new Intent(LoginActivity.this,
							InitActivity.class);
					LoginActivity.this.startActivity(initIntent);
				} else {

					Intent mainIntent = new Intent(LoginActivity.this,
							MainActivity.class);
					LoginActivity.this.startActivity(mainIntent);
				}
			}
		});
	}
}
