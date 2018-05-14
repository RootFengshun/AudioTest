package com.fengshun.audiotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fengshun.audiotest.tester.AudioCaptureTester;
import com.fengshun.audiotest.tester.AudioPlayerTester;
import com.fengshun.audiotest.tester.Tester;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("native-lib");
//    }
    private Tester mTester;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UnitView pcmView =  (UnitView)findViewById(R.id.pcm_test);
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


        UnitView realtimeView = (UnitView) findViewById(R.id.realtime_test);



    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     * public native String stringFromJNI();
     */

}
