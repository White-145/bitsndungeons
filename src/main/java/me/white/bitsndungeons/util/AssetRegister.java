package me.white.bitsndungeons.util;

import java.util.HashMap;
import java.util.Map;

public class AssetRegister {
    public static final Resource SHADER_COLOR = new Resource(Resource.Type.ASSET_SHADER, "color");
    public static final Resource SHADER_SPRITE = new Resource(Resource.Type.ASSET_SHADER, "sprite");
    public static final Resource TEXTURE_DEFAULT = new Resource(Resource.Type.ASSET_TEXTURE, "default");
    private static Map<Resource, Shader> shaders = new HashMap<>();
    private static Map<Resource, Texture> textures = new HashMap<>();

    static {
        getShader(SHADER_COLOR).compile();
        getShader(SHADER_SPRITE).compile();
        getTexture(TEXTURE_DEFAULT);
    }

    public static Shader getShader(String resource) {
        return getShader(new Resource(Resource.Type.ASSET_SHADER, resource));
    }

    public static Shader getShader(Resource resource) {
        if (!resource.isOf(Resource.Type.ASSET_SHADER)) {
            throw new IllegalStateException("Shader resource should be of ASSET_SHADER.");
        }
        if (shaders.containsKey(resource)) {
            return shaders.get(resource);
        }
        Shader shader = new Shader(resource);
        shader.compile();
        shaders.put(resource, shader);
        return shader;
    }

    public static Texture getTexture(String resource) {
        return getTexture(new Resource(Resource.Type.ASSET_TEXTURE, resource));
    }

    public static Texture getTexture(Resource resource) {
        if (!resource.isOf(Resource.Type.ASSET_TEXTURE)) {
            throw new IllegalStateException("Texture resource should be of ASSET_TEXTURE.");
        }
        if (textures.containsKey(resource)) {
            return textures.get(resource);
        }
        Texture texture = new Texture(resource);
        textures.put(resource, texture);
        return texture;
    }
}
