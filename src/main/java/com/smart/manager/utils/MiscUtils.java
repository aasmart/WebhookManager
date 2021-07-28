package com.smart.manager.utils;

/**
 * A class containing miscellaneous utility items
 */
public class MiscUtils {
    /**
     * Converts bytes to megabytes
     * @param bytes The amount of bytes
     * @return The amount of megabytes
     */
    public static long bytesToMegabyte(long bytes) {
        return bytes / 1000000;
    }
}
