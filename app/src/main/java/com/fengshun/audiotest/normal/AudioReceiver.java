package com.fengshun.audiotest.normal;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import com.fengshun.audiotest.MainActivity;
import com.fengshun.audiotest.Utils.ArrayUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class AudioReceiver {
    private boolean isRunning = false;
    private AudioTrack mAudioTrack;
    private DatagramSocket socket;
    public void startPlay(){
        if (isRunning) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                int sampleRate = 8000;
                int audioSource = MediaRecorder.AudioSource.MIC;
                MainActivity.webRtcAgcInit(0, 255, 8000);
                int min = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                initSocket();

                try {
                    mAudioTrack = new AudioTrack(
                            AudioManager.STREAM_MUSIC
                            , sampleRate
                            , AudioFormat.CHANNEL_OUT_MONO
                            , AudioFormat.ENCODING_PCM_16BIT
                            , min
                            ,AudioTrack.MODE_STREAM
                    );
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                mAudioTrack.play();
                short []recordData = new short[2000];
                byte[] recvData = new byte[4000];
                short[] gainedData = new short[2000];
                DatagramPacket recvPacket = new DatagramPacket(recvData, recvData.length);
                try {
                    while (isRunning)
                    {
                        Log.e("atom", "frrrrr");
                        socket.receive(recvPacket);
                        int num = recvPacket.getLength() / 2;
                        ArrayUtils.toShortArray(recvData, recordData);

                        MainActivity.webRtcAgcProcess(recordData, gainedData, num);

                        mAudioTrack.write(gainedData, 0, num);

                    }
                    Log.e("atom","exit while");
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                finally {

                    if (mAudioTrack != null) {
                        mAudioTrack = null;
                    }
                }

            }
        }).start();

    }
    public void stopPlay() {
        isRunning = false;
    }
    private void initSocket() {
        try {
            socket = new DatagramSocket(31001);
            socket.setReuseAddress(true);
        } catch (Exception e) {
            Log.e("atom", "init socket error");
            e.printStackTrace();
        }

    }
}
