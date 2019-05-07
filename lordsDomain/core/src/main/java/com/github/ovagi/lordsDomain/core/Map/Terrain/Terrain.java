package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Map;
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

    private static final int WATER_SHED_TOP = playn.core.Color.rgb(255, 255, 255);

    private static final int FOREST_LOWLAND_COLOR = playn.core.Color.rgb(47, 98, 70);
    private static final int FOREST_MIDLAND_COLOR = playn.core.Color.rgb(95, 167, 119);
    private static final int FOREST_HIGHLAND_COLOR = playn.core.Color.rgb(49, 120, 115);

    private static final int SWAMP_LOWLAND_COLOR = playn.core.Color.rgb(47, 79, 79);
    private static final int SWAMP_MIDLAND_COLOR = playn.core.Color.rgb(42, 74, 58);
    private static final int SWAMP_HIGHLAND_COLOR = playn.core.Color.rgb(0, 128, 128);

    private static final int TUNDRA_LOWLAND_COLOR = playn.core.Color.rgb(117, 117, 117);
    private static final int TUNDRA_MIDLAND_COLOR = playn.core.Color.rgb(58, 176, 158);
    private static final int TUNDRA_HIGHLAND_COLOR = playn.core.Color.rgb(255, 255, 255);

    private static final int SAVANNA_LOWLAND_COLOR = playn.core.Color.rgb(210, 180, 140);
    private static final int SAVANNA_MIDLAND_COLOR = playn.core.Color.rgb(230, 190, 138);
    private static final int SAVANNA_HIGHLAND_COLOR = playn.core.Color.rgb(225, 250, 112);

    private static final int TAGIA_LOWLAND_COLOR = playn.core.Color.rgb(139, 90, 43);
    private static final int TAGIA_MIDLAND_COLOR = playn.core.Color.rgb(0, 103, 104);
    private static final int TAGIA_HIGHLAND_COLOR = playn.core.Color.rgb(255, 255, 255);

    private static final int ARID_GRASSLAND_LOWLAND_COLOR = playn.core.Color.rgb(200, 200, 0);
    private static final int ARID_GRASSLAND_MIDLAND_COLOR = playn.core.Color.rgb(225, 189, 39);
    private static final int ARID_GRASSLAND_HIGHLAND_COLOR = playn.core.Color.rgb(207, 181, 59);

    private static final int RAINFOREST_LOWLAND_COLOR = playn.core.Color.rgb(0, 144, 0);
    private static final int RAINFOREST_MIDLAND_COLOR = playn.core.Color.rgb(33, 147, 33);
    private static final int RAINFOREST_HIGHLAND_COLOR = playn.core.Color.rgb(0, 128, 0);

    private static final int DESERT_LOWLAND_COLOR = playn.core.Color.rgb(228, 217, 111);
    private static final int DESERT_MIDLAND_COLOR = playn.core.Color.rgb(240, 230, 140);
    private static final int DESERT_HIGHLAND_COLOR = playn.core.Color.rgb(255, 230, 112);

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
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{OCEAN_SHORE_COLOR, WATER_SHED_TOP}, new float[]{OCEAN_HEIGHT, 1})))
                .fillRect(0, cellSize.height() * 2, cellSize.width() * 101, cellSize.height());

        //Forest Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{FOREST_LOWLAND_COLOR, FOREST_MIDLAND_COLOR, FOREST_HIGHLAND_COLOR}, new float[]{OCEAN_HEIGHT, HILL_ELEVATION,  1})))
                .fillRect(0, cellSize.height() * 3, cellSize.width() * 101, cellSize.height());

        //Swamp Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{SWAMP_LOWLAND_COLOR, SWAMP_MIDLAND_COLOR, SWAMP_HIGHLAND_COLOR}, new float[]{OCEAN_HEIGHT, HILL_ELEVATION,  1})))
                .fillRect(0, cellSize.height() * 4, cellSize.width() * 101, cellSize.height());

        //Tundra Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{TUNDRA_LOWLAND_COLOR, TUNDRA_MIDLAND_COLOR, TUNDRA_HIGHLAND_COLOR}, new float[]{OCEAN_HEIGHT, HILL_ELEVATION,  1})))
                .fillRect(0, cellSize.height() * 5, cellSize.width() * 101, cellSize.height());

        //Savanna Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{SAVANNA_LOWLAND_COLOR, SAVANNA_MIDLAND_COLOR, SAVANNA_HIGHLAND_COLOR}, new float[]{OCEAN_HEIGHT, HILL_ELEVATION,  1})))
                .fillRect(0, cellSize.height() * 6, cellSize.width() * 101, cellSize.height());

        //Tagia Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{TAGIA_LOWLAND_COLOR, TAGIA_MIDLAND_COLOR, TAGIA_HIGHLAND_COLOR}, new float[]{OCEAN_HEIGHT, HILL_ELEVATION,  1})))
                .fillRect(0, cellSize.height() * 7, cellSize.width() * 101, cellSize.height());

        //ARID_GRASSLAND Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{ARID_GRASSLAND_LOWLAND_COLOR, ARID_GRASSLAND_MIDLAND_COLOR, ARID_GRASSLAND_HIGHLAND_COLOR}, new float[]{OCEAN_HEIGHT, HILL_ELEVATION,  1})))
                .fillRect(0, cellSize.height() * 8, cellSize.width() * 101, cellSize.height());

        //RAINFOREST Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{RAINFOREST_LOWLAND_COLOR, RAINFOREST_MIDLAND_COLOR, RAINFOREST_HIGHLAND_COLOR}, new float[]{OCEAN_HEIGHT, HILL_ELEVATION,  1})))
                .fillRect(0, cellSize.height() * 9, cellSize.width() * 101, cellSize.height());

        //DESERT Gradient
        canvas.setFillGradient(canvas.createGradient(
                new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{DESERT_LOWLAND_COLOR, DESERT_MIDLAND_COLOR, DESERT_HIGHLAND_COLOR}, new float[]{OCEAN_HEIGHT, HILL_ELEVATION,  1})))
                .fillRect(0, cellSize.height() * 10, cellSize.width() * 101, cellSize.height());

        if(Map.DEBUG) {
            //WaterShed Gradient
            canvas.setFillGradient(canvas.createGradient(
                    new Gradient.Linear(0, 0, cellSize.width() * 101, cellSize.height(), new int[]{OCEAN_DEPTHS_COLOR, WATER_SHED_TOP}, new float[]{0, 1})))
                    .fillRect(0, cellSize.height() * 100, cellSize.width() * 101, cellSize.height());
        }

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
            case DEBUG_WATER_SHED:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), 100 * cellSize.height(), cellSize.width(), cellSize.height());
                break;
            case GRASSLANDS:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), 0, cellSize.width(), cellSize.height());
                break;
            case OCEAN:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height(), cellSize.width(), cellSize.height());
                break;
            case RIVER:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 2, cellSize.width(), cellSize.height());
                break;
            case FOREST:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 3, cellSize.width(), cellSize.height());
                break;
            case SWAMP:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 3, cellSize.width(), cellSize.height());
                break;
            case TUNDRA:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 4, cellSize.width(), cellSize.height());
                break;
            case SAVANNA:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 5, cellSize.width(), cellSize.height());
                break;
            case TAIGA:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 6, cellSize.width(), cellSize.height());
                break;
            case ARID_GRASSLAND:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 7, cellSize.width(), cellSize.height());
                break;
            case TEMPERATE_FOREST:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 8, cellSize.width(), cellSize.height());
                break;
            case RAINFOREST:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 9, cellSize.width(), cellSize.height());
                break;
            case DESERT:
                tile = tileSetTextures.tile((float) (cellSize.width() * 100 * elevation), cellSize.height() * 10, cellSize.width(), cellSize.height());
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

