package Map;

import Map.Terrain.Terrain;
import org.lwjgl.opengl.GL11;

public class Map implements Drawable {

    private final Terrain terrain;
    private final Settlements settlements;
    private final PlacesOfInterest placesOfInterest;

    public Map() {
        terrain = new Terrain();
        settlements = new Settlements();
        placesOfInterest = new PlacesOfInterest();
    }

    public void draw(int width, int height) {

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor3f((float) 0, (float) 1, (float) 0);
            GL11.glVertex2f(0, 0);
            GL11.glVertex2f(0 + 200, 0);
            GL11.glVertex2f(0 + 200, 0 + 200);
            GL11.glVertex2f(0, 0 + 200);
        }
        GL11.glEnd();

        width -= 100;
        height -= 100;

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glColor3f((float) 1, (float) 0, (float) 0);
            GL11.glVertex2f(width, height);
            GL11.glVertex2f(width + 10, height);
            GL11.glVertex2f(width + 10, height + 10);
            GL11.glVertex2f(width, height + 10);
        }
        GL11.glEnd();


//
//
//        terrain.draw(width, height);
//        settlements.draw(width, height);
//        placesOfInterest.draw(width, height);
    }
}
