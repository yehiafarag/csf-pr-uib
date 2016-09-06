package no.uib.probe.csf.pr.touch.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.logic.beans.QuantComparisonProtein;
import no.uib.probe.csf.pr.touch.logic.beans.QuantDiseaseGroupsComparison;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFListener;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFPR_Central_Manager;
import no.uib.probe.csf.pr.touch.selectionmanager.CSFSelection;
import no.uib.probe.csf.pr.touch.view.components.datasetfilters.GroupSwichBtn;
import no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents.FilterColumnButton;
import no.uib.probe.csf.pr.touch.view.components.linechartproteintablecomponents.ProteinTable;
import no.uib.probe.csf.pr.touch.view.core.ImageContainerBtn;
import no.uib.probe.csf.pr.touch.view.core.InformationButton;
import no.uib.probe.csf.pr.touch.view.core.SearchingField;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 *
 * @author Yehia Farag
 *
 * this class represents both protein table and linechart component the protein
 * line chart represents the overall protein trend across different comparisons
 */
public abstract class LineChartProteinTableComponent extends VerticalLayout implements CSFListener, LayoutEvents.LayoutClickListener {

    private final CSFPR_Central_Manager CSFPR_Central_Manager;
    private final VerticalLayout controlBtnsContainer;
    private final ProteinTable quantProteinTable;
    private final Map<String, QuantComparisonProtein> proteinSearchingMap;
    private final ImageContainerBtn removeFilters;
    private final FilterColumnButton filterSortSwichBtn;
//    private final ImageContainerBtn checkUncheckBtn;

