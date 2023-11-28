package com.nimmt.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionResponse {

    public enum Type {
        JOINED,
        LEFT,
        ERROR
    }

    @JsonProperty("type")
    private Type type;

    @JsonProperty("message")
    private String message;

    public SessionResponse () {};

    public SessionResponse(Type type, String message) {
        this.type = type;
        this.message = message;
    }
}
