package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.LordsDomain;
import org.jetbrains.annotations.NotNull;
import playn.core.Path;
import playn.core.Texture;
import playn.core.Tile;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;
import react.SignalView;

import java.awt.*;
import java.util.Arrays;

public class Terrain {

    private final Texture tileSetTextures;
    private final playn.core.Canvas tileSet;
    private final Dimension cellSize;

    public Terrain(playn.core.Canvas canvas, Dimension cellSize) {
        this.cellSize = cellSize;
        this.tileSet = canvas;

        System.out.println(cellSize);
        System.out.println(canvas);

        for (int i = 0; i < TerrainTypes.values().length; i++) {
            tileSet
                    //Fill in Shape
                    .setFillColor(getColor(TerrainTypes.values()[i]).hashCode()).fillRect(0, cellSize.height() * i, cellSize.width(), cellSize.height())
                    //Outline shape
                    .setStrokeColor(Color.BLACK.hashCode()).setStrokeWidth(1).strokeRect(0, cellSize.height() * i, cellSize.width(), cellSize.height());
        }


        tileSetTextures = tileSet.toTexture(Texture.Config.UNMANAGED);
    }

    public static Color getColor(TerrainTypes terrainType) {
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

    public Tile getTile(TerrainTypes terrainType) {
        Tile tile;
        switch (terrainType) {
            case GRASSLANDS:
                tile = tileSetTextures.tile(0, 0, cellSize.width(), cellSize.height());
                break;
            case FOREST:
                tile = tileSetTextures.tile(0, cellSize.height(), cellSize.width(), cellSize.height());
                break;
            case OCEAN:
                tile = tileSetTextures.tile(0,  cellSize.height() * 2, cellSize.width(), cellSize.height());
                break;
            case RIVER:
                tile = tileSetTextures.tile(0,  cellSize.height() * 3, cellSize.width(), cellSize.height());
                break;
            case SWAMP:
                tile = tileSetTextures.tile(0,  cellSize.height() * 4, cellSize.width(), cellSize.height());
                break;
            case DESERT:
                tile = tileSetTextures.tile(0,  cellSize.height() * 5, cellSize.width(), cellSize.height());
                break;
            default:
                tile = tileSetTextures.tile(0,0,0,0);
        }

        return tile;
    }

    public SignalView.Listener<? super Layer> onDisposed() {
        return tileSetTextures.disposeSlot();
    }
}

