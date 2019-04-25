package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Cord;

import java.awt.geom.Line2D;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

//https://medium.com/universe-factory/how-i-generated-artificial-rivers-on-imaginary-continents-747df12c5d4c

public class WaterBodies {

    public static final float OCEAN_HEIGHT = (float) ThreadLocalRandom.current().nextDouble(0, 1.0 / 3.0);
    private static final int MAX_RIVER_LENGTH = 500;
    private static final double RIVER_HEIGHT = (float) ThreadLocalRandom.current().nextDouble(3.0 / 10.0, 4.0 / 10.0);
    private static final int MIN_RIVERS = 4;
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
        cells.forEach(cell -> cell.setWaterFill(cell.getElevation()));
        //Find Spawn Point of River
        //First find border cells

        LinkedList<Line2D> river = new LinkedList<>();

        int totalNumberOfRivers = ThreadLocalRandom.current().nextInt(MIN_RIVERS, MAX_RIVERS);
        for (int numberOfRivers = 0; numberOfRivers < totalNumberOfRivers; numberOfRivers++) {
            Cell end = getRandomCell(cells.parallelStream().filter(Cell::isWater).collect(Collectors.toList()));
            Cell start = getRandomCell(cells.parallelStream().filter(cell -> !cell.isWater() && cell.getElevation() > end.getElevation() && Cord.getDistance(cell.getCord(), end.getCord()) < MAX_RIVER_LENGTH).collect(Collectors.toList()));
            river.add(new Line2D.Float(start.getCord().x, start.getCord().y, end.getCord().x, end.getCord().y));
        }


        cells.parallelStream().filter(cell -> river.parallelStream().anyMatch(line -> line.intersects(cell.getCord().x(), cell.getCord().y(), Cell.getWidth(), Cell.getHeight())) && !cell.isWater()).forEach(cell -> {
            cell.setTerrainType(TerrainTypes.RIVER);
            cell.setWater(true);
        });

        //Thicken Rivers
        cells.parallelStream().filter(cell -> cell.getTerrainType().equals(TerrainTypes.RIVER)).forEach(cell -> cell.getNeighboringCells().parallelStream().filter(cell1 -> !cell1.isWater()).forEach(cell1 -> cell1.setWater(true)));
        cells.parallelStream().filter(cell -> cell.isWater() && !cell.getTerrainType().equals(TerrainTypes.RIVER) && !cell.getTerrainType().equals(TerrainTypes.OCEAN)).forEach(cell -> cell.setTerrainType(TerrainTypes.RIVER));

        //Erosion
        List<Cell> riverCells = cells.parallelStream().filter(cell -> cell.getTerrainType().equals(TerrainTypes.RIVER)).collect(Collectors.toList());
        List<Set<Cell>> rivers = new LinkedList<>();

        for (Cell cell: riverCells) {
            if(rivers.parallelStream().noneMatch(set -> set.contains(cell))) {
                TreeSet<Cell> set = new TreeSet<>();
                rivers.add(set);
                set.add(cell);
                AtomicBoolean building = new AtomicBoolean(true);
                while(building.get()) {
                    building.set(false);
                    TreeSet<Cell> tempSet = new TreeSet<>(set);
                    for (Cell cell1 : set) {
                        cell1.getNeighboringCells().parallelStream()
                                .filter(cell2 -> cell2.getTerrainType().equals(TerrainTypes.RIVER))
                                .forEach(cell2 -> {
                                    tempSet.add(cell2);
                                    building.set(true);
                                });
                    }
                    set.addAll(tempSet);
                }
            }
        }

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
