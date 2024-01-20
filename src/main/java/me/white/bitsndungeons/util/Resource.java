package me.white.bitsndungeons.util;

import me.white.bitsndungeons.Main;

import java.io.IOException;

public class Resource {
    public enum Type {
        ASSET_SHADER("assets/shaders", "glsl");

        final String prefix;
        final String extension;

        Type(String prefix, String extension) {
            this.prefix = prefix;
            this.extension = extension;
        }
    }

    private Type type;
    private String path;

    public Resource(Type type, String path) {
        this.type = type;
        this.path = path;
    }

    public String getPath() {
        return type.prefix + "/" + path + "." + type.extension;
    }

    public String readText() throws IOException {
        return new String(Main.class.getClassLoader().getResourceAsStream(getPath()).readAllBytes());
    }

    public boolean isOf(Type type) {
        return type == this.type;
    }
}
