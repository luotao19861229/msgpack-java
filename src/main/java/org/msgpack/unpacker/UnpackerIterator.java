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
package org.msgpack.unpacker;

import java.io.IOException;
import java.io.EOFException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.msgpack.value.Value;
import org.msgpack.packer.Unconverter;

public class UnpackerIterator implements Iterator<Value> {
    private final Unpacker u;
    private final Unconverter uc;
    private IOException exception;

    public UnpackerIterator(Unpacker u) {
        this.u = u;
        this.uc = new Unconverter();
    }

    public boolean hasNext() {
        if(uc.getResult() != null) {
            return true;
        }
        try {
            u.read(uc);
        } catch (EOFException ex) {  // TODO NeedMoreBufferError
            return false;
        } catch (IOException ex) {
            // TODO error
            exception = ex;
            return false;
        }
        return uc.getResult() != null;
    }

    public Value next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        Value v = uc.getResult();
        uc.resetResult();
        return v;
    }

    /*
    private Value value;

    public boolean hasNext() {
        if(value != null) {
            return true;
        }
        try {
            value = u.read();
        } catch (IOException ex) {
            exception = ex;
            return false;
        }
        return value != null;
    }

    public Value next() {
        if(value != null) {
            Value v = value;
            value = null;
            return v;
        } else {
            Value v;
            try {
                v = u.read();
            } catch (IOException ex) {
                exception = ex;
                throw new NoSuchElementException();
            }
            if(v == null) {
                throw new NoSuchElementException();
            }
            return v;
        }
    }
    */

    public void remove() {
		throw new UnsupportedOperationException();
    }

    public IOException getException() {
        return exception;
    }
}

