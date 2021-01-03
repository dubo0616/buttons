package com.gaia.button.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class FileHelper {

	/**
	 * 判断文件或者文件目录是否存在
	 * 
	 * @return
	 */
	public static boolean fileIfExists(String filePath) {
		try {
			if (!isExternalStorageReadable()) {
				return false;
			}
			File f = new File(filePath);
			if (f.exists()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/** 检查外部存储是否可读 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)
				|| Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}

	/*
	 * @Remove a file or directory According to file path
	 */
	public static boolean removeFile(String filepath) {

		File file = new File(filepath);
		if (file.exists()) {
			return removeFile(file);
		}
		return false;
	}

	public static boolean crateFile(String dirpath, String fileName) {

		try {
			File dirFile = new File(dirpath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File myCaptureFile = new File(dirpath + "/" + fileName);
			if (!myCaptureFile.exists()) {
				myCaptureFile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static String getJsonStringFromJsonFile(String filePath) {

		String result = "";
		if (fileIfExists(filePath)) {
			FileInputStream fis = null;
			ByteArrayOutputStream outBa = null;
			DataOutputStream dos = null;
			try {
				fis = new FileInputStream(new File(filePath));
				outBa = new ByteArrayOutputStream();
				dos = new DataOutputStream(outBa);
				int currentCount = 0;
				byte[] tempBuffer = new byte[1024];
				while ((currentCount = fis.read(tempBuffer)) != -1) {
					dos.write(tempBuffer, 0, currentCount);
				}
				dos.flush();
				result = outBa.toString();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (outBa != null) {
						outBa.close();
					}
					if (dos != null) {
						dos.close();
					}
					if (fis != null) {
						fis.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
		return result;
	}

	/*
	 * @Remove a file or directory According to an file instance
	 */
	public static boolean removeFile(File file) {
		if (file != null && file.isDirectory()) {

			String[] childlist = file.list();

			for (int i = 0; i < childlist.length; i++) {
				boolean success = removeFile(new File(file, childlist[i]));

				if (!success) {
					return false;
				}
			}
		} else if (file != null) {
			return file.delete();
		}
		return false;
	}

	/** 获取文件夹路径 */
	private static long getDirectorySize(File parent) {
		long fileSize = 0;
		if(parent.exists() && parent.isDirectory()) {
			File[] childs = parent.listFiles();
			for (File child : childs) {
				if(child.isDirectory()) {
					//如果是文件夹计算文件夹大小
					fileSize += getDirectorySize(child);
				} else {
					//如果是文件计算文件大小
					fileSize += getSingleFileSize(child);
				}
			}
		}
		return fileSize;
	}

	public static long getSingleFileSize(File file) {
		return file.length();
	}

	public static long getFileSize(String filePath) {
		long fileSize = 0;
		if(fileIfExists(filePath)) {
			File parent = new File(filePath);
			if(parent.isDirectory()) {
				fileSize = getDirectorySize(parent);
			} else {
				fileSize = getSingleFileSize(parent);
			}
		}
		return fileSize;
	}

	/*
	 * @Check whether support sd card
	 */
	public static boolean isSupportSDCard() {
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * @Create directory with folder and the filepath should be like ""
	 */
	public static boolean createDirectory(String filepath) {

		File newfile = new File(filepath);
		boolean r = false;
		if (!newfile.exists())
			r = newfile.mkdirs();

		return r;
	}

	public static String getAppDataDirectoryPath() {
		boolean r = isSupportSDCard();
		File file = null;
		if (r) {
			file = Environment.getExternalStorageDirectory();
		} else {
			file = ContextHolder.getContext().getFilesDir();
		}

		return file.getPath();
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param path
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean deleteFolder(String path) {
		boolean flag = false;
		File file = new File(path);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(path);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(path);
			}
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param path
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String path) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		File dirFile = new File(path);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param path
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String path) {
		boolean flag = false;
		File file = new File(path);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	// public static int getBankIconResourceId(String bankId) {
	//
	// int sourceId = 0;
	//
	// try {
	// int id = Integer.parseInt(bankId);
	//
	// // '1' => '工商银行',
	// // '2' => '农业银行',
	// // '3' => '中国银行',
	// // '4' => '建设银行',
	// // '9' => '民生银行',
	// // '5' => '招商银行',
	// // '6' => '兴业银行',
	// // '7' => '光大银行',
	// // '8' => '平安银行',
	// // '9' => '民生银行',
	// // '10' => '广发银行',
	// // '11' => '邮政储蓄银行',
	// // '12' => '邯郸银行',
	//
	// switch (id) {
	// case 1:
	// sourceId = R.drawable.icon_bank_icbc;// 工商
	// break;
	// case 2:
	// sourceId = R.drawable.icon_bank_abc;// 农行
	// break;
	// case 3:
	// sourceId = R.drawable.icon_bank_bc;// 中行
	// break;
	// case 4:
	// sourceId = R.drawable.icon_bank_ccb;// 建行
	// break;
	// case 5:
	// sourceId = R.drawable.icon_bank_cmb;// 招商
	// break;
	// case 6:
	// sourceId = R.drawable.icon_bank_cib;// 兴业
	// break;
	// case 7:
	// sourceId = R.drawable.icon_bank_ceb;// 广大
	// break;
	// case 8:
	// sourceId = R.drawable.icon_bank_pabc;// 平安
	// break;
	// case 9:
	// sourceId = R.drawable.icon_bank_cmsb;// 民生
	// break;
	// case 10:
	// sourceId = R.drawable.icon_bank_guangfa;// 广发
	// break;
	// case 11:
	// sourceId = R.drawable.icon_bank_psbc;// 邮政储蓄银行
	// break;
	// case 12:
	// sourceId = R.drawable.icon_bank_handan;// 邯郸银行
	// break;
	//
	// default:
	// break;
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return sourceId;
	//
	// }

	/**
	 * 获取下载存放的路径
	 */
	public static String getDownloadDir() {

		String path = Config.downloadDir;
		return confirmDir(path);
	}

	/**
	 * 获取cache存放的路径
	 */
	public static String getCacheDir() {

		String path = Config.cacheDir;
		return confirmDir(path);
	}

	public static String getRootDir() {

		String dir = existsSd() ? Environment.getExternalStorageDirectory()
				.toString() : getPackageDir();
		return dir;
	}

	private static String confirmDir(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return dir;
	}

	public static String getPackageDir() {
		return ContextHolder.getContext().getFilesDir().getPath();
	}

	/**
	 * 判断是否存在sdk卡
	 * 
	 * @return
	 */
	public static boolean existsSd() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	// 获取pkgName的版本号
	public static int getVersionCode(String pkgName, Context cnt) {
		List<PackageInfo> packages = cnt.getPackageManager()
				.getInstalledPackages(0);
		PackageInfo packageInfo;
		for (int i = 0; i < packages.size(); i++) {
			packageInfo = packages.get(i);
			if (pkgName.equals(packageInfo.packageName)) {
				return packageInfo.versionCode;
			}
		}

		return 0;
	}

	/**
	 * 发送消息
	 * 
	 * @param handler
	 *            句柄
	 * @param funcName
	 *            方法名
	 * @param params
	 *            方法参数
	 */
	public static void sendMessage(Handler handler, String funcName,
                                   String... params) {
		if (handler == null) {
			return;
		}

		List<Object> args = new ArrayList<Object>();
		args.add(funcName);
		for (String str : params) {
			args.add(str);
		}

		Message msg = Message.obtain();
		msg.what = args.size() - 1;
		msg.obj = args;
		handler.sendMessage(msg);
	}

	/**
	 * 获取句柄
	 * 
	 * @param className
	 *            类
	 * @param instance
	 *            实例
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Handler getHandler(final Class className,
                                     final Object instance) {
		return new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 反射调用消息的方法
				@SuppressWarnings("unchecked")
                ArrayList<Object> args = (ArrayList<Object>) msg.obj;
				String funcName = args.remove(0).toString();

				Class[] cs = new Class[msg.what];
				for (int i = 0; i < msg.what; i++) {
					cs[i] = String.class;
				}

				try {
					Method method = null;
					method = className.getMethod(funcName, cs);
					method.invoke(instance, args.toArray());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};
	}

	public static boolean getImageFromNetAndSave(String url, String savePath,
                                                 String fileName) {
		try {
			byte[] bytes = getImage(url);
//			Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			
			InputStream input = null;
	        Bitmap bitmap = null;
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = 2;
	        input = new ByteArrayInputStream(bytes);
	        SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(BitmapFactory.decodeStream(input, null, options));
	        bitmap = (Bitmap) softRef.get();
	        if (bytes != null) {
	        	bytes = null;
	        }
	        try {
	            if (input != null) {
	                input.close();
	            }
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	        	e.printStackTrace();
	        }
			return saveFile(bitmap, fileName, savePath);
		} catch (OutOfMemoryError ooe) {
			ooe.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static boolean saveFile(Bitmap bm, String fileName, String savePath)
			throws IOException {

		try {
			File dirFile = new File(savePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File myCaptureFile = new File(savePath + "/" + fileName);
			if (!myCaptureFile.exists()) {
				myCaptureFile.createNewFile();
			}
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static boolean copyFile(String oldPath, String newPath) {
		boolean isok = true;
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			File newfile = new File(newPath.substring(0,
					newPath.lastIndexOf("/")));
			if (!newfile.exists()) {
				newfile.mkdirs();
			}
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				// int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				fs.flush();
				fs.close();
				inStream.close();
			} else {
				isok = false;
			}
		} catch (Exception e) {
			// System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			isok = false;
		}
		return isok;
	}

	/**
	 * 读取assets目录下的文件，输出字符串
	 * 
	 * @param filePath
	 * @param context
	 * @return
	 * @throws IOException
	 */
	public static String readAssetsFile(String filePath, Context context)
			throws IOException {
		InputStream is = context.getAssets().open(filePath);
		return readInputStream(is);
	}

	private static String readInputStream(InputStream is) throws IOException {
		InputStreamReader read = new InputStreamReader(is);
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		StringBuilder sb = new StringBuilder();
		while ((lineTxt = bufferedReader.readLine()) != null) {
			sb.append(lineTxt);
		}
		is.close();
		read.close();
		return sb.toString();
	}
	public static void createFolder(){
		createDirectory(Environment.DIRECTORY_DOWNLOADS+"/Gaia");
	}
}
