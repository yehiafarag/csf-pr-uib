package no.uib.probe.csf.pr.touch.view.core;

import com.byteowls.vaadin.chartjs.ChartJs;
import com.byteowls.vaadin.chartjs.config.ScatterChartConfig;
import com.byteowls.vaadin.chartjs.data.Data;
import com.byteowls.vaadin.chartjs.data.ScatterData;
import com.byteowls.vaadin.chartjs.data.ScatterDataset;
import com.byteowls.vaadin.chartjs.options.InteractionMode;
import com.byteowls.vaadin.chartjs.options.scale.Axis;
import com.byteowls.vaadin.chartjs.options.scale.LinearScale;
import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
public abstract class RangeComponent extends VerticalLayout implements Property.ValueChangeListener {

    private final VerticalLayout chartContainer;
    private final Slider upperRangeSlider;
    private final Slider lowerRangeSlider;
    private final AbsoluteLayout slidersContainer;
    private double[][] data;
    private final Double splitDataPoint;
    private final String[] colors;
    private final Label upperLabelValueComponent;

    public RangeComponent(String lowerLabel, double lowerValue, String upperLabel, double upperValue, String[] colors, Double splitDataPoint) {

        this.splitDataPoint = splitDataPoint;
        this.colors = colors;

        RangeComponent.this.setSizeFull();
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSizeFull();
        RangeComponent.this.addComponent(topLayout);
        Label loweLableComponent = initLabel(lowerLabel);
        topLayout.addComponent(loweLableComponent);
        topLayout.setComponentAlignment(loweLableComponent, Alignment.MIDDLE_CENTER);
        topLayout.setExpandRatio(loweLableComponent, 25);

        chartContainer = new VerticalLayout();
        chartContainer.setWidth(150, Unit.PIXELS);
        chartContainer.setHeight(100, Unit.PIXELS);
        chartContainer.setMargin(new MarginInfo(true, false, true, false));
        chartContainer.setStyleName("scatterchartcontainer");
//        chartContainer.setStyleName("blacklayout");
        topLayout.addComponent(chartContainer);
        topLayout.setComponentAlignment(chartContainer, Alignment.TOP_LEFT);
        topLayout.setExpandRatio(chartContainer, 50);

        Label upperLabelComponent = initLabel(upperLabel);
        topLayout.addComponent(upperLabelComponent);
        topLayout.setComponentAlignment(upperLabelComponent, Alignment.MIDDLE_RIGHT);
        topLayout.setExpandRatio(upperLabelComponent, 25);

        HorizontalLayout bottopmLayout = new HorizontalLayout();
        bottopmLayout.setSizeFull();
        RangeComponent.this.addComponent(bottopmLayout);

        Label loweLableValueComponent = initLabel((int) lowerValue + "");
        bottopmLayout.addComponent(loweLableValueComponent);
        bottopmLayout.setComponentAlignment(loweLableValueComponent, Alignment.MIDDLE_CENTER);
        bottopmLayout.setExpandRatio(loweLableValueComponent, 25);

        slidersContainer = new AbsoluteLayout();
        slidersContainer.setWidth(150, Unit.PIXELS);
        slidersContainer.setHeight(50, Unit.PIXELS);
        bottopmLayout.addComponent(slidersContainer);
        bottopmLayout.setComponentAlignment(slidersContainer, Alignment.MIDDLE_CENTER);
        bottopmLayout.setExpandRatio(slidersContainer, 50);

        lowerRangeSlider = new Slider();
        lowerRangeSlider.setWidth(130, Unit.PIXELS);
        lowerRangeSlider.setMin(lowerValue);
        lowerRangeSlider.setMax(upperValue);
        lowerRangeSlider.setValue(lowerValue);
        lowerRangeSlider.setStyleName("rangeslider");
        lowerRangeSlider.addStyleName("lower");
        lowerRangeSlider.addValueChangeListener(RangeComponent.this);
        slidersContainer.addComponent(lowerRangeSlider, "left:0px; top:50%;");

//        marker = new VerticalLayout();
//        marker.setHeight(5,Unit.PIXELS);
//        marker.setWidth(130,Unit.PIXELS);
//        marker.setStyleName("blacklayout");
//        slidersContainer.addComponent(marker, "left:0px; top:50%;");
        upperRangeSlider = new Slider();
        upperRangeSlider.setStyleName("rangeslider");

        upperRangeSlider.addStyleName("upper");
        upperRangeSlider.setWidth(130, Unit.PIXELS);
        upperRangeSlider.setMin((lowerValue));
        upperRangeSlider.setMax(upperValue);
        upperRangeSlider.setValue(upperValue);
        upperRangeSlider.addValueChangeListener(RangeComponent.this);
        slidersContainer.addComponent(upperRangeSlider, "left:0px; top:50%;");

        upperLabelValueComponent = initLabel((int) upperValue + "");
        bottopmLayout.addComponent(upperLabelValueComponent);
        bottopmLayout.setComponentAlignment(upperLabelValueComponent, Alignment.MIDDLE_RIGHT);
        bottopmLayout.setExpandRatio(upperLabelValueComponent, 25);

    }

