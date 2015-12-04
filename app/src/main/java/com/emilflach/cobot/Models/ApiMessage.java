package com.emilflach.cobot.Models;

/**
 * cobot
 * by Emil on 2015-11-24.
 */
public class ApiMessage {

    private int status_code;
    private String message;

    public ApiMessage() {
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public int status() {
        return status_code;
    }
    public String message() {
        return message;
    }
}
