package com.stcyclub.testhtmlgetlinkman;


import android.app.Activity;  
import android.content.Context;  
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;  
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.view.LayoutInflater;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.view.View.OnTouchListener;  
import android.view.ViewGroup.LayoutParams;  
import android.widget.Button;  
import android.widget.PopupWindow;  
import android.widget.Toast;
  
public class SelectPicPopupWindow2 extends PopupWindow {  
	public static final int CAMERA  = 0x01;
  
    private Button  btn_cancel;  
    private Button  getByCamera;  
    private Button  getByAlbum;  
    private View mMenuView;  
    Context ctx;
    public SelectPicPopupWindow2(Activity context) {  
        super(context);  
        this.ctx=context;
        LayoutInflater inflater = (LayoutInflater) ctx  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
       mMenuView = inflater.inflate(R.layout.mult2, null);  
       //初始化数据
       init();
    }

	private void init() {
//		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);         
//		getByAlbum = (Button) mMenuView.findViewById(R.id.getByAlbum);         
//		getByCamera = (Button) mMenuView.findViewById(R.id.getByCamera);         
//		//取消按钮  
//        btn_cancel.setOnClickListener(new OnClickListener() {  
//  
//            public void onClick(View v) {  
//                //销毁弹出框  
//                dismiss();  
//            }  
//        });   
//        //调用手机相册  
//        getByAlbum.setOnClickListener(new OnClickListener() {  
//        	
//        	public void onClick(View v) {  
//        		//销毁弹出框  
//        		dismiss();  
//        	}  
//        });   
        //调用手机相机
//        getByCamera.setOnClickListener(new OnClickListener() {  
//        	
//        	public void onClick(View v) {  
//        		Intent intent = new Intent();
//       		 Intent intent_camera = ctx.getPackageManager()
//       				.getLaunchIntentForPackage("com.android.camera");
//       		if (intent_camera != null) {
//       			intent.setPackage("com.android.camera");
//       		}
//       		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//       		//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(""));// file是拍照保存文件
//       		((Activity) ctx).startActivityForResult(intent, CAMERA);// r
//       		//销毁弹出框  
//            dismiss(); 
//        	}  
//        });   
		//设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.AnimationPreview);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {  
              
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
		
	}  
  
}  
