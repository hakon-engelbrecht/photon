package org.hakim.photon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hakim.photon.application.Layer;
import org.hakim.photon.application.Window;
import org.hakim.photon.renderer.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class AppLayer implements Layer {

    private static final Logger logger = LogManager.getLogger(AppLayer.class);

    private ShaderProgram shader;
    private VertexArray vertexArray;
    private final Texture brickTexture;
    private final Texture faceTexture;
    private final Camera camera;

    private final Vector3f[] CUBE_POSITIONS = {
            new Vector3f(0.0f, 0.0f, 0.0f),
            new Vector3f(2.0f, 5.0f, -15.0f),
            new Vector3f(-1.5f, -2.2f, -2.5f),
            new Vector3f(-3.8f, -2.0f, -12.3f),
            new Vector3f(2.4f, -0.4f, -3.5f),
            new Vector3f(-1.7f, 3.0f, -7.5f),
            new Vector3f(1.3f, -2.0f, -2.5f),
            new Vector3f(1.5f, 2.0f, -2.5f),
            new Vector3f(1.5f, 0.2f, -1.5f),
            new Vector3f(-1.3f, 1.0f, -1.5f)
    };

    public AppLayer() {

        VertexBuffer vertexBuffer = getVertexBuffer();
        createShader();
        initializeVertexArray(vertexBuffer);
        setTextureParameters();

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glEnable(GL_DEPTH_TEST);

        this.brickTexture = new Texture("wall.jpg", GL_TEXTURE0);
        this.faceTexture = new Texture("kimi.jpg", GL_TEXTURE1);

        this.camera = new Camera(
                new Vector3f(0.0f, 0.0f, 3.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                PhotonMain.DEFAULT_WIDTH,
                PhotonMain.DEFAULT_HEIGHT
        );

        glfwSetInputMode(PhotonMain.getApp().getWindow().getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPosCallback(PhotonMain.getApp().getWindow().getHandle(), (window, x, y) -> {
           this.camera.processMouseMovement((float) x, (float) y, true);
        });
        glfwSetScrollCallback(PhotonMain.getApp().getWindow().getHandle(), (window, x, y) -> {
            this.camera.processMouseScroll((float) y);
        });
    }

    private static VertexBuffer getVertexBuffer() {
        float[] vertices = {
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,

                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,

                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,

                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f
        };
        return new VertexBuffer(vertices);
    }

    private void createShader() {
        int vertexShader = ShaderUtils.createVertexShader(this.getClass().getClassLoader().getResource("shaders/vertexShader.glsl"));
        int fragmentShader = ShaderUtils.createFragmentShader(this.getClass().getClassLoader().getResource("shaders/fragmentShader.glsl"));
        this.shader = new ShaderProgram();
        this.shader.attachShader(vertexShader, GL_VERTEX_SHADER);
        this.shader.attachShader(fragmentShader, GL_FRAGMENT_SHADER);
        this.shader.link();
    }

    private void initializeVertexArray(VertexBuffer buffer) {
        this.vertexArray = new VertexArray();
        this.vertexArray.bind();
        buffer.bind();

        // position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // color attribute
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // texture coordinates
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    private void setTextureParameters() {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        float[] borderColor = {1.0f, 1.0f, 0.0f, 1.0f};
        glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, borderColor);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    }

    private void processInput(float delta) {
        if (PhotonMain.getApp().getWindow().getKeyPressed(GLFW_KEY_W)) {
            this.camera.processKeyboardInput(Camera.CameraMovement.FORWARD, delta);
        }

        if (PhotonMain.getApp().getWindow().getKeyPressed(GLFW_KEY_S)) {
            this.camera.processKeyboardInput(Camera.CameraMovement.BACKWARD, delta);
        }

        if (PhotonMain.getApp().getWindow().getKeyPressed(GLFW_KEY_A)) {
            this.camera.processKeyboardInput(Camera.CameraMovement.LEFT, delta);
        }

        if (PhotonMain.getApp().getWindow().getKeyPressed(GLFW_KEY_D)) {
            this.camera.processKeyboardInput(Camera.CameraMovement.RIGHT, delta);
        }
    }

    @Override
    public void onUpdate(float delta) {
        processInput(delta);
        logger.debug("Camera position: {}", this.camera.getPosition());
    }

    @Override
    public void onRender() {
        this.shader.use();
        this.brickTexture.bind();
        this.faceTexture.bind();
        this.shader.setUniform("texture1", 0);
        this.shader.setUniform("texture2", 1);

        Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(45.0), 1920.0f / 1080.0f, 0.1f, 100.0f);

        this.shader.setUniform("projection", projection);
        this.shader.setUniform("view", this.camera.getViewMatrix());

        this.vertexArray.bind();
        for (int i = 0; i < this.CUBE_POSITIONS.length; i++) {
            Matrix4f model = new Matrix4f().translate(this.CUBE_POSITIONS[i]);
            float angle = 60.0f * (i + 1);
            model.rotate((float) Math.toRadians(angle) * PhotonMain.getApp().getTime(), 1.0f, 0.3f, 0.5f);
            this.shader.setUniform("model", model);
            glDrawArrays(GL_TRIANGLES, 0, 36);
        }
        this.vertexArray.unbind();
    }
}
