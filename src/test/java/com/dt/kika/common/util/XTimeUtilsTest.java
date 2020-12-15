package com.dt.kika.common.util;

import org.junit.Test;

/**
 * @author yanping
 * @date 2020/12/15 5:48 下午
 */

public class XTimeUtilsTest {

    @Test
    public void testTimeBase() {

        byte[] timeBytes = XTimeUtils.timeStamp2Bytes();
        if (null != timeBytes) {
            System.out.println(timeBytes);
            int saveTimeStamp = XTimeUtils.bytes2TimeStamp(timeBytes);
            System.out.println(saveTimeStamp);
        }

        int key = 4885;
        byte keyLen = (byte)(key);
        System.out.println(keyLen);

        int key02 = 115;
        byte keyLen02 = (byte)(key02);
        System.out.println(keyLen02);

    }
}
