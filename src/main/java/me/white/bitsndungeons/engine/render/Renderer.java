package me.white.bitsndungeons.engine.render;

import me.white.bitsndungeons.engine.component.Entity;
import me.white.bitsndungeons.engine.component.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches = new ArrayList<>();

    public Renderer() { }

    public void add(Entity entity) {
        SpriteRenderer sprite = entity.getComponent(SpriteRenderer.class);
        if (sprite != null) {
            add(sprite);
        }
    }

    private void add(SpriteRenderer sprite) {
        for (RenderBatch batch : batches) {
            if (batch.hasRoom()) {
                batch.addSprite(sprite);
                return;
            }
        }
        RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
        newBatch.start();
        batches.add(newBatch);
        newBatch.addSprite(sprite);
    }

    public void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }
}
