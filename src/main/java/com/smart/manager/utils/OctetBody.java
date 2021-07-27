package com.smart.manager.utils;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

import java.io.IOException;

public class OctetBody extends RequestBody {
    private final byte[] data;

    public OctetBody(byte[] data) {
        this.data = data;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream; charset=utf-8");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        sink.write(data);
    }
}
