package com.github.ovagi.lordsDomain.core;


import com.github.ovagi.lordsDomain.core.Map.Map;
import playn.core.*;
import playn.scene.*;
import pythagoras.f.IDimension;

public class LordsDomain extends SceneGame {

    public final IDimension size;

    public LordsDomain(Platform plat) {
        super(plat, 1000); // update our "simulation" 33ms (30 times per second)


        // figure out how big the game view is
        size = plat.graphics().viewSize;

        // create a layer that just draws a grey background
        rootLayer.add(new Layer() {
            protected void paintImpl (Surface surf) {
                surf.setFillColor(Color.rgb(0,0,0)).fillRect(0, 0, size.width(), size.height());
            }
        });

        // create and add a board view
        rootLayer.add(new Map(this, size));
    }
}
