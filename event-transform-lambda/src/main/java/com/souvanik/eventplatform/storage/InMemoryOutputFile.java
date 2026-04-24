package com.souvanik.eventplatform.storage;

import org.apache.parquet.io.OutputFile;
import org.apache.parquet.io.PositionOutputStream;

import java.io.ByteArrayOutputStream;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class InMemoryOutputFile implements OutputFile {

    private final ByteArrayOutputStream baos;

    public InMemoryOutputFile(ByteArrayOutputStream baos) {
        this.baos = baos;
    }

    @Override
    public PositionOutputStream create(long blockSizeHint) {
        return new PositionOutputStream() {

            @Override
            public long getPos() {
                return baos.size();
            }

            @Override
            public void write(int b) {
                baos.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                baos.write(b, off, len);
            }
        };
    }

    @Override
    public PositionOutputStream createOrOverwrite(long blockSizeHint) {
        return create(blockSizeHint);
    }

    @Override
    public boolean supportsBlockSize() {
        return false;
    }

    @Override
    public long defaultBlockSize() {
        return 0;
    }
}