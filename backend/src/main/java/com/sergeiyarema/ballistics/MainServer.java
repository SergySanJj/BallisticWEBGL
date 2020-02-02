package com.sergeiyarema.ballistics;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainServer {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        executor.execute(new HttpServer(ServerConfig.httpPort));
        executor.execute(new BallisticServer(new InetSocketAddress(ServerConfig.host, ServerConfig.webSocketPort)));
    }

}
