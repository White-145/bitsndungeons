package me.white.bitsndungeons.engine.render;

import org.joml.Vector2f;

public class Transform {
    public Vector2f position = new Vector2f();
    public Vector2f scale = new Vector2f();

    public Transform() { }

    public Transform(Vector2f position) {
        this.position = position;
    }

    public Transform(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }
}
