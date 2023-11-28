package com.nimmt.server.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimmt.server.model.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFileReader {

    public static List<Session> fileToObjectList() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Session> sessions = objectMapper.readValue(JsonFileReader.class.getResourceAsStream("/data.json"),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Session.class));

            return sessions;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
