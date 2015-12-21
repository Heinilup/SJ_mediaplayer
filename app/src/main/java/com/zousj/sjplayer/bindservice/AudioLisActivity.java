package com.zousj.sjplayer.bindservice;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zousj.sjplayer.BaseActivity;
import com.zousj.sjplayer.R;
import com.zousj.sjplayer.domain.AudioItem;
import com.zousj.sjplayer.utils.Utils;

import java.util.ArrayList;

public class AudioLisActivity extends BaseActivity {
	private ListView lv_audiolist;
	private TextView lv_noaudio;
	private Utils utils;
	private ArrayList<AudioItem> audioItems;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(audioItems != null&& audioItems.size()>0){
				lv_noaudio.setVisibility(View.GONE);
				lv_audiolist.setAdapter(new AudioListAdapter());

			}else {
				lv_noaudio.setVisibility(View.VISIBLE);
			}

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRightButton(View.GONE);
		setTitle("本地音乐");
		lv_audiolist = (ListView) findViewById(R.id.lv_audiolist);
		lv_noaudio = (TextView) findViewById(R.id.lv_noaudio);
		utils = new Utils();
		lv_audiolist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {

				//传入播放列表和当前点击位置
				Intent intent = new Intent(AudioLisActivity.this, AudioPlayerActivity.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}

		});
		//Load the media data from MediaStore.Audio.Media.EXTERNAL_CONTENT_URI 加载视频数据
		getAllAudio();
	}
	private class AudioListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return audioItems.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView != null) {
			view=convertView;
			holder = (ViewHolder) view.getTag();
		}else {
			 view = View.inflate(AudioLisActivity.this, R.layout.audiolist_item, null);
        holder = new ViewHolder();
        holder.vi_name = (TextView) view.findViewById(R.id.vi_name);
        holder.vi_duration = (TextView) view.findViewById(R.id.vi_duration);
        holder.vi_size = (TextView) view.findViewById(R.id.vi_size);
	    //对象关系
        view.setTag(holder);
		
		}
	   //根据位置，得到具体某一个视频的信息
        AudioItem audioItem = audioItems.get(position);
        holder.vi_name.setText(audioItem.getTitle());
        holder.vi_duration.setText(utils.stringForTime(Integer.valueOf(audioItem.getDuration())));
        holder.vi_size.setText(Formatter.formatFileSize(AudioLisActivity.this, audioItem.getSize()));
         
		return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	
		
	}
	
	static class ViewHolder{
		TextView vi_name;
		TextView vi_duration;
		TextView vi_size;
	}
	/**
	 * 在子线程加载视频
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
		handler.sendEmptyMessage(0);
		}
	}.start();
		
	}


	@Override
	public View setContentView() {
		return View.inflate(this, R.layout.activity_audiolist, null);
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
