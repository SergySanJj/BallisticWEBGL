package com.sergeiyarema.ballistics;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BallisticParamsTest {
    private static double delta = 0.001;

    @Test
    public void shrinkToRange() {
        Assert.assertEquals(50.0, BallisticParams.shrinkToRange(50.0, 0., 100.), delta);
        Assert.assertEquals(0.0, BallisticParams.shrinkToRange(-50.0, 0., 100.), delta);
        Assert.assertEquals(100.0, BallisticParams.shrinkToRange(150.0, 0., 100.), delta);
    }

    @Test
    public void checkAndFix() {
        BallisticParams bpNormal = new BallisticParams(45., 1., 9.);
        bpNormal.checkAndFix();
        Assert.assertEquals(45.0, bpNormal.angle, delta);
        Assert.assertEquals(1.0, bpNormal.speed, delta);
        Assert.assertEquals(9.0, bpNormal.gravity, delta);

        BallisticParams bpFix = new BallisticParams(4500., 1000., 9000.);
        bpFix.checkAndFix();
        Assert.assertEquals(90.0, bpFix.angle, delta);
        Assert.assertEquals(2.5, bpFix.speed, delta);
        Assert.assertEquals(20.0, bpFix.gravity, delta);
    }
}