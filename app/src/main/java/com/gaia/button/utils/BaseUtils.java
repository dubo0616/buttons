package com.gaia.button.utils;

import android.app.ActivityManager;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.gaia.button.GaiaApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class BaseUtils {
	private static final String TAG = "BaseUtils";
	private static int currentVersionCode = -1;
	private static String currentVersionName = null;

    public static boolean hasIceCreamSandwich(){
        return  Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

	public static boolean hasHoneycombMR2() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
	}

    public static boolean hasJellyBean() {
        //return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    	return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean hasEclair() {
    	return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
    }

	public static boolean isTablet(Context context) {
		int screenLayoutMask = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		return (screenLayoutMask == 4)// Configuration.SCREENLAYOUT_SIZE_XLARGE = 4
				|| (screenLayoutMask == Configuration.SCREENLAYOUT_SIZE_LARGE);
	}

	public static boolean isAppInstalled(Context context, String name) {
		if (context == null || name == null || name.isEmpty())
			return false;
		PackageManager pm = context.getPackageManager();
		try {
			int status = pm.getApplicationEnabledSetting(name);
			return status == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT || status == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
		}
		catch (IllegalArgumentException e) {
		}
		catch (NullPointerException e) { // fix https://app.crittercism.com/developers/crash-details/1aaaa0f8def86aba780eb93005b06f66233ea33924565a7a2e9e8e4a
		}
		return false;
	}

	public static String getCpuFeatures() {
		FileReader fr = null;
		BufferedReader localBufferedReader = null;
		StringBuilder sb = new StringBuilder();
		try {
			fr = new FileReader("/proc/cpuinfo");
			localBufferedReader = new BufferedReader(fr, 8192);
			String s;
			while ((s = localBufferedReader.readLine()) != null) {
				s = s.trim();
				if (s.startsWith("Features")) {
					String[] arrayOfString = s.split("\\s+");
					if (arrayOfString.length >= 2) {
						for (int i = 2; i < arrayOfString.length; i++) {
							sb.append(arrayOfString[i]);
							sb.append(" ");
						}
					}
				}
			}
		}
		catch (Throwable e) {
		}
		finally {
			if (localBufferedReader != null)
				try {
					localBufferedReader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			if (fr != null)
				try {
					fr.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		}
		return sb.toString();
	}

	/**
	 * 检查CPU是否有指定的属性，默认算作有
	 *
	 * @param feature
	 * @return
	 */
	public static boolean cpuHasFeature(String feature) {
		FileReader fr = null;
		BufferedReader localBufferedReader = null;
		try {
			fr = new FileReader("/proc/cpuinfo");
			localBufferedReader = new BufferedReader(fr, 8192);
			String s;
			while ((s = localBufferedReader.readLine()) != null) {
				s = s.trim();
				if (s.startsWith("Features")) {
					String[] arrayOfString = s.split("\\s+");
					if (arrayOfString.length >= 2) {
						boolean hasFeatrue = false;
						for (int i = 2; i < arrayOfString.length; i++) {
							if (arrayOfString[i] != null && arrayOfString[i].trim().equalsIgnoreCase(feature)) {
								hasFeatrue = true;
								break;
							}
						}
						return hasFeatrue;
					}
				}
			}
		}
		catch (Throwable e) {
		}
		finally {
			if (localBufferedReader != null)
				try {
					localBufferedReader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			if (fr != null)
				try {
					fr.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
		}
		return true;
	}

	 /**
     * Checks if OpenGL ES 2.0 is supported on the current device.
     *
     * @param context the context
     * @return true, if successful
     */
    public static boolean supportsOpenGLES2(final Context context) {
        final ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.reqGlEsVersion >= 0x20000;
    }


    private static String GlEsVersionString = null;

	//获取OpenGLES的版本号
	public static String supportsOpenGLVersion(final Context context) {
		if (TextUtils.isEmpty(GlEsVersionString)) {
			final ActivityManager activityManager = (ActivityManager)
					context.getSystemService(Context.ACTIVITY_SERVICE);
			final ConfigurationInfo configurationInfo =
					activityManager.getDeviceConfigurationInfo();
			GlEsVersionString = configurationInfo.getGlEsVersion();
		}
		return GlEsVersionString;
	}


	public static boolean inList(Iterable<String> list, String model) {
		if (model == null || list == null)
			return false;
		// model = model.toUpperCase(Locale.ENGLISH);
		for (String blankModel : list) {
			if (model.startsWith(blankModel))
				return true;
		}
		return false;
	}

	public static boolean showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragmet, String tag) {
		if(fragmentManager == null || fragmentManager.isDestroyed()) return false;
		try {
			// 用官方推荐的方法：
			FragmentTransaction ft = fragmentManager.beginTransaction();
			Fragment prev = fragmentManager.findFragmentByTag(tag);
			if (prev != null) {
				ft.remove(prev);
			}
//			ft.addToBackStack(null);

			// Create and show the dialog.
			dialogFragmet.show(ft, tag);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int getDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 两个日期，是不是同一自然天
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameDay(long time1, long time2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(time1);
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(time2);
		return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
	}

	/**
	 * time1是不是time2的明天
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isTomorrow(long time1, long time2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(time1);
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(time2);
		return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)+1 && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
	}

	public static Drawable createShapeDrawable(int color, int cornerRadius) {
		PaintDrawable drawable = new PaintDrawable(color);
		// drawable.setPaddingRelative(padding, padding, padding, padding);
		drawable.setCornerRadius(cornerRadius);
		return drawable;
	}

	public static Drawable createShapeDrawable(int colorNormal, int colorPressed, int cornerRadius) {
		Drawable normal = createShapeDrawable(colorNormal, cornerRadius);
		if (colorPressed == colorNormal)
			return normal;
		Drawable press = createShapeDrawable(colorPressed, cornerRadius);
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[] { android.R.attr.state_pressed }, press);
		drawable.addState(new int[] {}, normal);
		return drawable;
	}



	/**
	 * 数组拼接
	 *
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] arrayConcat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * 设置TextView的上下左右drawable，并catch OOM
	 *
	 * @param tv
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return 是否设置成功
	 */
	public static boolean setTextCompoundDrawableWithCatchOOM(TextView tv, int left, int top, int right, int bottom) {
		if (tv == null)
			return false;
		try {
			tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
			return true;
		}
		catch (OutOfMemoryError e) {
			return false;
		}
	}

	/**
	 * 设置ImageView的drawable，并catch OOM
	 *
	 * @param imageView
	 * @param resID
	 */
	public static void setImageResourceWithCatchOOM(ImageView imageView, int resID) {
		// 20141023 从com.roidapp.photogrid.cloud.ShareFinish移到这里来
		if (imageView == null)
			return;
		try {
			imageView.setImageResource(resID);
		}
		catch (OutOfMemoryError e) {
			e.printStackTrace();
			imageView.setImageResource(0);
			imageView.setImageBitmap(null);
		}
	}

	public static int getAppVersionCode(Context context) {
		if (currentVersionCode == -1) {
			try {
				PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				currentVersionCode = packageInfo.versionCode;
				currentVersionName = packageInfo.versionName;
			}
			catch (NameNotFoundException e) {
				// should never happen
				// throw new RuntimeException("Could not get package name: " + e);
				return -1;
			}
		}
		return currentVersionCode;
	}

	public static String getAppVersionName(Context context) {
		if (currentVersionName == null) {
			try {
				PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
				currentVersionCode = packageInfo.versionCode;
				currentVersionName = packageInfo.versionName;
			}
			catch (NameNotFoundException e) {
				// should never happen
				// throw new RuntimeException("Could not get package name: " + e);
				return null;
			}
		}
		return currentVersionName;
	}

	public static int[] randomSort(int[] seed) {
		int len = seed.length;
		int[] result = new int[len];
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			int r = random.nextInt(len - i);
			result[i] = seed[r];
			seed[r] = seed[len - 1 - i];
		}
		return result;
	}


	private static String PAGENAME = null;

	public static String getPkgName(Context context) {
		if (!TextUtils.isEmpty(PAGENAME)) {
			return PAGENAME;
		}
		ComponentName cn = new ComponentName(context, context.getClass());
		return PAGENAME = cn.getPackageName();
	}

	public static String getPkgName() {
		if (!TextUtils.isEmpty(PAGENAME)) {
			return PAGENAME;
		}
		Context context = GaiaApplication.getInstance();
		ComponentName cn = new ComponentName(context, context.getClass());
		return PAGENAME = cn.getPackageName();
	}

	/**
	 * 获取一个可用的目录
	 *
	 * @param dirName 目录名
	 * @return
	 */
	public static File getAvailableDir(String dirName) {
		File dir;
		dir = BaseUtils.getAvailableFilesDir(dirName);
		if (dir == null) {
			dir = BaseUtils.getAvailableCacheDir(dirName);
		}
		return dir;
	}

	/**
	 * 获取应用私有文件目录, 此目录无需存储权限
	 * getExternalFilesDir(如果path为空) : /storage/emulated/0/Android/data/com.alive.android/files
	 * getExternalFilesDir(如果path不为空) : /storage/emulated/0/Android/data/com.alive.android/files/path
	 * getFilesDir : /data/user/0/com.alive.android/files
	 *
	 * @param dirName 前后带不带"/"最后得到的路径末尾都不带"/"
	 */
	public static File getAvailableFilesDir(String dirName) {
		File filesDir = null;
		Context context = GaiaApplication.getInstance().getApplicationContext();

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			filesDir = context.getExternalFilesDir(dirName);
		}
		if (filesDir == null) {
			filesDir = context.getFilesDir();
		}

		return filesDir;
	}

	/**
	 * 获取应用私有缓存目录, 此目录无需存储权限
	 * getExternalCacheDir : /storage/emulated/0/Android/data/com.alive.android/cache
	 * getCacheDir : /data/user/0/com.alive.android/cache
	 */
	public static File getAvailableCacheDir(String dirName) {
		File cacheDir = null;
		Context context = GaiaApplication.getInstance().getApplicationContext();

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			cacheDir = context.getExternalCacheDir();
		}
		if (cacheDir == null) {
			cacheDir = context.getCacheDir();
		}

		if (!TextUtils.isEmpty(dirName)) {
			File file = new File(cacheDir.getPath() + File.separator + dirName);
			if (!file.exists()) {
				file.mkdirs();
			}
			return file;
		}

		return cacheDir;
	}

	/**
	 * 根据uri信息获取文件名
	 *
	 * @param uri 文件的uri信息
	 */
	public static String getFileNameByUri(Uri uri) {
		if (uri == null) {
			return "";
		}
		String fileName = "";
		Cursor cursor = null;
		try {
			cursor = GaiaApplication.getInstance().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				fileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(cursor);
		}
		return fileName;
	}

	/**
	 * 判断android 10文件是否存在的方法
	 *
	 * @param uri
	 * @return
	 */
	public static boolean isAndroidQFileExists(Uri uri) {
		AssetFileDescriptor afd = null;
		ContentResolver cr = GaiaApplication.getInstance().getApplicationContext().getContentResolver();
		try {
			afd = cr.openAssetFileDescriptor(uri, "r");
			if (afd == null) {
				return false;
			}
		} catch (FileNotFoundException e) {
			return false;
		} finally {
			close(afd);
		}
		return true;
	}

	public static void close(ParcelFileDescriptor afd) {
		if (afd != null) {
			try {
				afd.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(AssetFileDescriptor afd) {
		if (afd != null) {
			try {
				afd.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(OutputStream outputStream) {
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(InputStream inputStream) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(Cursor cursor) {
		if (cursor != null) {
			try {
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}







	/**
	 *  将字符串颜色转换为color
	 * @param colorStr 字符串颜色
	 * @param defaultColor 默认
	 * @return color
	 */
	public static int getColor(String colorStr, int defaultColor) {
		int color = defaultColor;
		if (TextUtils.isEmpty(colorStr) || TextUtils.equals("-1", colorStr)) {
			return color;
		}
		try {
			color = Color.parseColor(colorStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return color;
	}

	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isButtonDevice(BluetoothDevice device) {
		if(device == null || TextUtils.isEmpty(device.getAddress()) || TextUtils.isEmpty(device.getName())){
			return false;
		}
		if(device.getAddress().startsWith("F4:0E") && (device.getName().contains("Buttons") ||
				device.getName().contains("BUTTONS")) || device.getName().contains("buttons")){
			return true;
		}
		return false;
	}

}
