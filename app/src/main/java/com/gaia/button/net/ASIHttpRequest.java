package com.gaia.button.net;

import android.os.Handler;
import android.os.Message;

import com.gaia.button.data.PreferenceManager;
import com.gaia.button.model.AccountInfo;
import com.gaia.button.utils.ConstantUtil;
import com.gaia.button.utils.DcError;
import com.gaia.button.utils.FileHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.FutureTask;
import java.util.zip.GZIPInputStream;

public class ASIHttpRequest implements Runnable {
	private FutureTask<Object> mTask = new FutureTask<Object>(this, null);
	private static final int BUFFER_SIZE = 1024 * 10; // 10k byte
	// default support resume broken transfer
	private String mUrl;
	private boolean mStop = false;
	private String mDownloadDstFilePath;
	private String mAccpetEcoding;
	private boolean mIsDownLoad = false;
	private String mContentType;
	private String mRequestMethod;
	private Object mRequestData;
	private Handler mCbkHandler;
	private int mRequestTag;
	// indicate whether support Resume broken transfer
	private boolean mIsSupportBt = true;
	private int mRepeatCount = 0;

	public Handler getCbkHandler() {
		return this.mCbkHandler;
	}

	public void setCbkHandler(Handler cbkhandler) {
		this.mCbkHandler = cbkhandler;

	}

	public int getRequestTag() {
		return this.mRequestTag;
	}

	public void setRequestTag(int requestTag) {
		this.mRequestTag = requestTag;
	}

	public Object getRequestData() {
		return mRequestData;
	}

	public void setRequestData(Object requestdata) {
		this.mRequestData = requestdata;
	}

