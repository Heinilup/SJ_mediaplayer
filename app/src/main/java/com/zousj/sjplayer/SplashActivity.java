package com.zousj.sjplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SplashActivity extends Activity {
	private boolean isEnterMained = false;
	private AnimationDrawable rocketAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		ImageView rocketImage = (ImageView) findViewById(R.id.splash_icon);
		rocketImage.setBackgroundResource(R.drawable.annimation_list);
		rocketAnimation = (AnimationDrawable)rocketImage.getBackground();
		rocketAnimation.start();
		isEnterMained = false;
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			enterMain();	
			}
		}, 3000);
		
	}

	protected void enterMain(){
		
		if (!isEnterMained) {
		isEnterMained = true;
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
		}
	}
	

@Override
public boolean onTouchEvent(MotionEvent event) {
	enterMain();
	return super.onTouchEvent(event);
}

}
