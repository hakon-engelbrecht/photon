package org.hakim.photon.renderer;

import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray {

    private final int handle;

    public VertexArray() {
        this.handle = glGenVertexArrays();
    }

    public void bind() {
        glBindVertexArray(this.handle);
    }

    public void unbind() {
        glBindVertexArray(0);
    }
}
