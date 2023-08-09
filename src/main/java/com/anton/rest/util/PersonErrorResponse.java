package com.anton.rest.util;

public class PersonErrorResponse {
    private String message;
    private Long timestamp;

    public PersonErrorResponse(String message, Long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }



    public String getMessage() {
        return message;
    }

    public void setName(String name) {
        this.message = name;
    }

    public Long getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timestamp = timeStamp;
    }


}
