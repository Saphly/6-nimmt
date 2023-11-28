package com.nimmt.server.model;


import java.security.Principal;

public class Player implements Principal {

    private final String playerId;

    public Player(String playerId) {
        this.playerId = playerId;
    }
    @Override
    public String getName() {
        return playerId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId='" + playerId + '\'' +
                '}';
    }
}
