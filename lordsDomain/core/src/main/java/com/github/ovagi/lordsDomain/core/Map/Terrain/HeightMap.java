package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Map;
import playn.core.Color;
import playn.core.Surface;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;


import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.github.ovagi.lordsDomain.core.Map.Map.NUMBER_OF_CELLS;
import static com.github.ovagi.lordsDomain.core.Map.Map.NUMBER_OF_CELLS_ROW;

public class HeightMap {


    public static final int MAX_HEIGHT = 1000;

    public void generateHeightMap(ArrayList<Cell> cells, Dimension cellSize) {
        int[][] heightMap = DSquare.generateHeightMapWithBound(ThreadLocalRandom.current(), 100, MAX_HEIGHT, Map.NUMBER_OF_CELLS_COLUMN, NUMBER_OF_CELLS_ROW, (int) cellSize.height());

        for (int i = 0; i < NUMBER_OF_CELLS_ROW; i++) {
            for (int j = 0; j < Map.NUMBER_OF_CELLS_COLUMN; j++) {
                cells.get(NUMBER_OF_CELLS_ROW * i + j).setElevation(heightMap[i][j]);
            }
        }
    }
}

