package com.nimmt.server.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("sessions")
@JsonIgnoreProperties(value={"players", "status"}, allowGetters = true)
public class Session {

    @Id
    @JsonProperty("_id")
    private String _id;

    @JsonProperty("name")
    @NotEmpty(message = "Session must have a name")
    private String name;

    @JsonProperty("players")
    private List<String> players = new ArrayList<String>();

    @JsonProperty("status")
    private String status = "OPEN";
}