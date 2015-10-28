package com.stcyclub.testhtmlgetlinkman;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int CAMERA = 0x01;
	private String contactId, contactName;
	private WebView webView;
	private Handler handler = new Handler();
	// 自定义的弹出框类
	SelectPicPopupWindow menuWindow;
	ValueCallback<Uri> mUploadMessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
		findViewById(R.id.testWebView).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				webView.loadUrl("javascript:testWebView('android传过去的！')");
			}
		});
	}

	private void init() {
		webView = (WebView) findViewById(R.id.myweb);
		// 重要：让webview支持javascript
		webView.getSettings().setJavaScriptEnabled(true);
		// 重要：添加可以供html中可供javascript调用的接口类
		webView.addJavascriptInterface(new MyJavaScript(this, handler),
				"myjavascript");
		// 加载index.html
		webView.loadUrl("file:///android_asset/index.html");
//		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebChromeClient(new WebChromeClient() {
			// For Android 3.0+
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
				if (mUploadMessage != null) return;
				mUploadMessage = uploadMsg;
				selectImage();
			}

			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				openFileChooser(uploadMsg, "");
			}

			// For Android  > 4.1.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
				openFileChooser(uploadMsg, acceptType);
			}

		});
	}

	public void btnClick(View v) {
		// 实例化SelectPicPopupWindow
		menuWindow = new SelectPicPopupWindow(MainActivity.this, itemsOnClick);
		// 显示窗口
		menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

	}
	/**
	 * 检查SD卡是否存在
	 *
	 * @return
	 */
	public final boolean checkSDcard() {
		boolean flag = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!flag) {
			Toast.makeText(this, "请插入手机存储卡再使用本功能",Toast.LENGTH_SHORT).show();
		}
		return flag;
	}
	String compressPath = "";
	protected final void selectImage() {
		if (!checkSDcard())
			return;
		String[] selectPicTypeStr = { "camera","photo" };
		new AlertDialog.Builder(this)
				.setItems(selectPicTypeStr,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								switch (which) {
									// 相机拍摄
									case 0:
										openCarcme();
										break;
									// 手机相册
									case 1:
										chosePic();
										break;
									default:
										break;
								}
								compressPath = Environment
										.getExternalStorageDirectory()
										.getPath()
										+ "/fuiou_wmp/temp";
								new File(compressPath).mkdirs();
								compressPath = compressPath + File.separator
										+ "compress.jpg";
							}
						}).show();
	}
	/**
	 * 本地相册选择图片
	 */
	private void chosePic() {
		FileUtils.delFile(compressPath);
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
		String IMAGE_UNSPECIFIED = "image/*";
		innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
		Intent wrapperIntent = Intent.createChooser(innerIntent, null);
		startActivityForResult(wrapperIntent, REQ_CHOOSE);
	}
	String imagePaths;
	Uri  cameraUri;
	public static final int FILECHOOSER_RESULTCODE = 1;
	private static final int REQ_CAMERA = FILECHOOSER_RESULTCODE+1;
	private static final int REQ_CHOOSE = REQ_CAMERA+1;
	/**
	 * 打开照相机
	 */
	private void openCarcme() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		imagePaths = Environment.getExternalStorageDirectory().getPath()
				+ "/fuiou_wmp/temp/"
				+ (System.currentTimeMillis() + ".jpg");
		// 必须确保文件夹路径存在，否则拍照后无法完成回调
		File vFile = new File(imagePaths);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		cameraUri = Uri.fromFile(vFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(intent, REQ_CAMERA);
	}
	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {

			default:
				break;
			}
		}
	};

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("TAG", "requestCode" + requestCode + "\t resultCode" + resultCode
				+ "\t" + data);
		String filename = null;
		String name = null;
		if (requestCode == CAMERA && resultCode == Activity.RESULT_OK
				&& null != data) {
			String sdState = Environment.getExternalStorageState();
			if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
				Log.d("Tag", "sd card unmount");
				return;
			}
			new DateFormat();
			name = DateFormat.format("yyyyMMdd_hhmmss",
					Calendar.getInstance(Locale.CHINA))
					+ ".jpg";
			Bundle bundle = data.getExtras();
			// 获取相机返回的数据，并转换为图片格式
			Bitmap bitmap = (Bitmap) bundle.get("data");
			FileOutputStream fout = null;
			File file = new File("/sdcard/pintu/");
			if (!file.exists()) {
				file.mkdirs();
			}
