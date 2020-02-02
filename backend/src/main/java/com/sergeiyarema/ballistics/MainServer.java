package com.sergeiyarema.ballistics;

import java.net.InetSocketAddress;

public class MainServer {
    public static void main(String[] args) {
        String host = "localhost";
        int webSocketPort = 8081;
        int httpPort = 8080;

        Thread httpThread = new Thread(new HttpServer(httpPort));
        httpThread.start();

        Thread ballisticThread = new Thread(new BallisticServer(new InetSocketAddress(host, webSocketPort)));
        ballisticThread.start();
    }

}
