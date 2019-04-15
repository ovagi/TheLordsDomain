package com.github.ovagi.lordsDomain.core.Map.Terrain;

import playn.core.Gradient;
import playn.core.Texture;
import playn.core.Tile;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import react.SignalView;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.github.ovagi.lordsDomain.core.Map.Terrain.WaterBodies.OCEAN_HEIGHT;

public class Terrain {

    private static final int OCEAN_DEPTHS_COLOR = playn.core.Color.rgb(0, 0, 100);
    private static final int GRASSLAND_FLAT_COLOR = playn.core.Color.rgb(124,252,0);
    private static final int GRASSLAND_LOWLAND_COLOR = playn.core.Color.rgb(34,139,34);
    private static final int SLOPES_COLOR = playn.core.Color.rgb(139, 90, 43);
    private static final int MOUNTAINS_COLOR = playn.core.Color.rgb(255, 255, 255);
    private static final int OCEAN_SHORE_COLOR = playn.core.Color.rgb(26, 141, 255);

    private static final float HILL_ELEVATION = (float) ThreadLocalRandom.current().nextDouble(5.0/9.0, 7.0/9.0);
    private static final float GRASSLAND_ELEVATION = (float) ThreadLocalRandom.current().nextDouble(2.0/10.0, 3.0/10.0);

    private static final int WATER_SHED_BOTTOM = playn.core.Color.rgb(0, 0, 0);
    private static final int WATER_SHED_MIDDLE = playn.core.Color.rgb(0, 0, 255);
    private static final int WATER_SHED_TOP = playn.core.Color.rgb(255, 255, 255);

    private final Texture tileSetTextures;
    private final Dimension cellSize;

    private HeightMap heightMap;


    public Terrain(playn.core.Canvas canvas, Dimension cellSize) {
        this.cellSize = cellSize;

        heightMap = new HeightMap();

        for (int i = 0; i < TerrainTypes.values().length; i++) {
            canvas
                    //Fill in Shape
                    .setFillColor(getColor(TerrainTypes.values()[i]).hashCode()).fillRect(0, cellSize.height() * i, cellSize.width(), cellSize.height());
        }

        //Grassland Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{GRASSLAND_LOWLAND_COLOR, GRASSLAND_FLAT_COLOR, SLOPES_COLOR, MOUNTAINS_COLOR}, new float[]{0, GRASSLAND_ELEVATION, HILL_ELEVATION, 1})))
                .fillRect(0, 0, cellSize.width() * 101, cellSize.height());

        //Ocean Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{OCEAN_DEPTHS_COLOR, OCEAN_SHORE_COLOR}, new float[]{0, OCEAN_HEIGHT})))
                .fillRect(0, cellSize.height(), cellSize.width() * 101, cellSize.height());

        //River Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{WATER_SHED_BOTTOM, WATER_SHED_MIDDLE, WATER_SHED_TOP}, new float[]{0, .5f, 1})))
                .fillRect(0, cellSize.height() * 2, cellSize.width() * 101, cellSize.height());

        tileSetTextures = canvas.toTexture(Texture.Config.UNMANAGED);

    }

    protected static Color getColor(TerrainTypes terrainType) {
        if (terrainType == null) {
            return null;
        }

        switch (terrainType) {
            case GRASSLANDS:
                return Color.GREEN;
            case FOREST:
                return Color.CYAN;
            case OCEAN:
                return Color.BLUE;
            case RIVER:
                return Color.MAGENTA;
            case SWAMP:
                return Color.DARK_GRAY;
            case DESERT:
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }

    public Tile getTile(TerrainTypes terrainType, double elevation) {
        Tile tile;
        switch (terrainType) {
            case GRASSLANDS:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), 0, cellSize.width(), cellSize.height());
                break;
            case OCEAN:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height(), cellSize.width(), cellSize.height());
                break;
            case RIVER:
//                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 2, cellSize.width(), cellSize.height());
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 2, cellSize.width(), cellSize.height());
                break;
            case SWAMP:
                tile = tileSetTextures.tile(0, cellSize.height() * 3, cellSize.width(), cellSize.height());
                break;
            case DESERT:
                tile = tileSetTextures.tile(0, cellSize.height() * 4, cellSize.width(), cellSize.height());
                break;
            case FOREST:
                tile = tileSetTextures.tile(0, cellSize.height() * 5, cellSize.width(), cellSize.height());
                break;
            default:
                tile = tileSetTextures.tile(0, 0, 0, 0);
        }

        return tile;
    }

    public SignalView.Listener<? super Layer> onDisposed() {
        return tileSetTextures.disposeSlot();
    }

    public void generateBasicTerrain(ArrayList<Cell> cells) {
        heightMap.generateHeightMap(cells);
    }
}

