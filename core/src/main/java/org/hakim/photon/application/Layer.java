package org.hakim.photon.application;

/**
 * Interface for an app layer.
 * Layers have can be updated, rendered and react to events.
 */
public interface Layer {

    /**
     * Handles events for the layer.
     */
    default void onEvent() {};

    /**
     * Updates the contents of this layer.
     *
     * @param delta time since last frame
     */
    default void onUpdate(float delta) {};

    /**
     * Renders the contents of the layer.
     */
    default void onRender() {};
}
