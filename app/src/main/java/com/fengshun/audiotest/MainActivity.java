package com.fengshun.audiotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fengshun.audiotest.normal.AudioReceiver;
import com.fengshun.audiotest.normal.AudioSender;
import com.fengshun.audiotest.tester.AudioCaptureTester;
import com.fengshun.audiotest.tester.AudioPlayerTester;
import com.fengshun.audiotest.tester.Tester;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private Tester mTester;
    private AudioSender sender;
    private AudioReceiver receiver;

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    public static native void webRtcAgcInit(long minVolume, long maxVolume, long freq);

    public static native void webRtcAgcFree();

    public static native void webRtcAgcProcess(short[] srcData, short[] desData, int srcLen);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sender = new AudioSender();
        receiver = new AudioReceiver();
        UnitView pcmView = findViewById(R.id.pcm_test);
        Log.e("atom", stringFromJNI());
        pcmView.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("atom", "hhh");
                switch (v.getId()) {
                    case R.id.start_record:
                        mTester = new AudioCaptureTester();
                        mTester.startTesting();
                        break;
                    case R.id.stop_record:
                        mTester.stopTesting();
                        break;
                    case R.id.start_play:
                        mTester = new AudioPlayerTester();
                        mTester.startTesting();
                        break;
                    case R.id.stop_play:
                        mTester.stopTesting();
                        break;
                }
            }
        });


        UnitView realtimeView = findViewById(R.id.realtime_test);
        realtimeView.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("atom", "hhh");
                switch (v.getId()) {
                    case R.id.start_record:
                        sender.startRecordAndroid();
                        break;
                    case R.id.stop_record:
                        sender.stopRecord();
                        break;
                    case R.id.start_play:
                        receiver.startPlay();
                        break;
                    case R.id.stop_play:
                        receiver.stopPlay();
                        break;
                }
            }
        });



    }

}
