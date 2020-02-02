package com.sergeiyarema.ballistics;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainServer {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        String host = "localhost";
        int webSocketPort = 8081;
        int httpPort = 8080;

        executor.execute(new HttpServer(httpPort));
        executor.execute(new BallisticServer(new InetSocketAddress(host, webSocketPort)));
    }

}
