package com.diff.util;

import java.util.Base64;

public class Base64Encoder {
    public static byte[] encode(String aString) {
        return Base64.getEncoder().encode(aString.getBytes());
    }
}
