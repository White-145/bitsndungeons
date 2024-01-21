package me.white.bitsndungeons.engine.render;

import me.white.bitsndungeons.engine.Window;
import me.white.bitsndungeons.engine.component.SpriteRenderer;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL30;

public class RenderBatch {
    // Vertex
    // ------
    // Pos             Color
    // float, float,   float, float, float, float,
    private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 4;

    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites = 0;
    private boolean hasRoom = true;
    private float[] vertices;

    private int vaoId;
    private int vboId;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        shader = new Shader("color");
        shader.compile();
        sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
    }

    public void start() {
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        vboId = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL30.GL_DYNAMIC_DRAW);

        int eboId = GL30.glGenBuffers();
        int[] indices = generateIndices();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indices, GL30.GL_STATIC_DRAW);

        GL30.glVertexAttribPointer(0, POS_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        GL30.glEnableVertexAttribArray(0);
        GL30.glVertexAttribPointer(1, COLOR_SIZE, GL30.GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        GL30.glEnableVertexAttribArray(1);
    }

    public boolean hasRoom() {
        return hasRoom;
    }

    public void addSprite(SpriteRenderer sprite) {
        sprites[numSprites] = sprite;
        loadVertexProperties(numSprites);

        numSprites += 1;
        if (numSprites >= maxBatchSize) {
            hasRoom = false;
        }
    }

    public void render() {
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
        GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, vertices);

        shader.bind();
        shader.uploadMat4f("uProjection", Window.getInstance().getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getInstance().getScene().getCamera().getViewMatrix());

        GL30.glBindVertexArray(vaoId);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);

        GL30.glDrawElements(GL30.GL_TRIANGLES, numSprites * 6, GL30.GL_UNSIGNED_INT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        if (sprite != null) {
            int offset = index * 4 * VERTEX_SIZE;
            Vector4f color = sprite.getColor();
            float[] xAdd = {1.0f, 1.0f, 0.0f, 0.0f};
            float[] yAdd = {1.0f, 0.0f, 0.0f, 1.0f};
            for (int i = 0; i < 4; ++i) {
                vertices[offset] = sprite.entity.transform.position.x + (xAdd[i] * sprite.entity.transform.scale.x);
                vertices[offset + 1] = sprite.entity.transform.position.y + (yAdd[i] * sprite.entity.transform.scale.y);
                vertices[offset + 2] = color.x;
                vertices[offset + 3] = color.y;
                vertices[offset + 4] = color.z;
                vertices[offset + 5] = color.w;
                offset += VERTEX_SIZE;
            }
        }
    }

    private int[] generateIndices() {
        int[] elements = new int[6 * maxBatchSize];
        for (int i = 0; i < maxBatchSize; ++i) {
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3 0
        // 2 1
        int[] offsets = {3, 2, 0, 0, 2, 1};

        for (int i = 0; i < 6; ++i) {
            elements[offsetArrayIndex + i] = offset + offsets[i];
        }
    }
}
