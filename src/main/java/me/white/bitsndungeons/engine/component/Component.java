package me.white.bitsndungeons.engine.component;

public abstract class Component {
    public Entity entity = null;

    public abstract void update(double dt);

    public abstract void start();
}
