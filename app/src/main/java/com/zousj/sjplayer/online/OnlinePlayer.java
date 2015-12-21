package com.zousj.sjplayer.online;

/**
 * @ Author: Shuangjun Zou (Rob)
 * @ Email:seolop@gmail.com
 * @ Data:2015/12/1
 */

import android.app.Activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zousj.sjplayer.R;

public class OnlinePlayer extends Activity implements OnPreparedListener {

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlineplayer);
    }

    public void click(View v) {
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            mMediaPlayer.reset();
			mMediaPlayer.setDataSource("http://192.168.235.167/babaqunale.mp3");
            mMediaPlayer.prepareAsync();//网络音乐.应该异步准备
            mMediaPlayer.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {//如果使用prepareAsync,应该把start()方法放到这个地方
        Toast.makeText(getApplicationContext(), "准备好了", 0).show();
        mMediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }
}
