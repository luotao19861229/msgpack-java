package org.msgpack.core;

import java.io.*;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Entry point for creating MessagePacker and MessageUnpacker
 */
public class MessagePack {

    public static Charset UTF8 = Charset.forName("UTF-8");

    public static class Code {
        public static final byte FIXMAP_PREFIX = (byte) 0x80;
        public static final byte FIXARRAY_PREFIX = (byte) 0x00;
        public static final byte FIXSTR_PREFIX = (byte) 0xa0;
        public static final byte NIL = (byte) 0xc0;
        public static final byte NEVER_USED = (byte) 0xc1;
        public static final byte FALSE = (byte) 0xc2;
        public static final byte TRUE = (byte) 0xc3;
        public static final byte BIN8 = (byte) 0xc4;
        public static final byte BIN16 = (byte) 0xc5;
        public static final byte BIN32 = (byte) 0xc6;
        public static final byte EXT8 = (byte) 0xc7;
        public static final byte EXT16 = (byte) 0xc8;
        public static final byte EXT32 = (byte) 0xc9;
        public static final byte FLOAT32 = (byte) 0xca;
        public static final byte FLOAT64 = (byte) 0xcb;
        public static final byte UINT8 = (byte) 0xcc;
        public static final byte UINT16 = (byte) 0xcd;
        public static final byte UINT32 = (byte) 0xce;
        public static final byte UINT64 = (byte) 0xcf;

        public static final byte INT8 = (byte) 0xd0;
        public static final byte INT16 = (byte) 0xd1;
        public static final byte INT32 = (byte) 0xd2;
        public static final byte INT64 = (byte) 0xd3;

        public static final byte FIXEXT1 = (byte) 0xd4;
        public static final byte FIXEXT2 = (byte) 0xd5;
        public static final byte FIXEXT4 = (byte) 0xd6;
        public static final byte FIXEXT8 = (byte) 0xd7;
        public static final byte FIXEXT16 = (byte) 0xd8;

        public static final byte STR8 = (byte) 0xd9;
        public static final byte STR16 = (byte) 0xda;
        public static final byte STR32 = (byte) 0xdb;

        public static final byte ARRAY16 = (byte) 0xdc;
        public static final byte ARRAY32 = (byte) 0xdd;

        public static final byte MAP16 = (byte) 0xde;
        public static final byte MAP32 = (byte) 0xdf;

        public static final byte NEGFIXINT_PREFIX = (byte) 0xe0;
    }


    public static class ExtendedTypeHeader {
        private final int type;
        private final int length;
        ExtendedTypeHeader(int type, int length) {
            this.type = type;
            this.length = length;
        }

        public int getType() {
            return type;
        }

        public int getLength() {
            return length;
        }
    }

    /**
     * Create a new MessagePacker that writes the message packed data to a file
     * @param outputFile
     * @return MessagePacker
     * @throws IOException if the target file cannot be created or opened
     */
    public static MessagePacker newPacker(File outputFile) throws IOException {
        return newPacker(new FileOutputStream(outputFile));
    }

    public static MessagePacker newPacker(OutputStream out) {
        if(out instanceof FileOutputStream) {
            return newPacker(((FileOutputStream) out).getChannel());
        }
        else {
            return new MessagePacker(new MessageBufferOutputStream(out));
        }
    }

    public static MessagePacker newPacker(WritableByteChannel out) {
        return new MessagePacker(new MessageBufferOutputChannel(out));
    }

    /**
     * Create a new MessageUnpacker that decodes the message packed data in a file
     * @param inputFile
     * @return MessageUnpacker
     * @throws IOException if the input file is not found
     */
    public static MessageUnpacker newUnpacker(File inputFile) throws IOException {
        return newUnpacker(new FileInputStream(inputFile));
    }

    public static MessageUnpacker newUnpacker(InputStream in) {
        // TODO
        return null;
    }

    public static MessageUnpacker newUnpacker(ReadableByteChannel in) {
        // TODO
        return null;
    }


}