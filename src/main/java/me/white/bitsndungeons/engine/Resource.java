package me.white.bitsndungeons.engine;

import java.io.IOException;

public class Resource {
    public enum Type {
        ASSET_SHADER("assets/shaders", "glsl"),
        ASSET_TEXTURE("assets/textures", "png");

        final String prefix;
        final String extension;

        Type(String prefix, String extension) {
            this.prefix = prefix;
            this.extension = extension;
        }
    }

    private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
    private byte[] bytes = null;
    private Type type;
    private String path;

    public Resource(Type type, String path) {
        this.type = type;
        this.path = path;
    }

    public String getPath() {
        return type.prefix + "/" + path + "." + type.extension;
    }

    public byte[] readBytes() {
        if (bytes == null) {
            try {
                bytes = CLASS_LOADER.getResourceAsStream(getPath()).readAllBytes();
            } catch (IOException e) {
                throw new IllegalStateException("Could not open resource '" + getPath() + "': " + e);
            }
        }
        return bytes;
    }

    public String readText() {
        return new String(readBytes());
    }

    public boolean isOf(Type type) {
        return type == this.type;
    }
}
