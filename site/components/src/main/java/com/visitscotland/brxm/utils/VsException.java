package com.visitscotland.brxm.utils;

public class VsException extends RuntimeException {

    public VsException() {
    }

    public VsException(String s) {
        super(s);
    }

    public VsException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public VsException(Throwable throwable) {
        super(throwable);
    }

    public VsException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
