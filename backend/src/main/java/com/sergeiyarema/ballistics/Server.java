package com.sergeiyarema.ballistics;

import java.net.HttpCookie;
import java.net.ServerSocket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private static Logger logger = Logger.getLogger("server");

    public static void main(String[] args) {


        try {
            ServerSocket serverConnect = new ServerSocket(HTTPServer.PORT);
            logger.log(Level.INFO,
                    "Server started.\nListening for connections on port : " + HTTPServer.PORT + " ...\n");

            // we listen until user halts server execution
            while (true) {
                HTTPServer myServer = new HTTPServer(serverConnect.accept());

                System.out.println("Connecton opened. (" + new Date() + ")");


                // create dedicated thread to manage the client connection
                Thread thread = new Thread(myServer);
                thread.start();
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Server Connection error : " + e.getMessage());
        }
    }
}
