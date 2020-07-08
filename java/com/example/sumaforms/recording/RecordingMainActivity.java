package com.example.sumaforms.recording;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sumaforms.R;
import com.example.sumaforms.preferences.UserPreference;
import com.example.sumaforms.recording.utils.AMRAudioRecorder;
import com.example.sumaforms.recording.utils.PermissionsDialogue;

import java.io.File;

public class RecordingMainActivity extends AppCompatActivity {

    private int mRecordTimeInterval;

    private TextView mRecordingTime, mRecordingStatus;
    private AMRAudioRecorder mRecorder;
    private EasyTimer mAudioTimeLabelUpdater;
    private PermissionsDialogue.Builder alertPermissions;
    private Context mContext;
    private ImageView closeRecordingDialog;
    private String[] permissionsList = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_main_activity);

        String[] permissionArrays = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        Intent intent = getIntent();
        jobId = intent.getExtras().getString("recording");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        }

        mContext = getApplicationContext();
        mRecordingTime = (TextView) findViewById(R.id.recordingTime);
        mRecordingStatus = (TextView) findViewById(R.id.textRecordingStatus);
        closeRecordingDialog = (ImageView) findViewById(R.id.closeRecordingButton);
        onCloseMapDialogButtonClick();
        //Request permissions on Marshmallow and above
    /*    if (Build.VERSION.SDK_INT >= 23) {
            if (!PermissionUtils.IsPermissionEnabled(mContext, String.valueOf(permissionsList)))
            {
                alertPermissions = new PermissionsDialogue.Builder(this)
                        .setMessage("Suma Soft recorder audio and requires the following permissions: ")
                        .setRequireStorage(PermissionsDialogue.REQUIRED)
                        .setRequireAudio(PermissionsDialogue.REQUIRED)
                        .setOnContinueClicked(new PermissionsDialogue.OnContinueClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(false)
                        .build();
                alertPermissions.show();
            }
        }*/


    }


    private void onCloseMapDialogButtonClick() {
        closeRecordingDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecorder!=null && mRecorder.isRecording()==true) {
                    if (mRecorder == null) {
                        return;
                    }
                    mRecorder.clear();
                    mRecorder = null;
                    resetRecording();
                    mRecordingStatus.setText("Recording stoped");
                    finish();
                }
                else {
                    finish();
                }
            }
        });
    }

    public void viewOnClick(View view) {
        int id = view.getId();
        if (id == R.id.toggleRecord) {
            if (mRecorder == null) {

                resetRecording();

                String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                String recordingDirectory = sdcardPath + "/Dusmile/Audio/" + UserPreference.getUserRecord(mContext).getUsername() + "/" + jobId;
                File dir = new File(recordingDirectory);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                mRecorder = new AMRAudioRecorder(recordingDirectory);
                mRecorder.start();

                ((ImageButton) view).setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);

                mAudioTimeLabelUpdater = new EasyTimer(1000, new EasyTimer.CallBack() {
                    @Override
                    public void execute() {
                        int time = mRecordTimeInterval;

                        int min = time / 60, sec = time % 60;

                        String minStr = min < 10 ? "0" + min : "" + min;
                        String secStr = sec < 10 ? "0" + sec : "" + sec;

                        mRecordingTime.setText(minStr + ":" + secStr);
                        if (time == 90) {
                            mRecorder.stop();
                            Toast.makeText(mContext, "You Riched at maximum size, Recording saved succesfully!",
                                    Toast.LENGTH_LONG).show();
                            mRecorder = null;
                            resetRecording();
                        }
                        mRecordTimeInterval++;
                    }
                });
                Toast.makeText(mContext, "Recording Started",
                        Toast.LENGTH_SHORT).show();
                mRecordingStatus.setText("Recording Started");
            } else {
                if (mRecorder.isRecording()) {
                    ((ImageButton) view).setImageResource(R.drawable.ic_play_circle_filled_black_24dp);

                    mAudioTimeLabelUpdater.stop();
                    mRecorder.pause();
                    Toast.makeText(mContext, "Recording paused",
                            Toast.LENGTH_SHORT).show();
                    mRecordingStatus.setText("Recording Paused");
                } else {
                    ((ImageButton) view).setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    Toast.makeText(mContext, "Recording Resumed",
                            Toast.LENGTH_SHORT).show();
                    mRecorder.resume();
                    mAudioTimeLabelUpdater.restart();
                    mRecordingStatus.setText("Recording Started");
                }
            }
        } else if (id == R.id.done) {
            if (mRecorder == null) {
                return;
            }

            mRecorder.stop();

                /*Intent intent = new Intent(this, PlaybackActivity.class);
                intent.putExtra("audioFilePath", mRecorder.getAudioFilePath());
                startActivity(intent);*/
            Toast.makeText(mContext, "Recording saved succesfully!",
                    Toast.LENGTH_LONG).show();
            mRecorder = null;
            resetRecording();
        } else if (id == R.id.trash) {
            if (mRecorder == null) {
                return;
            }

            mRecorder.clear();
            mRecorder = null;
            resetRecording();
            mRecordingStatus.setText("Recording Deleted");
            Toast.makeText(mContext, "Recording Deleted succesfully!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void resetRecording() {
        if (mAudioTimeLabelUpdater != null) {
            mAudioTimeLabelUpdater.stop();
            mAudioTimeLabelUpdater = null;
        }

        mRecordTimeInterval = 0;
        mRecordingTime.setText("00:00");
        mRecordingStatus.setText(null);
        mRecordingStatus.setText("Recording Saved");

        ((ImageButton) findViewById(R.id.toggleRecord)).setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
    }
}
