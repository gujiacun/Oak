package com.oath.oak.common.intbuffer;

import com.oath.oak.OakComparator;
import com.oath.oak.OakReadBuffer;
import com.oath.oak.OakUnsafeDirectBuffer;

import java.nio.ByteBuffer;

public class OakIntBufferComparator implements OakComparator<ByteBuffer> {

    private final int size;

    public OakIntBufferComparator(int size) {
        this.size = size;
    }

    @Override
    public int compareKeys(ByteBuffer buff1, ByteBuffer buff2) {
        return compare(buff1, 0, size, buff2, 0, size);
    }

    @Override
    public int compareSerializedKeys(OakReadBuffer serializedKey1, OakReadBuffer serializedKey2) {
        OakUnsafeDirectBuffer unsafeKey1 = (OakUnsafeDirectBuffer) serializedKey1;
        OakUnsafeDirectBuffer unsafeKey2 = (OakUnsafeDirectBuffer) serializedKey2;
        return compare(unsafeKey1.getByteBuffer(), unsafeKey1.getOffset(), size,
                unsafeKey2.getByteBuffer(), unsafeKey2.getOffset(), size);
    }

    @Override
    public int compareKeyAndSerializedKey(ByteBuffer key, OakReadBuffer serializedKey) {
        OakUnsafeDirectBuffer unsafeKey = (OakUnsafeDirectBuffer) serializedKey;
        return compare(key, 0, size, unsafeKey.getByteBuffer(), unsafeKey.getOffset(), size);
    }

    public static int compare(ByteBuffer buff1, int pos1, int size1, ByteBuffer buff2, int pos2, int size2) {
        int minSize = Math.min(size1, size2);

        for (int i = 0; i < minSize; i++) {
            int i1 = buff1.getInt(pos1 + Integer.BYTES * i);
            int i2 = buff2.getInt(pos2 + Integer.BYTES * i);
            int compare = Integer.compare(i1, i2);
            if (compare != 0) {
                return compare;
            }
        }

        return Integer.compare(size1, size2);
    }
}