package com.example.jobsimpledemo.exception;


public class JobExistsException extends RuntimeException {
    public JobExistsException(String msg){
        super(msg);
    }
}
