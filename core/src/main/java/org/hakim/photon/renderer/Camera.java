package org.hakim.photon.renderer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private static final Logger logger = LogManager.getLogger(Camera.class);

    // default values
    private static final float YAW = -90.0f;
    private static final float PITCH = 0.0f;
    private static final float SPEED = 10.0f;
    private static final float SENSITIVITY = 0.1f;
    private static final float ZOOM = 45.0f;

    public enum CameraMovement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    // camera attributes
    private Vector3f position;
    private Vector3f front;
    private Vector3f up;
    private Vector3f right;
    private final Vector3f worldUp;

    // euler angles
    private float yaw = YAW;
    private float pitch = PITCH;

    // options
    private float zoom = ZOOM;
    private final float movementSpeed = SPEED;
    private final float mouseSensitivity = SENSITIVITY;

    private boolean firstMouse = true;
    private float lastX;
    private float lastY;

    public Camera(
            Vector3f position,
            Vector3f up,
            Vector3f front,
            Vector3f worldUp,
            int windowWidth,
            int windowHeight
    ) {
        this.position = position;
        this.up = up;
        this.front = front;
        this.worldUp = worldUp;

        this.lastX = (float) windowWidth / 2.0f;
        this.lastY = (float) windowHeight / 2.0f;

        updateCameraVectors();
    }

    public Vector3f getPosition() {
        return this.position;
    }

    /**
     * Returns the view matrix of this camera.
     *
     * @return view matrix
     */
    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(this.position, new Vector3f(this.position).add(this.front), this.up);
    }

    public void processKeyboardInput(CameraMovement direction, float delta) {
        float velocity = this.movementSpeed * delta;
        Vector3f velocityVector = new Vector3f();
        switch (direction) {
            case FORWARD:
                velocityVector = new Vector3f(this.front).mul(velocity);
                break;
            case BACKWARD:
                velocityVector = new Vector3f(this.front).mul(-velocity);
                break;
            case LEFT:
                velocityVector = new Vector3f(this.right).mul(-velocity);
                break;
            case RIGHT:
                velocityVector = new Vector3f(this.right).mul(velocity);
                break;
        }
        this.position = this.position.add(velocityVector);
    }

    public void processMouseMovement(float x, float y, boolean constrainPitch) {

        if (this.firstMouse) {
            this.lastX = x;
            this.lastY = y;
            this.firstMouse = false;
        }

        float xOffset = x - this.lastX;
        float yOffset = y - this.lastY;
        lastX = x;
        lastY = y;

        xOffset *= this.mouseSensitivity;
        yOffset *= this.mouseSensitivity;

        this.yaw += xOffset;
        this.pitch -= yOffset;

        if (constrainPitch) {
            if (this.pitch > 89.0f) this.pitch = 89.0f;
            if (this.pitch < -89.0f) this.pitch = -89.0f;
        }

        updateCameraVectors();
    }

    public void processMouseScroll(float yOffset) {
        this.zoom -= yOffset;
        if (this.zoom < 1.0f) this.zoom = 1.0f;
        if (this.zoom > 45.0f) this.zoom = 45.0f;
    }

    private void updateCameraVectors() {
        this.front = new Vector3f(
                Math.cos(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)),
                Math.sin(Math.toRadians(this.pitch)),
                Math.sin(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch))
        ).normalize();
        this.right = this.front.cross(this.worldUp, new Vector3f()).normalize();
        this.up = this.right.cross(this.front, new Vector3f()).normalize();
    }

}
