package controllers;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;


@ServerEndpoint("/websocket")
public class WebSocketServer {

    @OnOpen
    public void onOpen(Session session) throws IOException {
        session.getBasicRemote().sendText("onOpen");

        System.out.println("Inside @On Open Annotation sessionId: "+session.getId());
    }

    @OnMessage
    public String echo(String message) {
        System.out.println("Inside @OnMessage annotation");
        return message + " (from your server)";
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("Inside @OnErrpr Annotataion");
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
       System.out.println("Inside @OnClose Annotation");
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