    public void updateData(double[][] data) {
        this.data = data;
    }

    public void resetRange() {
        upperRangeSlider.removeValueChangeListener(RangeComponent.this);
        lowerRangeSlider.removeValueChangeListener(RangeComponent.this);
        upperRangeSlider.setValue(upperRangeSlider.getMax());
        lowerRangeSlider.setValue(lowerRangeSlider.getMin());
        updateFilter();
        upperRangeSlider.addValueChangeListener(RangeComponent.this);
        lowerRangeSlider.addValueChangeListener(RangeComponent.this);

    }

    public void updateFilter() {
        int min = -100;
        int max = 100;

        if (scatterChartConfig == null) {
            List<ScatterData> scatterdataList = new ArrayList<>();
            List<ScatterData> lowerDataList = new ArrayList<>();
            List<ScatterData> splitDataList = new ArrayList<>();
            List<ScatterData> upperDataList = new ArrayList<>();
            List<ScatterDataset> datasetsList = new ArrayList<>();
            boolean splitDataExist = false;
            if (splitDataPoint != null) {
                for (double[] dArr : data) {
                    ScatterData sd = new ScatterData();
                    sd.x(dArr[0]);
                    sd.y(dArr[1]);
                    scatterdataList.add(sd);
                    splitDataList.add(sd);
                    if (dArr[0] > splitDataPoint) {
                        upperDataList.add(sd);
                    } else if (dArr[0] < splitDataPoint) {
                        lowerDataList.add(sd);
                    } else {
                        splitDataExist = true;
                    }
                }
                if (!splitDataExist) {
                    splitDataList.clear();
                }
                if (scatterdataList.size() == 1) {
                    ScatterData sd = new ScatterData();
                    String[] arr = scatterdataList.get(0).toString().replace("\\[", "").replace("]", "").replace(" ", "").split(",");
                    sd.x(Double.valueOf(arr[0].split("=")[1]) + 3.0);
                    sd.y(Double.valueOf(arr[1].split("=")[1]));
                    scatterdataList.add(sd);
                }
                if (lowerDataList.size() == 1) {
                    ScatterData sd = new ScatterData();
                    String[] arr = lowerDataList.get(0).toString().replace("\\[", "").replace("]", "").replace(" ", "").split(",");
                    sd.x(Double.valueOf(arr[0].split("=")[1]) + 3.0);
                    sd.y(Double.valueOf(arr[1].split("=")[1]));
                    lowerDataList.add(sd);
                }
                if (splitDataList.size() == 1) {
                    ScatterData sd = new ScatterData();
                    String[] arr = splitDataList.get(0).toString().replace("\\[", "").replace("]", "").replace(" ", "").split(",");
                    sd.x(Double.valueOf(arr[0].split("=")[1]) + 3.0);
                    sd.y(Double.valueOf(arr[1].split("=")[1]));
                    splitDataList.add(sd);
                }
                if (upperDataList.size() == 1) {
                    ScatterData sd = new ScatterData();
                    String[] arr = upperDataList.get(0).toString().replace("\\[", "").replace("]", "").replace(" ", "").split(",");
                    sd.x(Double.valueOf(arr[0].split("=")[1]) - 3.0);
                    sd.y(Double.valueOf(arr[1].split("=")[1]));
                    upperDataList.add(sd);
                }

                ScatterDataset ds = (new ScatterDataset().label("1").fill(false).dataAsList(scatterdataList).backgroundColor("lightgray"));
                ScatterDataset lowerHightLightDs = (new ScatterDataset().label("2").fill(true).dataAsList(lowerDataList).backgroundColor(colors[0]));
                ScatterDataset splitHightLightDs = (new ScatterDataset().label("3").fill(true).dataAsList(splitDataList).backgroundColor("rgb(154, 208, 248)"));
                ScatterDataset upperHightLightDs = (new ScatterDataset().label("4").fill(true).dataAsList(upperDataList).backgroundColor(colors[1]));

                datasetsList.add(ds);
                datasetsList.add(lowerHightLightDs);
                datasetsList.add(upperHightLightDs);
                datasetsList.add(splitHightLightDs);

            } else {
                for (double[] dArr : data) {
                    ScatterData sd = new ScatterData();
                    sd.x(dArr[0]);
                    sd.y(dArr[1]);
                    scatterdataList.add(sd);
                }
                min = 0;
                max = (int) data[data.length - 1][0];
                if (scatterdataList.size() == 1) {
                    ScatterData sd = new ScatterData();
                    sd.x((double) min);
                    sd.y(0.0);
                    scatterdataList.add(sd);
                }

                ScatterDataset ds = (new ScatterDataset().label("1").fill(false).dataAsList(scatterdataList).backgroundColor("lightgray"));
                ScatterDataset hightLightDs = (new ScatterDataset().label("2").fill(true).dataAsList(scatterdataList).backgroundColor("#197de1"));
                datasetsList.add(ds);
                datasetsList.add(hightLightDs);
            }
            upperLabelValueComponent.setValue("<center>" + max + "</center>");

            upperRangeSlider.removeValueChangeListener(RangeComponent.this);
            lowerRangeSlider.removeValueChangeListener(RangeComponent.this);
            lowerRangeSlider.setMin(min);
            lowerRangeSlider.setMax(max);
            upperRangeSlider.setMin(min);
            upperRangeSlider.setMax(max);
            upperRangeSlider.addValueChangeListener(RangeComponent.this);
            lowerRangeSlider.addValueChangeListener(RangeComponent.this);
            chartContainer.removeAllComponents();
            ChartJs chart = updateChart(datasetsList, min, max);
            chartContainer.addComponent(chart);
            chartContainer.setComponentAlignment(chart, Alignment.MIDDLE_LEFT);
        } else {
            redrawRangeOnChart(lowerRangeSlider.getValue(), upperRangeSlider.getValue());
        }
    }

