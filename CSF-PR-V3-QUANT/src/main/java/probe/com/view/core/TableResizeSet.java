package probe.com.view.core;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import java.io.Serializable;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.BaseTheme;

/**
 *
 * @author Yehia Farag
 */
public class TableResizeSet extends HorizontalLayout implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Table table;
    private final String SMALL_SIZE = "160px";
    private final String MEDIUM_SIZE = "267.5px";
    private final String LARGE_SIZE = "360px";
    private String currentSize;

    /**
     *
     * @param table
     * @param currentSize
     */
    public TableResizeSet(Table table, String currentSize) {
        this.currentSize = currentSize;
        this.table = table;
        this.setWidth("90px");
        this.setHeight("21px");

        Button b1 = init("img/small.jpg", SMALL_SIZE);
        Button b2 = init("img/med.jpg", MEDIUM_SIZE);
        Button b3 = init("img/larg.jpg", LARGE_SIZE);

        b1.setDescription("Small table size");
        b2.setDescription("Medium table size");
        b3.setDescription("Large table size");
        
        this.addComponent(b1);
        this.addComponent(b2);
        this.addComponent(b3);
        this.setComponentAlignment(b1, Alignment.BOTTOM_RIGHT);
        this.setComponentAlignment(b2, Alignment.BOTTOM_RIGHT);
        this.setComponentAlignment(b3, Alignment.BOTTOM_RIGHT);
        MarginInfo m = new MarginInfo(false, true, false, false);
        this.setMargin(m);


    }

    private Button init(String link, final String size) {
        Button b = new Button();
        b.setStyleName(BaseTheme.BUTTON_LINK);
        b.setIcon(new ThemeResource(link));
        b.addClickListener(new ClickListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                // TODO Auto-generated method stub
                table.setHeight(size);
                currentSize = size;
            }
        });
        return b;
    }

    /**
     *
     * @return
     */
    public String getCurrentSize() {
        return this.currentSize;
    }

    /**
     *
     * @param table
     */
    public void setTable(Table table) {
        this.table = table;
        this.table.setHeight(currentSize);
    }
}
