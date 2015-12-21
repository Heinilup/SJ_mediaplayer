package com.zousj.sjplayer.bindservice;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.Toast;

import com.zousj.sjplayer.domain.AudioItem;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service {
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            play();
        }
    };
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    };
    private MediaPlayer.OnErrorListener mOnErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(getApplicationContext(),"播放出错",Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    public MusicPlayerService() {
    }

    private ArrayList<AudioItem> audioItems;
    /**
     * 当前播放的音频信息
     */
    private AudioItem currentAudioItem;
    /**
     * 音频列表中的位置
     */
    private int currentPosition;
    /**
     * 播放器：可以播放本地视频、本地音乐、网络视频和音乐
     */
    private MediaPlayer mediaPlayer;
    private IMusicPlayerService.Stub iBinder = new IMusicPlayerService.Stub() {
        MusicPlayerService service = MusicPlayerService.this;


        @Override
        public void openAudio(int position) throws RemoteException {
            try {
                service.openAudio(position);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void play() throws RemoteException {
                 service.play();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            service.seekTo(position);

        }

        @Override
        public void setPlayMode(int mode) throws RemoteException {
            service.setPlayMode(mode);
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
       return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getAllAudio();
    }

    /**
     * 根据位置去打开一个音频文件并播放
     * @param position
     */
    private void openAudio(int position) throws IOException {
        currentPosition = position;
       AudioItem  currentAudioItem = audioItems.get(position);
        if(mediaPlayer != null){
            mediaPlayer.reset();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(mOnPreparedListener);
        mediaPlayer.setOnCompletionListener(mOnCompletionListener);
        mediaPlayer.setOnErrorListener(mOnErrorListener);
        mediaPlayer.setDataSource(currentAudioItem.getData());
        mediaPlayer.prepareAsync();//一般用这个异步准备
  /*      mediaPlayer.prepare();//同步准备，播放本地资源用*/

    }

    /**
     * 播放
     */
    private void play(){
        if(mediaPlayer != null){
            mediaPlayer.start();
        }

    }
    /**
     * 暂停
     */
    private void pause(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }

    }

    /**
     * 歌唱者名称
     * @return
     */
    private String getArtist(){
        return "";

    }

    /**
     * 歌曲名称
     * @return
     */
    private String getName(){
        return "";

    }

    /**
     * 得到歌曲的总时长
     * @return
     */
    private  int getDuration(){
        return 0;
    }
    /**
     * 得到歌曲当前播放位置
     * @return
     */
    private  int getCurrentPosition(){
        return 0;
    }

    /**
     * 定位到音频的播放位置
     * @param position
     */
    private void seekTo(int position){

    }

    /**
     * 设置播放歌曲的模式：顺序、单曲、全部
     * @param mode
     */
    private void setPlayMode(int mode){

    }

    /**
     * 上一曲
     */
    private void pre(){

    }
    /**
     * 下一曲
     */
    private void next(){

    }

    /**
     * 加载音频--在子线程加载视频
     */

    private void getAllAudio() {
        new Thread(){
            public void run(){
                //把子线程视频读取出来
                audioItems = new ArrayList<AudioItem>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.Audio.Media.TITLE,//标题或者使用DISPLAY_NAME
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ARTIST
                };
                Cursor cursor =	resolver.query(uri, projection , null, null, null);
                while (cursor.moveToNext() ) {
                    long size = cursor.getLong(2);
                    //具体音频大小信息
                    if(size> 1*1024*1024){
                        AudioItem item = new AudioItem();

                        String title = cursor.getString(0);
                        item.setTitle(title);

                        String duration = cursor.getString(1);
                        item.setDuration(duration);


                        item.setSize(size);

                        String data = cursor.getString(3);
                        item.setData(data);

                        String artist = cursor.getString(4);
                        item.setData(artist);

                        audioItems.add(item);
                    }
                }

            }
        }.start();

    }
}
