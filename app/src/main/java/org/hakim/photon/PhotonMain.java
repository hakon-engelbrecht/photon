package org.hakim.photon;

import org.hakim.photon.application.Application;
import org.hakim.photon.specification.ApplicationSpecification;

/**
 * Main class of photon.
 */
public class PhotonMain
{

    /** reference to the app singleton */
    private static Application app;

    public static Application getApp() {
        return app;
    }

    public static void main( String[] args ) {
        ApplicationSpecification appSpec = new ApplicationSpecification("Photon");
        appSpec.getWindowSpecification().setWidth(1920);
        appSpec.getWindowSpecification().setHeight(1080);

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
