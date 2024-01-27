package me.white.bitsndungeons.util;

import java.io.IOException;

public class Resource {
    public enum Type {
        ASSET_SHADER("assets/shaders", "glsl"),
        ASSET_TEXTURE("assets/textures", "png");

        final String path;
        final String file_type;

        Type(String prefix, String extension) {
            this.path = prefix;
            this.file_type = extension;
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
        return type.path + "/" + path + "." + type.file_type;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Resource) {
            Resource resource = (Resource) obj;
            return type == resource.type && path.equals(resource.path);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 37 * (37 * type.file_type.hashCode() + path.hashCode()) + type.path.hashCode();
    }
}
