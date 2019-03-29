package com.github.ovagi.lordsDomain.core.Map.Terrain;

import com.github.ovagi.lordsDomain.core.Map.Cord;
import com.github.ovagi.lordsDomain.core.Map.Map;
import playn.core.Event;
import playn.core.Surface;
import playn.scene.Layer;
import pythagoras.f.IDimension;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class WaterBodies extends Layer {

    public static final int WATER_BODY_COMPLEXITY = 100;

    private final River river;
    private final ArrayList<Lake> lake;

    public WaterBodies(IDimension size) {
        lake = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lake.add(new Lake(size, i));
        }

        river = new River(size);
    }

    @Override
    protected void paintImpl(Surface surface) {
        surface.setFillColor(Color.BLUE.hashCode());

        if (lake != null) {
            for (Lake myLake: lake) {
                myLake.paintImpl(surface);
            }
        }

        if (river != null) {
            river.paintImpl(surface);
        }
    }

    private class Lake extends Layer {

        private Cord lakeCenter;
        private int lakeSize;

        private ArrayList<Cord> lakeBorder;

        public Lake(IDimension size, int waterBodyComplexity) {
            lakeCenter = new Cord(ThreadLocalRandom.current().nextInt((int) size.width()), ThreadLocalRandom.current().nextInt((int) size.height()));
            lakeSize = 100;//ThreadLocalRandom.current().nextInt((int) Math.min(size.height(), size.width()));
            lakeBorder = new ArrayList<>();
            this.generate(size, waterBodyComplexity);
        }

        public void generate(IDimension size, int waterBodyComplexity) {
            while (lakeBorder.size() < waterBodyComplexity) {
                lakeBorder.add(new Cord(
                        lakeCenter.x + ThreadLocalRandom.current().nextInt(-lakeSize, lakeSize),
                        lakeCenter.y + ThreadLocalRandom.current().nextInt(-lakeSize, lakeSize)
                        ));
            }


            lakeBorder.sort((point1, point2) ->
                (int) (
                Math.toDegrees(Math.atan2(lakeCenter.y - point2.y, lakeCenter.x - point2.x)) -
                Math.toDegrees(Math.atan2(lakeCenter.y - point1.y, lakeCenter.x - point1.x)))

            );
        }

        protected void paintImpl(Surface surface) {
            for (int i = 0; i < lakeBorder.size(); i++) {
                if (i == lakeBorder.size() - 1) {
                    surface.drawLine(lakeBorder.get(i), lakeBorder.get(0), Map.LINE_WIDTH);
                } else {
                    surface.drawLine(lakeBorder.get(i), lakeBorder.get(i + 1), Map.LINE_WIDTH);
                }
            }
        }
    }

    private class River extends Layer {

        public River(IDimension size) {

        }

        public void generate() {
        }

        @Override
        protected void paintImpl(Surface surface) {

        }
    }


}
