package com.androidyuan.rxbus.exception;

/**
 * Created by wei on 16/9/19.
 */
public class REventIsNullException extends RuntimeException {

    public REventIsNullException() {

        super("mEvent is null!");
    }

    public REventIsNullException(String message) {

        super(message);
    }


}
