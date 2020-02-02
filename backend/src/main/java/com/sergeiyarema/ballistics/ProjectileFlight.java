package com.sergeiyarema.ballistics;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;

import java.util.concurrent.TimeUnit;

public class ProjectileFlight implements Runnable {
    public static final double freq = 25;
    private static Gson gson = new Gson();

    private WebSocket receiver;
    private double angle = 45;
    private double speed = 1;
    private double gravity = 9;
    private int projectileId;

    private double time = 0.;

    private double x = 0.;
    private double y = 0.5;

    ProjectileFlight(WebSocket receiver, BallisticParams ballisticParams, int projectileId) {
        this.receiver = receiver;

        this.angle = ballisticParams.angle;
        this.speed = ballisticParams.speed;
        this.gravity = ballisticParams.gravity;
        this.projectileId = projectileId;
    }

    public void run() {
        while (y > 0.001) {
            time += 1 / freq;
            updateCoords();

            Message msg = new Message("flight", gson.toJson(new FlightInfo(projectileId, x, y)));
            try {
                receiver.send(gson.toJson(msg));
                Thread.sleep((long) (1000 / freq));
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void updateCoords() {
        double x0 = 0.0;
        double y0 = 0.01;
        double alpha = Math.toRadians(this.angle);
        double vx = this.speed * Math.cos(alpha);
        double vy = this.speed * Math.sin(alpha);
        x = time * vx;
        y = time * vy - this.gravity * time * time / 2.f;

        x = x + x0;
        y = y + y0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
