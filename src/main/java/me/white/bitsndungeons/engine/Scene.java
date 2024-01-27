package me.white.bitsndungeons.engine;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;

    public Camera getCamera() {
        return camera;
    }

    public abstract void update(double dt);

    public abstract void init();

    public void start() {
        isRunning = true;
    }
}
