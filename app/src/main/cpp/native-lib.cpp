#include <jni.h>
#include <string>
#include "gain_control.h"
#include "_android_log_print.h"
#include "noise_suppression.h"
#include "analog_agc.h"

#include <malloc.h>

extern "C" JNIEXPORT jstring
JNICALL
Java_com_fengshun_audiotest_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++ hi~";
    return env->NewStringUTF(hello.c_str());
}
//音量增益
void *agcHandle = NULL;

extern "C" JNIEXPORT void JNICALL
Java_com_fengshun_audiotest_MainActivity_webRtcAgcFree(JNIEnv *env, jclass type) {
    if (agcHandle != NULL) {
        int free = WebRtcAgc_Free(agcHandle);
        if (free == -1) {
            LOGE("WebRtcAgc_Free error");
        }
        agcHandle = NULL;
    }
}


extern "C" JNIEXPORT void JNICALL
Java_com_fengshun_audiotest_MainActivity_webRtcAgcProcess(JNIEnv *env, jclass type,
                                                          jshortArray srcData_,
                                                          jshortArray desData_, jint srcLen) {

    jshort *srcData = env->GetShortArrayElements(srcData_, NULL);
    jshort *desData = env->GetShortArrayElements(desData_, NULL);

    jsize src_len = env->GetArrayLength(srcData_);
    int frameSize = 160;

    int micLevelIn = 0;
    int micLevelOut = 0;
//    LOGD("src_len=== %d", src_len);
    int16_t echo = 1;                                 //增益放大是否考虑回声影响
    uint8_t saturationWarning;

    int iFrame;
    int nFrame = src_len / frameSize; //帧数
    int leftLen = src_len % frameSize; //最后一帧的大小
    int onceLen = frameSize;
    nFrame = (leftLen > 0) ? nFrame + 1 : nFrame;

    short *agcIn = (short *) malloc(frameSize * sizeof(short));
    short *agcOut = (short *) malloc(frameSize * sizeof(short));

    for (iFrame = 0; iFrame < nFrame; iFrame++) {

        if (iFrame == nFrame - 1 && leftLen != 0) {
            onceLen = leftLen;
        }
//        LOGE("WebRtcAgc_Process onceLen ==%d", onceLen);
        memcpy(agcIn, srcData + iFrame * frameSize, onceLen * sizeof(short));

        int state = WebRtcAgc_Process(agcHandle, agcIn, NULL, 160, agcOut, NULL,
                                      micLevelIn, &micLevelOut, echo, &saturationWarning);
        if (state != 0) {
            LOGE("WebRtcAgc_Process error");
            break;
        }
        if (saturationWarning != 0) {
            LOGE("[AgcProc]: saturationWarning occured");
        }
        memcpy(desData + iFrame * frameSize, agcOut + iFrame * frameSize, onceLen * sizeof(short));
        micLevelIn = micLevelOut;
    }

    free(agcIn);
    free(agcOut);
    env->ReleaseShortArrayElements(srcData_, srcData, 0);
    env->ReleaseShortArrayElements(desData_, desData, 0);
}


extern "C" JNIEXPORT void JNICALL
Java_com_fengshun_audiotest_MainActivity_webRtcAgcInit(JNIEnv *env, jclass type, jlong minVolume,
                                                       jlong maxVolume,
                                                       jlong freq) {
    int agc = WebRtcAgc_Create(&agcHandle);
    if (agc == 0) {
        int16_t agcMode = kAgcModeFixedDigital;
        int agcInit = WebRtcAgc_Init(agcHandle, (int32_t) minVolume, (int32_t) maxVolume, agcMode,
                                     (uint32_t) freq);
        if (agcInit == 0) {
            WebRtcAgc_config_t agcConfig;
            agcConfig.compressionGaindB = 20;
            agcConfig.limiterEnable = 1;
            agcConfig.targetLevelDbfs = 3;

            int initConfig = WebRtcAgc_set_config(agcHandle, agcConfig);
            if (initConfig == -1) {
                LOGE("WebRtcAgc_set_config error");
            }
        } else {
            LOGE("WebRtcAgc_Init error");
        }
    } else {
        LOGE("WebRtcAgc_Create error");
    }
}

