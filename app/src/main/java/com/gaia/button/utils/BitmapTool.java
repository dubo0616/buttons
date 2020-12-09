package com.gaia.button.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BitmapTool {

	public static final int DEFAULT_WIDTH = 480; // 图片宽度限制
	public static final int DEFAULT_HEIGHT = 800; // 图片高度限制
	public static final String IMAGE_TYPE_JPG = "image/jpeg";
	public static final String IMAGE_TYPE_PNG = "image/png";
	public static final String IMAGE_TYPE_GIF = "image/gif";

	//图片尺寸压缩梯度
	public static final long SIZE_COMPRESS_MAX = 1024*1024L;
	//图片质量压缩阀值
	public static final long QUALITY_COMPRESS_MAX = 200*1024L;
	//压缩图像比例
	public static final int COMPRESS_QUALITY = 80;
	
	private static final int QUALITY = 90; // 压缩比，0~100， 越小图片质量越差

	/**
	 * 将图片文件进行质量压缩
	 * 
	 * @param srcPath: 原图片文件保存路径
	 * 
	 */
	public static void compressFile(String srcPath) {
		File file = new File(srcPath);
		if (file.exists()) {
			if(file.length() > QUALITY_COMPRESS_MAX) {
				//文件
				String type = getTypeFromPath(srcPath);
				if (!TextUtils.isEmpty(type)) {
					// 支持的图片文件类型
					Bitmap destbmp = null;
					//步骤一: 从文件读取图片
					Bitmap bitmap = loadBitmapFromFile(srcPath);
					if(null != bitmap) {
						try{
							//步骤二: Bitmap质量压缩
							byte[] bitmapByte = compressImage(bitmap, type);
							if(bitmapByte != null && bitmapByte.length > 0) {						
								// 把压缩后的数据baos存放到ByteArrayInputStream中
								ByteArrayInputStream isBm = new ByteArrayInputStream(bitmapByte);
								try{
									destbmp = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
								}catch(OutOfMemoryError e) {
									e.printStackTrace();
									if(null != destbmp && !destbmp.isRecycled()) {
										destbmp.recycle();
										destbmp = null;
									}
								}
							}
							Bitmap tmpBmp = (null != destbmp) ? destbmp : bitmap;
							//步骤三: 将压缩图片保存为临时图片
							String destpath = srcPath + ".tmp";
							saveFile(tmpBmp, type, destpath);
							//步骤四: 将临时文件重命名为目标文件
							File destFile = new File(destpath);
							if(destFile.exists()) {
								file.delete();
								destFile.renameTo(file);
							}
						} catch(IOException ex) {
							ex.printStackTrace();
						} finally {
							if(null != bitmap && !bitmap.isRecycled()) {
								bitmap.recycle();
								bitmap = null;

							}
							if(null != destbmp && !destbmp.isRecycled()) {
								destbmp.recycle();
								destbmp = null;
							}
							System.gc();
						}
					}
				}
			}
		}
	}

	/**
	 * 从文件加载Bitmap图片对象
	 *
	 * @param srcPath: 图片文件路径
	 *
	 * @return 压缩后的Bitmap对象
	 */
	public static Bitmap loadBitmapFromFile(String srcPath) {
		Bitmap bmp = null;
		//获取图片文件宽度和高度
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		try {
			newOpts.inSampleSize = 1;//设置缩放比例 1为不缩放
			newOpts.inJustDecodeBounds = false; //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			newOpts.inInputShareable = true;
			newOpts.inPurgeable = true;
			bmp = BitmapFactory.decodeFile(srcPath, newOpts);
		} catch (OutOfMemoryError e) {
			if(null != bmp) {
				bmp.recycle();
				bmp = null;
				System.gc();
			}
			e.printStackTrace();
		}
		return bmp;
	}

	/**
	 * 根据给定的长宽分辨率，提取重采样后的位图
	 * 
	 * @param filename
	 * @param width
	 * @param height
	 * @param exact
	 *            是否准确按照最小尺寸压缩(只对超过大小的图片进行精确压缩)
	 * @return
	 */
	public static Bitmap loadResizedBitmap(String filename, int width,
                                           int height, boolean exact) {
		Bitmap resizedBmp = null;
		int minScale = Math.min(width, height);
		int maxScale = Math.max(width, height);
		int minOriScale;
		int maxOriScale;
		int scale = 1;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		minOriScale = Math.min(options.outHeight, options.outWidth);
		maxOriScale = Math.max(options.outHeight, options.outWidth);
		options.inSampleSize = 1; // inSample:1 表示不做重采样，图片大小不改变
		boolean isSampleSized = false;
		boolean flag = false;
		if (minOriScale > minScale || maxOriScale > maxScale) {
			isSampleSized = true;
			flag = (minOriScale / (float) maxOriScale) > (minScale / (float) maxScale);
			if (flag) {
				scale = (int) (maxOriScale / (float) maxScale);
			} else {
				scale = (int) (minOriScale / (float) minScale);
			}
		}

		options.inJustDecodeBounds = false;
		options.inSampleSize = scale;
		resizedBmp = BitmapFactory.decodeFile(filename, options);

		// 如果图片本身分辨率比设定值小，就直接使用原图
		if (exact && isSampleSized && resizedBmp != null) {
			int minNewScale = Math.min(options.outWidth, options.outHeight);
			int maxNewScale = Math.max(options.outWidth, options.outHeight);
			float newScale = flag ? maxScale / (float) maxNewScale : minScale / (float) minNewScale;
			int newHeight = (int) (options.outHeight * newScale);
			int newWidth = (int) (options.outWidth * newScale);
			resizedBmp = Bitmap.createScaledBitmap(resizedBmp, newWidth, newHeight, false);
		}
		return resizedBmp;
	}

	/**
	 * 按尺寸压缩图片
	 * @param filename
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap reLoadBitmapSize(String filename, int width, int height) {
		Bitmap resizedBmp = null;
		try {
			int scale = 1;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			try {
				BitmapFactory.decodeFile(filename, options);
			} catch (Exception e) {
				e.printStackTrace();
			}
			options.inSampleSize = 1; // inSample:1 表示不做重采样，图片大小不改变 大于1按照压缩处理
			if ((options.outHeight != 0
					&& options.outWidth != 0
					&& options.outHeight * options.outWidth < 200
							* 200)
					|| (options.outHeight == 0)
					|| (options.outWidth == 0)) {
				Toast.makeText(ContextHolder.getContext(), "图片尺寸太小，请重新选择图片！", Toast.LENGTH_SHORT).show();
				return null;
			}
			double oriScale = options.outHeight * options.outWidth;
			if (oriScale != 0) {
				double pixScale = width * height * 2f;
				scale = (int) Math.ceil(oriScale / pixScale + 0.5f);
			}
			options.inJustDecodeBounds = false;
			//必须大于等于1
			options.inSampleSize = Math.max(1, scale);
			resizedBmp = BitmapFactory.decodeFile(filename, options);
		} catch (OutOfMemoryError e) {
			Toast.makeText(ContextHolder.getContext(), "内存不足请重新选择图片！", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resizedBmp;
	}

	// 生成各种图片格式: jp(e)g:image/jpeg png:image/png gif:image/gif
	public static String getTypeFromPath(String imagePath) {
		int pos = imagePath.lastIndexOf('.') + 1;
		String type = imagePath.substring(pos);
		String result = "";
		if (type.equalsIgnoreCase("jpg") || type.equalsIgnoreCase("jpeg")) {
			result = IMAGE_TYPE_JPG;
		} else if (type.equalsIgnoreCase("png")) {
			result = IMAGE_TYPE_PNG;
		} else if (type.equalsIgnoreCase("gif")) {
			result = IMAGE_TYPE_GIF;
		} else {
			LogUtil.v("zsz", "ImageType Invalid: " + type);
		}
		return result;
	}

	/**
	 * 给定图片格式，将位图压缩为该格式的字节流
	 * 
	 * @param bmp
	 * @param type
	 * @return
	 */
	public static byte[] compressImage(Bitmap bmp, String type) {
		//图片质量比例值
		int quality = 100;
		boolean isFirst = true;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while (true) {
				baos.reset();//重置baos即清空baos
				if (type.equals(IMAGE_TYPE_JPG)) {
					bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
				} else if (type.equals(IMAGE_TYPE_PNG)) {
					bmp.compress(Bitmap.CompressFormat.PNG, quality, baos);
				}
				if (isFirst && baos.toByteArray().length <= SIZE_COMPRESS_MAX) {
					isFirst = false;
					break;
				} else if(baos.toByteArray().length > SIZE_COMPRESS_MAX && quality > COMPRESS_QUALITY) {
					quality -= 5; //每次都减少5
				} else {
					break;
				}
			};
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * 由位图BMP生成真实的JPG图片
	 * 
	 * @param bmp
	 * @param filePath
	 */
	public static void creatJPGFromBitmap(Bitmap bmp, String filePath) {
		File avatarFile = new File(filePath);
		FileOutputStream bos = null;
		try {
			bos = new FileOutputStream(avatarFile);
			//将Bitmap压缩保存ss
			bmp.compress(Bitmap.CompressFormat.JPEG,BitmapTool.QUALITY, bos);
			bos.flush();
			bos.close();
			bos = null;
			//图片保存成功释放Bitmap对象
			if (bmp != null && !bmp.isRecycled()) {
				bmp.recycle();
				bmp = null;
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(null != bos) {
				//关闭文件输出流
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 放大或者缩小图片
	 * 
	 * @param bitmap
	 * @param oriWidth
	 * @param oriHeight
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bitmap, int oriWidth, int oriHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) oriWidth / width);
		float scaleHeight = ((float) oriHeight / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	/**
	 * Decode and sample down a bitmap from a file to the requested width and
	 * height.
	 * 
	 * @param filename
	 *            The full path of the file to decode
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return A bitmap sampled down from the original with the same aspect
	 *         ratio and dimensions that are equal to or greater than the
	 *         requested width and height
	 */
	public static Bitmap decodeBitmapFromFile(String filename, int reqWidth,
                                              int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = (int) calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	public static float calculateInSampleSize(BitmapFactory.Options options,
                                              int w, int h) {
		final float height = options.outHeight;
		final float width = options.outWidth;
		float reqWidth = w;
		float reqHeight = h;

		float inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final float heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final float widthRatio = Math.round((float) width
					/ (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	/**
	 * 由Drawable对象，获得位图Bitmap对象
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
				: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 由位图转化为字节流
	 * 
	 * @param bmp
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, baos);
		return baos.toByteArray();
	}

	/**
	 * 设置圆角
	 * 
	 * @param bitmap
	 * @param roundPx
	 *            导角幅度
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,
                                                final float roundPx) {
		if (bitmap == null) {
			return null;
		}
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// final float roundPx = 12;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/**
	 * 手动回收ImageView的图片资源
	 * 
	 * @param imageView
	 */
	public static void releaseImageViewResouce(ImageView imageView) {
		if (imageView == null)
			return;
		Drawable drawable = imageView.getDrawable();
		if (drawable != null && drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			Bitmap bitmap = bitmapDrawable.getBitmap();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) throws Exception {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = (int) calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;

		Bitmap temp = null;
		try {
			temp = BitmapFactory.decodeResource(res, resId, options);
		} catch (OutOfMemoryError e) {
			try {
				options.inSampleSize = 2;
				temp = BitmapFactory.decodeResource(res, resId, options);
			} catch (OutOfMemoryError e2) {
				try {
					options.inSampleSize = 4;
					temp = BitmapFactory.decodeResource(res, resId, options);
				} catch (OutOfMemoryError e3) {
                    try {
                        options.inSampleSize = 8;
                        temp = BitmapFactory.decodeResource(res, resId, options);
                    } catch (OutOfMemoryError e4) {
                        try {
                            
                            options.inSampleSize = 16;
                            temp = BitmapFactory.decodeResource(res, resId, options);
                        } catch (Exception e5) {
                            try {
                                throw new Exception();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
				}
			}
		}
		return temp;
	}

	public static Bitmap decodeSampledBitmapFromFile(Resources res,
                                                     String fileName, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// BitmapFactory.decodeResource(res, resId, options);
		BitmapFactory.decodeFile(fileName, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = (int) calculateInSampleSize(options, reqWidth,
				reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;

		Bitmap temp = null;
		try {
			temp = BitmapFactory.decodeFile(fileName, options);
			;
		} catch (OutOfMemoryError e) {
			try {
				options.inSampleSize = 2;
				temp = BitmapFactory.decodeFile(fileName, options);
			} catch (OutOfMemoryError e2) {
				try {
					options.inSampleSize = 4;
					temp = BitmapFactory.decodeFile(fileName, options);
				} catch (OutOfMemoryError e3) {
					options.inSampleSize = 8;
					temp = BitmapFactory.decodeFile(fileName, options);
				}
			}
		}
		return temp;
	}

	// public static int calculateInSampleSize(BitmapFactory.Options options,
	// int reqWidth, int reqHeight) {
	// // 源图片的高度和宽度
	// final int height = options.outHeight;
	// final int width = options.outWidth;
	// int inSampleSize = 1;
	// if (height > reqHeight || width > reqWidth) {
	// // 计算出实际宽高和目标宽高的比率
	// final int heightRatio = Math.round((float) height
	// / (float) reqHeight);
	// final int widthRatio = Math.round((float) width / (float) reqWidth);
	// // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
	// // 一定都会大于等于目标的宽和高。
	// inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	// }
	// return inSampleSize;
	// }
	/**
	 * 读取assets文件夹下指定的图片
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 获取本地图片,防止OOM
	 * 
	 * @param dst
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmapFromFile(File dst, int width, int height) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options(); // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height); // 这里一定要将其设置回false，因为之前我们将其设置成了true
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	
	/** 
	 * 将图片纠正到正确方向 
	 *  
	 * @param degree ： 图片被系统旋转的角度 
	 * @param bitmap ： 需纠正方向的图片 
	 * @return 纠向后的图片 
	 */  
	public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
	    Matrix matrix = new Matrix();
	    matrix.postRotate(degree);  
	  
	    Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
	            bitmap.getHeight(), matrix, true);  
	    return bm;  
	}

	/**
	 * 保存文件
	 *
	 * @param bmp
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFile(Bitmap bmp, String type, String fileName) throws IOException {
		File myCaptureFile = new File(fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		if (type.equals(IMAGE_TYPE_JPG)) {
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		} else if (type.equals(IMAGE_TYPE_PNG)) {
			bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
		}
		bos.flush();
		bos.close();
	}
	public static Bitmap getImageFromNet(String url) {
		HttpURLConnection conn = null;
		try {
			URL mURL = new URL(url);
			conn = (HttpURLConnection) mURL.openConnection();
			conn.setRequestMethod("GET"); //设置请求方法
			conn.setConnectTimeout(10000); //设置连接服务器超时时间
			conn.setReadTimeout(5000);  //设置读取数据超时时间
			conn.connect(); //开始连接
			int responseCode = conn.getResponseCode(); //得到服务器的响应码
			if (responseCode == 200) {
				//访问成功
				LogUtil.d("dubo", "responseCode：" + responseCode);
				InputStream is = conn.getInputStream(); //获得服务器返回的流数据
				Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象
				return bitmap;

			} else {
				//访问失败	LogUtil.d("dubo", "responseCode：" + responseCode);
				LogUtil.d("dubo", "responseCode：" + responseCode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect(); //断开连接
			}
		}
		return null;
	}

}