    private Label initLabel(String labelValue) {
        Label label = new Label("<center>" + labelValue + "</center>");
        label.setStyleName(ValoTheme.LABEL_SMALL);
        label.setStyleName(ValoTheme.LABEL_TINY);
        label.setContentMode(ContentMode.HTML);
        label.setWidth(100, Unit.PERCENTAGE);
        return label;

    }

    private ScatterChartConfig scatterChartConfig;

    private ChartJs updateChart(List<ScatterDataset> datasetsList, int min, int max) {
        scatterChartConfig = new ScatterChartConfig();
        Data<ScatterChartConfig> linechartData = scatterChartConfig.data();
        for (ScatterDataset ds : datasetsList) {
            linechartData.addDataset(ds);
        }

        linechartData.and()
                .options().animation().duration(1).and()
                .legend().display(false).and()
                .responsive(false)
                .title()
                .display(false)
                .and()
                .tooltips()
                .enabled(false)
                .and()
                .hover()
                .mode(InteractionMode.NEAREST)
                .intersect(true)
                .and()
                .scales()
                .add(Axis.X, new LinearScale()
                        .display(false)
                        .scaleLabel()
                        .display(false)
                        .and().ticks().min(min).max(max).fixedStepSize(1.0).display(false).and().gridLines().drawBorder(false).drawTicks(false).and())
                .add(Axis.Y, new LinearScale()
                        .display(false)
                        .scaleLabel()
                        .display(false)
                        .and()
                        .ticks().beginAtZero(Boolean.TRUE).display(false)
                        .and().gridLines().drawBorder(false).drawTicks(false).and())
                .and().elements().point().radius(0).and().and()
                .done();
        // add random data for demo   
        int count = 0;
//        for (Dataset<?, ?> ds : lineConfig.data().getDatasets()) {
//            LineDataset lds = (LineDataset) ds;   
//            lds.pointBackgroundColor(colors);
//            lds.dataAsList(data);
//            lds.borderColor("lightgray");
//            lds.backgroundColor("lightgray");
//        }

        ChartJs chart = new ChartJs(scatterChartConfig);
        chart.setWidth(130, Unit.PIXELS);
        chart.setHeight(70, Unit.PIXELS);
        return chart;

    }

