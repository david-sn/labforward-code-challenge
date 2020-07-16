package com.labforward.api.core.domain;

/**
 * Base message response
 * @param <T>
 */
public class ApiMessage<T> {

    private String status;

    private T result;

    protected String message;

    protected String[] error;

    public ApiMessage(String message) {
        this.message = message;
        this.error = new String[0];
        this.status = this.message.toUpperCase().replace(" ", "_");
    }

    public ApiMessage(String message, String[] error) {
        this.message = message;
        this.error = error;
        this.status = this.message.toUpperCase().replace(" ", "_");
    }

    public ApiMessage(String status, T result, String message) {
        this(message);
        this.result = result;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String[] getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public T getResult() {
        return result;
    }

}
