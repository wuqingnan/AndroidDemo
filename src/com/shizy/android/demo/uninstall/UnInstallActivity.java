package com.shizy.android.demo.uninstall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class UnInstallActivity extends Activity {
	
	private static final String TAG = UnInstallActivity.class.getSimpleName();
	
	// 监听进程pid
	private int mObserverProcessPid = -1;

	static {
		System.loadLibrary("uninstall");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// API level小于17，不需要获取userSerialNumber
		if (Build.VERSION.SDK_INT < 17) {
			mObserverProcessPid = UnInstallManager.listen(null);
		}
		// 否则，需要获取userSerialNumber
		else {
			mObserverProcessPid = UnInstallManager.listen(getUserSerial());
		}
	}

	// 由于targetSdkVersion低于17，只能通过反射获取
	private String getUserSerial() {
		Object userManager = getSystemService("user");
		if (userManager == null) {
			Log.e(TAG, "userManager not exsit !!!");
			return null;
		}

		try {
			Method myUserHandleMethod = android.os.Process.class.getMethod(
					"myUserHandle", (Class<?>[]) null);
			Object myUserHandle = myUserHandleMethod.invoke(
					android.os.Process.class, (Object[]) null);

			Method getSerialNumberForUser = userManager.getClass().getMethod(
					"getSerialNumberForUser", myUserHandle.getClass());
			long userSerial = (Long) getSerialNumberForUser.invoke(userManager,
					myUserHandle);
			return String.valueOf(userSerial);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "", e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "", e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "", e);
		} catch (InvocationTargetException e) {
			Log.e(TAG, "", e);
		}
		return null;
	}
}
