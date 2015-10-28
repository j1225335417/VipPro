package com.stcyclub.testhtmlgetlinkman.application;

import java.text.SimpleDateFormat;
import java.util.Stack;






import com.stcyclub.testhtmlgetlinkman.dialog.LodingDialog;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;
/**
 * ����ģʽ��Application
 * @author Administrator
 *
 */
public class MyApplication extends Application {
	public static LodingDialog ld=null;
	public static final String ip="http://192.168.1.134:8080/Hnne_Test/";
	public static MyApplication myApplicaton;
	Stack<Activity> stack=new Stack<Activity>();
	public static boolean autoLogin=false;     //
	public static boolean autoLoginTag=true;  //�����Զ���¼
	public static boolean checkUpdate=true;   //������
	//定义时间格式
	public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myApplicaton= MyApplication.this;
		
		Log.i("moLog", "Application已启动啦....");
	}
	
	/**
	 * ���ص�ǰʵ��
	 * @return MyApplicaton
	 */
	public static MyApplication getInstens(){
			
			return myApplicaton;
		}
	//���Activity��ջ��
	public void addActivity(Activity ac){
		stack.push(ac);  //ѹջ
	}
	//��ջ
	public void popActivity(Activity ac){
		ac.finish();
		stack.remove(ac);
	}
	//������е�Activity
	public void destoryActivity(){
		
		Log.i("moLog", "stack.size()==="+stack.size());
		for (Activity ac : stack) {
			if(ac!=null){
				ac.finish();
			}
		}
		MyApplication.this.stack.clear();  //���ջ���
		
	}
	//�˳�Ӧ�ó���
	public void myExitApp(){
		destoryActivity();
		System.exit(0);
	}
}
