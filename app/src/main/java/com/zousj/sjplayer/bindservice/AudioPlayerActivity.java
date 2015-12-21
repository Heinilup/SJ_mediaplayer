package com.zousj.sjplayer.bindservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zousj.sjplayer.BaseActivity;
import com.zousj.sjplayer.R;

/**
 * Created by Administrator on 2015-11-11 0011.
 */
public class AudioPlayerActivity extends BaseActivity {
    private TextView tv_audio_artist;
    private TextView tv_audio_title;
    private TextView tv_audio_time;
    private SeekBar seekbar_audio;
    private Button btn_audio_mode;
    private Button btn_audio_back;
    private Button btn_audio_play_pause;
    private Button btn_audio_next;
    private Button btn_audio_lyric;
    private boolean isPlaying = false;
    private AnimationDrawable rocketAnimation;
    /**
     * 要播放的音频列表位置
     */
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        setListener();
        getData();
        bindService();
    }

    private void setListener() {
        //设置按钮的监听
        View.OnClickListener mOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_play_pause:
                            if (isPlaying){
                                try {
                                    service.pause();
                                    btn_audio_play_pause.setBackgroundResource(R.drawable.pause_selector);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                try {
                                    service.play();
                                    btn_audio_play_pause.setBackgroundResource(R.drawable.play_selector);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                        break;


                    default:
                        break;
                }
            }
        };

        btn_audio_play_pause.setOnClickListener(mOnclickListener);

    }



    private void initView() {
        setTitle("音乐播放");
        setRightButton(View.GONE);
        ImageView rocketImage = (ImageView) findViewById(R.id.iv_icon);
        tv_audio_artist = (TextView) findViewById(R.id.tv_audio_artist);
        tv_audio_title = (TextView) findViewById(R.id.tv_audio_title);
        tv_audio_time = (TextView) findViewById(R.id.tv_audio_time);
        seekbar_audio = (SeekBar) findViewById(R.id.seekbar_audio);
        btn_audio_mode = (Button) findViewById(R.id.btn_audio_mode);
        btn_audio_back = (Button) findViewById(R.id.btn_audio_back);
        btn_audio_play_pause = (Button) findViewById(R.id.btn_audio_play_pause);
        btn_audio_next = (Button) findViewById(R.id.btn_audio_next);
        btn_audio_lyric = (Button) findViewById(R.id.btn_audio_lyric);
        rocketImage.setBackgroundResource(R.drawable.annimation_list);
        rocketAnimation = (AnimationDrawable)rocketImage.getBackground();
        rocketAnimation.start();
    }


    private void getData() {
       position = getIntent().getIntExtra("position",0);
    }

    private IMusicPlayerService service;
    private ServiceConnection conn = new ServiceConnection() {
     /**
      * 当绑定成功的时候回调这个方法
      */
     @Override
     public void onServiceConnected(ComponentName name, IBinder ibinder) {
         service = IMusicPlayerService.Stub.asInterface(ibinder);
         if(service != null){
             try {
                 service.openAudio(position);
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
         }

     }

     /**
      * 当取消绑定的时候回调这个方法
      * @param name
      */
     @Override
     public void onServiceDisconnected(ComponentName name) {

         service = null;

     }
 };
    /**
     * 绑定方式启动服务
     */
    private void bindService() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.setAction("com.zousj.sjplayer.bindservice.audio");
        intent.putExtras(bundle);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);

    }

    @Override
    public View setContentView() {
        return View.inflate(this, R.layout.activity_audioplay, null);
    }
    @Override
    public void rightButtonClick(){
        Toast.makeText(this, "右边点击按键成功", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void leftButtonClick(){
        finish();
    }
}
