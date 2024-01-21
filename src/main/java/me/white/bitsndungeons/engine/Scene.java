package me.white.bitsndungeons.engine;

import me.white.bitsndungeons.engine.component.Entity;
import me.white.bitsndungeons.engine.render.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<Entity> entities = new ArrayList<>();

    public Camera getCamera() {
        return camera;
    }

    public abstract void update(double dt);

    public abstract void init();

    public void start() {
        for (Entity entity : entities) {
            entity.start();
            renderer.add(entity);
        }
        isRunning = true;
    }

    public void addEntity(Entity entity) {
        if (isRunning) {
            entities.add(entity);
        } else {
            entities.add(entity);
            entity.start();
            renderer.add(entity);
        }
    }
}
