package com.example.opendor;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example222.opendor.R;

public class MainActivity extends Activity {
	private LinearLayout liLayout;

	int screen_w;
	int screen_h;
	

	private static AnimatorSet mSetAnim;
	private DisplayMetrics metrics;
	int halfWidth;
	int height;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		screen_w = getWindowManager().getDefaultDisplay().getWidth();
		screen_h = getWindowManager().getDefaultDisplay().getHeight();
		
		Log.e("ffc", "screen_w  ==="+screen_w+"  screen_h== "+screen_h);
		liLayout = (LinearLayout) findViewById(R.id.split_ll);
		
		
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Log.e("byf","------------metrics.widthPixels ="+metrics.widthPixels);
		Log.e("byf","------------metrics.heightPixels ="+metrics.heightPixels);
		
		halfWidth = metrics.widthPixels/2;
		height = metrics.heightPixels;
		Log.e("byf","------------halfWidth ="+halfWidth);

		Animation animation = new TranslateAnimation(0, -(halfWidth), 0, 0);// 向左移动的动画效果
		animation.setDuration(1200);// 设置动画过程时间
		animation.setStartOffset(1000);// 设置动画延时时间//一秒后开
		animation.setRepeatCount(10);
		
		
		Animation animation1 = new TranslateAnimation(0,(float)(halfWidth), 0, 0);// 向右移动的动画效果
		animation1.setDuration(1200);
		animation1.setStartOffset(1000);
		animation1.setRepeatCount(10);

		final ImageView tt = new ImageView(this);
		tt.setLayoutParams(new LayoutParams(halfWidth, (metrics.heightPixels)));// 设置改ImageView
													// 的大小，宽为屏幕的一半，高为屏幕的高度
		// 这样做的好处可以对不同屏幕分辨率进行适配
		Bitmap leftBmp = getBitmap(0);
		tt.setImageBitmap(leftBmp);
		tt.setScaleType(ImageView.ScaleType.FIT_XY);

		final ImageView tt1 = new ImageView(this);
		tt1.setLayoutParams(new LayoutParams(halfWidth, (metrics.heightPixels)));
		Bitmap rightBmp = getBitmap(1);
		tt1.setImageBitmap(rightBmp);
		tt1.setScaleType(ImageView.ScaleType.FIT_XY);
		
		tt.setAnimation(animation);// 设置动画效果
		tt1.setAnimation(animation1);// 设置动画效果me
		

		animation.setAnimationListener(new Animation.AnimationListener() {// 设置动画监听
					@Override
					public void onAnimationStart(Animation animation) {// 动画状态开始
						//Toast.makeText(context, "I'm begin", 0).show();// 日志提示开始了
						// 如果想开们效果中出现锯齿效果，在动画开始时，在本位置，将ImageView 的背景置换一下就可以了
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {// 动画状态结束
						//tt.setVisibility(View.GONE);// 设置改控件隐藏，当然你也可以在结束这个位置放置其他处理。
					}
				});

		/**
		 * 与上一个监听一样
		 */
		animation1.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				//tt1.setVisibility(View.GONE);
			}
		});
		
		liLayout.addView(tt);
		liLayout.addView(tt1);

	}



	
	private Bitmap getBitmap(int type){
		Bitmap bmp = null;
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ttt);
		if(type == 0){
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth()/2, bmp.getHeight());
		}else if(type == 1){
			bmp = Bitmap.createBitmap(bmp, bmp.getWidth()/2, 0, bmp.getWidth()/2, bmp.getHeight());
		}
		return bmp;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

}
