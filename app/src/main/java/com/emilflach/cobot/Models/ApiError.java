package com.emilflach.cobot.Models;

/**
 * cobot
 * by Emil on 2015-11-24.
 */
public class ApiError {

    private int status_code;
    private String message;
    private String validation_messages[];

    public ApiError() {
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setValidation_messages(String[] validation_messages) {
        this.validation_messages = validation_messages;
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
