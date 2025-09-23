package org.hakim.photon.application;

import org.hakim.photon.specification.ApplicationSpecification;
import org.joml.Math;

import java.util.Stack;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11C.*;

public class Application {

    private final Window window;

    private final Stack<Layer> layerStack;

    private boolean isRunning;

    public Application(ApplicationSpecification applicationSpecification) throws Exception {
        try {
            this.window = new Window(applicationSpecification.getWindowSpecification());
            this.layerStack = new Stack<>();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize application");
        }
    }

    public void pushLayer(Layer layer) {
        this.layerStack.push(layer);
    }

    public float getTime() {
        return (float) glfwGetTime();
    }

    public void run() {
        System.out.println("Application started");

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
            double currentTime = this.getTime();
            double timestep = Math.clamp(currentTime - lastTime, 0.0001, 1.0);
            lastTime = currentTime;

            // update all layers
            for(Layer layer : this.layerStack) {
                layer.onUpdate(timestep);
            }

            // render all layers
            for (Layer layer : this.layerStack) {
                layer.onRender();
            }

            this.window.update();

            this.window.pollEvents();
        }
        this.window.dispose();
    }

    public void stop() {
        this.isRunning = false;
    }
}
