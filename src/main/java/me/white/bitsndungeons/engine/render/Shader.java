package me.white.bitsndungeons.engine.render;

import me.white.bitsndungeons.engine.Resource;
import org.joml.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Shader {
    private static final String SHADER_TYPE_PREPROCESSOR = "#type";
    private static Shader boundShader = null;
    private int vertexId;
    private int fragmentId;
    private int programId;
    private String vertexSource;
    private String fragmentSource;
    private String path;

    public Shader(String resource) {
        this(new Resource(Resource.Type.ASSET_SHADER, resource));
    }

    public Shader(Resource resource) {
        if (!resource.isOf(Resource.Type.ASSET_SHADER)) {
            throw new IllegalArgumentException("Shader resource should be of ASSET_SHADER.");
        }

        // load file
        path = resource.getPath();
        String source = resource.readText();

        // parse sources
        List<String> types = new ArrayList<>();
        List<String> sources = new ArrayList<>();
        int start = -1;
        int end = -1;
        while ((start = source.indexOf(SHADER_TYPE_PREPROCESSOR, start + 1)) != -1) {
            if (end != -1) {
                sources.add(source.substring(end, start).trim());
            }
            end = source.indexOf("\r\n", start);
            types.add(source.substring(start + SHADER_TYPE_PREPROCESSOR.length() + 1, end).trim().replaceAll("\\s+", ""));
        }
        sources.add(source.substring(end + 2));

        assert sources.size() == types.size();

        // get vertex shader
        int vertexIndex = types.indexOf("vertex");
        if (vertexIndex == -1) {
            throw new IllegalStateException("Could not find vertex shader in shader file '" + path + "'.");
        }
        vertexSource = sources.get(vertexIndex);

        // get fragment shader
        int fragmentIndex = types.indexOf("fragment");
        if (fragmentIndex == -1) {
            throw new IllegalStateException("Could not find fragment shader in shader file '" + path + "'.");
        }
        fragmentSource = sources.get(fragmentIndex);
    }

    public void compile() {
        // vertex shader
        vertexId = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
        GL30.glShaderSource(vertexId, vertexSource);
        GL30.glCompileShader(vertexId);
        if (GL30.glGetShaderi(vertexId, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
            int len = GL30.glGetShaderi(vertexId, GL30.GL_INFO_LOG_LENGTH);
            String error = GL30.glGetShaderInfoLog(vertexId, len);
            throw new IllegalStateException("'" + path + "': Vertex shader compilation failed.\n" + error);
        }

        // fragment shader
        fragmentId = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);
        GL30.glShaderSource(fragmentId, fragmentSource);
        GL30.glCompileShader(fragmentId);
        if (GL30.glGetShaderi(fragmentId, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
            int len = GL30.glGetShaderi(fragmentId, GL30.GL_INFO_LOG_LENGTH);
            String error = GL30.glGetShaderInfoLog(fragmentId, len);
            throw new IllegalStateException("'" + path + "': Fragment shader compilation failed.\n" + error);
        }

        // link
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
        if (boundShader != this) {
            if (boundShader != null) {
                throw new IllegalStateException("Cannot bind shader over another.");
            }
            GL30.glUseProgram(programId);
            boundShader = this;
        }
    }

    public void unbind() {
        if (boundShader != null) {
            if (boundShader != this) {
                throw new IllegalStateException("Cannot unbind unbound shader.");
            }
            GL30.glUseProgram(0);
            boundShader = null;
        }
    }

    private int getUploadLocation(String name) {
        if (boundShader != this) {
            throw new IllegalStateException("Can not upload to unbound shader.");
        }
        return GL30.glGetUniformLocation(programId, name);
    }

    public void uploadTexture(String name, int slot) {
        int location = getUploadLocation(name);
        GL30.glUniform1i(location, slot);
    }

    public void uploadMat4f(String name, Matrix4f mat4) {
        int location = getUploadLocation(name);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        GL30.glUniformMatrix4fv(location, false, matBuffer);
    }

    public void uploadMat3f(String name, Matrix3f value) {
        int location = getUploadLocation(name);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        value.get(matBuffer);
        GL30.glUniformMatrix3fv(location, false, matBuffer);
    }

    public void uploadVec4f(String name, Vector4f value) {
        int location = getUploadLocation(name);
        GL30.glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public void uploadVec3f(String name, Vector3f value) {
        int location = getUploadLocation(name);
        GL30.glUniform3f(location, value.x, value.y, value.z);
    }

    public void uploadVec2f(String name, Vector2f value) {
        int location = getUploadLocation(name);
        GL30.glUniform2f(location, value.x, value.y);
    }

    public void uploadFloat(String name, float value) {
        int location = getUploadLocation(name);
        GL30.glUniform1f(location, value);
    }

    public void uploadInt(String name, int value) {
        int location = getUploadLocation(name);
        GL30.glUniform1i(location, value);
    }
}