    public LineChartProteinTableComponent(CSFPR_Central_Manager CSFPR_Central_Manager, int width, int height, QuantDiseaseGroupsComparison userCustomizedComparison, boolean smallScreen) {

        this.CSFPR_Central_Manager = CSFPR_Central_Manager;
        this.proteinSearchingMap = new HashMap<>();

     this.setWidth(width, Unit.PIXELS);
        this.setHeight(height, Unit.PIXELS);
//        if (!smallScreen) {
//            this.setMargin(new MarginInfo(false, false, false, true));
//        }

        VerticalLayout bodyContainer = new VerticalLayout();
        bodyContainer.setWidth(100, Unit.PERCENTAGE);
        bodyContainer.setHeightUndefined();
        bodyContainer.setSpacing(true);
        this.addComponent(bodyContainer);

        //init toplayout
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setHeight(25, Unit.PIXELS);
        topLayout.setWidthUndefined();
        topLayout.setSpacing(true);
        topLayout.setMargin(new MarginInfo(false, false, false, true));
        bodyContainer.addComponent(topLayout);

        HorizontalLayout titleLayoutWrapper = new HorizontalLayout();
        titleLayoutWrapper.setHeight(25, Unit.PIXELS);
        titleLayoutWrapper.setWidth(213,Unit.PIXELS);
        titleLayoutWrapper.setSpacing(true);
        
        titleLayoutWrapper.setMargin(false);
        titleLayoutWrapper.addStyleName("margintop7");
        topLayout.addComponent(titleLayoutWrapper);
        topLayout.setExpandRatio(titleLayoutWrapper, 10);

        Label overviewLabel = new Label("Proteins");
        overviewLabel.setStyleName(ValoTheme.LABEL_BOLD);
        overviewLabel.addStyleName(ValoTheme.LABEL_SMALL);
        overviewLabel.addStyleName(ValoTheme.LABEL_TINY);
        
        overviewLabel.setWidth(47, Unit.PIXELS);
        titleLayoutWrapper.addComponent(overviewLabel);
        titleLayoutWrapper.setComponentAlignment(overviewLabel, Alignment.TOP_LEFT);
         titleLayoutWrapper.setExpandRatio(overviewLabel,47);

        SearchingField searchingFieldLayout = new SearchingField() {

            @Override
            public void textChanged(String text) {
                quantProteinTable.filterViewItemTable(getSearchingProteinsList(text));
                this.updateLabel("("+quantProteinTable.getRowsNumber()+")");

            }

        };
        titleLayoutWrapper.addComponent(searchingFieldLayout);
        titleLayoutWrapper.setComponentAlignment(searchingFieldLayout, Alignment.TOP_LEFT);
        titleLayoutWrapper.setExpandRatio(overviewLabel,166);
//        InfoPopupBtn info = new InfoPopupBtn("The protein table and overview chart give an overview for the selected proteins in the selected comparisons.");
//        titleLayoutWrapper.addComponent(info);
//        titleLayoutWrapper.setComponentAlignment(info, Alignment.MIDDLE_CENTER);

        TrendLegend legendLayout = new TrendLegend("linechart");
        legendLayout.setWidthUndefined();
        legendLayout.setHeight(25, Unit.PIXELS);
        legendLayout.addStyleName("margintop10");
        legendLayout.addStyleName("floatright");
//        if (width / 2 < 700) {
//            legendLayout.addStyleName("showonhover");
//        }
        topLayout.addComponent(legendLayout);
        topLayout.setComponentAlignment(legendLayout, Alignment.TOP_RIGHT);
        topLayout.setExpandRatio(legendLayout, 90);

        //end of toplayout
        //start chart layout
        VerticalLayout tableLayoutFrame = new VerticalLayout();
        height = height - 44;

        width = width - 50;
        tableLayoutFrame.setWidth(width, Unit.PIXELS);
        tableLayoutFrame.setHeightUndefined();//setHeight(tableHeight, Unit.PIXELS);
        tableLayoutFrame.addStyleName("roundedborder");
        tableLayoutFrame.addStyleName("whitelayout");
        tableLayoutFrame.addStyleName("padding20");
        bodyContainer.addComponent(tableLayoutFrame);
        bodyContainer.setComponentAlignment(tableLayoutFrame, Alignment.MIDDLE_CENTER);
        height = height - 70;
        width = width - 60;

        quantProteinTable = new ProteinTable(width, height, smallScreen) {

            @Override
            public void dropComparison(QuantDiseaseGroupsComparison comparison) {
                Set<QuantDiseaseGroupsComparison> updatedComparisonList = CSFPR_Central_Manager.getSelectedComparisonsList();
                updatedComparisonList.remove(comparison);
                CSFSelection selection = new CSFSelection("comparisons_selection", getFilterId(), updatedComparisonList, null);
                CSFPR_Central_Manager.selectionAction(selection);

            }

            @Override
            public void selectProtein(String selectedProtein, int custTrend) {
                CSFSelection selection = new CSFSelection("peptide_selection", getFilterId(), null, null);
                selection.setSelectedProteinAccession(selectedProtein);
                selection.setCustProteinSelection(custTrend);
                CSFPR_Central_Manager.selectionAction(selection);

            }

            @Override
            public void updateRowNumber(int rowNumber, String url) {
                searchingFieldLayout.updateLabel("("+rowNumber+")");
                LineChartProteinTableComponent.this.updateRowNumber(rowNumber, url);
            }

        };//this.initProteinTable();
        tableLayoutFrame.addComponent(quantProteinTable);
        
        
          HorizontalLayout  controlsLayout = new HorizontalLayout();
        controlsLayout.setWidth(100,Unit.PERCENTAGE);
        controlsLayout.setHeight(20,Unit.PIXELS);

        Label clickcommentLabel = new Label("Click in the table to select data");
        clickcommentLabel.setStyleName(ValoTheme.LABEL_SMALL);
        clickcommentLabel.addStyleName(ValoTheme.LABEL_TINY);
        clickcommentLabel.addStyleName(ValoTheme.LABEL_BOLD);
        clickcommentLabel.setWidth(182,Unit.PIXELS);
        
        controlsLayout.addComponent(clickcommentLabel);
        controlsLayout.setComponentAlignment(clickcommentLabel, Alignment.BOTTOM_RIGHT);  
        tableLayoutFrame.addComponent(controlsLayout);
        
        
        
        

        //init side control btns layout 
        controlBtnsContainer = new VerticalLayout();
        controlBtnsContainer.setHeightUndefined();
        controlBtnsContainer.setWidthUndefined();
        controlBtnsContainer.setSpacing(true);

        GroupSwichBtn groupSwichBtn = new GroupSwichBtn() {

            @Override
            public Set<QuantDiseaseGroupsComparison> getUpdatedComparsionList() {
                return CSFPR_Central_Manager.getSelectedComparisonsList();
            }

            @Override
            public void updateComparisons(LinkedHashSet<QuantDiseaseGroupsComparison> updatedComparisonList) {

                CSFSelection selection = new CSFSelection("comparisons_selection_update", getFilterId(), updatedComparisonList, null);
                CSFPR_Central_Manager.selectionAction(selection);

            }

            @Override
            public Map<QuantDiseaseGroupsComparison, QuantDiseaseGroupsComparison> getEqualComparsionMap() {
                return CSFPR_Central_Manager.getEqualComparisonMap();
            }

        };

        if (smallScreen) {
            groupSwichBtn.setWidth(25, Unit.PIXELS);
            groupSwichBtn.setHeight(25, Unit.PIXELS);
            groupSwichBtn.removeStyleName("smallimg");
            groupSwichBtn.addStyleName("nopaddingimg");
        }
        controlBtnsContainer.addComponent(groupSwichBtn);
        controlBtnsContainer.setComponentAlignment(groupSwichBtn, Alignment.MIDDLE_CENTER);

        ImageContainerBtn exportPdfBtn = new ImageContainerBtn() {

            @Override
            public void onClick() {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        };

        if (smallScreen) {
            exportPdfBtn.setWidth(25, Unit.PIXELS);
            exportPdfBtn.setHeight(25, Unit.PIXELS);
            exportPdfBtn.removeStyleName("smallimg");
            exportPdfBtn.addStyleName("nopaddingimg");
        } else {
            exportPdfBtn.setHeight(40, Unit.PIXELS);
            exportPdfBtn.setWidth(40, Unit.PIXELS);
        }
        exportPdfBtn.updateIcon(new ThemeResource("img/pdf-text-o.png"));
        exportPdfBtn.setEnabled(true);
        controlBtnsContainer.addComponent(exportPdfBtn);
        controlBtnsContainer.setComponentAlignment(exportPdfBtn, Alignment.MIDDLE_CENTER);
        exportPdfBtn.setDescription("Export table");

//        StreamResource myResource = null;//createResource();
//        FileDownloader fileDownloader = new FileDownloader(myResource);
//
//        fileDownloader.extend(groupSwichBtn);
        controlBtnsContainer.addComponent(exportPdfBtn);
        removeFilters = new ImageContainerBtn() {

            @Override
            public void onClick() {
                quantProteinTable.clearColumnFilters();

            }

            @Override
            public void setEnabled(boolean enabled) {
                if (enabled) {
                    this.removeStyleName("unapplied");
                } else {
                    this.addStyleName("unapplied");
                }
                super.setEnabled(enabled); //To change body of generated methods, choose Tools | Templates.
            }

        };

        filterSortSwichBtn = new FilterColumnButton() {

            @Override
            public void onClickFilter(boolean isFilter) {
                removeFilters.setEnabled(isFilter);
                quantProteinTable.switchHeaderBtns();
            }
        };

        if (smallScreen) {
            filterSortSwichBtn.setWidth(25, Unit.PIXELS);
            filterSortSwichBtn.setHeight(25, Unit.PIXELS);
            filterSortSwichBtn.removeStyleName("smallimg");
            filterSortSwichBtn.addStyleName("nopaddingimg");
        }
        controlBtnsContainer.addComponent(filterSortSwichBtn);
        controlBtnsContainer.setComponentAlignment(filterSortSwichBtn, Alignment.MIDDLE_CENTER);
        removeFilters.setEnabled(false);

        if (smallScreen) {
            removeFilters.setWidth(25, Unit.PIXELS);
            removeFilters.setHeight(25, Unit.PIXELS);
            removeFilters.removeStyleName("smallimg");
            removeFilters.addStyleName("nopaddingimg");
        } else {
            removeFilters.setHeight(40, Unit.PIXELS);
            removeFilters.setWidth(40, Unit.PIXELS);
            removeFilters.addStyleName("smallimg");
        }
        removeFilters.updateIcon(new ThemeResource("img/filter_disables.png"));
        controlBtnsContainer.addComponent(removeFilters);
        controlBtnsContainer.setComponentAlignment(removeFilters, Alignment.MIDDLE_CENTER);
        removeFilters.setDescription("Clear all applied filters");

//        ThemeResource checkedApplied = new ThemeResource("img/check-square.png");
//        ThemeResource checkedUnApplied = new ThemeResource("img/check-square-o.png");

//        checkUncheckBtn = new ImageContainerBtn() {
//
//            private boolean enabled = true;
//
//            @Override
//            public void onClick() {
////                quantProteinTable.clearColumnFilters();
//                if (enabled) {
//                    enabled = false;
//
//                    this.updateIcon(checkedApplied);
//                } else {
//                    enabled = true;
//                    this.updateIcon(checkedUnApplied);
//                }
//                quantProteinTable.hideCheckedColumn(enabled);
//            }
//
//            @Override
//            public void reset() {
//                enabled = true;
//                this.updateIcon(checkedUnApplied);
//                quantProteinTable.hideCheckedColumn(enabled);
//
//            }
//        };
//        checkUncheckBtn.setEnabled(true);
//
//        if (smallScreen) {
//            checkUncheckBtn.setWidth(25, Unit.PIXELS);
//            checkUncheckBtn.setHeight(25, Unit.PIXELS);
//            checkUncheckBtn.removeStyleName("smallimg");
//        } else {
//            checkUncheckBtn.setHeight(40, Unit.PIXELS);
//            checkUncheckBtn.setWidth(40, Unit.PIXELS);
//        }
//        checkUncheckBtn.addStyleName("pointer");
//        checkUncheckBtn.updateIcon(checkedUnApplied);
//        controlBtnsContainer.addComponent(checkUncheckBtn);
//        controlBtnsContainer.setComponentAlignment(checkUncheckBtn, Alignment.MIDDLE_CENTER);
//        checkUncheckBtn.setDescription("Show/hide checked column");

        InformationButton info = new InformationButton("The protein table provides an overview of the quantitative information available for each protein, classified into Increased, Decreased or Equal. If the quantitative data for a given comparison is not exclusively in the same direction an average value will be shown. To find proteins of interest use the search field at the top, or sort/filter on the individual comparisons using the options above the table. The icons at the lower right enables further modification of the table. Select a row in the table to show the protein details.", false);
        controlBtnsContainer.addComponent(info);
        if (smallScreen) {
            info.setWidth(25, Unit.PIXELS);
            info.setHeight(25, Unit.PIXELS);
            info.removeStyleName("smallimg");
        }

        CSFPR_Central_Manager.registerListener(LineChartProteinTableComponent.this);

    }

    public void filterSearchSelection(Set<String> keywords) {
        Set<QuantComparisonProtein> searchSet = new LinkedHashSet<>();

        keywords.stream().forEach((key) -> {
            searchSet.addAll(getSearchingProteinsList(key));
        });
        quantProteinTable.filterViewItemTable(searchSet);

    }

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("protein_selection")) {

            Set<QuantComparisonProtein> selectedProteinsList;
            filterSortSwichBtn.reset();

            if (CSFPR_Central_Manager.getSelectedProteinsList() == null) {
                proteinSearchingMap.clear();
                selectedProteinsList = new LinkedHashSet<>();

                Map<String, QuantComparisonProtein> proteinsFilterMap = new LinkedHashMap<>();;
                CSFPR_Central_Manager.getSelectedComparisonsList().stream().forEach((comparison) -> {
                    selectedProteinsList.addAll(comparison.getQuantComparisonProteinMap().values());
                    for (QuantComparisonProtein prot : comparison.getQuantComparisonProteinMap().values()) {
                        proteinsFilterMap.put(prot.getProteinAccession() + "__" + prot.getProteinName(), prot);
                    }
                });

                quantProteinTable.updateTableData(CSFPR_Central_Manager.getSelectedComparisonsList(), new LinkedHashSet<>(proteinsFilterMap.values()));

            } else {
                Set<QuantComparisonProtein> searchSet = new LinkedHashSet<>();
                selectedProteinsList = CSFPR_Central_Manager.getSelectedProteinsList();
                selectedProteinsList.stream().forEach((key) -> {
                    searchSet.addAll(getSearchingProteinsList(key.getProteinAccession() + "__" + key.getProteinName()));
                });
//                quantProteinTable.filterTableItem(searchSet);
                quantProteinTable.updateTableData(CSFPR_Central_Manager.getSelectedComparisonsList(), searchSet);

            }

            selectedProteinsList.stream().forEach((protein) -> {
                proteinSearchingMap.put(protein.getProteinAccession() + "__" + protein.getProteinName(), protein);

            });
            quantProteinTable.hideCheckedColumn(true);
//            checkUncheckBtn.reset();

        } else if (type.equalsIgnoreCase("comparisons_selection")) {
            removeFilters.setEnabled(false);
            filterSortSwichBtn.reset();
            quantProteinTable.hideCheckedColumn(true);
//            checkUncheckBtn.reset();

        }
    }

    @Override
    public String getFilterId() {
        return this.getClass().getName();
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public VerticalLayout getControlBtnsContainer() {
        return controlBtnsContainer;
    }

    /**
     * searching for proteins using name or accessions within the quant
     * comparisons table
     *
     * @param keyword query keyword
     * @return list of found quant proteins
     */
    private Set<QuantComparisonProtein> getSearchingProteinsList(String keyword) {
        Set<QuantComparisonProtein> subAccessionMap = new HashSet<>();
        proteinSearchingMap.keySet().stream().filter((key) -> (key.trim().toLowerCase().contains(keyword.toLowerCase().trim()))).forEach((key) -> {
            subAccessionMap.add(proteinSearchingMap.get(key));
        });
        return subAccessionMap;
    }

    public abstract void updateRowNumber(int rowNumber, String imgURl);

    public void setUserCustomizedComparison(QuantDiseaseGroupsComparison userCustomizedComparison) {
        quantProteinTable.setUserCustomizedComparison(userCustomizedComparison);
    }

}
