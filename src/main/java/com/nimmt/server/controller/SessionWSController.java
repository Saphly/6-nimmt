package com.nimmt.server.controller;

import com.nimmt.server.model.SessionMessage;
import com.nimmt.server.model.SessionResponse;
import com.nimmt.server.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class SessionWSController {

    private final SessionService sessionService;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public SessionWSController(SessionService sessionService, SimpMessagingTemplate messagingTemplate) {
        this.sessionService = sessionService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/session/{sessionId}")
    @SendTo("/topic/session/{sessionId}")
    public SessionResponse session(@DestinationVariable String sessionId, @Payload SessionMessage message, Principal player) {
        String playerId = player.getName();

        try {
            return sessionService.handleSessionMessage(sessionId, playerId, message);
        } catch (Exception e) {
            messagingTemplate.convertAndSendToUser(playerId, "/error", new SessionResponse(SessionResponse.Type.ERROR, e.getMessage()));
            throw e;
        }
    }

}
