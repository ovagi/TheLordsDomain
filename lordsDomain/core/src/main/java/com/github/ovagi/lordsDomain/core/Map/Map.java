package com.github.ovagi.lordsDomain.core.Map;

import com.github.ovagi.lordsDomain.core.LordsDomain;
import com.github.ovagi.lordsDomain.core.Map.Terrain.*;
import org.jetbrains.annotations.NotNull;
import playn.core.Canvas;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Map extends GroupLayer {

    public static final boolean DEBUG = false;

    public static final int NUMBER_OF_CELLS_ROW = 1024;
    public static final int NUMBER_OF_CELLS_COLUMN = 1024;
    public static final int NUMBER_OF_CELLS = NUMBER_OF_CELLS_ROW * NUMBER_OF_CELLS_COLUMN;
    public static final float LINE_WIDTH = 2;

    private final GroupLayer groupLayer = new GroupLayer();
    private ArrayList<Cell> cells;

    public Map(LordsDomain lordsDomain, @NotNull IDimension size) {

        cells = new ArrayList<>();
        Dimension cellSize = new Dimension(size.width() / NUMBER_OF_CELLS_ROW, size.height() / NUMBER_OF_CELLS_COLUMN);

        System.out.println(cellSize);

        Cell.setWidth(size.width() / NUMBER_OF_CELLS_ROW);
        Cell.setHeight(size.height() / NUMBER_OF_CELLS_COLUMN);

        for (int i = 0; i < NUMBER_OF_CELLS_COLUMN; i++) {
            for (int j = 0; j < NUMBER_OF_CELLS_ROW; j++) {
                cells.add(new Cell(
                        new Cord(
                                (int) size.width() / NUMBER_OF_CELLS_ROW * i,
                                (int) size.height() / NUMBER_OF_CELLS_COLUMN * j))
                );
            }
        }

        for (int i = 0; i < NUMBER_OF_CELLS_ROW; i++) {
            for (int j = 0; j < NUMBER_OF_CELLS_COLUMN; j++) {
                cells.get(NUMBER_OF_CELLS_ROW * i + j).setNeighboringCells(new ArrayList<>());
                //Above
                if (i != 0) {
                    cells.get(NUMBER_OF_CELLS_ROW * i + j).getNeighboringCells().add(
                            cells.get(NUMBER_OF_CELLS_ROW * (i - 1) + j)
                    );
                }
                //Below
                if (i != NUMBER_OF_CELLS_ROW - 1) {
                    cells.get(NUMBER_OF_CELLS_ROW * i + j).getNeighboringCells().add(
                            cells.get(NUMBER_OF_CELLS_ROW * (i + 1) + j)
                    );
                }
                //Left
                if (j != 0) {
                    cells.get(NUMBER_OF_CELLS_ROW * i + j).getNeighboringCells().add(
                            cells.get(NUMBER_OF_CELLS_ROW * i + j - 1)
                    );
                }
                //Right
                if (j != NUMBER_OF_CELLS_COLUMN - 1) {
                    cells.get(NUMBER_OF_CELLS_ROW * i + j).getNeighboringCells().add(
                            cells.get(NUMBER_OF_CELLS_ROW * i + j + 1)
                    );
                }
            }
        }
        Canvas canvas = lordsDomain.plat.graphics().createCanvas(size);

        cells.forEach(cell -> cell.setTerrainType(TerrainTypes.GRASSLANDS));

        Terrain terrain = new Terrain(canvas, cellSize);
        terrain.generateBasicTerrain(cells);
        new WaterBodies(cells);
        new Enviroments(cells);

        onDisposed(terrain.onDisposed());
        if (DEBUG) {
            for (Cell cell : cells) {
                cell.setTerrainType(TerrainTypes.values()[ThreadLocalRandom.current().nextInt(TerrainTypes.values().length)]);

                cell.setTile(terrain.getTile(cell.getTerrainType(), 0));
                setPiece(cell.getCord(), cell);
            }
        } else {
            for (Cell cell : cells) {
                //cell.setTerrainType(TerrainTypes.values()[ThreadLocalRandom.current().nextInt(TerrainTypes.values().length)]);
                cell.setTile(terrain.getTile(cell.getTerrainType(), cell.getElevation()));
                setPiece(cell.getCord(), cell);
            }
        }


        addAt(groupLayer, 0, 0);
    }

    @Override
    public void close() {
        super.close();
        cells.forEach(Cell::close);
    }

    private final java.util.Map<Cord, ImageLayer> pviews = new HashMap<>();

    private ImageLayer addCell(Cord at, Cell cell) {
        ImageLayer pview = new ImageLayer(cell.getTile());
        groupLayer.addAt(pview, cell.getCord().x(), cell.getCord().y());
        return pview;
    }

    private void setPiece(Cord at, Cell cell) {
        ImageLayer pview = pviews.get(at);
        if (pview == null) {
            pviews.put(at, addCell(at, cell));
        } else {
            pview.setTile(cell.getTile());
        }
    }

}



