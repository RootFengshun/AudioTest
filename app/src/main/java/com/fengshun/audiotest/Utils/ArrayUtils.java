package com.fengshun.audiotest.Utils;

import android.support.annotation.NonNull;

public class ArrayUtils {
    /**
     * byte数组转short数组
     * @param src
     * @return
     */
    public static short[] toShortArray(byte[] src, short[]dest) {

        int count = src.length >> 1;
        for (int i = 0; i < count; i++) {
            dest[i] = (short) ((src[i * 2] & 0xff) | ((src[2 * i + 1] & 0xff) << 8));
        }
        return dest;
    }

    /**
     * short数组转byte数组
     * @param src
     * @return
     */
    public static byte[] toByteArray(short[] src, @NonNull byte[] dest) {

        int count = src.length;
        for (int i = 0; i < count; i++) {
            dest[i * 2] = (byte) (src[i]);
            dest[i * 2 + 1] = (byte) (src[i] >> 8);
        }

        return dest;
    }
}
