package com.zousj.sjplayer.video;

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
import com.zousj.sjplayer.domain.VideoItem;
import com.zousj.sjplayer.utils.Utils;

import java.util.ArrayList;

public class VideoLisActivity extends BaseActivity {
	private ListView lv_videolist;
	private TextView lv_novideo;
	private Utils utils;
	private ArrayList<VideoItem> videoItems;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(videoItems != null&&videoItems.size()>0){
				lv_novideo.setVisibility(View.GONE);
				lv_videolist.setAdapter(new VideoListAdapter());

			}else {
				lv_novideo.setVisibility(View.VISIBLE);
			}

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRightButton(View.GONE);
		setTitle("本地视频");
		lv_videolist = (ListView) findViewById(R.id.lv_videolist);
		lv_novideo = (TextView) findViewById(R.id.lv_novideo);
		utils = new Utils();
		lv_videolist.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
/*			//根据点击位置position取出对应视频信息
			VideoItem videoItem = videoItems.get(position);
			Intent intent= new Intent(VideoLisActivity.this, VideoPlayerActivity.class);
			intent.setData(Uri.parse(videoItem.getData()));
				startActivity(intent);*/

			//在Android数据的传递一般用意图，发送和接收

            //传入播放列表和当前点击位置
			Intent intent= new Intent(VideoLisActivity.this, VideoPlayerActivity.class);
				Bundle extras = new Bundle();
				//视频列表
				extras.putSerializable("videolist",videoItems);
				intent.putExtras(extras);
				//在视频列表中点击的某个位置
				intent.putExtra("position",position);
				startActivity(intent);
			}
			
		});
		//Load the media data from MediaStore.Video.Media.EXTERNAL_CONTENT_URI 加载视频数据
		getAllVideo();
	}
	private class VideoListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return videoItems.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView != null) {
			view=convertView;
			holder = (ViewHolder) view.getTag();
		}else {
			 view = View.inflate(VideoLisActivity.this, R.layout.videolist_item, null);
        holder = new ViewHolder();
        holder.vi_name = (TextView) view.findViewById(R.id.vi_name);
        holder.vi_duration = (TextView) view.findViewById(R.id.vi_duration);
        holder.vi_size = (TextView) view.findViewById(R.id.vi_size);
	    //对象关系
        view.setTag(holder);
		
		}
	   //根据位置，得到具体某一个视频的信息
        VideoItem videoItem = videoItems.get(position);
        holder.vi_name.setText(videoItem.getTitle());
        holder.vi_duration.setText(utils.stringForTime(Integer.valueOf(videoItem.getDuration())));
        holder.vi_size.setText(Formatter.formatFileSize(VideoLisActivity.this, videoItem.getSize())); 
         
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

	private void getAllVideo() {
	new Thread(){
		public void run(){
		//把子线程视频读取出来
			videoItems = new ArrayList<VideoItem>();
			ContentResolver resolver = getContentResolver();
			Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

		String[] projection = {
				MediaStore.Video.Media.TITLE,
				MediaStore.Video.Media.DURATION,
				MediaStore.Video.Media.SIZE,
				MediaStore.Video.Media.DATA,
		};
		Cursor cursor =	resolver.query(uri, projection , null, null, null);
		while (cursor.moveToNext() ) {
			long size = cursor.getLong(2);
			//具体视频信息
			if(size> 3*1024*1024){
				VideoItem item = new VideoItem();
				
				String title = cursor.getString(0);
				item.setTitle(title);
				
				String duration = cursor.getString(1);
				item.setDuration(duration);
				
				
				item.setSize(size);
				
				String data = cursor.getString(3);
				item.setData(data);
				
				videoItems.add(item);
			}
    }
		handler.sendEmptyMessage(0);
		}
	}.start();
		
	}


	@Override
	public View setContentView() {
		return View.inflate(this, R.layout.activity_videolist, null);
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
