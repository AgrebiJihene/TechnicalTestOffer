package com.example.user.exception;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * custom error message
 */
@Data
public class ErrorMessage {
    private int statusCode;
    private Date timestamp;
    private List<String> message;
    private String description;

    public ErrorMessage(int statusCode, Date timestamp, List<String> message, String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "statusCode=" + statusCode +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}