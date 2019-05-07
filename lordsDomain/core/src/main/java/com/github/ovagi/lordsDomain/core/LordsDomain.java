package com.github.ovagi.lordsDomain.core;


import com.github.ovagi.lordsDomain.core.Map.Map;
import playn.core.*;
import playn.core.Pointer;
import playn.scene.*;
import playn.scene.Mouse;
import pythagoras.f.IDimension;

public class LordsDomain extends SceneGame {

    public final IDimension size;
    public final Pointer pointer;

    public LordsDomain(Platform plat) {
        super(plat, 1000); // update our "simulation" 33ms (30 times per second)

        pointer = new Pointer(plat);
        plat.input().mouseEvents.connect(new Mouse.Dispatcher(rootLayer, false));
        // figure out how big the game view is
        size = plat.graphics().viewSize;

        // create a layer that just draws a grey background
        rootLayer.add(new Layer() {
            protected void paintImpl (Surface surf) {
                surf.setFillColor(Color.rgb(0,0,0)).fillRect(0, 0, size.width(), size.height());
            }
        });

        // create and add a board view
        Map map = newMap();
        map.events().connect(new playn.scene.Pointer.Listener() {
            @Override
            public void onStart(playn.scene.Pointer.Interaction iact) {
                super.onStart(iact);
                System.out.println("LOG: Generating new Map");
                rootLayer.forEach(Layer::close);
                rootLayer.add(newMap());
                System.out.println("LOG: Done Generating new Map");
            }
        });
        rootLayer.add(map);

        System.out.println("I got this far");

    }

    public Map newMap() {
        return new Map(this, size);
    }
}
