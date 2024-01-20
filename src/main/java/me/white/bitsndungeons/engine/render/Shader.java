package me.white.bitsndungeons.engine.render;

import me.white.bitsndungeons.util.Resource;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Shader {
    private static final String SHADER_TYPE_PREPROCESSOR = "#type";
    private int vertexId;
    private int fragmentId;
    private int programId;
    private String vertexSource;
    private String fragmentSource;
    private String path;

    public Shader(Resource resource) {
        if (!resource.isOf(Resource.Type.ASSET_SHADER)) {
            throw new IllegalArgumentException("Shader resource should be of ASSET_SHADER.");
        }
        path = resource.getPath();
        String source;
        try {
            source = resource.readText();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not open file for shader: '" + path + "'.");
        }
        // parse sources
        List<String> types = new ArrayList<>();
        List<String> sources = new ArrayList<>();
        int start = -1;
        int end = -1;
        while ((start = source.indexOf(SHADER_TYPE_PREPROCESSOR, start)) != -1) {
            start += SHADER_TYPE_PREPROCESSOR.length();
            if (end != -1) {
                sources.add(source.substring(end + 2, start - 6));
            }
            end = source.indexOf("\r\n", start);
            types.add(source.substring(start + 1, end).trim().replaceAll("\\s+", ""));
        }
        sources.add(source.substring(end + 2));

        int vertexIndex = types.indexOf("vertex");
        if (vertexIndex == -1) {
            throw new IllegalStateException("Could not find vertex shader in shader file '" + path + "'.");
        }
        vertexSource = sources.get(vertexIndex);
        int fragmentIndex = types.indexOf("fragment");
        if (fragmentIndex == -1) {
            throw new IllegalStateException("Could not find fragment shader in shader file '" + path + "'.");
        }
        fragmentSource = sources.get(fragmentIndex);

        System.out.println("Vertex source: " + vertexSource);
        System.out.println("Fragment source: " + fragmentSource);
    }

    public void compile() {
        vertexId = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(vertexId, vertexSource);
        GL30.glCompileShader(vertexId);
        if (GL30.glGetShaderi(vertexId, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
            int len = GL30.glGetShaderi(vertexId, GL30.GL_INFO_LOG_LENGTH);
            String error = GL30.glGetShaderInfoLog(vertexId, len);
            throw new IllegalStateException("'" + path + "': Vertex shader compilation failed.\n" + error);
        }

        fragmentId = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(fragmentId, fragmentSource);
        GL30.glCompileShader(fragmentId);
        if (GL30.glGetShaderi(fragmentId, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
            int len = GL30.glGetShaderi(fragmentId, GL30.GL_INFO_LOG_LENGTH);
            String error = GL30.glGetShaderInfoLog(fragmentId, len);
            throw new IllegalStateException("'" + path + "': Fragment shader compilation failed.\n" + error);
        }

        programId = GL30.glCreateProgram();
        GL30.glAttachShader(programId, vertexId);
        GL30.glAttachShader(programId, fragmentId);
        GL30.glLinkProgram(programId);
        if (GL30.glGetProgrami(programId, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
            int len = GL30.glGetProgrami(programId, GL30.GL_INFO_LOG_LENGTH);
            String error = GL30.glGetProgramInfoLog(programId, len);
            throw new IllegalStateException("'" + path + "': Linking of shaders failed.\n" + error);
        }
    }

    public void bind() {
        GL30.glUseProgram(programId);
    }

    public void unbind() {
        GL30.glUseProgram(0);
    }
}