	public String getRequestMethod() {

		return mRequestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.mRequestMethod = requestMethod;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public boolean getStop() {
		return mStop;
	}

	public void cancelRequest() {
		this.mStop = true;
	}

	public String getDownloadDstFilePath() {
		return mDownloadDstFilePath;
	}

	public void setDownloadDstFilePath(String downloadDstFilePath) {
		this.mDownloadDstFilePath = downloadDstFilePath;
		this.mIsDownLoad = true;
	}

	public String getAccpetEcoding() {
		return mAccpetEcoding;
	}

	public void setAccpetEcoding(String mAccpetEcoding) {
		this.mAccpetEcoding = mAccpetEcoding;
	}

	public boolean isIsDownLoad() {
		return mIsDownLoad;
	}

	public void setIsDownLoad(boolean isDownLoad) {
		this.mIsDownLoad = isDownLoad;
	}

	public String getContentType() {
		return mContentType;
	}

	public void setContentType(String contentType) {
		this.mContentType = contentType;
	}

	public void sendCbkMessage(Message msg) {

		if (this.mCbkHandler != null) {

			if (msg == null) {
				Message _msg = new Message();
				_msg.what = 9;

				mCbkHandler.sendMessage(_msg);
			} else {
				mCbkHandler.sendMessage(msg);
			}
		}
	}

	private OutputStream initOutPutIO() throws IOException {
		OutputStream res = null;

		if (this.mIsDownLoad) {
			// create the folder
			new File(mDownloadDstFilePath.substring(0,
					mDownloadDstFilePath.lastIndexOf("/"))).mkdirs();

			// use a temp file and rename after finish download
			if (mIsSupportBt) {
				res = new FileOutputStream(mDownloadDstFilePath + ".temp", true);
			} else {
				res = new FileOutputStream(mDownloadDstFilePath + ".temp");
			}
		} else {
			res = new ByteArrayOutputStream();
		}
		return res;
	}

	private void handleNetRequest() {
		// HttpClient client = HttpClientHelper.getHttpClient();
		HttpClient client = HttpClientHelper.getHttpClient();
		HttpPost postRequest = null;
		HttpGet getRequest = null;
		HttpResponse response = null;
		// end
		OutputStream outputio = null;
		InputStream inio = null;
		try {
			do {
				byte[] bs = null;
				if (mDownloadDstFilePath != null) {
					setIsDownLoad(true);
					getRequest = HttpClientHelper.getHttpGetRequest(mUrl);
					if (this.mIsSupportBt) {
						long tempfileSize = FileHelper
								.getFileSize(mDownloadDstFilePath + ".temp");
						getRequest.setHeader("RANGE", "bytes=" + tempfileSize
								+ "-");
					}
					try {
						response = client.execute(getRequest);
					} catch (ConnectTimeoutException timeoutE0) {
						throw timeoutE0;
					} catch (Exception e) {
						client = HttpClientHelper.getHttpClient();
						try {
							response = client.execute(getRequest);
						} catch (ConnectTimeoutException timeoutE1) {
							throw timeoutE1;
						} catch (Exception ex) {
							client = HttpClientHelper.getHttpClient();
							try {
								response = client.execute(getRequest);
							} catch (ConnectTimeoutException timeoutE) {
								throw timeoutE;
							} catch (Exception ex1) {
								handleErrorEvent(
										"receive data length not equals specify length",
										DcError.DC_NET_GENER_ERROR);
								mTask.cancel(true);
								return;
							}
						}
					}
				} else {

					setIsDownLoad(false);

					// if (this.mRequestTag ==
					// ConstantUtil.Net_Tag_Get_ChapterInfo || this.mRequestTag
					// == ConstantUtil.Net_Tag_Get_ChapterList ||
					// this.mRequestTag == ConstantUtil.Net_Tag_Get_Select)
					// {
					// postRequest = HttpClientHelper.getHttpPostRequest(mUrl,
					// true);
					// StringEntity reqEntity = new StringEntity(mRequestData,
					// "UTF-8");
					// postRequest.setEntity(reqEntity);
					// }
					// else
					// {

					if (NetConfig.isGet) {
						getRequest = HttpClientHelper.getHttpGetRequest(mUrl);
					} else {
						// postRequest =
						// HttpClientHelper.getHttpPostRequest(mUrl,
						// false);
						postRequest = HttpClientHelper.getHttpPostRequest(mUrl,
								false);

						String token = "";
						AccountInfo user = PreferenceManager.getInstance().getAccountInfo();
						if (user != null) {
							token = user.getToken();
						}
						postRequest.setHeader("Cookie", "token=" + token);

						if (ConstantUtil.HTTP_DATA_TYPE == NetConfig.SubmitDataType.JSON) {
							StringEntity reqEntity = new StringEntity(
									(String) mRequestData, "UTF-8");
							postRequest.setEntity(reqEntity);
						} else {
							HttpEntity entity = new UrlEncodedFormEntity(
									(ArrayList) mRequestData, HTTP.UTF_8);
							postRequest.setEntity(entity);
						}

					}

					// 设置参数，仿html表单提交
					// List<NameValuePair> paramList = new
					// ArrayList<NameValuePair>();
					// ArrayList paramList=new ArrayList();
					// BasicNameValuePair param = new
					// BasicNameValuePair("mobile", "15822089098");
					// paramList.add(param);
					// BasicNameValuePair param2 = new
					// BasicNameValuePair("sms_code", "8988");
					// paramList.add(param2);
					// HttpEntity entity = new UrlEncodedFormEntity(paramList,
					// HTTP.UTF_8);
					// postRequest.setEntity(entity);

					// }

					if (this.mStop) {
						// modify by wenzutong
						throw new StopRequest(1, "request canceled");
					}
					try {
						if (NetConfig.isGet) {
							response = client.execute(getRequest);
						} else {
							response = client.execute(postRequest);
						}
					} catch (ConnectTimeoutException timeoutE0) { // add by
						throw timeoutE0;
					} catch (Exception e) {
						client = HttpClientHelper.getHttpClient();
						try {
							if (NetConfig.isGet) {
								response = client.execute(getRequest);
							} else {
								response = client.execute(postRequest);
							}
						} catch (ConnectTimeoutException timeoutE1) { // add by
							throw timeoutE1;
						} catch (Exception ex) {
							client = HttpClientHelper.getHttpClient();
							try {
								if (NetConfig.isGet) {
									response = client.execute(getRequest);
								} else {
									response = client.execute(postRequest);
								}
							} catch (ConnectTimeoutException timeoutE) { // add
								throw timeoutE;
							} catch (Exception ex1) {
								handleErrorEvent(
										"receive data length not equals specify length",
										DcError.DC_NET_GENER_ERROR);
								NetConfig.isGet = false;
								mTask.cancel(true);
								return;
							}
						}
					}
				}
				NetConfig.isGet = false;
				outputio = this.initOutPutIO();
				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode == HttpStatus.SC_OK
						|| responseCode == HttpStatus.SC_PARTIAL_CONTENT) {

					// String results =
					// EntityUtils.toString(response.getEntity());

					int currentReadbyteCount = 0;
					// long responseDataLen = response.getEntity()
					// .getContentLength();
					long havedownDataSize = 0;
					inio = response.getEntity().getContent();
					BufferedInputStream bis = new BufferedInputStream(inio);

					bis.mark(2);

					// 取前两个字节

					byte[] header = new byte[2];

					int result = bis.read(header);

					// reset输入流到开始位置

					bis.reset();

					// 判断是否是GZIP格式
					if (result != -1 && getShort(header) == 0x1f8b) {

						inio = new GZIPInputStream(bis);
					} else {
						inio = bis;
					}

					byte[] tempBytes = new byte[BUFFER_SIZE];

					// if (this.mIsDownLoad && this.mIsSupportBt)
					// {
					// havedownDataSize = FileHelper
					// .getFileSize(mDownloadDstFilePath + ".temp");
					// responseDataLen += havedownDataSize;
					// }
					while ((currentReadbyteCount = inio.read(tempBytes)) != -1) {
						if (this.mStop) {
							throw new StopRequest(1, "request canceled");
						}
						havedownDataSize += currentReadbyteCount;
						// if is download request
						// then need create the progress event and
						// handle the event to UI layer
						if (this.mIsDownLoad) {
							// handleDownLoadingEvent(responseDataLen,
							// havedownDataSize);
						}
						try {// 友盟统计bug修复
							outputio.write(tempBytes, 0, currentReadbyteCount);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						} catch (OutOfMemoryError e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					}
					if (this.mStop) {
						throw new StopRequest(1, "request canceled");
					}

					// if (responseDataLen != -1)
					// {
					// if (havedownDataSize != responseDataLen)
					// {
					// // error happen
					// handleErrorEvent(
					// "receive data length not equals specify length",
					// DcError.DC_NET_DATA_ERROR);
					// break;
					// }
					// else if (havedownDataSize == responseDataLen)
					// {
					if (!this.mIsDownLoad) {
						bs = ((ByteArrayOutputStream) outputio).toByteArray();
					} else {
						File file = new File(mDownloadDstFilePath + ".temp");
						file.renameTo(new File(mDownloadDstFilePath));
					}
					handleSuccessEvent(bs);
					// }
					// }
					// else
					// {
					// LogUtil.v(
					// "response data length is -1");
					// {
					// // When download file
					// handleErrorEvent("content len is error",
					// DcError.DC_NET_GENER_ERROR);
					// }
					//
					// }
				} else if (responseCode == HttpStatus.SC_MOVED_PERMANENTLY
						|| responseCode == HttpStatus.SC_MOVED_TEMPORARILY) {
					String redirectUrl = response.getFirstHeader("location")
							.getValue();
					if (redirectUrl != null && redirectUrl.length() > 0) {
						setUrl(redirectUrl);
						handleNetRequest();
					}
				} else {
					if (responseCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
						handleErrorEvent("connect time out",
								DcError.DC_NET_TIME_OUT);
					} else if (responseCode == -1 && mRepeatCount == 0) {
						mRepeatCount = 1;
						handleNetRequest();
					} else {
						String codestr = String.format("Net Error Code: %d",
								responseCode);
						String msgstr = String.format("Net Error Msg: %s",
								response.getStatusLine().getReasonPhrase());


						handleErrorEvent("net error",
								DcError.DC_NET_GENER_ERROR);
					}
				}

			} while (false);

			// close the io pipe
			if (inio != null) {
				inio.close();
				inio = null;
			}

			if (outputio != null) {
				outputio.close();
				outputio = null;
			}

		} catch (ConnectTimeoutException timeoutEx) {
			timeoutEx.printStackTrace();
			handleErrorEvent("exception happen", DcError.DC_NET_TIME_OUT);
		} catch (StopRequest stopEx) {
			// TODO Auto-generated catch block
			mTask.cancel(true);
			stopEx.printStackTrace();
			handleCancelEvent("request canceled");
		} catch (Exception e) {
			e.printStackTrace();
			handleErrorEvent("exception happen", DcError.DC_NET_GENER_ERROR);
		} finally {

			if (inio != null) {
				inio = null;
			}

			if (outputio != null) {
				outputio = null;
			}

			// add by wenzutong, 2012-01-10,显示断开连接
			if (postRequest != null) {
				postRequest.abort();
				postRequest = null;
			}
			if (getRequest != null) {
				getRequest.abort();
				getRequest.abort();
			}
		}
	}

	private void handleDownLoadingEvent(long totalSize, long currentSize) {
		// create custom message instance and
		// set parameter
		NetMessage msg = new NetMessage();
		msg.setMessageType(NetMessage.NetMessageType.NetDownloadling);
		msg.setTotalSize(totalSize);
		msg.setCurentSize(currentSize);
		msg.setRequestId(this.hashCode());

		// create system message instance
		Message sysmsg = new Message();
		sysmsg.obj = msg;

		sendCbkMessage(sysmsg);
	}

	private void handleErrorEvent(String netError, int errorCode) {

		NetMessage msg = new NetMessage();
		if (this.mIsDownLoad) {
			msg.setMessageType(NetMessage.NetMessageType.NetDownloadFailure);
			File file = new File(mDownloadDstFilePath + ".temp");
			file.delete();
		} else {
			msg.setMessageType(NetMessage.NetMessageType.NetFailure);
			msg.setErrorCode(errorCode);
		}
		msg.setErrorString(netError);
		msg.setRequestId(this.hashCode());

		// create system message instance
		Message sysmsg = new Message();
		sysmsg.obj = msg;

		sendCbkMessage(sysmsg);
	}

	private void handleSuccessEvent(byte[] responseStr) {
		NetMessage msg = new NetMessage();
		if (this.mIsDownLoad) {
			msg.setMessageType(NetMessage.NetMessageType.NetDownloadSuccess);
		} else {
			msg.setMessageType(NetMessage.NetMessageType.NetSuccess);

			String decryptData = null;

			// 解密 decrypt responseStr
			// AES myaes = new AES();
			// decryptData = myaes.aesDecrypt(new String(responseStr));
			//
			// LogUtil.d("下行数据 === " + decryptData);
			// msg.setResponseString(decryptData.getBytes());

			BaseResult res = null;
			try {
				String resString = new String(responseStr, "UTF-8");
				res = new JsonHelper().parserWithTag(this.mRequestTag, resString);
				msg.setResponseData(res);
				msg.setResponesStr(resString);

			} catch (JSONException e) {
				// 捕获JSON异常
				e.printStackTrace();
				msg.setErrorCode(DcError.DC_JSON_PARSER_ERROR);
				msg.setErrorString("parse json error");
			} catch (Exception ex) {
				// net request failed

				if (res != null) {
					res.setErrorCode(DcError.DC_NET_DATA_ERROR);
				}
				msg.setErrorCode(DcError.DC_NET_DATA_ERROR);
				msg.setErrorString("receive data error");
			} finally {

			}
		}
		msg.setRequestId(this.hashCode());

		// create system message instance
		Message sysmsg = new Message();
		sysmsg.obj = msg;

		sendCbkMessage(sysmsg);
	}

	/*
	 * @Transfer request cancel event to UI layer
	 * 
	 * @Param cancelStr specify the cancel reason and not use yet
	 */
	private void handleCancelEvent(String cancelStr) {

		NetMessage msg = new NetMessage();
		msg.setMessageType(NetMessage.NetMessageType.NetCancel);
		msg.setRequestId(this.hashCode());

		// create system message instance
		Message sysmsg = new Message();
		sysmsg.obj = msg;

		sendCbkMessage(sysmsg);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		handleNetRequest();
	}

	private int getShort(byte[] data) {
		return (int) ((data[0] << 8) | data[1] & 0xFF);
	}
}
