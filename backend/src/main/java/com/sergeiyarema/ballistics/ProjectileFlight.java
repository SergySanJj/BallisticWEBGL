package com.sergeiyarema.ballistics;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;

public class ProjectileFlight implements Runnable {
    public static final double freq = 25;
    private static Gson gson = new Gson();

    private WebSocket receiver;
    private double angle = 45;
    private double speedX = 1;
    private double speedY = 1;
    private double gravity = 9;
    private int projectileId;

    ProjectileFlight(WebSocket receiver, BallisticParams ballisticParams, int projectileId) {
        this.receiver = receiver;

        this.angle = ballisticParams.angle;
        this.speedX = ballisticParams.speedX;
        this.speedY = ballisticParams.speedY;
        this.gravity = ballisticParams.gravity;
        this.projectileId = projectileId;
    }

    public void run() {
        double x = 0.;
        double y = 0.;
        for (int i = 0; i < 20; i++) {
            try {
                x+=0.01;
                y+=0.01;
                Message msg = new Message("flight", gson.toJson(new FlightInfo(projectileId, x, y)));
                receiver.send(gson.toJson(msg));
                Thread.sleep((long) (1000 / freq));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
