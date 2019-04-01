package com.github.ovagi.lordsDomain.core.Map;

import com.github.ovagi.lordsDomain.core.LordsDomain;
import com.github.ovagi.lordsDomain.core.Map.Terrain.Cell;
import com.github.ovagi.lordsDomain.core.Map.Terrain.Terrain;
import com.github.ovagi.lordsDomain.core.Map.Terrain.TerrainTypes;
import org.jetbrains.annotations.NotNull;
import playn.core.Canvas;
import playn.core.Surface;
import playn.core.Texture;
import playn.core.Tile;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import pythagoras.f.Dimension;
import pythagoras.f.IDimension;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Map extends GroupLayer {

    public static final int NUMBER_OF_CELLS = 10000;
    public static final int NUMBER_OF_CELLS_ROW = 100;
    public static final int NUMBER_OF_CELLS_COLUMN = NUMBER_OF_CELLS / NUMBER_OF_CELLS_ROW;
    public static final float LINE_WIDTH = 2;
    public Dimension cellSize;
    private final GroupLayer groupLayer = new GroupLayer();
    private ArrayList<Cell> cells;

    private final LordsDomain lordsDomain;
    private final Terrain terrain;

    public Map(LordsDomain lordsDomain, @NotNull IDimension size) {
        this.lordsDomain = lordsDomain;

        cells = new ArrayList<>();
        cellSize = new Dimension(size.width() / NUMBER_OF_CELLS_ROW, size.height() / NUMBER_OF_CELLS_COLUMN);

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

        terrain = new Terrain(canvas, cellSize);
        terrain.generateBasicTerrain(cells);

        onDisposed(terrain.onDisposed());

        for (Cell cell : cells) {
//            cell.setTerrainType(TerrainTypes.values()[ThreadLocalRandom.current().nextInt(TerrainTypes.values().length)]);
            cell.setTile(terrain.getTile(cell.getTerrainType()));
            setPiece(cell.getCellCenter(), cell);
            System.out.println(cell.getElevation());
            System.out.println(cell.getColor().getAlpha());
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
        groupLayer.addAt(pview, cell.getCellCenter().x(), cell.getCellCenter().y());
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



