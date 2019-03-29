package com.github.ovagi.lordsDomain.core.Map.Terrain;

import playn.core.Color;
import playn.core.Surface;
import playn.scene.Layer;
import pythagoras.f.IDimension;


import java.util.Random;

import static com.github.ovagi.lordsDomain.core.Map.Map.NUMBER_OF_CELLS;

public class HeightMap extends Layer {

    private static final double AMOUNT_OF_NOISE = 1;
    double[][] heightMap;
    private IDimension size;
    private Random random = new Random();

    public HeightMap(IDimension size) {
        this.size = size;
        generateTerrainHeight();
    }

    @Override
    protected void paintImpl(Surface surface) {
        surface.setFillColor(playn.core.Color.rgb(255, 255, 255));

        for (int row = 0; row < heightMap.length; row++) {
            for (int column = 0; column < heightMap.length; column++) {
                drawSquare(surface, row, column);
            }
        }

        System.out.println("I painted the screen");
    }
    private void drawSquare(Surface surface,int row, int column) {
        double cell = heightMap[row][column];
        // set the color of the quad (R,G,B,A)

        surface.setFillColor(Color.rgb( 0, (int) (cell * 255), 0));
        surface.fillRect(row * size.width() / NUMBER_OF_CELLS, column * size.height() / NUMBER_OF_CELLS, size.width() / NUMBER_OF_CELLS, size.height() / NUMBER_OF_CELLS);
    }

    public void generateTerrainHeight() {



    }
}

