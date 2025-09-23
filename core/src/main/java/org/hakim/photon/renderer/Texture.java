package org.hakim.photon.renderer;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {

    private final int handle;

    private final int textureUnit;

    public Texture(String filename, int textureUnit) {
        this.textureUnit = textureUnit;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            URL url = Texture.class.getClassLoader().getResource("textures/" + filename);
            if (url == null) {
                throw new RuntimeException("Couldn't find texture file: " + filename);
            }

            File file = new File(url.getFile());
            String filePath = file.getAbsolutePath();
            STBImage.stbi_set_flip_vertically_on_load(true);
            ByteBuffer buffer = STBImage.stbi_load(filePath, width, height, channels, 4);

            if (buffer == null) {
                throw new RuntimeException("Failed to load texture: " + filename);
            }

            this.handle = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, this.handle);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
            glGenerateMipmap(GL_TEXTURE_2D);
            STBImage.stbi_image_free(buffer);
        }
    }

    public void bind() {
        glActiveTexture(this.textureUnit);
        glBindTexture(GL_TEXTURE_2D, this.handle);
    }
}
