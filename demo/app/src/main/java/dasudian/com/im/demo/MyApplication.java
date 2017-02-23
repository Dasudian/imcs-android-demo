package dasudian.com.im.demo;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dasudian.im.sdk.Connection;
import com.dasudian.im.sdk.ConnectionCallback;
import com.dasudian.im.sdk.ServerInfo;
import com.dasudian.im.sdk.ServiceException;
import com.dasudian.im.sdk.UserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class MyApplication extends Application {
	private static final String TAG = "MyApplication";
	public static Connection client;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static void connectIMServer(final String username, final String signature
					, final String serverUrl, final String appid, final String appsec, final ConnectionCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "username=" + username + ", signature=" + signature);
				String userid = "android"+username;
				String clientid = "clientid"+username;
				ServerInfo serverInfo = new ServerInfo(appid, appsec, userid,
						clientid, ServerInfo.CTYPE_ANDROID);
				UserInfo userInfo = new UserInfo(userid, username, "http://xxx.png",
						"广东/深圳", UserInfo.ENUM_SEX.MALE, signature);
				try {
					if (TextUtils.isEmpty(serverUrl)) {
						client = new Connection.Builder(serverInfo, userInfo)
								.setAucDefaultCertificate(false).setIMDefaultCertificate(false).setCallback(callback).build();
					} else {
						client = new Connection.Builder(serverInfo, userInfo).setAucURL(new URL(serverUrl))
								.setAucDefaultCertificate(false).setIMDefaultCertificate(false).setCallback(callback).build();
					}
				} catch (ServiceException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

    public static void logout() {
        if (client != null) {
            client.logout();
        }
    }
}
