package me.white.bitsndungeons.engine.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class MeshLoader {
    private static List<Integer> vaos = new ArrayList<>();
    private static List<Integer> vbos = new ArrayList<>();

    private static FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    private static IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    private static void storeData(int attribute, int dimensions, float[] data) {
        // Creates a VBO ID
        int vbo = GL30.glGenBuffers();
        vbos.add(vbo);
        // Loads the current VBO to store the data
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = createFloatBuffer(data);
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
        GL30.glVertexAttribPointer(attribute, dimensions, GL30.GL_FLOAT, false, 0, 0);
        // Unloads the current VBO when done.
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
    }

    private static void bindIndices(int[] data) {
        int vbo = GL30.glGenBuffers();
        vbos.add(vbo);
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = createIntBuffer(data);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
    }

    public static Mesh createMesh(float[] positions, float[] UVs, int[] indices) {
        int vao = genVAO();
        storeData(0, 3, positions);
        storeData(1, 2, UVs);
        bindIndices(indices);
        GL30.glBindVertexArray(0);
        return new Mesh(vao, indices.length);
    }

    private static int genVAO() {
        int vao = GL30.glGenVertexArrays();
        vaos.add(vao);
        GL30.glBindVertexArray(vao);
        return vao;
    }
}
