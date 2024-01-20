package me.white.bitsndungeons.scene;

import me.white.bitsndungeons.engine.Scene;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class LevelScene extends Scene {
    private String vertextShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main() {\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexId;
    private int fragmentId;
    private int shaderProgram;

    private float[] vertexArray = {
            // position             // color
             0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, // bottom right
            -0.5f,  0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, // Top left
             0.5f,  0.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f, // Top right
            -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f  // Bottom left
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
        GL30.glUseProgram(shaderProgram);
        GL30.glBindVertexArray(vaoId);
        GL30.glEnableVertexAttribArray(0);
        GL30.glEnableVertexAttribArray(1);
        GL30.glDrawElements(GL30.GL_TRIANGLES, elementArray.length, GL30.GL_UNSIGNED_INT, 0);

        GL30.glDisableVertexAttribArray(0);
        GL30.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        GL30.glUseProgram(0);
    }

    @Override
    public void init() {
        // Compile and link shaders
        vertexId = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(vertexId, vertextShaderSrc);
        GL30.glCompileShader(vertexId);
        if (GL30.glGetShaderi(vertexId, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
            int len = GL30.glGetShaderi(vertexId, GL30.GL_INFO_LOG_LENGTH);
            String error = GL30.glGetShaderInfoLog(vertexId, len);
            throw new IllegalStateException("'defaultShader.glsl'\n\nVertex shader compilation failed.\n" + error);
        }

        fragmentId = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(fragmentId, fragmentShaderSrc);
        GL30.glCompileShader(fragmentId);
        if (GL30.glGetShaderi(fragmentId, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
            int len = GL30.glGetShaderi(fragmentId, GL30.GL_INFO_LOG_LENGTH);
            String error = GL30.glGetShaderInfoLog(fragmentId, len);
            throw new IllegalStateException("'defaultShader.glsl'\n\nFragment shader compilation failed.\n" + error);
        }

        shaderProgram = GL30.glCreateProgram();
        GL30.glAttachShader(shaderProgram, vertexId);
        GL30.glAttachShader(shaderProgram, fragmentId);
        GL30.glLinkProgram(shaderProgram);
        if (GL30.glGetProgrami(shaderProgram, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
            int len = GL30.glGetProgrami(shaderProgram, GL30.GL_INFO_LOG_LENGTH);
            String error = GL30.glGetProgramInfoLog(shaderProgram, len);
            throw new IllegalStateException("'defaultShader.glsl'\n\nLinking of shaders failed.\n" + error);
        }

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
