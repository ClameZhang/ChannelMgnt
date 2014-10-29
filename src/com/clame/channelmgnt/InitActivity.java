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

public class InitActivity extends Activity {
	EditText newPwd;
	EditText confirmPwd;
	TextView errorInfo;
	Button btnConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);

		newPwd = (EditText) findViewById(R.id.tv_content_pwd);
		confirmPwd = (EditText) findViewById(R.id.tv_content_confirm);
		errorInfo = (TextView) findViewById(R.id.tv_content_errorinfo);
		btnConfirm = (Button) findViewById(R.id.btn_confirm);
		
		if (!Helper.isNetworkConnected(InitActivity.this)) {
			errorInfo.setText(getResources().getString(R.string.error_nonetwork));
			return;
		}
		
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				errorInfo.setText("");
				
				// TODO Auto-generated method stub
				String sPWD = newPwd.getText().toString();
				if ("".equals(sPWD)) {
					errorInfo.setText(getResources().getString(R.string.init_error_no_password));
					return;
				}
				
				if (sPWD.length() < 6) {
					errorInfo.setText(getResources().getString(R.string.init_error_wrong_password));
					return;
				}
				
				String sConfirm = confirmPwd.getText().toString();
				if ("".equals(sConfirm) || !sConfirm.equals(sPWD)) {
					errorInfo.setText(getResources().getString(R.string.init_error_unmatch_password));
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

				Intent mainIntent = new Intent(InitActivity.this,
						MainActivity.class);
				InitActivity.this.startActivity(mainIntent);
			}
		});
	}
}
