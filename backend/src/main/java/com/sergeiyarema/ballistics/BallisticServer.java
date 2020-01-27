package com.sergeiyarema.ballistics;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class BallisticServer extends WebSocketServer {
    private static int maxId = 0;
    private static Gson gson = new Gson();

    public BallisticServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);

        Message msg = gson.fromJson(message, Message.class);

        if (msg.message.equals("fire")) {
            BallisticParams bp = gson.fromJson(msg.data, BallisticParams.class);

            int projectileId = BallisticServer.getNewId();

            Message res = new Message("createBall", Integer.toString(projectileId));
            conn.send(gson.toJson(res));

            Thread fireThread = new Thread(new ProjectileFlight(conn, bp, projectileId));
            fireThread.start();
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        System.out.println("received ByteBuffer from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("An error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully");
    }

    private static int getNewId() {
        maxId++;
        return maxId;
    }
}

