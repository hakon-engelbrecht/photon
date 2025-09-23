package org.hakim.photon.specification;

public class ApplicationSpecification {

    /** app name */
    private String name;

    /** specification of the app window */
    private WindowSpecification windowSpecification;

    public ApplicationSpecification(String title) {
        this.name = title;
        this.windowSpecification = new WindowSpecification();
        this.windowSpecification.setTitle(title);
    }

    public ApplicationSpecification(String name, WindowSpecification windowSpecification) {
        this.name = name;
        this.windowSpecification = windowSpecification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WindowSpecification getWindowSpecification() {
        return windowSpecification;
    }

    public void setWindowSpecification(WindowSpecification windowSpecification) {
        this.windowSpecification = windowSpecification;
    }
}
