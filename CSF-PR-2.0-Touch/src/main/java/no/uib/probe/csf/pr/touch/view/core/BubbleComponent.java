package no.uib.probe.csf.pr.touch.view.core;

import com.vaadin.ui.Label;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class BubbleComponent extends Label implements Comparable<Label> {

    private final Map<String, Object> param = new HashMap<>();

    /**
     *
     * @param type
     */
    public BubbleComponent(String type) {
        this.setValue("  ");
        this.setStyleName("transparent");
        if (type.equalsIgnoreCase("cycle")) {
            this.addStyleName("cycle");
        }

    }

    /**
     *
     * @param key
     * @param value
     */
    public void setParam(String key, Object value) {
        param.put(key, value);
    }

    /**
     *
     * @param key
     * @return
     */
    public Object getParam(String key) {

        return param.get(key);
    }

    public void select(boolean selectAction) {
        if (selectAction) {
            this.removeStyleName("unselectbubble");
        } else {
            this.addStyleName("unselectbubble");
        }
    }

    @Override
    public int compareTo(Label other) {
        if (this.getWidth() > other.getWidth()) {
            return -1;
        } else {
            return 1;
        }
    }

}
