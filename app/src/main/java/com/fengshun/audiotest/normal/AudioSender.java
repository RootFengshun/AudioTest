package com.fengshun.audiotest.normal;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import com.fengshun.audiotest.Utils.ArrayUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class AudioSender {
    public boolean isRunning = false;
    AudioRecord mAudioRecord;
    DatagramSocket socket;

    public void startRecordAndroid(){
        if (isRunning) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                int sampleRate = 8000;
                int audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
                int min = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                try {
                    mAudioRecord = new AudioRecord(
                            audioSource
                            , sampleRate
                            , AudioFormat.CHANNEL_IN_MONO
                            , AudioFormat.ENCODING_PCM_16BIT
                            , min);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }

                if(mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                    mAudioRecord.startRecording();
                }
                else {
                    mAudioRecord.release();
                    mAudioRecord = new AudioRecord(
                            audioSource
                            , sampleRate
                            , AudioFormat.CHANNEL_IN_MONO
                            , AudioFormat.ENCODING_PCM_16BIT
                            , min * 2);
                    try{

                        mAudioRecord.startRecording();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                initSocket();
                short []recordData = new short[2000];

                byte[] sendData = new byte[4000];
                DatagramPacket sendPacket;
                // 160个short
                int frameSize = 160;

                try {
                    while (isRunning)
                    {
                        // 录制
                        int num = 0;
                        num = mAudioRecord.read(recordData, 0, frameSize);
                        if(num <= 0) {
                            continue;
                        }
                        ArrayUtils.toByteArray(recordData, sendData);
                        ByteBuffer.wrap(sendData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(recordData);
                        Log.e("fengshun", "num"+ Arrays.toString(sendData));

                        sendPacket = new DatagramPacket(sendData, num * 2, InetAddress.getByName("10.15.6.36"), 31000);
                        socket.send(sendPacket);

                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(mAudioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                        mAudioRecord.release();
                    }
                    if (mAudioRecord != null) {
                        mAudioRecord = null;
                    }
                    releaseSocket();
                }

            }
        }).start();

    }
    public void stopRecord(){
        isRunning = false;
    }
    private void initSocket() {
        try {
            socket = new DatagramSocket(31000);
            socket.setReuseAddress(true);
        } catch (Exception e) {
            Log.e("atom", "init socket error");
            e.printStackTrace();
        }

    }
    private void releaseSocket() {
        if (socket!=null) {
            socket.close();
            socket = null;
        }
    }
}
