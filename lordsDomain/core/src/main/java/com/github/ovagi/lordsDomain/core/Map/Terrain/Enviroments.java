package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Map;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.github.ovagi.lordsDomain.core.Map.Map.NUMBER_OF_CELLS_COLUMN;
import static com.github.ovagi.lordsDomain.core.Map.Map.NUMBER_OF_CELLS_ROW;
import static java.util.Arrays.stream;

public class Enviroments {

    public static double globalHumidity = ThreadLocalRandom.current().nextDouble(0, 1);
    public static double globalTemperature = ThreadLocalRandom.current().nextDouble(0, 1);

    private static final int FEATURE_SIZE = 16;
    private double[][] output;

    public Enviroments(List<Cell> cells) {
        int maxDistanceToWater = 0;
        boolean notFinished = true;
        //Set Cell humidity
        cells.parallelStream().forEach(cell -> cell.setHumidity(-1));
        cells.parallelStream().filter(Cell::isWater).forEach(cell -> cell.setHumidity(0));

        for (int distance = 0; notFinished && distance < 512; distance++) {
            int finalDistance = distance;
            cells.parallelStream().filter(cell -> cell.getHumidity() == -1 && cell.getNeighboringCells().parallelStream().anyMatch(cell1 -> cell1.getHumidity() == finalDistance)).forEach(cell -> cell.setHumidity(finalDistance + 1));
            notFinished = cells.parallelStream().anyMatch(cell -> cell.getHumidity() == -1);
            maxDistanceToWater = distance > maxDistanceToWater ? distance : maxDistanceToWater;
        }

        cells.parallelStream().filter(Cell::isWater).forEach(cell -> cell.setHumidity(1));

        int finalMaxDistanceToWater = maxDistanceToWater;
        cells.parallelStream().filter(cell -> !cell.isWater()).forEach(cell -> cell.setHumidity(globalHumidity + 1 - (cell.getHumidity() / finalMaxDistanceToWater)));

        //Set Cell Temperature
        double[][] heightMap = generate();

        double minHeight = Math.abs(stream(heightMap).mapToDouble(a -> stream(a).min().orElse(0)).min().orElse(0));
        double maxHeight = stream(heightMap).mapToDouble(a -> stream(a).max().orElse(0)).max().orElse(0) + minHeight;


        for (int i = 0; i < NUMBER_OF_CELLS_ROW; i++) {
            for (int j = 0; j < NUMBER_OF_CELLS_COLUMN; j++) {
                cells.get(NUMBER_OF_CELLS_ROW * i + j).setTemperature(((heightMap[i][j] + minHeight) / maxHeight + globalTemperature));
            }
        }

        cells.parallelStream().filter(cell -> !cell.isWater()).forEach(cell -> cell.setTerrainType(generateBiome(cell.getTemperature(), cell.getHumidity())));
    }

    private TerrainTypes generateBiome(double temperature, double humidity) {
        humidity = humidity * temperature;

        if (temperature < 0.1)
            return TerrainTypes.TUNDRA;

        if (humidity < 0.2) {
            if (temperature < 0.5)
                return TerrainTypes.TUNDRA;
            if (temperature < 0.95)
                return TerrainTypes.SAVANNA;
            return TerrainTypes.DESERT;
        }

        if ((humidity > 0.5) && (temperature < 0.7))
            return TerrainTypes.SWAMP;

        if (temperature < 0.5)
            return TerrainTypes.TAIGA;

        if (temperature < 0.97) {
            if (humidity < 0.35)
                return TerrainTypes.ARID_GRASSLAND;
            return TerrainTypes.FOREST;
        }

        if (humidity < 0.45)
            return TerrainTypes.GRASSLANDS;

        if (humidity < 0.9)
            return TerrainTypes.TEMPERATE_FOREST;

        return TerrainTypes.RAINFOREST;
    }

    private double[][] generate() {
        output = new double[Map.NUMBER_OF_CELLS_ROW][Map.NUMBER_OF_CELLS_COLUMN];

        for (int y = 0; y < NUMBER_OF_CELLS_COLUMN; y += FEATURE_SIZE) {
            for (int x = 0; x < NUMBER_OF_CELLS_ROW; x += FEATURE_SIZE) {
                setCord(x, y, ThreadLocalRandom.current().nextInt(-1, 1));
            }
        }

        int sampleSize = NUMBER_OF_CELLS_ROW;
        double scale = 1.0;

        while (sampleSize > 1) {

            DiamondSquare(sampleSize, scale);

            sampleSize /= 2;
            scale /= 2;
        }


        return output;
    }

    private void DiamondSquare(int sampleSize, double scale) {
        int halfstep = sampleSize / 2;

        for (int y = halfstep; y < NUMBER_OF_CELLS_ROW + halfstep; y += sampleSize) {
            for (int x = halfstep; x < NUMBER_OF_CELLS_COLUMN + halfstep; x += sampleSize) {
                doSquare(x, y, sampleSize, scale * ThreadLocalRandom.current().nextInt(-1, 1));
            }
        }

        for (int y = 0; y < NUMBER_OF_CELLS_ROW; y += sampleSize) {
            for (int x = 0; x < NUMBER_OF_CELLS_COLUMN; x += sampleSize) {
                doDiamond(x + halfstep, y, sampleSize, scale * ThreadLocalRandom.current().nextInt(-1, 1));
                doDiamond(x, y + halfstep, sampleSize, scale * ThreadLocalRandom.current().nextInt(-1, 1));
            }
        }
    }

    private double averageWithRandom(double randomValue, double... doubles) {
        return (stream(doubles).average().getAsDouble()) + randomValue;
    }

    private void doSquare(int x, int y, int stepSize, double randomValue) {
        double bottomLeft = getCord(x - stepSize / 2, y - stepSize / 2);
        double bottomRight = getCord(x + stepSize / 2, y - stepSize / 2);
        double topLeft = getCord(x - stepSize / 2, y + stepSize / 2);
        double topRight = getCord(x + stepSize / 2, y + stepSize / 2);
        setCord(x, y, averageWithRandom(randomValue, bottomLeft, bottomRight, topLeft, topRight));
    }

    private void doDiamond(int x, int y, int stepSize, double randomValue) {
        double left = getCord(x - stepSize / 2, y);
        double right = getCord(x + stepSize / 2, y);
        double down = getCord(x, y - stepSize / 2);
        double up = getCord(x, y + stepSize / 2);
        setCord(x, y, averageWithRandom(randomValue, left, right, down, up));
    }

    private double getCord(int x, int y) {
        return output[(x & (NUMBER_OF_CELLS_ROW - 1))][y & (NUMBER_OF_CELLS_COLUMN - 1)];
    }

    private void setCord(int x, int y, double value) {
        output[(x & (NUMBER_OF_CELLS_ROW - 1))][y & (NUMBER_OF_CELLS_COLUMN - 1)] = value;
    }
}