//			filename = file.getPath() + "/" + name;
//			 try {
//				 fout = new FileOutputStream(filename);
//				 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);	
//			 } catch (FileNotFoundException e) {
//				 e.printStackTrace();
//			 } finally {
//			 try {
//				 fout.flush();
//				 fout.close();
//			 } catch (IOException e) {
//				 e.printStackTrace();
//			 }
//			 }
			
			String str ="iVBORw0KGgoAAAANSUhEUgAAAKsAAAC0CAYAAADraNxXAAAEaElEQVR4Xu3YS04UahhFUWgxMYbNmOjT0kACKct6ija2Z92mQTn/t1cqlfv4+vr648F/LhC4wCOsgUomflwAVhAyF4A1k8pQWBnIXADWTCpDYWUgcwFYM6kMhZWBzAVgzaQyFFYGMheANZPKUFgZyFwA1kwqQ2FlIHMBWDOpDIWVgcwFYM2kMhRWBjIXgDWTylBYGchcANZMKkNhZSBzAVgzqQyFlYHMBWDNpDIUVgYyF4A1k8pQWBnIXADWTCpDYWUgcwFYM6kMhZWBzAVgzaQyFFYGMheANZPKUFgZyFwA1kwqQ2FlIHMBWDOpDIWVgcwFYM2kMhRWBjIXgDWTylBYGchcANZMKkNhZSBzAVgzqQyFlYHMBWDNpDIUVgYyF4A1k8pQWBnIXADWTCpDYWUgcwFYM6kMhZWBzAVgzaQyFFYGMheANZPKUFgZyFwA1kwqQ2FlIHMBWDOpDIWVgcwFYM2kMhRWBjIXgDWTylBYGchcANZMKkNhZSBzAVgzqQyFlYHMBWDNpDIUVgYyF4A1k8pQWBnIXADWTCpDYWUgcwFYM6kMhZWBzAVgzaQyFFYGMheANZPKUFgZyFwA1kwqQ6exvry8fAh4fn7+kvD5Z8c0Tv3M4Z/dQunU73v/e8e/89Kee3/nLbsqPzOL9RDIOYjXMN8D55bfdwj3/d8+/v3nsFewfXfnLNZjGKcOee2T8B6s537fJZCw/loF1qOvAZ/nufQpdukT9+np6eHt7e3ryrd8fTj3NQBWWH/7fnrqE/JerJ+fnIdYj//dS8g/f/bwZ2CF9Z9hvfbVAtbvfWv1NeAvfQ3wyfo9iLf87Vmsl/530aVPyGvfL899Z/3T/z11bectkf+Xn5nF+r8EXHoHrEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142+FNR5waT6sS7Xjb4U1HnBpPqxLteNvhTUecGk+rEu142/9CRINeTax7S6yAAAAAElFTkSuQmCC";			
			
			//bitmap=stringtoBitmap(str);
			 
			String str1=bitmaptoString(bitmap);
			Log.d("TAG....", str1.toString());
			Log.d("TAG....", str.length()+"");
			// 输出设置到html上面
			String url ="javascript:printInfo('data:image/png;base64,"+str1+"')";
			webView.loadUrl(url);

			// BitmapDrawable bd=new BitmapDrawable(bitmap);
			// image.setBackground(bd);
			// image.setImageBitmap(bitmap);;

		}
		// StringBuffer sb=new StringBuffer();
		// Log.d("TAG", "filename..."+filename);
		// Log.d("TAG",
		// "filename..."+Environment.getExternalStorageDirectory()+File.separator+"a.jpg");
		// Log.d("TAG",
		// "filename..."+"file://"+Environment.getExternalStorageDirectory()+File.separator+"pintu/"+name);
		// String fileUrl="file:///mnt/sdcard/pintu/20140221_114052.jpg";
		// // sb.append("<img src=\"file:///"+filename+"\"/>");
		// sb.append("<img src="+fileUrl+">");
		//
		// // webView.loadDataWithBaseURL(null, sb.toString(), "text/html",
		// "utf-8", null);
		// webView.loadDataWithBaseURL(null,sb.toString(), "text/html", "UTF-8",
		// null);
	}*/

	public String bitmaptoString(Bitmap bitmap) {
		// 将Bitmap转换成Base64字符串
		StringBuffer string = new StringBuffer();
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		
		try {
			bitmap.compress(CompressFormat.PNG, 100, bStream);
			bStream.flush();
			bStream.close();
			byte[] bytes = bStream.toByteArray();
			string.append(Base64.encodeToString(bytes, Base64.NO_WRAP));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("string.."+string.length());
		return string.toString();
	}
	public Bitmap stringtoBitmap(String string){
	    //将字符串转换成Bitmap类型
	    Bitmap bitmap=null;
	    try {
		    byte[]bitmapArray;
		    bitmapArray=Base64.decode(string, Base64.DEFAULT);
		    bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
	} catch (Exception e) {
		e.printStackTrace();
	}
	   
	    return bitmap;
	    }

	private String getPhoneContacts(String contactId2) {
		Cursor cursor = null;
		String name = "";
		String phone = "";
		try {
			// Uri uri = People.CONTENT_URI;
			Uri uri = ContactsContract.Contacts.CONTENT_URI;
			cursor = getContentResolver().query(uri, null,
					ContactsContract.Contacts._ID + "=?",
					new String[] { contactId }, null);
			if (cursor.moveToFirst()) {
				name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				phone = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._COUNT));
				Log.d("Tag", phone);
			} else {
				Toast.makeText(this, "No contact found.", Toast.LENGTH_LONG)
						.show();
			}
		} catch (Exception e) {
			name = "";
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return name;
	}

	/**
	 * 返回文件选择
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
									Intent intent) {
//		if (requestCode == FILECHOOSER_RESULTCODE) {
//			if (null == mUploadMessage)
//				return;
//			Uri result = intent == null || resultCode != RESULT_OK ? null
//					: intent.getData();
//			mUploadMessage.onReceiveValue(result);
//			mUploadMessage = null;
//		}

		if (null == mUploadMessage)
			return;
		Uri uri = null;
		if(requestCode == REQ_CAMERA ){
			afterOpenCamera();
			uri = cameraUri;
		}else if(requestCode == REQ_CHOOSE){
			uri = afterChosePic(intent);
		}
		mUploadMessage.onReceiveValue(uri);
		mUploadMessage = null;
		super.onActivityResult(requestCode, resultCode, intent);
	}
	/**
	 * 选择照片后结束
	 *
	 * @param data
	 */
	private Uri afterChosePic(Intent data) {

		// 获取图片的路径：
		String[] proj = { MediaStore.Images.Media.DATA };
		// 好像是android多媒体数据库的封装接口，具体的看Android文档
		Cursor cursor = managedQuery(data.getData(), proj, null, null, null);
		if(cursor == null ){
			Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
			return null;
		}
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		// 将光标移至开头 ，这个很重要，不小心很容易引起越界
		cursor.moveToFirst();
		// 最后根据索引值获取图片路径
		String path = cursor.getString(column_index);
		if(path != null && (path.endsWith(".png")||path.endsWith(".PNG")||path.endsWith(".jpg")||path.endsWith(".JPG"))){
			File newFile = FileUtils.compressFile(path, compressPath);
			return Uri.fromFile(newFile);
		}else{
			Toast.makeText(this, "上传的图片仅支持png或jpg格式",Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	/**
	 * 拍照结束后
	 */
	private void afterOpenCamera() {
		File f = new File(imagePaths);
		addImageGallery(f);
		File newFile = FileUtils.compressFile(f.getPath(), compressPath);
	}

	/** 解决拍照后在相册中找不到的问题 */
	private void addImageGallery(File file) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}

}
