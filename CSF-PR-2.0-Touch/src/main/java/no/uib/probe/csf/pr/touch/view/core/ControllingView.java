package no.uib.probe.csf.pr.touch.view.core;

/**
 * This interface for Controlling View components to allow the view manager to
 * control different layout across the system.
 *
 * @author Yehia Farag
 */
public interface ControllingView {

    /**
     * Resize the component.
     *
     * @param width The new width of the component.
     * @param height The new height of the component.
     */
    public void resizeComponent(int width, int height);

    /**
     *Get the view unique id
     * @return
     */
    public String getViewId();

    /**
     *View the layout.
     */
    public void view();

    /**
     *Hide the layout.
     */
    public void hide();
}
