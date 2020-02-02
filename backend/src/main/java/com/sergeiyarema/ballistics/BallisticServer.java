package com.sergeiyarema.ballistics;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BallisticServer extends WebSocketServer {
    private static int maxId = 0;
    private static Gson gson = new Gson();
    private Logger logger = Logger.getLogger("Ballistic");

    public BallisticServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.log(Level.INFO, "New connection from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.log(Level.INFO,
                "closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.log(Level.INFO, "received message from " + conn.getRemoteSocketAddress() + ": " + message);

        try {
            Message msg = gson.fromJson(message, Message.class);

            if (msg.message.equals("fire")) {
                BallisticParams bp = gson.fromJson(msg.data, BallisticParams.class);
                bp.checkAndFix();

                int projectileId = BallisticServer.getNewId();

                Message res = new Message("createBall", Integer.toString(projectileId));
                conn.send(gson.toJson(res));

                Thread fireThread = new Thread(new ProjectileFlight(conn, bp, projectileId));
                fireThread.start();
            }
        } catch (Exception e) { // pass incorrect json
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        logger.log(Level.INFO, "received ByteBuffer from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.log(Level.WARNING, "An error occurred on connection " + conn.getRemoteSocketAddress() + ":" + ex);
    }

    @Override
    public void onStart() {
        logger.log(Level.INFO, "Server started successfully");
    }

    private static int getNewId() {
        maxId++;
        return maxId;
    }
}

