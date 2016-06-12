package models;

import play.libs.F;
import play.mvc.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notification {
    //list for storing outConnections
    private static List<WebSocket.Out<String>> outConnections = new ArrayList<WebSocket.Out<String>>();
    //map for storing in and out connections
    private static List<WebSocket.In<String>> inConnections = new ArrayList<WebSocket.In<String>>();

    private static List<String> emailIds = new ArrayList<>();

    private static Map<WebSocket.In<String>, WebSocket.Out<String>> map = new HashMap<>();
    //map for storing incoming message and out connections
    private static Map<WebSocket.Out<String>, String> stringMap = new HashMap<>();

    public static void start(final WebSocket.In<String> in, final WebSocket.Out<String> out) {
        outConnections.add(out);
        inConnections.add(in);
        map.put(in, out);
        in.onMessage(new F.Callback<String>() {
            @Override
            public void invoke(String s) throws Throwable {
                emailIds.add(s);
                stringMap.put(out, s);
            }
        });

        in.onClose(new F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                if (map.containsKey(in)) {
                    WebSocket.Out<String> temp = map.get(in);
                    System.out.println("Removing connection for " + stringMap.get(temp));
                    stringMap.remove(temp);
                    outConnections.remove(temp);
                }

            }
        });
//        out.write("hi you are connected to websocket server");

    }

    public static void messageToSubscribers(String emailId, String message) {
        if (!message.equals("")) {
            if (stringMap.containsValue(emailId)) {
                for (Map.Entry<WebSocket.Out<String>, String> entry : stringMap.entrySet()) {
                    if (entry.getValue().equals(emailId)) {
                        entry.getKey().write(message);
                        System.out.println("Message sent to: " + entry.getValue());
                    }
                }
            }
        }
    }
}
