package org.hakim.photon;

import org.hakim.photon.application.Application;
import org.hakim.photon.specification.ApplicationSpecification;

/**
 * Main class of photon.
 */
public class PhotonMain
{
    public static final int DEFAULT_WIDTH = 1920;
    public static final int DEFAULT_HEIGHT = 1080;

    /** reference to the app singleton */
    private static Application app;

    public static Application getApp() {
        return app;
    }

    public static void main( String[] args ) {
        ApplicationSpecification appSpec = new ApplicationSpecification("Photon");
        appSpec.getWindowSpecification().setWidth(DEFAULT_WIDTH);
        appSpec.getWindowSpecification().setHeight(DEFAULT_HEIGHT);

        try {
            app = new Application(appSpec);
            app.pushLayer(new AppLayer());
            app.run();
        } catch (Exception e) {
            System.err.println("Application failed: " + e.getMessage());
            System.err.println("Exiting...");
            System.exit(1);
        }
    }
}
