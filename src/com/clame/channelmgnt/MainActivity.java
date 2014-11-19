package com.clame.channelmgnt;

import java.util.ArrayList;

import com.clame.channelmgnt.bean.GoodBean;
import com.clame.channelmgnt.bean.LevelBean;
import com.clame.channelmgnt.bean.LimitBean;
import com.clame.channelmgnt.bean.SerialBean;
import com.clame.channelmgnt.bean.UserBean;
import com.clame.channelmgnt.bean.UserInfoBean;
import com.clame.channelmgnt.helper.Helper;
import com.clame.channelmgnt.widgets.BottomBarDelivery;
import com.clame.channelmgnt.widgets.BottomBarDelivery.OnItemChangedListener;
import com.clame.channelmgnt.widgets.BottomBarManagement;
import com.clame.channelmgnt.widgets.BottomBarManagement.OnManagementItemChangedListener;
import com.clame.channelmgnt.widgets.BottomBarPackage;
import com.clame.channelmgnt.widgets.BottomBarPackage.OnPackageItemChangedListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

	private NfcAdapter nfcAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	UserBean userBean;
	ArrayList<GoodBean> goodList = new ArrayList<GoodBean>();
	ArrayList<LimitBean> limitList = new ArrayList<LimitBean>();
	ArrayList<LevelBean> levelList = new ArrayList<LevelBean>();
	ArrayList<SerialBean> serialList = new ArrayList<SerialBean>();
	ArrayList<UserInfoBean> userInfoList = new ArrayList<UserInfoBean>();

	int userAuth = 0;
	int delAuth = 0;
	
	public String fragmentName;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Bundle bundle = getIntent().getBundleExtra("BUNDLE");
		userBean = (UserBean) bundle.getSerializable("USERBEAN");
		goodList = (ArrayList<GoodBean>) bundle.getSerializable("GOODBEANS");
		limitList = (ArrayList<LimitBean>) bundle.getSerializable("LIMITBEANS");
		levelList = (ArrayList<LevelBean>) bundle.getSerializable("LEVELBEANS");
		serialList = (ArrayList<SerialBean>) bundle
				.getSerializable("SERIALBEANS");
		userInfoList = (ArrayList<UserInfoBean>) bundle
				.getSerializable("USERINFOBEANS");

		if ("0".equals(userBean.getuLevel())) {
			userAuth = 0;
		} else if ("1".equals(userBean.getuLevel())) {
			userAuth = 1;
		} else {
			if ("2".equals(userBean.getuLevel())) {
				delAuth = 0;
			} else {
				delAuth = 1;
			}
			userAuth = 2;
		}

		// initialize the nfc related settings
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

		mFilters = new IntentFilter[] { ndef, };

		mTechLists = new String[][] { new String[] { MifareUltralight.class
				.getName() } };

		// declare the bottombar, and add the ItemChangedListener
		RelativeLayout llPackage = (RelativeLayout) findViewById(R.id.ll_bottom_bar_package);
		final BottomBarPackage bottomBarPackage = (BottomBarPackage) findViewById(R.id.main_bottom_bar_package);
		bottomBarPackage
				.setOnPackageItemChangedListener(new OnPackageItemChangedListener() {

					@Override
					public void onPackageItemChanged(int index) {
						// TODO Auto-generated method stub
						showDetails(index);
					}
				});
		bottomBarPackage.setSelectedState(0);

		RelativeLayout llDelivery = (RelativeLayout) findViewById(R.id.ll_bottom_bar_delivery);
		final BottomBarDelivery bottomBarDelivery = (BottomBarDelivery) findViewById(R.id.main_bottom_bar_delivery);
		bottomBarDelivery.setOnItemChangedListener(new OnItemChangedListener() {
			@Override
			public void onItemChanged(int index) {
				showDetails(index);
			}
		});
		bottomBarDelivery.setSelectedState(0);

		RelativeLayout llManagement = (RelativeLayout) findViewById(R.id.ll_bottom_bar_management);
		final BottomBarManagement bottomBarManagement = (BottomBarManagement) findViewById(R.id.main_bottom_bar_management);
		bottomBarManagement
				.setOnManagementItemChangedListener(new OnManagementItemChangedListener() {
					@Override
					public void onManagementItemChanged(int index) {
						showDetails(index);
					}
				});
		bottomBarManagement.setSelectedState(0);

		if (userAuth == 0) {
			llPackage.setVisibility(View.GONE);
			llDelivery.setVisibility(View.GONE);
			llManagement.setVisibility(View.GONE);
		} else if (userAuth == 1) {
			llManagement.setVisibility(View.GONE);
			llDelivery.setVisibility(View.GONE);
		} else {
			llManagement.setVisibility(View.GONE);
			llPackage.setVisibility(View.GONE);
		}
	}

	/**
	 * @FunName showDetails
	 * @Description switch the fragment content according to the selected item
	 *              on bottombar
	 * @param index
	 * @return N/A
	 * 
	 */
	private void showDetails(int index) {
		Fragment details = (Fragment) getSupportFragmentManager()
				.findFragmentById(R.id.main_details);
		String tag = "";

		// set the target fragment according to the index
		switch (index) {
		case 0:
			if (userAuth == 0) {
				details = new FragmentManagementCheck();
				tag = "FragmentManagementCheck";
			} else if (userAuth == 1) {
				details = new FragmentPackageOne();
				tag = "FragmentPackageOne";
			} else {
				details = new FragmentDeliveryOne();
				tag = "FragmentDeliveryOne";
//				if (delAuth == 0) {
//					details = new FragmentDeliveryOneSenior();
//					tag = "FragmentDeliveryOneSenior";
//				} else {
//					details = new FragmentDeliveryOneNormal();
//					tag = "FragmentDeliveryOneNormal";
//				}
			}
			break;
		case 1:
			if (userAuth == 0) {
				details = new FragmentManagementHistory();
				tag = "FragmentManagementHistory";
			} else if (userAuth == 1) {
				details = new FragmentPackageHistory();
				tag = "FragmentPackageHistory";
			} else {
				details = new FragmentDeliveryCheck();
				tag = "FragmentDeliveryCheck";
			}
			break;
		case 2:
			details = new FragmentDeliveryHistory();
			tag = "FragmentDeliveryHistory";
			break;
		}

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
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
		bundle.putSerializable("USERINFOBEANS",
				userInfoList);
		details.setArguments(bundle);
		ft.replace(R.id.main_details, details, tag);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (nfcAdapter == null) {
			// msg.setText(R.string.main_nonfc_warning);
			return;
		}
		nfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
				mTechLists);
	}

	@Override
	public void onNewIntent(Intent intent) {
		// 处理该intent
		if (nfcAdapter.isEnabled()) {
			processIntent(intent);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		if (nfcAdapter == null) {
			// msg.setText(R.string.main_nonfc_warning);
			return;
		}
		nfcAdapter.disableForegroundDispatch(this);
	}

	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	@SuppressLint("NewApi")
	private void processIntent(Intent intent) {
		// 取出封装在intent中的TAG
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		for (String tech : tagFromIntent.getTechList()) {
			System.out.println(tech);
		}
		// 读取TAG
		MifareUltralight mfc = MifareUltralight.get(tagFromIntent);
		try {
			// Enable I/O operations to the tag from this TagTechnology object.
			mfc.connect();

			int type = mfc.getType();// 获取TAG的类型

			if (type != MifareUltralight.TYPE_ULTRALIGHT) {
				return;
			}

			String mCurrentID = Helper.getNfcID(mfc.readPages(0),
					mfc.readPages(1));

			
			final FragmentRecorder app = (FragmentRecorder)getApplication();
			String fragmentName = app.getFragmentname();
			
			FragmentPackageTwo fp2 = (FragmentPackageTwo) getSupportFragmentManager()
					.findFragmentByTag("FragmentPackageTwo");
			if (fp2 != null && fragmentName.equals("FragmentPackageTwo")) {
				String mCurrentContent = Helper.getNfcContent(intent);
				fp2.update(mCurrentID, mCurrentContent);
			}

			FragmentPackageThree fp3 = (FragmentPackageThree) getSupportFragmentManager()
					.findFragmentByTag("FragmentPackageThree");
			if (fp3 != null && fragmentName.equals("FragmentPackageThree")) {
				String mCurrentContent = Helper.getNfcContent(intent);
				fp3.update(mCurrentID, mCurrentContent);
			}

			FragmentManagementCheck fc = (FragmentManagementCheck) getSupportFragmentManager()
					.findFragmentByTag("FragmentManagementCheck");
			if (fc != null && fragmentName.equals("FragmentManagementCheck")) {
				String mCurrentContent = Helper.getNfcContent(intent);
				fc.update(mCurrentID, mCurrentContent);
			}

			FragmentDeliveryCheck fdc = (FragmentDeliveryCheck) getSupportFragmentManager()
					.findFragmentByTag("FragmentDeliveryCheck");
			if (fdc != null && fragmentName.equals("FragmentDeliveryCheck")) {
				String mCurrentContent = Helper.getNfcContent(intent);
				fdc.update(mCurrentID, mCurrentContent);
			}

			FragmentDeliveryThree fdt = (FragmentDeliveryThree) getSupportFragmentManager()
					.findFragmentByTag("FragmentDeliveryThree");
			if (fdt != null && fragmentName.equals("FragmentDeliveryThree")) {
				String mCurrentContent = Helper.getNfcContent(intent);
				fdt.update(mCurrentID, mCurrentContent);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
