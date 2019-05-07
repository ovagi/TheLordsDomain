package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Cord;
import org.jetbrains.annotations.NotNull;
import playn.core.Tile;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class Cell implements Comparable {

    private static double width;
    private static double height;
    private Cord cord;
    private List<Cell> neighboringCells;
    private TerrainTypes terrainType;
    private double elevation;
    private double temperature;
    private double humidity;
    private boolean isWater;
    private Color color;
    private Tile tile;
    private Cell drainsIntoMe;

    public Cell (Cord cord) {
        this.cord = cord;
    }

    public Cell (Cord cord, List<Cell> neighboringCells) {
        this.cord = cord;
        this.neighboringCells = neighboringCells;
    }

    public Cell (Cord cord, List<Cell> neighboringCells, TerrainTypes terrainType) {
        this.cord = cord;
        this.neighboringCells = neighboringCells;
    }

    public Cell() {
    }

    public static double getWidth() {
        return width;
    }

    public static void setWidth(double width) {
        Cell.width = width;
    }

    public static double getHeight() {
        return height;
    }

    public static void setHeight(double height) {
        Cell.height = height;
    }


    public Cord getCord() {
        return cord;
    }

    public void setCord(Cord cord) {
        this.cord = cord;
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

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public boolean isWater() {
        return isWater;
    }

    public void setWater(boolean water) {
        this.isWater = water;
    }

    public Cell getDrainsIntoMe() {
        return drainsIntoMe;
    }

    public void setDrainsIntoMe(Cell drainsIntoMe) {
        this.drainsIntoMe = drainsIntoMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return Double.compare(cell.elevation, elevation) == 0 &&
                Double.compare(cell.humidity, humidity) == 0 &&
                isWater == cell.isWater &&
                Objects.equals(cord, cell.cord) &&
                Objects.equals(neighboringCells, cell.neighboringCells) &&
                terrainType == cell.terrainType &&
                Objects.equals(color, cell.color) &&
                Objects.equals(tile, cell.tile) &&
                Objects.equals(drainsIntoMe, cell.drainsIntoMe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cord, terrainType, elevation, humidity, isWater, color, tile, drainsIntoMe);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if(o instanceof Cell) {
            return ((Cell) o).getCord().compareTo(this.getCord());
        }
        return 0;
    }

    public Cell withWaterFill(int waterFill) {
        this.humidity = waterFill;
        return this;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
