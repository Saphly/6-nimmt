package com.nimmt.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionMessage {

    public enum Action {
        JOIN,
        LEAVE
    }

    @JsonProperty("action")
    private Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "SessionMessage{" +
                "action=" + action +
                '}';
    }
}
