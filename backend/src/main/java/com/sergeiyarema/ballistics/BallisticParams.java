package com.sergeiyarema.ballistics;

public class BallisticParams {
    public double angle;
    public double speed;
    public double gravity;

    BallisticParams(double angle, double speed, double gravity) {
        this.angle = angle;
        this.speed = speed;
        this.gravity = gravity;
    }

    public void checkAndFix() {
        angle = shrinkToRange(angle, 1, 90);
        speed = shrinkToRange(speed, 0.5, 2.5);
        gravity = shrinkToRange(gravity, 5., 20.);
    }

    public static double shrinkToRange(double val, double a, double b) {
        if (val < a)
            val = a;
        else if (val > b)
            val = b;
        return val;
    }
}
