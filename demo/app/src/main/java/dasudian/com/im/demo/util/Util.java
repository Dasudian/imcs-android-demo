package dasudian.com.im.demo.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.widget.Toast;


import java.util.UUID;

import dasudian.com.im.demo.dao.ClientInfo;

public class Util {
	private static Toast toast;

	public static void showToast(Context context, String content) {
		if (toast == null) {
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}

	public static void saveClientInfo(Context context, ClientInfo clientInfo) {
		SharedPreferences sp = context.getSharedPreferences("userInfo",
				android.content.Context.MODE_PRIVATE);
		Editor ed = sp.edit();
		ed.putString("userId", clientInfo.getUserId());
		ed.putString("clientId", clientInfo.getClientId());
		ed.commit();
	}

	public static ClientInfo getClientInfo(Context context) {
		SharedPreferences sp = context.getSharedPreferences("userInfo",
				android.content.Context.MODE_PRIVATE);
		String userId = sp.getString("userId", null);
		String clientId = sp.getString("clientId", null);
		
		if (TextUtils.isEmpty(userId)) {
			userId = UUID.randomUUID().toString();
		}
		if (TextUtils.isEmpty(clientId)) {
			clientId = CommonUtils.getClientId(context);
		}
		ClientInfo clientInfo = new ClientInfo(userId, clientId);
		saveClientInfo(context, clientInfo);
		
		return clientInfo;
	}

	private static ProgressDialog progressDialog;
	public static void showProgressDialog(Context context) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
		}
		progressDialog.show();
	}
	
	public static void disMissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}
}
