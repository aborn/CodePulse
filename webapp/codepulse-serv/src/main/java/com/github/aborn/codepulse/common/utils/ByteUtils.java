package com.github.aborn.codepulse.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author aborn (jiangguobao)
 * @date 2023/07/11 11:50
 */
@Slf4j
public class ByteUtils {

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES); // BIG_ENDIAN
        buffer.putLong(0, x);
        byte[] bytes = buffer.array();
        if (bytes.length != 8) {
            log.error(String.format("long value:%d not 8 byte length.", x));
        }
        return bytes;
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();
        if (bytes.length != 8) {
            log.warn("ee");
        }
        return buffer.getLong();
    }
}
