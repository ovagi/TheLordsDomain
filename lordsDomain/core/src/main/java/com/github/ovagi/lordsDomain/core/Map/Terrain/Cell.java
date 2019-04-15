package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Cord;
import playn.core.Tile;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class Cell {

    private Cord cellCenter;
    private List<Cell> neighboringCells;
    private TerrainTypes terrainType;
    private double elevation;
    private double waterFill;
    private boolean drains;
    private Color color;
    private Tile tile;
    private Set<Cell> drainsIntoMe;

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

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getWaterFill() {
        return waterFill;
    }

    public void setWaterFill(double waterFill) {
        this.waterFill = waterFill;
    }

    public boolean isDrains() {
        return drains;
    }

    public void setDrains(boolean drains) {
        this.drains = drains;
    }

    public Set<Cell> getDrainsIntoMe() {
        return drainsIntoMe;
    }

    public void setDrainsIntoMe(Set<Cell> drainsIntoMe) {
        this.drainsIntoMe = drainsIntoMe;
    }
}
