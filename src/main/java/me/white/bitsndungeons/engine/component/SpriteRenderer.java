package me.white.bitsndungeons.engine.component;

import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    public Vector4f getColor() {
        return color;
    }

    @Override
    public void start() { }

    @Override
    public void update(double dt) { }
}
