package com.terminus.sz.nnglibrarydemo;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public  class NNGSocket extends Structure {
    public int id;
    public NNGSocket(){
    }

    public NNGSocket(Pointer pointer){
        super(pointer);
    }
    @Override
    protected List getFieldOrder() {
        return Arrays.asList(new String[] { "id"});
    }
    public static class ByReference extends NNGSocket implements Structure.ByReference {}
    public static class ByValue extends NNGSocket implements Structure.ByValue {
        public ByValue(){
        }

        public ByValue(Pointer pointer){
            super(pointer);
            read();
        }
    }
}
