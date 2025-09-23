package org.hakim.photon.specification;

/**
 * Defines the specification of an app window.
 */
public class WindowSpecification {

    /** width of the window in pixels */
    private int width;

    /** height of the window in pixels */
    private int height;

    /** title of the window */
    private String title;

    /** flag if this window is resizable */
    private boolean isResizable;

    /** flag if this window has v sync enabled */
    private boolean isVSyncEnabled;

    /**
     * Constructor of a window specification.
     * Width and height are initialized to 0 and are expected to be explicitly set later.
     */
    public WindowSpecification() {
        this(0, 0, "Window", true, true);
    }

    /**
     * Constructor for a window specification with explicit value initializers
     *
     * @param width width of the window
     * @param height height of the window
     */
    public WindowSpecification(int width, int height, String title, boolean isResizable,  boolean isVSyncEnabled) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.isResizable = isResizable;
        this.isVSyncEnabled = isVSyncEnabled;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isResizable() {
        return isResizable;
    }

    public void setResizable(boolean resizable) {
        isResizable = resizable;
    }

    public boolean isVSyncEnabled() {
        return isVSyncEnabled;
    }

    public void setVSyncEnabled(boolean VSyncEnabled) {
        isVSyncEnabled = VSyncEnabled;
    }
}
