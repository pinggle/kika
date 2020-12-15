package com.dt.kika.common.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yanping
 * @date 2020/12/15 7:04 下午
 */
@Slf4j
public class XStringUtils {

    /**
     * int转换成4-byte数组;
     **/
    public static byte[] intTo4Bytes(int value) {
        try {
            byte[] bytes = new byte[4];
            for (int i = bytes.length - 1; i >= 0; i--) {
                bytes[i] = (byte) (value & 0xFF);
                value >>= 8;
            }
            return bytes;
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    /**
     * 4-byte数组转换成int;
     **/
    public static int fourBytesToInt(byte[] bytes) {
        int retValue = 0;
        try {
            retValue = (bytes[0] << 24) & 0xFF000000 |
                    (bytes[1] << 16) & 0xFF0000 |
                    (bytes[2] << 8) & 0xFF00 |
                    (bytes[3]) & 0xFF;
        } catch (Exception e) {
            log.error("{}", e);
        }
        return retValue;
    }

    /**
     * 1-byte转换成int;
     **/
    public static int oneBytesToInt(byte inByte) {
        try {
            return (inByte & 0xFF);
        } catch (Exception e) {
            log.error("{}", e);
        }
        return 0;
    }
}
