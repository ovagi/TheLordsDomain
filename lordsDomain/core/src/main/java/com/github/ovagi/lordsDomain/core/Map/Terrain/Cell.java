package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Cord;

import java.util.List;

public class Cell {

    private Cord cellCenter;
    private List<Cell> neighboringCells;

    public Cell (Cord cellCenter) {
        this.cellCenter = cellCenter;
    }

    public Cell (Cord cellCenter, List<Cell> neighboringCells) {
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
}
