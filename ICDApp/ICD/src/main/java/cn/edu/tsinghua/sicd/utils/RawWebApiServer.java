package cn.edu.tsinghua.sicd.utils;

import android.content.*;

import org.apache.http.HttpEntity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RawWebApiServer {

	public final static String GET = "get";
	public final static String POST = "post";
	public final static String PUT = "put";
	public final static String DELETE = "delete";

	private Context context;

	public RawWebApiServer(Context context){
		this.context=context;
	}

	public Context getContext(){
		return context;
	}

	private String getRootUrl(Context context){
		return MetaUtils.getValue(context,"WebApiRoot");
	}

	private String getQueryString(String[] keys, String[] values) {
		String url = "";
		if (keys != null && keys.length > 0) {
			for (int i = 0; i < keys.length; i++) {
				if (keys.length - 1 != i)
					url += keys[i] + "=" + URLEncoder.encode(values[i]) + "&";
				else
					url += keys[i] + "=" + URLEncoder.encode(values[i]);
			}
		}


		return url;
	}

	public HttpRequestBase create(Context context, String httpAction, String rootUrl,
										 String model,String[] keys, String[] values,
										 String[] otherkeys, String[] othervalues)
			throws UnsupportedEncodingException {
		String url = rootUrl + model;
		if (keys != null) {
			url += "?" + getQueryString(keys, values);
		}

		HttpRequestBase httpRequest = null;

		if (httpAction.equals(GET)) {
			httpRequest = new HttpGet(url);// 创建HttpGet对象
		}

		if (httpAction.equals(POST)) {
			httpRequest = new HttpPost(url);// 创建HttpPost对象
			if (otherkeys != null && othervalues != null) {
				((HttpPost) httpRequest).setEntity(new UrlEncodedFormEntity(
						getParam(otherkeys, othervalues), "UTF-8"));
			}
		}
		if (httpAction.equals(PUT)) {
			httpRequest = new HttpPut(url);// 创建HttpPut对象
			if (otherkeys != null && othervalues != null) {
				((HttpPut) httpRequest).setEntity(new UrlEncodedFormEntity(
						getParam(otherkeys, othervalues), "UTF-8"));
			}
		}
		if (httpAction.equals(DELETE)) {
			httpRequest = new HttpDelete(url);// 创建HttpDelete对象
		}


		return httpRequest;
	}

	private List<NameValuePair> getParam(String[] keys, String[] values) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		if (keys != null && keys.length > 0) {
			for (int i = 0; i < keys.length; i++) {
				params.add(new BasicNameValuePair(keys[i], values[i]));
			}
		}
		return params;

	}


	public String call(String action, String model, String[] keys,
					   String[] values) {
		try {
			String result = null;

			HttpRequestBase httpRequest = create(context,
					action, getRootUrl(context), model,  keys, values, null, null);

			HttpClient hc = new DefaultHttpClient();

			HttpResponse httpResponse = hc.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();

				result = EntityUtils.toString(httpEntity);// 取出应答字符串

				System.out.println("result = "+result);


			} else {
				System.out.println("code = "
						+ httpResponse.getStatusLine().getStatusCode());
				System.out.println("reason = "
						+ httpResponse.getStatusLine().getReasonPhrase());

				HttpEntity httpEntity = httpResponse.getEntity();

				result = EntityUtils.toString(httpEntity);// 取出应答字符串

			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
