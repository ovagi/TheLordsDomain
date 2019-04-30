package com.github.ovagi.lordsDomain.core.Map.Terrain;

import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

//https://medium.com/universe-factory/how-i-generated-artificial-rivers-on-imaginary-continents-747df12c5d4c

public class WaterBodies {

    public static final float OCEAN_HEIGHT = (float) ThreadLocalRandom.current().nextDouble(0, 1.0 / 3.0);
    private static final int MAX_RIVER_LENGTH = 500;
    private static final double RIVER_HEIGHT = (float) ThreadLocalRandom.current().nextDouble(3.0 / 10.0, 4.0 / 10.0);
    private static final int MIN_RIVERS = 1;
    private static final int MAX_RIVERS = 5;
    private static final double MAX_RIVER_TURN_LENGTH = 200;

    public WaterBodies(ArrayList<Cell> cells) {
        //        Oceans
        cells.parallelStream().forEach(cell -> {
            if (cell.getElevation() < OCEAN_HEIGHT) {
                cell.setTerrainType(TerrainTypes.OCEAN);
                cell.setWater(true);
            }
        });

        //Rivers
//        elev = a 2-d numeric array
        System.out.println("LOG: Starting River Generation");

        //Generate Rivers
        int numberOfRivers = ThreadLocalRandom.current().nextInt(MIN_RIVERS, MAX_RIVERS);
        boolean notFinished;
        for (int i = 0; i < numberOfRivers; i++) {
            Cell riverSeed = getRandomCell(cells.parallelStream().filter(cell -> !cell.isWater()).collect(Collectors.toList()));

            do {
                Cell finalRiverSeed = riverSeed;
                riverSeed = riverSeed.getNeighboringCells().parallelStream()
                        .filter(cell -> cell.getElevation() < finalRiverSeed.getElevation() && !cell.isWater())
                        .min((o1, o2) -> (int) (o1.getElevation() - o2.getElevation()))
                        .orElse(
                                riverSeed.getNeighboringCells().parallelStream()
                                        .filter(cell -> !cell.isWater())
                                        .min((o1, o2) -> (int) (o1.getElevation() - o2.getElevation()))
                                        .orElse(
                                            getRandomCell(riverSeed.getNeighboringCells())
                                        )
                        );



                riverSeed.setTerrainType(TerrainTypes.RIVER);
                riverSeed.setWater(true);
                notFinished = riverSeed.getNeighboringCells().parallelStream().noneMatch(cell -> cell.getTerrainType().equals(TerrainTypes.OCEAN));
            } while (notFinished);

        }

        //Thicken Rivers
        cells.parallelStream().filter(cell -> cell.getTerrainType().equals(TerrainTypes.RIVER)).forEach(cell -> cell.getNeighboringCells().parallelStream().filter(cell1 -> !cell1.isWater()).forEach(cell1 -> cell1.setWater(true)));
        cells.parallelStream().filter(cell -> cell.isWater() && !cell.getTerrainType().equals(TerrainTypes.RIVER) && !cell.getTerrainType().equals(TerrainTypes.OCEAN)).forEach(cell -> cell.setTerrainType(TerrainTypes.RIVER));

        System.out.println("LOG: Ending River Generation");


    }

    private Cell getRandomCell(List<Cell> cells) {
        if (cells == null) {
            return null;
        }

        if (cells.isEmpty()) {
            return null;
        }

        return cells.get(ThreadLocalRandom.current().nextInt(cells.size()));

    }

    private static <T> List<T> randomSubList(List<T> list, int newSize) {
        System.out.print("List Size: " + list.size() + " ");
        System.out.println("New List Size: " + newSize);
        list = new ArrayList<>(list);
        Collections.shuffle(list);
        return list.subList(0, newSize);
    }

}
