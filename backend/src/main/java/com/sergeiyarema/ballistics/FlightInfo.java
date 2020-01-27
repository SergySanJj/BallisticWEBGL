package com.sergeiyarema.ballistics;

public class FlightInfo {
    public int id;
    public double x;
    public double y;

    FlightInfo(int projectileId, double x, double y) {
        this.id = projectileId;
        this.x = x;
        this.y = y;
    }
}
