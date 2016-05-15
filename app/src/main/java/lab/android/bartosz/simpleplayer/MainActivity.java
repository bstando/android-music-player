package lab.android.bartosz.simpleplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = new MediaPlayer();
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        try {
            mediaPlayer.setDataSource(new StringBuilder().append(Environment.getExternalStorageDirectory().getPath()).append("/audio/file.mp3").toString());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seekToZero();
                    mHandler.postDelayed(mUpdateTimeTask, 100);
                    mediaPlayer.start();
                }
            });

            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //mediaPlayer.seekTo(progress);
                    //seekBar.setProgress(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mHandler.removeCallbacks(mUpdateTimeTask);

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    int currentDuration = seekBar.getProgress();
                    mediaPlayer.seekTo(currentDuration);
                    mHandler.postDelayed(mUpdateTimeTask, 100);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        mUpdateTimeTask.run();
    }

    void seekToZero()
    {
        seekBar.setProgress(0);
        mediaPlayer.seekTo(0);
    }

    public void stopPlaying(View v)
    {
        if(mediaPlayer!=null) {
            mediaPlayer.pause();

            Button btn = (Button) findViewById(R.id.controlButton);
            btn.setText("PLAY");
            seekToZero();
        }
    }

    public void restartPlaying(View v)
    {
        if(mediaPlayer!=null) {

            seekToZero();
        }
    }

    public void controlPlaying(View v)
    {
        if(mediaPlayer!=null)
        {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.pause();
                Button btn = (Button) v.findViewById(R.id.controlButton);
                btn.setText("PLAY");
            }
            else
            {
                mediaPlayer.start();
                Button btn = (Button) v.findViewById(R.id.controlButton);
                btn.setText("PAUSE");
            }
        }
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            int currentDuration = mediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            //songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            //songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar

            seekBar.setProgress(currentDuration);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };
}
