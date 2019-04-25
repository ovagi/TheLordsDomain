package com.github.ovagi.lordsDomain.core.Map.Terrain;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.github.ovagi.lordsDomain.core.Map.Map.DRAWING_WATER_SHED;
import static com.github.ovagi.lordsDomain.core.Map.Map.NUMBER_OF_CELLS;

//https://medium.com/universe-factory/how-i-generated-artificial-rivers-on-imaginary-continents-747df12c5d4c

public class WaterBodies {

    public static final float OCEAN_HEIGHT = (float) ThreadLocalRandom.current().nextDouble(0, 1.0 / 3.0);
    private static final int MIN_RIVER_LENGTH = NUMBER_OF_CELLS / 8;
    private static final int MAX_RIVER_LENGTH = NUMBER_OF_CELLS / 4;
    private static final double RIVER_HEIGHT = (float) ThreadLocalRandom.current().nextDouble(3.0 / 10.0, 4.0 / 10.0);

    public WaterBodies(ArrayList<Cell> cells) {


        //Rivers
//        elev = a 2-d numeric array
        System.out.println("LOG: Starting River Generation");
        cells.forEach(cell -> cell.setWaterFill(cell.getElevation()));
        //Find Spawn Point of River
            //First find border cells
        Cell start = getRandomCell(cells.parallelStream().filter(cell -> cell.getNeighboringCells().size() < 4).collect(Collectors.toCollection(ArrayList::new)));
        start.setTerrainType(TerrainTypes.RIVER);

        //Find End Point of River
        Cell end = getRandomCell(cells.parallelStream().filter(cell -> cell.getNeighboringCells().size() < 4).collect(Collectors.toCollection(ArrayList::new)));


        //Check that end is not on the same column / row as start
        boolean checking = true;
        ArrayList<Cell> edgeCells = start.getNeighboringCells().parallelStream().filter(cell -> cell.getNeighboringCells().size() < 4).collect(Collectors.toCollection(ArrayList::new));
        boolean foundEnd = false;
        do {
            while (checking && !foundEnd) {
                Cell finalEnd = end;
                foundEnd = edgeCells.parallelStream().anyMatch(cell -> cell.equals(finalEnd));


                edgeCells = edgeCells.parallelStream()
                        .filter(cell -> cell.getNeighboringCells().size() == 3)
                        .map(Cell::getNeighboringCells)
                        .flatMap(Collection::parallelStream)
                        .collect(Collectors.toCollection(ArrayList::new));

                checking = edgeCells.isEmpty();
            }
            end = getRandomCell(cells.parallelStream().filter(cell -> cell.getNeighboringCells().size() < 4).collect(Collectors.toCollection(ArrayList::new)));
            checking = false;
        } while (foundEnd);

        end.setTerrainType(TerrainTypes.RIVER);

        boolean notFinished = true;

        int timeStamp = 0;

        System.out.println("Max Timestamp = " + MAX_RIVER_LENGTH);

        Cell temp = start;

        //Bellman ford
        int numberOfConnections = cells.parallelStream().mapToInt(cell -> cell.getNeighboringCells().size()).sum();

        Cell finalEnd1 = end;
        cells.forEach(
                    cell -> {
                            cell.setDrainsIntoMe(cell.getNeighboringCells().parallelStream().min((o1, o2) -> (int) (o1.getElevation() - o2.getElevation())).orElse(
                                    cell.getNeighboringCells().parallelStream().max(Comparator.comparingInt(o -> o.getCellCenter().compareTo(finalEnd1.getCellCenter()))).get()));
                            cell.setDrains(true);
                    });


        end.setDrainsIntoMe(end);
        start.setDrains(true);

            do {
                System.out.println(++timeStamp);

//                if(!temp.getDrainsIntoMe().isDrains()) {
                    temp = temp.getDrainsIntoMe();
                    temp.setDrains(true);
                    temp.setTerrainType(TerrainTypes.RIVER);

//                    if(temp.equals(end)) {
//                        notFinished = false;
//                    }
//                } else {
//                    cells.forEach(cell -> cell.setTerrainType(TerrainTypes.GRASSLANDS));
                    temp = getRandomCell(start.getNeighboringCells().parallelStream().filter(Cell::isDrains).collect(Collectors.toList()));
//                }

            } while (notFinished && timeStamp < MAX_RIVER_LENGTH);

        System.out.println("LOG: Ending River Generation");

        //Oceans
//        cells.parallelStream().forEach(cell -> {
//            if (cell.getElevation() < OCEAN_HEIGHT) {
//                cell.setTerrainType(TerrainTypes.OCEAN);
//            }
//        });

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

}
