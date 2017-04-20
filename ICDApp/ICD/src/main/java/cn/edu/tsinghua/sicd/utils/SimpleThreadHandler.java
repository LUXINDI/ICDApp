package cn.edu.tsinghua.sicd.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SimpleThreadHandler {
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			runInFrontend(msg.getData().getString("result")); // added
		}
	};

	public void doInBackground() {
		new Thread() {
			public void run() {
				String result=doHardWork();
				
				Message msg=new Message();
				Bundle bd=new Bundle();
				bd.putString("result", result);
				msg.setData(bd);
				handler.sendMessage(msg);
			}

			private String doHardWork() {
				return runInBackend(); // added
			}
		}.start();
	}

	// added
	protected String runInBackend() {
		return null;
	}

	// added
	protected void runInFrontend(String data) {
	}
	
	
}