    public void valueChange() {
        upperRangeSlider.removeStyleName("lower");
        upperRangeSlider.removeStyleName("upper");
        lowerRangeSlider.removeStyleName("lower");
        lowerRangeSlider.removeStyleName("upper");

        AbsoluteLayout.ComponentPosition lowerPos = slidersContainer.getPosition(lowerRangeSlider);
        AbsoluteLayout.ComponentPosition upperPos = slidersContainer.getPosition(upperRangeSlider);
        slidersContainer.removeAllComponents();
        double min;
        double max;
        double finalMax;

        if (lowerRangeSlider.getValue() <= upperRangeSlider.getValue() && slidersContainer.getComponentCount() == 0) {
            slidersContainer.addComponent(lowerRangeSlider, lowerPos.getCSSString());
            slidersContainer.addComponent(upperRangeSlider, upperPos.getCSSString());
            upperRangeSlider.addStyleName("upper");
            lowerRangeSlider.addStyleName("lower");
            min = lowerRangeSlider.getValue();
            max = upperRangeSlider.getValue();

        } else {
            slidersContainer.addComponent(upperRangeSlider, upperPos.getCSSString());
            slidersContainer.addComponent(lowerRangeSlider, lowerPos.getCSSString());
            lowerRangeSlider.addStyleName("upper");
            upperRangeSlider.addStyleName("lower");
            min = upperRangeSlider.getValue();
            max = lowerRangeSlider.getValue();

        }
        
        redrawRangeOnChart(min, max);
        selectedRange(min, max,!(max == lowerRangeSlider.getMax() && min == lowerRangeSlider.getMin()));

//       
//        slidersContainer.removeAllComponents();
//        slidersContainer.addComponent(lower,lowerPos.getCSSString());
//        slidersContainer.addComponent(upper,upperPos.getCSSString());
//        slidersContainer.setPosition(lower,slidersContainer.getPosition(lower) );
//         slidersContainer.setPosition(upper,slidersContainer.getPosition(upper) );
//
//       
    }

