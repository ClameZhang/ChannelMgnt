package com.clame.channelmgnt.helper;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;

import com.clame.channelmgnt.bean.DownBean;
import com.clame.channelmgnt.bean.GoodBean;
import com.clame.channelmgnt.bean.LevelBean;
import com.clame.channelmgnt.bean.LimitBean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Helper {
	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (mConnectivityManager == null) {
				return false;
			}

			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo == null || !mNetworkInfo.isAvailable()) {
				return false;
			}
		}

		return true;
	}

	public static String getLocalIpAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(android.content.Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		// 获取32位整型IP地址
		int ipAddress = wifiInfo.getIpAddress();

		// 返回整型地址转换成“*.*.*.*”地址
		return String.format("%d.%d.%d.%d", (ipAddress & 0xff),
				(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
	}

	public static String getIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						// if (!inetAddress.isLoopbackAddress() && inetAddress
						// instanceof Inet6Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSystemVersion() {
		String version;
		int versionNum = 0;
		try {
			versionNum = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		} catch (NumberFormatException e) {
		}
		
		version = "Android " + String.valueOf(versionNum);
		
		return version;
	}
	
	public static String getNfcID(byte[] pageload1, byte[] pageload2) {
		StringBuilder stringBuilder = new StringBuilder();
		if (pageload1 == null || pageload1.length <= 0 || pageload2 == null
				|| pageload2.length <= 0) {
			return null;
		}

		char[] buffer = new char[2];
		for (int i = 0; i < 3; i++) {
			buffer[0] = Character.forDigit((pageload1[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(pageload1[i] & 0x0F, 16);
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		for (int i = 0; i < 4; i++) {
			buffer[0] = Character.forDigit((pageload2[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(pageload2[i] & 0x0F, 16);
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}

		return stringBuilder.toString();
	}
	
	public static String getLimit(ArrayList<LimitBean> limitList, ArrayList<GoodBean> goodList, String goodName) {
		String limit = "";
		String id = "";
		
		for (int i = 0; i < goodList.size(); i++) {
			GoodBean goodBean = goodList.get(i);
			if (goodName.equals(goodBean.getgName())) {
				id = goodBean.getgID();
				break;
			}
		}
		
		for (int i = 0; i < limitList.size(); i++) {
			LimitBean limitBean = limitList.get(i);
			if (id.equals(limitBean.getlID())) {
				limit = limitBean.getlNum();
				break;
			}
		}
		
		return limit;
	}
	
	public static String getGoodID(ArrayList<GoodBean> goodList, String goodName) {
		String id = "";
		
		for (int i = 0; i < goodList.size(); i++) {
			GoodBean goodBean = goodList.get(i);
			if (goodName.equals(goodBean.getgName())) {
				id = goodBean.getgID();
				break;
			}
		}
		
		return id;
	}
	
	public static String getLevelID(ArrayList<DownBean> downList, String name) {
		String id = "";
		
		for (int i = 0; i < downList.size(); i++) {
			DownBean downBean = downList.get(i);
			if (name.equals(downBean.getgName())) {
				id = downBean.getgID();
				break;
			}
		}
		return id;
	}
	
	public static String getLevelName(ArrayList<LevelBean> levelList, String ID) {
		String name = "";
		
		for (int i = 0; i < levelList.size(); i++) {
			LevelBean levelBean = levelList.get(i);
			if (ID.equals(levelBean.getlID())) {
				name = levelBean.getlName();
				break;
			}
		}
		return name;
	}
	
	public static String getLevelIDByName(ArrayList<LevelBean> levelList, String name) {
		String id = "";
		
		for (int i = 0; i < levelList.size(); i++) {
			LevelBean levelBean = levelList.get(i);
			if (name.equals(levelBean.getlName())) {
				id = levelBean.getlID();
				break;
			}
		}
		return id;
	}
}
