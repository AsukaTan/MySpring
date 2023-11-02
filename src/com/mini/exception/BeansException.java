package com.mini.exception;

public class BeansException extends Exception{

    //直接调用父类的Exception constructor
    public BeansException(String message) {
        super(message);
    }
}
