package com.emilflach.cobot;

/**
 * Created by Emil on 2015-11-24.
 */
public class ApiError {

    private int status_code;
    private String message;
    private String validation_messages[];

    public ApiError() {
    }

    public int status() {
        return status_code;
    }

    public String message() {
        return message;
    }
    public String[] validation_messages() {
        return validation_messages;
    }
}
