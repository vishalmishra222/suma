package com.example.sumaforms.recording;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.sumaforms.R;

public class PlaybackActivity extends AppCompatActivity {

    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        String audioFilePath = getIntent().getStringExtra("audioFilePath");
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(audioFilePath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        stopPlayback(null);
        super.onDestroy();
    }

    public void stopPlayback(View view) {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

        finish();
    }
}
