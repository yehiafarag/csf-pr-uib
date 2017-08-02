package no.uib.probe.csf.pr.touch.view.core;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.LineChartConfig;
import com.byteowls.vaadin.chartjs.data.Dataset;
import com.byteowls.vaadin.chartjs.data.LineDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.Position;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.CategoryScale;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.byteowls.vaadin.chartjs.utils.ColorUtils;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents range component with lower and upper range
 *
 * @author Yehia Farag
 */
public class RangeComponent extends VerticalLayout {

    public RangeComponent(String lowerLabel, double lowerValue, String upperLabel, double upperValue) {
        RangeComponent.this.setSizeFull();
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSizeFull();
        RangeComponent.this.addComponent(topLayout);
        Label loweLableComponent = initLabel(lowerLabel);
        topLayout.addComponent(loweLableComponent);
        topLayout.setComponentAlignment(loweLableComponent, Alignment.MIDDLE_CENTER);
        topLayout.setExpandRatio(loweLableComponent, 25);

        VerticalLayout chartContainer = new VerticalLayout();
        chartContainer.setWidth(100, Unit.PERCENTAGE);
        chartContainer.setHeight(50, Unit.PIXELS);
        topLayout.addComponent(chartContainer);
        topLayout.setComponentAlignment(chartContainer, Alignment.MIDDLE_CENTER);
        topLayout.setExpandRatio(chartContainer, 50);
        
        chartContainer.addComponent(updateChart());


        Label upperLabelComponent = initLabel(upperLabel);
        topLayout.addComponent(upperLabelComponent);
        topLayout.setComponentAlignment(upperLabelComponent, Alignment.MIDDLE_RIGHT);
        topLayout.setExpandRatio(upperLabelComponent, 25);

        HorizontalLayout bottopmLayout = new HorizontalLayout();
        bottopmLayout.setSizeFull();
        RangeComponent.this.addComponent(bottopmLayout);
        
        
        Label loweLableValueComponent = initLabel((int)lowerValue+"");
        bottopmLayout.addComponent(loweLableValueComponent);
        bottopmLayout.setComponentAlignment(loweLableValueComponent, Alignment.MIDDLE_CENTER);
        bottopmLayout.setExpandRatio(loweLableValueComponent, 25);
        
        
        AbsoluteLayout slidersContainer = new AbsoluteLayout();
        slidersContainer.setWidth(100, Unit.PERCENTAGE);
        slidersContainer.setHeight(50, Unit.PIXELS);
        bottopmLayout.addComponent(slidersContainer);
        bottopmLayout.setComponentAlignment(slidersContainer, Alignment.MIDDLE_CENTER);
        bottopmLayout.setExpandRatio(slidersContainer, 50);

        Slider lowerRangeSlider = new Slider();
        lowerRangeSlider.setWidth(90, Unit.PERCENTAGE);
        lowerRangeSlider.setMin(lowerValue);
        lowerRangeSlider.setMax(upperValue);
        lowerRangeSlider.setValue(lowerValue);
        lowerRangeSlider.setStyleName("rangeslider");
        lowerRangeSlider.addValueChangeListener(event -> Notification.show("Value changed:",
                String.valueOf(event.getProperty().getValue()),
                Notification.Type.TRAY_NOTIFICATION));
        slidersContainer.addComponent(lowerRangeSlider, "left:0px; top:50%;");

        Slider upperRangeSlider = new Slider();
        upperRangeSlider.setStyleName("rangeslider");
        upperRangeSlider.setWidth(90, Unit.PERCENTAGE);
        upperRangeSlider.setMin((lowerValue));
        upperRangeSlider.setMax(upperValue);
        upperRangeSlider.setValue(upperValue);
        upperRangeSlider.addValueChangeListener(event -> Notification.show("Value changed:",
                String.valueOf(event.getProperty().getValue()),
                Notification.Type.TRAY_NOTIFICATION));
        slidersContainer.addComponent(upperRangeSlider, "left:0px; top:50%;");

        Label upperLabelValueComponent = initLabel((int)upperValue+"");
        bottopmLayout.addComponent(upperLabelValueComponent);
        bottopmLayout.setComponentAlignment(upperLabelValueComponent, Alignment.MIDDLE_RIGHT);
        bottopmLayout.setExpandRatio(upperLabelValueComponent, 25);

    }

    private Label initLabel(String labelValue) {
        Label label = new Label("<center>" + labelValue + "</center>");
        label.setStyleName(ValoTheme.LABEL_SMALL);
        label.setStyleName(ValoTheme.LABEL_TINY);
        label.setContentMode(ContentMode.HTML);
        label.setWidth(100, Unit.PERCENTAGE);
        return label;

    }
    private ChartJs updateChart(){
    LineChartConfig  lineConfig = new LineChartConfig ();
    lineConfig.data()
             .labels("January", "February", "March", "April", "May", "June", "July")
            .addDataset(new LineDataset().label("My First dataset").fill(false))
            .addDataset(new LineDataset().label("My Second dataset").fill(false))
            .addDataset(new LineDataset().label("Hidden dataset").hidden(true))
            .and()
        .options()
            .responsive(true)
            .title()
            .display(true)
            .text("Chart.js Line Chart")
            .and()
        .tooltips()
            .mode(InteractionMode.INDEX)
            .intersect(false)
            .and()
        .hover()
            .mode(InteractionMode.NEAREST)
            .intersect(true)
            .and()
        .scales()
        .add(Axis.X, new CategoryScale()
                .display(true)
                .scaleLabel()
                    .display(true)
                    .labelString("Month")
                    .and()
                .position(Position.TOP))
        .add(Axis.Y, new LinearScale()
                .display(true)
                .scaleLabel()
                    .display(true)
                    .labelString("Value")
                    .and()
                .ticks()
                    .suggestedMin(-10)
                    .suggestedMax(250)
                    .and()
                .position(Position.RIGHT))
        .and()
        .done();
      // add random data for demo
        List<String> labels = lineConfig.data().getLabels();
        for (Dataset<?, ?> ds : lineConfig.data().getDatasets()) {
            LineDataset lds = (LineDataset) ds;
            List<Double> data = new ArrayList<>();
            for (int i = 0; i < labels.size(); i++) {
                data.add((double) Math.round(Math.random() * 100));
            }
            lds.dataAsList(data);
            lds.borderColor(ColorUtils.randomColor(0.3));
            lds.backgroundColor(ColorUtils.randomColor(0.5));
        }

        ChartJs chart = new ChartJs(lineConfig);
        chart.setJsLoggingEnabled(true);
        chart.setWidth(100,Unit.PERCENTAGE);
        chart.setHeight(100,Unit.PERCENTAGE);
        return chart;
    
    }

}
