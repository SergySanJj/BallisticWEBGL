package com.sergeiyarema.ballistics;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectileFlightTest {

    @Test
    public void updateCoords() {
        BallisticParams bp = new BallisticParams(45.0,1.0,10.0);
        ProjectileFlight projectileFlight = new ProjectileFlight(null,bp,1);

        projectileFlight.updateCoords();
        Assert.assertEquals(0.0,projectileFlight.getX(),0.001);
        Assert.assertEquals(0.01,projectileFlight.getY(),0.001);
    }
}