package com.poyster.buslinestops;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ConsoleOutputCatcher extends OutputStream {
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    @Override
    public void write(int b) throws IOException {
        buffer.write(b);
    }

    public String getOutput() {
        return buffer.toString().trim();
    }

    public void clear() {
        buffer.reset();
    }
}
