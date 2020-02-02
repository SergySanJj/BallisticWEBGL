package com.sergeiyarema.ballistics;

import java.net.ServerSocket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer implements Runnable {
    private Logger logger = Logger.getLogger("http server");
    private int port = 8080;

    public static Thread createNewServerThread(int port){
        return new Thread(new HttpServer(port));
    }

    HttpServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverConnect = new ServerSocket(this.port);
            logger.log(Level.INFO,
                    "HTTP Server started.\nListening for connections on port : " + this.port + " ...\n");

            while (true) {
                HttpHandler handler = new HttpHandler(serverConnect.accept());
                logger.log(Level.INFO, "Connecton opened. (" + new Date() + ")");

                Thread thread = new Thread(handler);
                thread.start();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server Connection error : " + e.getMessage());
        }
    }
}
