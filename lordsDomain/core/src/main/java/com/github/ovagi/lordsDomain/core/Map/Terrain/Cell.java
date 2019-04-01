package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Cord;
import playn.core.Tile;

import java.awt.*;
import java.util.List;

public class Cell {

    private Cord cellCenter;
    private List<Cell> neighboringCells;
    private TerrainTypes terrainType;
    private int elevation;
    private Color color;
    private Tile tile;

    public Cell (Cord cellCenter) {
        this.cellCenter = cellCenter;
    }

    public Cell (Cord cellCenter, List<Cell> neighboringCells) {
        this.cellCenter = cellCenter;
        this.neighboringCells = neighboringCells;
    }

    public Cell (Cord cellCenter, List<Cell> neighboringCells, TerrainTypes terrainType) {
        this.cellCenter = cellCenter;
        this.neighboringCells = neighboringCells;
    }


    public Cord getCellCenter() {
        return cellCenter;
    }

    public void setCellCenter(Cord cellCenter) {
        this.cellCenter = cellCenter;
    }

    public List<Cell> getNeighboringCells() {
        return neighboringCells;
    }

    public void setNeighboringCells(List<Cell> neighboringCells) {
        this.neighboringCells = neighboringCells;
    }

    public Color getColor() {
        if(color == null) {
            color = Terrain.getColor(terrainType);
        }
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTerrainType(TerrainTypes terrainType) {
        this.terrainType = terrainType;
    }

    public void close() {
        tile.texture().close();
    }

    public TerrainTypes getTerrainType() {
        return terrainType;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }
}
