package com.sergeiyarema.ballistics;

public class BallisticParams {
    public double angle;
    public double speedX;
    public double speedY;
    public double gravity;

    BallisticParams(double angle, double speedX, double speedY, double gravity) {
        this.angle = angle;
        this.speedX = speedX;
        this.speedY = speedY;
        this.gravity = gravity;
    }
}
