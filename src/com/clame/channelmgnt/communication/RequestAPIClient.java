package com.clame.channelmgnt.communication;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

// TODO: sid is dynamic
public class RequestAPIClient {
	private static final String BASE_URL = "http://112.124.117.97:3000/";
//	private static final String BASE_URL = "http://192.168.0.115:3000/";
	public static final String STATUS_SUCC = "SUCC";
	public static final String STATUS_FAIL = "FAIL";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), responseHandler);
	}

	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), responseHandler);
	}
	
	public static void post(Context context, String url, HttpEntity params, AsyncHttpResponseHandler responseHandler) {
		client.post(context, getAbsoluteUrl(url), null, params, RequestParams.APPLICATION_JSON, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}
}