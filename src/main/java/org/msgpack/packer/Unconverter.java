//
// MessagePack for Java
//
// Copyright (C) 2009-2011 FURUHASHI Sadayuki
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package org.msgpack.packer;

import java.math.BigInteger;
import java.io.IOException;
import org.msgpack.MessageTypeException;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;

public class Unconverter extends Packer {
    private PackerStack stack;
    private Object[] values;
    private Value result;

    public Value getResult() {
        return result;
    }

    public void resetResult() {
        this.result = null;
    }

    @Override
    public void writeNil() {
        put(ValueFactory.nilValue());
    }

    @Override
    public void writeBoolean(boolean v) {
        put(ValueFactory.booleanValue(v));
    }

    @Override
    public void writeByte(byte v) {
        put(ValueFactory.integerValue(v));
    }

    @Override
    public void writeShort(short v) {
        put(ValueFactory.integerValue(v));
    }

    @Override
    public void writeInt(int v) {
        put(ValueFactory.integerValue(v));
    }

    @Override
    public void writeBigInteger(BigInteger v) {
        put(ValueFactory.integerValue(v));
    }

    @Override
    public void writeLong(long v) {
        put(ValueFactory.integerValue(v));
    }

    @Override
    public void writeFloat(float v) {
        put(ValueFactory.floatValue(v));
    }

    @Override
    public void writeDouble(double v) {
        put(ValueFactory.floatValue(v));
    }

    @Override
    public void writeBytes(byte[] b, int off, int len) {
        put(ValueFactory.rawValue(b, off, len));
    }

    @Override
    public void writeString(String s) {
        put(ValueFactory.rawValue(s));
    }

    @Override
    public void writeArrayBegin(int size) {
        Value[] array = new Value[size];
        put(ValueFactory.arrayValue(array, true));
        stack.pushArray(size);
        values[stack.getDepth()] = array;
    }

    @Override
    public void writeArrayEnd(boolean check) {
        if(stack.topIsArray()) {
            throw new MessageTypeException("writeArrayEnd() is called but writeArrayBegin() is not called");
        }

        int remain = stack.getTopCount();
        if(remain > 0) {
            if(check) {
                throw new MessageTypeException("writeArrayEnd(check=true) is called but the array is not end");
            }
            for(int i=0; i < remain; i++) {
                writeNil();
            }
        }
        stack.pop();
    }

    @Override
    public void writeMapBegin(int size) {
        Value[] array = new Value[size*2];
        put(ValueFactory.mapValue(array, true));
        stack.pushMap(size);
        values[stack.getDepth()] = array;
    }

    @Override
    public void writeMapEnd(boolean check) {
        if(stack.topIsArray()) {
            throw new MessageTypeException("writeArrayEnd() is called but writeArrayBegin() is not called");
        }

        int remain = stack.getTopCount();
        if(remain > 0) {
            if(check) {
                throw new MessageTypeException("writeArrayEnd(check=true) is called but the array is not end");
            }
            for(int i=0; i < remain; i++) {
                writeNil();
            }
        }
        stack.pop();
    }

    @Override
    public void write(Value v) {
        put(v);
    }

    private void put(Value v) {
        if(stack.getDepth() <= 0) {
            this.result = v;
        } else {
            stack.checkCount();
            Value[] array = (Value[])values[stack.getDepth()];
            array[array.length - stack.getTopCount()] = v;
            stack.reduceCount();
        }
    }
}

