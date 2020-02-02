package com.sergeiyarema.ballistics;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectileFlightTest {

    @Test
    public void updateCoords() {
        BallisticParams bp = new BallisticParams(45.0, 10.0, 10.0);
        ProjectileFlight projectileFlight = new ProjectileFlight(null, bp, 1);

        projectileFlight.setTime(0);
        Assert.assertEquals(0.0, projectileFlight.getX(), 0.001);
        Assert.assertEquals(0.01, projectileFlight.getY(), 0.001);

        projectileFlight.setTime(1.0);
        Assert.assertEquals(7, projectileFlight.getX(), 0.1);
        Assert.assertEquals(2, projectileFlight.getY(), 0.1);
    }
}