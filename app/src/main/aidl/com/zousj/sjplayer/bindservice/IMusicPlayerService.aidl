// IMusicPlayerService.aidl
package com.zousj.sjplayer.bindservice;

// Declare any non-default types here with import statements

interface IMusicPlayerService {

        /**
         * 根据位置去打开一个音频文件并播放
         * @param position
         */
        void openAudio(int position);

        /**
         * 播放
         */
        void play();
        /**
         * 暂停
         */
        void pause();
        /**
         * 歌唱者名称
         * @return
         */
        String getArtist();

        /**
         * 歌曲名称
         * @return
         */
        String getName();
        /**
         * 得到歌曲的总时长
         * @return
         */
        int getDuration();
        /**
         * 得到歌曲当前播放位置
         * @return
         */
        int getCurrentPosition();
        /**
         * 定位到音频的播放位置
         * @param position
         */
        void seekTo(int position);
        /**
         * 设置播放歌曲的模式：顺序、单曲、全部
         * @param mode
         */
        void setPlayMode(int mode);
        /**
         * 上一曲
         */
        void pre();
        /**
         * 下一曲
         */
        void next();
}
