package com.nimmt.server.service;

import com.nimmt.server.model.Session;
import com.nimmt.server.model.SessionMessage;
import com.nimmt.server.model.SessionResponse;
import com.nimmt.server.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(String sessionId) {
        return sessionRepository.findById(sessionId);
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public SessionResponse handleSessionMessage(String sessionId, String playerId, SessionMessage message) {
        Session session = getSessionById(sessionId).orElseThrow();

        switch (message.getAction()) {
            case JOIN:
                if (session.getStatus() != Session.Status.OPEN) {
                    throw new IllegalStateException("Session is no longer open");
                }

                if (session.getPlayers().size() >= session.getMaxPlayers()) {
                    throw new IllegalStateException("Session is full");
                }

                if (!session.getPlayers().contains(playerId)) {
                    session.getPlayers().add(playerId);
                    sessionRepository.save(session);
                }
                return new SessionResponse(SessionResponse.Type.JOINED, playerId);
            case LEAVE:
                if (session.getStatus() != Session.Status.OPEN) {
                    throw new IllegalStateException("Session is no longer open");
                }

                if (session.getPlayers().contains(playerId)) {
                    session.getPlayers().remove(playerId);
                    sessionRepository.save(session);
                }
                return new SessionResponse(SessionResponse.Type.LEFT, playerId);
            default:
                throw new IllegalArgumentException("Unknown action");
        }
    }
}
