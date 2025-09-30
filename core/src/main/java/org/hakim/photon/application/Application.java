package org.hakim.photon.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hakim.photon.specification.ApplicationSpecification;
import org.joml.Math;

import java.util.Stack;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

/**
 * Class representing an application.
 * An application has a single window and consists of several layers.
 * This class is the heart of any project and handles the main loop.
 * The desired behavior can be added by pushing app layers onto the layer stack.
 * In the loop each layer will be updated and rendered one after another.
 */
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    /** window reference for this app */
    private final Window window;

    /** stack of app layers */
    private final Stack<Layer> layerStack;

    /** flag if this app is running */
    private boolean isRunning;

    /**
     * Constructor for an application.
     * The parameters of the app are specified by the application specification used to initialize
     * this. This constructor will also create a window instance for the app.
     *
     * @param applicationSpecification specifications to initialize the app
     * @throws RuntimeException when the initialization fails
     */
    public Application(ApplicationSpecification applicationSpecification) throws RuntimeException {
        try {
            logger.info("Creating application...");
            this.window = new Window(applicationSpecification.getWindowSpecification());
            this.layerStack = new Stack<>();
            logger.info("Application initialized successfully");
        } catch (Exception e) {
            logger.error("Application initialization failed");
            throw new RuntimeException("Failed to initialize application");
        }
    }

    /**
     * Pushes a layer to the applications stack.
     *
     * @param layer app layer to push
     */
    public void pushLayer(Layer layer) {
        assert layer != null;

        this.layerStack.push(layer);
    }

    /**
     * Global getter for the time of the app.
     *
     * @return current app time
     */
    public float getTime() {
        return (float) glfwGetTime();
    }

    /**
     * Getter for the app window.
     *
     * @return window of this app
     */
    public Window getWindow() {
        return this.window;
    }

    /**
     * Runs the main loop of the application.
     * This will update all layers each cycle and then render each layer.
     */
    public void run() {
        logger.info("Application started");

        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

        // initialize timer
        this.isRunning = true;
        double lastTime = this.getTime();

        // main loop
        while (this.isRunning) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            if (this.window.shouldClose()) {
                this.stop();
                break;
            }

            // calculate time since last frame
            float currentTime = this.getTime();
            float timestep = (float) Math.clamp(currentTime - lastTime, 0.0001, 1.0);
            lastTime = currentTime;
            logger.debug("Frame time {}", timestep);

            // update all layers
            for(Layer layer : this.layerStack) {
                logger.debug("Updating layer {}", layer.getClass().getSimpleName());
                layer.onUpdate(timestep);
            }

            // render all layers
            for (Layer layer : this.layerStack) {
                logger.debug("Rendering layer {}", layer.getClass().getSimpleName());
                layer.onRender();
            }

            this.window.update();

            this.window.pollEvents();
        }
        this.window.dispose();
    }

    public void stop() {
        this.isRunning = false;
        logger.info("Application stopped");
    }
}
