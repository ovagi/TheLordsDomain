package com.github.ovagi.lordsDomain.core.Map.Terrain;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.github.ovagi.lordsDomain.core.Map.Map.DRAWING_WATER_SHED;

//https://medium.com/universe-factory/how-i-generated-artificial-rivers-on-imaginary-continents-747df12c5d4c

public class WaterBodies {

    private static final int NUMBER_OF_RIVERS = 5;
    public static final float OCEAN_HEIGHT = (float) ThreadLocalRandom.current().nextDouble(0, 1.0 / 3.0);
    private static final int MAX_RIVER_LENGTH = 25;
    private static final int MAX_TIME_STEP = 100;
    private static final double RIVER_HEIGHT = (float) ThreadLocalRandom.current().nextDouble(3.0 / 10.0, 4.0 / 10.0);

    public WaterBodies(ArrayList<Cell> cells) {


        //Rivers
//        elev = a 2-d numeric array
        System.out.println("LOG: Starting River Generation");
        cells.forEach(cell -> cell.setDrainsIntoMe(new HashSet<>()));
        cells.forEach(cell -> cell.setWaterFill(cell.getElevation()));
        double maxDrains = 0;


        //Find Spawn Point of River
            //First find border cells
        Cell randomBroder = getRandomCell(cells.parallelStream().filter(cell -> cell.getNeighboringCells().size() < 8).collect(Collectors.toCollection(ArrayList::new)));
        randomBroder


        //Find End Point of River

        //

        for (Cell cell : cells) {
            boolean draining = false;
            do {
                Cell min = cell.getNeighboringCells().parallelStream().min(Comparator.comparingDouble(Cell::getElevation)).orElse(null);
                if (min != null) {
                    if (min.getElevation() < cell.getElevation()) {
                        min.getDrainsIntoMe().add(cell);
                        min.getDrainsIntoMe().addAll(cell.getDrainsIntoMe());
                        if (min.getDrainsIntoMe().size() > maxDrains) {
                            maxDrains = min.getDrainsIntoMe().size();
                        }
                        cell = min;
                        draining = true;

                    } else {
                        System.out.println("DEBUG: Found Non-Draining Cell");
                        draining = false;
                    }
                }
            } while (draining);
        }

        for (Cell cell: cells) {
            cell.setWaterFill(cell.getDrainsIntoMe().size() / maxDrains);
            if(cell.getWaterFill() > RIVER_HEIGHT) {
                cell.setTerrainType(TerrainTypes.RIVER);
            }
        }

        System.out.println("LOG: Ending River Generation");
//        for every cell
//        find neighboring cells and store fill values
//        set neighbor values to NULL where drain = 0
//        get max neighbor value
//        if max is higher than self fill
//        set fill to max
//        set drains to 1
//        output fill
        //output fill

        //        //Oceans
        cells.parallelStream().forEach(cell -> {
            if (cell.getElevation() < OCEAN_HEIGHT) {
                cell.setTerrainType(TerrainTypes.OCEAN);
            }
        });

    }

    private Cell getRandomCell(ArrayList<Cell> cells) {
        if (cells == null) {
            return null;
        }

        if (cells.isEmpty()) {
            return null;
        }

        return cells.get(ThreadLocalRandom.current().nextInt(cells.size()));

    }

}
