package org.hakim.photon.renderer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private final int handle;

    private int vertexShader = -1;
    private int fragmentShader = -1;

    public ShaderProgram() {
        this.handle = glCreateProgram();
    }

    public void attachShader(int shaderHandle, int type) {
        if (type == GL_VERTEX_SHADER && this.vertexShader == -1) {
            this.vertexShader = shaderHandle;
        } else if (type == GL_FRAGMENT_SHADER && this.fragmentShader == -1) {
            this.fragmentShader = shaderHandle;
        } else {
            System.err.println("The shader you are trying to bind is either already attached or unsupported.");
            return;
        }
        glAttachShader(this.handle, shaderHandle);
    }

    public void link() {
        glLinkProgram(this.handle);

        int success = glGetProgrami(this.handle, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            String info = glGetProgramInfoLog(this.handle);
            System.err.println("Error linking program: " + info);
            throw new RuntimeException("Error linking program: " + info);
        }

        glDeleteShader(this.vertexShader);
        glDeleteShader(this.fragmentShader);
    }

    public void use() {
        glUseProgram(this.handle);
    }

    private int getUniformLocation(String name) {
        return glGetUniformLocation(this.handle, name);
    }

    public void setUniform(String name, int value) {
        glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, Vector4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer floatBuffer = value.get(stack.mallocFloat(4));
            glUniform4fv(getUniformLocation(name), floatBuffer);
        }
    }

    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer floatBuffer = value.get(stack.mallocFloat(16));
            glUniformMatrix4fv(getUniformLocation(name), false, floatBuffer);
        }
    }
}
