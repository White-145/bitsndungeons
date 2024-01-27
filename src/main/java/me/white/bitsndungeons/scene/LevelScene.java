package me.white.bitsndungeons.scene;

import me.white.bitsndungeons.engine.Scene;
import me.white.bitsndungeons.engine.render.Mesh;
import me.white.bitsndungeons.engine.render.MeshLoader;
import org.lwjgl.opengl.GL30;

public class LevelScene extends Scene {
    private static Mesh mesh;

    @Override
    public void update(double dt) {
        GL30.glBindVertexArray(mesh.getVaoID());
        GL30.glEnableVertexAttribArray(0);
        GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getVertexCount(), GL30.GL_UNSIGNED_INT, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void init() {
        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                 0.5f, -0.5f, 0.0f,
                 0.0f,  0.5f, 0.0f
        };
        float[] UVs = {};
        int[] indices = {0, 1, 2};
        mesh = MeshLoader.createMesh(vertices, UVs, indices);
    }
}
