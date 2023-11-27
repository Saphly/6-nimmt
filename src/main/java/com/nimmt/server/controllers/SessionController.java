package com.nimmt.server.controllers;

import com.nimmt.server.models.Session;
import com.nimmt.server.services.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping(value = "/sessions")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping(value = "/sessions/{sessionId}")
    public Session getSessionById(@PathVariable String sessionId) {
        return sessionService.getSessionById(sessionId);
    }

    @PostMapping(value = "/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    public Session createSession(@Valid @RequestBody Session session) {
        return sessionService.createSession(session);
    }

}
