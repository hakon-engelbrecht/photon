package org.hakim.photon.renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position;
    private Vector3f up;
    private Vector3f target;

    public Camera(Vector3f position, Vector3f target, Vector3f up) {
        this.position = position;
        this.up = up;
        this.target = target;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(this.position, this.target, this.up);
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getUp() {
        return this.up;
    }

    public void setUp(Vector3f up) {
        this.up = up;
    }

    public Vector3f getTarget() {
        return this.target;
    }

    public void setTarget(Vector3f target) {
        this.target = target;
    }
}
