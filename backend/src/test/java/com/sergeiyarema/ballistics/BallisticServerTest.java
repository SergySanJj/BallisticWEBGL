package com.sergeiyarema.ballistics;

import com.google.gson.Gson;
import org.awaitility.Awaitility;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


import java.net.InetSocketAddress;
import java.net.URI;

import static org.junit.Assert.*;

public class BallisticServerTest {
    private static String serverBase = "ws://localhost:8081";
    private static String host = ServerConfig.host;
    private static int webSocketPort = ServerConfig.webSocketPort;

    private static Gson gson = new Gson();

    private int ballCount = 0;
    private int coordsReceived = 0;

    @BeforeClass
    public static void setUpConnection() {
        Thread ballisticThread = new Thread(new BallisticServer(new InetSocketAddress(host, webSocketPort)));
        ballisticThread.start();
    }

    @Test(timeout = 5000)
    public void checkFlight() {
        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI(serverBase)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    BallisticParams ballisticParams = new BallisticParams(45., 1., 9.);
                    Message msg = new Message("fire", gson.toJson(ballisticParams));
                    send(gson.toJson(msg));
                    send(gson.toJson(msg));
                    send(gson.toJson(msg));
                }

                @Override
                public void onMessage(String message) {
                    Message msg = gson.fromJson(message, Message.class);
                    if (msg.message.equals("createBall")) {
                        Assert.assertNotEquals("", msg.data);
                        ballCount++;
                    } else if (msg.message.equals("flight")) {
                        Assert.assertNotEquals("", msg.data);
                        coordsReceived++;
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                }

                @Override
                public void onError(Exception ex) {
                    Assert.assertNull("Error during work with socket: " + ex.getStackTrace().toString());
                }
            };
            Thread clientThread = new Thread(webSocketClient);
            clientThread.start();

            Awaitility.await().until(() -> {
                return ballCount == 3 && coordsReceived > 0;
            });
            assertEquals(3, ballCount);
            Assert.assertTrue(coordsReceived > 0);

            clientThread.interrupt();
        } catch (Exception e) {
            Assert.assertNull("Error during work with socket");
            e.printStackTrace();
        }

    }
}