package com.nimmt.server.config;

import com.nimmt.server.model.Player;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;

class WebSocketChannelInterceptor implements ChannelInterceptor {
    private static final String PLAYER_ID_HEADER = "playerId";

    @Override
    public Message preSend(Message message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Principal player = new Player(accessor.getFirstNativeHeader(PLAYER_ID_HEADER));
            accessor.setUser(player);
        }
        return message;
    }
}
