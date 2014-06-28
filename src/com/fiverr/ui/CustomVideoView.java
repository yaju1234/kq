package com.fiverr.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVideoView extends VideoView{
    public PlayPauseListener mListener;
    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        mListener = listener;
    }
   
   

    @Override
    public void pause() {
        super.pause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    public void start() {
        super.start();
        if (mListener != null) {
            mListener.onPlay();
        }
    }
   
   




    public interface PlayPauseListener {
        void onPlay();
        void onPause();
     
    }

}
