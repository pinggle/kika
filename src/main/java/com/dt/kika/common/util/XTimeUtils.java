package com.dt.kika.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yanping
 * @date 2020/12/15 5:45 下午
 */
@Slf4j
public class XTimeUtils {

    /**
     * 当前时间戳转换成byte数组;(精确到秒)
     **/
    public static byte[] timeStamp2Bytes() {
        try {
            int time = (int) (System.currentTimeMillis() / 1000);
            return XStringUtils.intTo4Bytes(time);
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    /**
     * byte数组转化成时间戳;(精确到秒)
     **/
    public static int bytes2TimeStamp(byte[] bytes) {
        return XStringUtils.fourBytesToInt(bytes);
    }
}
