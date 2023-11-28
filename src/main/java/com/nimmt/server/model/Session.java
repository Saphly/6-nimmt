package com.nimmt.server.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document("sessions")
@JsonIgnoreProperties(value={"players", "status"}, allowGetters = true)
public class Session {

    public enum Status {
        OPEN,
        CLOSED
    }

    @Id
    @JsonProperty("_id")
    private String _id;

    @JsonProperty("name")
    @NotEmpty(message = "Session must have a name")
    private String name;

    @JsonProperty("minPlayers")
    @Min(value = 2)
    @Max(value = 10)
    private int minPlayers = 2;

    @JsonProperty("maxPlayers")
    @Min(value = 2)
    @Max(value = 10)
    private int maxPlayers = 10;

    @JsonProperty("players")
    private Set<String> players = new HashSet<String>();

    @JsonProperty("status")
    private Status status = Status.OPEN;

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Set<String> getPlayers() {
        return players;
    }

    public Status getStatus() {
        return status;
    }
}