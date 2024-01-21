package me.white.bitsndungeons.scene;

import me.white.bitsndungeons.engine.Camera;
import me.white.bitsndungeons.engine.Scene;
import me.white.bitsndungeons.engine.component.Entity;
import me.white.bitsndungeons.engine.component.SpriteRenderer;
import me.white.bitsndungeons.engine.render.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class LevelScene extends Scene {

    @Override
    public void update(double dt) {
        for (Entity object : entities) {
            object.update(dt);
        }

        renderer.render();
    }

    @Override
    public void init() {
        camera = new Camera(new Vector2f(-250, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;
        float padding = 3;

        for (int x=0; x < 100; x++) {
            for (int y=0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX) + (padding * x);
                float yPos = yOffset + (y * sizeY) + (padding * y);

                Entity entity = new Entity(new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                entity.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                addEntity(entity);
            }
        }
    }
}
