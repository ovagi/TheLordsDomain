package Map.Terrain;

import Map.Drawable;
import Map.WaterBodies;

public class Terrain implements Drawable {

    WaterBodies waterBodies;
    HeightMap heightMap;

    public Terrain() {
        heightMap = new HeightMap();
        waterBodies = new WaterBodies(heightMap);
    }

    @Override
    public void draw(int width, int height) {
        waterBodies.draw(width, height);
        heightMap.draw(width, height);
    }
}
