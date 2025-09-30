package org.hakim.photon.renderer.raytracing;

import org.joml.Vector3f;

public class Ray {

    private final Vector3f origin;
    private final Vector3f direction;

    public Ray(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3f getOrigin() {
        return this.origin;
    }

    public Vector3f getDirection() {
        return this.direction;
    }

    public Vector3f at(float t) {
        return this.origin.add(this.direction.mul(t));
    }
}
