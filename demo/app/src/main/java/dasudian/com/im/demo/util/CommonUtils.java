package dasudian.com.im.demo.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.UUID;

public class CommonUtils {

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	private static float sDensity = 0;

	/**
	 * DP转换为像素
	 * 
	 * @param context
	 * @param nDip
	 * @return
	 */
	public static int dipToPixel(Context context, int nDip) {
		if (sDensity == 0) {
			final WindowManager wm = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			sDensity = dm.density;
		}
		return (int) (sDensity * nDip);
	}

	/**
	 * 获取设备IMEI码
	 * 
	 * @param context
	 * @return
	 */
	public static String getImei(Context context) {
		String mImei = "NULL";
		try {
			mImei = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
			System.out.println("获取IMEI码失败");
			mImei = "NULL";
		}
		return mImei;
	}

	/**
	 * 获取设备唯一id
	 * @param context
	 * @return
	 */
	public static String getClientId(Context context) {
		// 在Android6.0后需要动态申请权限，不然app会崩溃
		int permissionCheck = ContextCompat.checkSelfPermission(context,
				Manifest.permission.READ_PHONE_STATE);
		if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
			return UUID.randomUUID().toString();
		}

		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();
		return deviceId;
	}
}
