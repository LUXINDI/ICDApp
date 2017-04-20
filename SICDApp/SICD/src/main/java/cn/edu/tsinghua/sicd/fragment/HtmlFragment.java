package cn.edu.tsinghua.sicd.fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import android.app.Activity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.webkit.WebSettings;
import android.webkit.WebView;


import org.apache.cordova.Config;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;

import cn.edu.tsinghua.sicd.R;

public class HtmlFragment extends BaseFragment implements CordovaInterface {

	private CordovaWebView webView;

	private Context context;

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	private String rootUrl = "";

	public HtmlFragment() {
		this.rootUrl = "file:///android_asset/www/index.html";

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		context = inflater.getContext();

		LayoutInflater localInflater = inflater
				.cloneInContext(new CordovaContext(getActivity(), this));
		View rootView = localInflater.inflate(R.layout.fragment_html, container,
				false);


		webView = (CordovaWebView) rootView.findViewById(R.id.myWebView);

		Config.init(getActivity());

		if (this.rootUrl != null && !this.rootUrl.equals(""))
			webView.loadUrl(this.rootUrl);


		CordovaWebViewClient cordovaWebViewClient = new CordovaWebViewClient(
				this, webView) {

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				System.out.println("onPageStarted " + url);
				super.onPageStarted(view, url, favicon);

			}

			public void onPageFinished(WebView view, String url) {
				System.out.println("onPageFinished " + url);
				super.onPageFinished(view, url);

			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
				System.out.println("onReceivedError ");
				super.onReceivedError(view, errorCode, description, failingUrl);


			}
		};

		//setting

		WebSettings webSetting = webView.getSettings();

		String appCachePath = this.getActivity().getCacheDir().getAbsolutePath();
		webSetting.setAppCachePath(appCachePath);
		webSetting.setAllowFileAccess(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		webSetting.setDefaultTextEncodingName("UTF-8");
		webSetting.setJavaScriptEnabled(true);


		webView.setWebViewClient(cordovaWebViewClient);

		return rootView;

	}

	public CordovaWebView getWebView() {
		return this.webView;
	}

	public void reloadUrl() {
		this.getWebView().reload();
	}

	// Plugin to call when activity result is received
	protected CordovaPlugin activityResultCallback = null;
	protected boolean activityResultKeepRunning;

	// Keep app running when pause is received. (default = true)
	// If true, then the JavaScript and native code continue to run in the
	// background
	// when another application (activity) is started.
	protected boolean keepRunning = true;

	private final ExecutorService threadPool = Executors.newCachedThreadPool();

	public Object onMessage(String id, Object data) {
		return null;
	}

	public void onDestroy() {
		super.onDestroy();
		if (webView.pluginManager != null) {
			webView.pluginManager.onDestroy();
		}
	}

	@Override
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin plugin) {
		this.activityResultCallback = plugin;
	}

	public void startActivityForResult(CordovaPlugin command, Intent intent,
									   int requestCode) {
		this.activityResultCallback = command;
		this.activityResultKeepRunning = this.keepRunning;
		// If multitasking turned on, then disable it for activities that return
		// results
		if (command != null) {
			this.keepRunning = false;
		}
		// Start activity
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		CordovaPlugin callback = this.activityResultCallback;
		if (callback != null) {
			callback.onActivityResult(requestCode, resultCode, intent);
		}
	}

	private class CordovaContext extends ContextWrapper implements
			CordovaInterface {
		CordovaInterface cordova;

		public CordovaContext(Context base, CordovaInterface cordova) {
			super(base);
			this.cordova = cordova;
		}

		public void startActivityForResult(CordovaPlugin command,
										   Intent intent, int requestCode) {
			cordova.startActivityForResult(command, intent, requestCode);
		}

		public void setActivityResultCallback(CordovaPlugin plugin) {
			cordova.setActivityResultCallback(plugin);
		}

		public Activity getActivity() {
			return cordova.getActivity();
		}

		public Object onMessage(String id, Object data) {
			return cordova.onMessage(id, data);
		}

		public ExecutorService getThreadPool() {
			return cordova.getThreadPool();
		}
	}

	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return "HtmlFragment";
	}

}
