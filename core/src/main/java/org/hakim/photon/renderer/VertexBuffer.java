package org.hakim.photon.renderer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;

public class VertexBuffer {

    private final int handle;
    private final float[] vertices;

    public VertexBuffer(float[] vertices) {
        this.vertices = vertices;
        this.handle = glGenBuffers();
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, this.handle);
        glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_STATIC_DRAW);
    }
}
