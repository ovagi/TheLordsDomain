package Map.Terrain;
import Map.Drawable;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class HeightMap implements Drawable {

    private static final double AMOUNT_OF_NOISE = 10000;
    double[][] heightMap;
    private final static int MAP_SIZE = 101;
    private int width;
    private int height;

    public HeightMap() {
        generateTerrainHeight();
    }

    public HeightMap(double[][] heightMap) {
        this.heightMap = heightMap;
    }

    private void drawSquare(int row, int column) {
        double cell = heightMap[row][column];
        // set the color of the quad (R,G,B,A)


        int xOffset = width / heightMap.length * row;
        int yOffset = height / heightMap.length * column;
        int xCellSize = width / heightMap.length;
        int yCellSize = height / heightMap.length;

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor3f((float) cell, (float) cell, (float) cell);
            GL11.glVertex2f(xOffset, yOffset);
            GL11.glVertex2f(xOffset + xCellSize, yOffset);
            GL11.glVertex2f(xOffset + xCellSize, yOffset + yCellSize);
            GL11.glVertex2f(xOffset, yOffset + yCellSize);
        }
        GL11.glEnd();
    }

    public void generateTerrainHeight() {
        //Perform a diamond-square step to generate terrain values
        heightMap = DSquare.generateHeightMap(new Random(), AMOUNT_OF_NOISE, MAP_SIZE, MAP_SIZE, 1);

    }

    @Override
    public void draw(int width, int height) {
        this.width = width;
        this.height = height;

        for (int row = 0; row < heightMap.length; row++) {
            for (int column = 0; column < heightMap.length; column++) {
                drawSquare(row, column);
            }
        }
    }
}