    private void redrawRangeOnChart(double min, double max) {
//        
//        List<ScatterData> list = (List<ScatterData>) scatterChartConfig.data().getFirstDataset().getData();

        if (scatterChartConfig.data().getDatasets().size() < 3) {
            List<ScatterData> updatedList = new ArrayList<>();
            for (double[] dArr : data) {
                ScatterData sd = new ScatterData();
                if (dArr[0] >= min && dArr[0] <= max) {
                    sd.y(dArr[1]);
                    sd.x(dArr[0]);
                    updatedList.add(sd);
                }
            }
            if (updatedList.size() == 1) {
                ScatterData sd = new ScatterData();
                sd.x(min);
                sd.y(0.0);
                updatedList.add(sd);
            }
            ScatterDataset hightLightDs = (new ScatterDataset().label("2").fill(true).dataAsList(updatedList).backgroundColor("#197de1"));
            scatterChartConfig.data().getDatasets().remove(1);
            scatterChartConfig.data().addDataset(hightLightDs);
        } else {
            boolean splitDataExist = false;
            List<ScatterData> scatterdataList = new ArrayList<>();
            List<ScatterData> lowerDataList = new ArrayList<>();
            List<ScatterData> splitDataList = new ArrayList<>();
            List<ScatterData> upperDataList = new ArrayList<>();
            for (double[] dArr : data) {
                ScatterData sd = new ScatterData();
                sd.x(dArr[0]);
                sd.y(dArr[1]);
                scatterdataList.add(sd);
                if (dArr[0] >= min && dArr[0] <= max) {
                    splitDataList.add(sd);
                    if (dArr[0] > splitDataPoint) {
                        upperDataList.add(sd);
                    } else if (dArr[0] < splitDataPoint) {
                        lowerDataList.add(sd);
                    } else {
                        splitDataExist = true;
                    }
                }
            }

            if (!splitDataExist) {
                splitDataList.clear();
            }
            if (scatterdataList.size() == 1) {
                ScatterData sd = new ScatterData();
                String[] arr = scatterdataList.get(0).toString().replace("\\[", "").replace("]", "").replace(" ", "").split(",");
                sd.x(Double.valueOf(arr[0].split("=")[1]) + 3.0);
                sd.y(Double.valueOf(arr[1].split("=")[1]));
                scatterdataList.add(sd);
            }
            if (lowerDataList.size() == 1) {
                ScatterData sd = new ScatterData();
                String[] arr = lowerDataList.get(0).toString().replace("\\[", "").replace("]", "").replace(" ", "").split(",");
                sd.x(Double.valueOf(arr[0].split("=")[1]) + 3.0);
                sd.y(Double.valueOf(arr[1].split("=")[1]));
                lowerDataList.add(sd);
            }
            if (splitDataList.size() == 1) {
                ScatterData sd = new ScatterData();
                String[] arr = splitDataList.get(0).toString().replace("\\[", "").replace("]", "").replace(" ", "").split(",");
                sd.x(Double.valueOf(arr[0].split("=")[1]) + 3.0);
                sd.y(Double.valueOf(arr[1].split("=")[1]));
                splitDataList.add(sd);
            }
            if (upperDataList.size() == 1) {
                ScatterData sd = new ScatterData();
                String[] arr = upperDataList.get(0).toString().replace("\\[", "").replace("]", "").replace(" ", "").split(",");
                sd.x(Double.valueOf(arr[0].split("=")[1]) - 3.0);
                sd.y(Double.valueOf(arr[1].split("=")[1]));
                upperDataList.add(sd);
            }
            ScatterDataset ds = (new ScatterDataset().label("1").fill(false).dataAsList(scatterdataList).backgroundColor("lightgray"));
            ScatterDataset lowerHightLightDs = (new ScatterDataset().label("2").fill(true).dataAsList(lowerDataList).backgroundColor(colors[0]));
            ScatterDataset splitHightLightDs = (new ScatterDataset().label("3").fill(true).dataAsList(splitDataList).backgroundColor("rgb(154, 208, 248)"));
            ScatterDataset upperHightLightDs = (new ScatterDataset().label("4").fill(true).dataAsList(upperDataList).backgroundColor(colors[1]));

            scatterChartConfig.data().getDatasets().clear();
            scatterChartConfig.data().addDataset(ds);
            scatterChartConfig.data().addDataset(lowerHightLightDs);
            scatterChartConfig.data().addDataset(upperHightLightDs);
            scatterChartConfig.data().addDataset(splitHightLightDs);

        }
        chartContainer.removeAllComponents();
        ChartJs chart = new ChartJs(scatterChartConfig);
        chart.setWidth(130, Unit.PIXELS);
        chart.setHeight(70, Unit.PIXELS);
        chartContainer.addComponent(chart);
        chartContainer.setComponentAlignment(chart, Alignment.MIDDLE_LEFT);
    }

    public abstract void selectedRange(double min, double max,boolean filterApplied);

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        valueChange();
    }

}
