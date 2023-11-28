package com.nimmt.server.controller;

import com.nimmt.server.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Controller
public class SessionUnsubscribeListener implements ApplicationListener<SessionUnsubscribeEvent> {

    private final SessionService sessionService;

    @Autowired
    public SessionUnsubscribeListener(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent event) {
        System.out.println(event.getUser());
        System.out.println(event.getMessage());
//        System.out.println(event.getMessage().getHeaders("simpSubscriptionId"));
    }
}
