package com.stcyclub.testhtmlgetlinkman;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.stcyclub.testhtmlgetlinkman.application.MyApplication;
import com.stcyclub.testhtmlgetlinkman.dialog.LodingDialog;
import com.stcyclub.testhtmlgetlinkman.po.LinkMan;

public class MyJavaScript {  
	/**获取库Phon表字段**/  
    private static final String[] PHONES_PROJECTION = new String[] {  
    Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };  

	public static final int CAMERA  = 0x01;
    /**联系人显示名称**/  
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;  
      
    /**电话号码**/  
    private static final int PHONES_NUMBER_INDEX = 1;  
      
    /**头像ID**/  
    private static final int PHONES_PHOTO_ID_INDEX = 2;  
     
    /**联系人的ID**/  
    private static final int PHONES_CONTACT_ID_INDEX = 3;  
      
    /**联系人名称**/  
    private ArrayList<String> mContactsName = new ArrayList<String>();  
      
    /**联系人头像**/  
    private ArrayList<String> mContactsNumber = new ArrayList<String>();  
 
    /**联系人头像**/  
    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>(); 
    /**保存联系人**/
    private ArrayList<LinkMan> linkMans = new ArrayList<LinkMan>();  
      
	 private final int DIALOG          = 0;
	 private final int REQUEST_CONTACT = 1;
    private WebView webview;  
    //使用一个handler来处理加载事件  
    private Handler handler;  
     private Context mContext;
    public MyJavaScript(Context context,Handler handler){  
        this.handler = handler;  
        webview = (WebView) ((Activity)context).findViewById(R.id.myweb); 
         mContext  =  context;
    }  
//    /* 
//     * java调用显示网页，异步 
//     */  
//    public void show(){  
//      handler.post(new Runnable() {           
//        public void run() {  
//       // 重要：url的生成,传递数据给网页  
//        String url = "javascript:contactlist('" + generateData() + "')";  
//        webview.loadUrl(url);  
//        }  
//       });  
//    }  
//   
    /* 
     * 拨打电话方法 
     */  
    @JavascriptInterface
    public void call(final String phone){
          Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + phone));
          mContext.startActivity(intent);
    }
    
    /**
     * 调用发送短信接口
     * @param phone 电话号码
     * @param str_content 短信内容
     */
    @JavascriptInterface
    public void sendMsg(String phone,String str_content){  
    	String content;
    	try {
    		content = new String(str_content.getBytes(),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			 Toast.makeText(mContext, "发送失败!", Toast.LENGTH_LONG).show();//提示成功   
			e.printStackTrace();
		} 
    	SmsManager manager_sms = SmsManager.getDefault();//得到短信管理器
    	//由于短信可能较长，故将短信拆分
    	 ArrayList<String> texts = manager_sms.divideMessage(str_content); 
    	for(String text : texts){
    		manager_sms.sendTextMessage(phone, null, text, null, null);//分别发送每一条短信
    	}
    	    Toast.makeText(mContext, "发送成功!", Toast.LENGTH_LONG).show();//提示成功   	   
    } 
    /**
     * 调用系统发布短信界面
     * @param phone 电话号码
     * @param str_content 短信内容
     * @return 
     */

    @JavascriptInterface
    public void systemSendMsg(String phone,String str_content){
    	String content;
    	try {
    		content = new String(str_content.getBytes(),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			 Toast.makeText(mContext, "发送失败!", Toast.LENGTH_LONG).show();//提示成功   
			e.printStackTrace();
		} 
    	Uri uri = Uri.parse("smsto:" + phone);
    	Intent it = new Intent(Intent.ACTION_SENDTO, uri);          
    	it.putExtra("sms_body", str_content);          
    	mContext.startActivity(it);
    }
    @JavascriptInterface
    public void androidAlert(String s ){
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }
    /**
     * 获取所有的联系人
     */
    @JavascriptInterface
    public void getAllLinkMan(){
    	 Intent intent = new Intent();
         intent.setAction(Intent.ACTION_PICK);
         intent.setData(ContactsContract.Contacts.CONTENT_URI);
         ((MainActivity) mContext).startActivityForResult(intent, REQUEST_CONTACT);
    }
   
    /**
     * 获取手机联系人
     */
    @JavascriptInterface
    public String getPhoneLinkMan(){
    	//清空linkMans
    	linkMans.clear();
    	//得到ContentResolver对象
    	//super.onCreate(savedInstanceState);
        ContentResolver cr = mContext.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //向下移动光标
        while(cursor.moveToNext())
        {
            //取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
           //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            while(phone.moveToNext())
            {
                String Number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); 
               // string += (contact + ":" + Number + "");
                LinkMan man=new LinkMan(contact, Number);
                Log.d("TAG",contact + ":" + Number + "");
                linkMans.add(man);
            }
        }
        cursor.close();
        Log.d("TAG", "linkMans.size()"+linkMans.size());
//        handler.post(new Runnable() {           
//            public void run() {  
//           // 重要：url的生成,传递数据给网页  
//            String url = "javascript:contactlist('" + ObjectToJson() + "')";  
//            webview.loadUrl(url);  
//            }  
//           });  
        String json=ObjectToJson();
    	return json;
    }
   
    /**
     * 将对象转换成为json数据
     */
    public String ObjectToJson(){
    	 try {    
	        //将构造好json数组
	        JSONArray arr = new JSONArray(); 
	        for(int i =0; i<linkMans.size();i++){
	        	 //构造一个json对象  
    	        JSONObject obj = new JSONObject();  
    	        obj.put("id", i);  
    	        obj.put("name", linkMans.get(i).getName());  
    	        obj.put("phone", linkMans.get(i).getPhone());
    	        arr.put(obj);  
	        }
	        return arr.toString();  
	        } catch (JSONException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	        return ""; 
    }
    @JavascriptInterface
    public void getNext(String url,String direction,int dialog){
    //	webview.loadUrl("file:///android_asset/index2.html");
    	//webview.loadUrl("http://www.baidu.com");
//    	Uri uri=Uri.parse("file:///android_asset/index2.html");
//    	Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//    	mContext.startActivity(intent);
    	if(dialog==1){
    		MyApplication.ld=new LodingDialog(((MainActivity)mContext));	
    		MyApplication.ld.show();
    	}
    	Intent intent=new Intent(mContext,MainActivity2.class);
    	Log.d("Tag", "url"+url+"direction"+direction);
    	intent.putExtra("url", url);
    	((Activity)mContext).startActivity(intent);
    	((Activity) mContext).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);  
		//ld.dismiss();
    }
    /**
     * 调用手机相机
     */
    @JavascriptInterface
    public void getCamera(){
    	 Intent intent = new Intent();
		 Intent intent_camera = mContext.getPackageManager()
				.getLaunchIntentForPackage("com.android.camera");
		if (intent_camera != null) {
			intent.setPackage("com.android.camera");
		}
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(""));// file是拍照保存文件
		((Activity) mContext).startActivityForResult(intent, CAMERA);// r
    }
}  
