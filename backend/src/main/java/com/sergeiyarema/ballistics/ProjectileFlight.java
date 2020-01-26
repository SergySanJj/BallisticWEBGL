package com.sergeiyarema.ballistics;

import org.java_websocket.WebSocket;

public class ProjectileFlight implements Runnable{
    public static final double freq = 25;


    private WebSocket receiver;
    private double angle = 45;
    private double speedX = 1;
    private double speedY = 1;
    private double gravity = 9;

    ProjectileFlight(WebSocket receiver, double angle, double speedX, double speedY, double gravity) {
        this.receiver = receiver;

        this.angle = angle;
        this.speedX = speedX;
        this.speedY = speedY;
        this.gravity = gravity;
    }

    public void run() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < 10; i++) {
            try {
                x++;
                y++;
                receiver.send(x + " " + y);
                Thread.sleep((long) (1000 / freq));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}
