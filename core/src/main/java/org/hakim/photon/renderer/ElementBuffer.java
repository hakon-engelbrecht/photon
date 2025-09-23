package org.hakim.photon.renderer;

import static org.lwjgl.opengl.GL15.*;

public class ElementBuffer {

    private final int handle;
    private final int[] indices;

    public ElementBuffer(int[] indices) {
        this.indices = indices;
        this.handle = glGenBuffers();
    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.handle);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
    }
}
