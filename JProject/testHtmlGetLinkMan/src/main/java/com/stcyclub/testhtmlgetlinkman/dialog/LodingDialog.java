package com.stcyclub.testhtmlgetlinkman.dialog;



import com.stcyclub.testhtmlgetlinkman.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

public class LodingDialog extends Dialog {
	

	public LodingDialog(Context ctx){
		super(ctx);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//dialog后台不允许操作
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.loading);
	}
	

}
