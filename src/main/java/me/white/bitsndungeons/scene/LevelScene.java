package me.white.bitsndungeons.scene;

import me.white.bitsndungeons.engine.Scene;
import me.white.bitsndungeons.engine.render.Shader;
import me.white.bitsndungeons.util.Resource;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LevelScene extends Scene {
    private Shader shader;

    private float[] vertexArray = {
            // position             // color
             1.0f, -1.0f, 0.0f,     0.0f, 0.0f, 0.0f, 1.0f, // bottom right
            -1.0f,  1.0f, 0.0f,     1.0f, 0.0f, 1.0f, 1.0f, // Top left
             1.0f,  1.0f, 0.0f,     0.0f, 1.0f, 1.0f, 1.0f, // Top right
            -1.0f, -1.0f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f  // Bottom left
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3  // Bottom left triangle
    };

    private int vaoId;
    private int vboId;
    private int eboId;

    @Override
    public void update(double dt) {
        shader.bind();
        GL30.glBindVertexArray(vaoId);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glDrawElements(GL30.GL_TRIANGLES, elementArray.length, GL30.GL_UNSIGNED_INT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    @Override
    public void init() {
        shader = new Shader(new Resource(Resource.Type.ASSET_SHADER, "default"));
        shader.compile();

        // Generate buffers and send them to GPU
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboId = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, eboId);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL30.GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int positionSizeBytes = positionSize * Float.BYTES;
        int colorSizeBytes = colorSize * Float.BYTES;
        int vertexSizeBytes = positionSizeBytes + colorSizeBytes;
        GL30.glVertexAttribPointer(0, positionSize, GL30.GL_FLOAT, false, vertexSizeBytes, 0);
        GL30.glEnableVertexAttribArray(0);
        GL30.glVertexAttribPointer(1, colorSize, GL30.GL_FLOAT, false, vertexSizeBytes, positionSizeBytes);
        GL30.glEnableVertexAttribArray(1);
    }
}
