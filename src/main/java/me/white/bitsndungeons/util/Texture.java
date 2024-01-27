package me.white.bitsndungeons.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {
    private String path;
    private int textureId;
    private int width;
    private int height;
    private int channel;

    public Texture(String resource) {
        this(new Resource(Resource.Type.ASSET_TEXTURE, resource));
    }

    public Texture(Resource resource) {
        if (!resource.isOf(Resource.Type.ASSET_TEXTURE)) {
            throw new IllegalArgumentException("Texture resource should be of ASSET_TEXTURE.");
        }

        this.path = resource.getPath();

        // generate texture on GPU
        textureId = GL30.glGenTextures();
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureId);

        // set texture parameters
        // repeat image
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
        // pixelate when stretching
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_NEAREST);
        // pixelate when shrinking
        GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_NEAREST);

        IntBuffer widthBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer heightBuffer = BufferUtils.createIntBuffer(1);
        IntBuffer channelsBuffer = BufferUtils.createIntBuffer(1);
        byte[] bytes = resource.readBytes();
        ByteBuffer imageByteBuffer = BufferUtils.createByteBuffer(bytes.length);
        imageByteBuffer.put(bytes).flip();
        STBImage.stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = STBImage.stbi_load_from_memory(imageByteBuffer, widthBuffer, heightBuffer, channelsBuffer, 0);

        width = widthBuffer.get(0);
        height = heightBuffer.get(0);
        if (channelsBuffer.get(0) == 3) {
            channel = GL30.GL_RGB;
        } else if (channelsBuffer.get(0) == 4) {
            channel = GL30.GL_RGBA;
        } else {
            throw new IllegalStateException("Invalid number of channelsBuffer in image '" + path + "': " + channelsBuffer.get(0) + ".");
        }

        if (image == null) {
            throw new IllegalStateException("Could not load texture '" + path + "': " + STBImage.stbi_failure_reason());
        }
        GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
        GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, channel, width, height, 0, channel, GL30.GL_UNSIGNED_BYTE, image);
        GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);
        STBImage.stbi_image_free(image);
    }

    public void bind() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, textureId);
    }

    public void unbind() {
        GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
    }
}
