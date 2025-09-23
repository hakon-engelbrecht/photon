package org.hakim.photon.application;

public interface Layer {
    default void onEvent() {};
    default void onUpdate(double delta) {};
    default void onRender() {};
}
