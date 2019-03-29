package com.github.ovagi.lordsDomain.core.Map.Terrain;

import playn.core.Surface;
import playn.scene.Layer;
import pythagoras.f.IDimension;

import java.util.Random;

public class Terrain extends Layer {

    private static final int NUMBER_OF_CELLS = 100;

    private final IDimension size;
    public HeightMap heightMap;
    public static Random random = new Random();
    private WaterBodies waterBodies;

    public Terrain(IDimension size) {
        this.size = size;
        heightMap = new HeightMap(size);
        waterBodies = new WaterBodies(size);
    }

    @Override
    public void paintImpl(Surface surface) {
        //waterBodies.paintImpl(surface);



    }


}
