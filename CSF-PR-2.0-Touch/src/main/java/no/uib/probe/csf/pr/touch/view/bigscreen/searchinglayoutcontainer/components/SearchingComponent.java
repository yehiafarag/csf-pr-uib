package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import no.uib.probe.csf.pr.touch.Data_Handler;
import no.uib.probe.csf.pr.touch.database.Query;
import no.uib.probe.csf.pr.touch.logic.beans.QuantProtein;
import no.uib.probe.csf.pr.touch.view.core.BigBtn;
import no.uib.probe.csf.pr.touch.view.core.PieChart;
import no.uib.probe.csf.pr.touch.view.core.PopupWindow;
import no.uib.probe.csf.pr.touch.view.core.ProteinSearcingResultLabel;
import no.uib.probe.csf.pr.touch.view.core.TrendLegend;

/**
 *
 * @author Yehia Farag
 *
 * this class represents searching components
 */
public class SearchingComponent extends BigBtn {

    private final PopupWindow searchingPanel;
    private List<QuantProtein> searchQuantificationProtList;
    private final GridLayout quantDataResult;
    private final VerticalLayout idDataResult;
    private final Label noresultsLabel;
    private final Data_Handler Data_handler;
    private final String noresultMessage = "No results found";

    public SearchingComponent(final Data_Handler Data_handler) {
        super("Search", "Search quantitative and  identification proteins.", "img/search.png");
        this.Data_handler = Data_handler;
        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(100, Unit.PERCENTAGE);
        popupbodyLayout.setMargin(new MarginInfo(true, true, false, true));
        searchingPanel = new PopupWindow(popupbodyLayout, "Search");

        SearchingUnitComponent searchingUnit = new SearchingUnitComponent() {

            @Override
            public void search(Query query) {
                searchProteins(query);
                System.out.println("search for quant");
            }

        };

        VerticalLayout resultsLayout = new VerticalLayout();
        resultsLayout.addStyleName("roundedborder");
        resultsLayout.addStyleName("whitelayout");

        resultsLayout.setWidth(100, Unit.PERCENTAGE);
        Panel searchingResults = new Panel(resultsLayout);
        searchingResults.setStyleName(ValoTheme.PANEL_BORDERLESS);
        searchingResults.setWidth(100, Unit.PERCENTAGE);

        quantDataResult = new GridLayout();
        resultsLayout.addComponent(quantDataResult);
        resultsLayout.setComponentAlignment(quantDataResult, Alignment.MIDDLE_CENTER);

        noresultsLabel = new Label(noresultMessage);
        noresultsLabel.setStyleName(ValoTheme.LABEL_BOLD);
        noresultsLabel.setVisible(false);
        resultsLayout.addComponent(noresultsLabel);
        resultsLayout.setComponentAlignment(noresultsLabel, Alignment.MIDDLE_CENTER);

        idDataResult = new VerticalLayout();
        resultsLayout.addComponent(idDataResult);

        popupbodyLayout.addComponent(searchingUnit);
        popupbodyLayout.addStyleName("searchpopup");

        HorizontalLayout middleLayout = new HorizontalLayout();

        middleLayout.setWidth(100, Unit.PERCENTAGE);
        Label resultsLabel = new Label("Search Results");
        resultsLabel.setStyleName(ValoTheme.LABEL_BOLD);
        middleLayout.addComponent(resultsLabel);

        TrendLegend legend = new TrendLegend("diseaselegend");
        middleLayout.addComponent(legend);
        middleLayout.setComponentAlignment(legend, Alignment.MIDDLE_RIGHT);

        popupbodyLayout.setSpacing(true);
        popupbodyLayout.addComponent(middleLayout);

        popupbodyLayout.addComponent(searchingResults);

    }

    private void searchProteins(Query query) {
        query.setValidatedProteins(false);
        query.setSearchDataset("");

        //searching quant data
        String defaultText = query.getSearchKeyWords();
        defaultText = defaultText.replace(",", "\n").trim();
        Set<String> filterKeywordSet = new LinkedHashSet<>();
        filterKeywordSet.addAll(Arrays.asList(defaultText.split("\n")));
        defaultText = "";
        defaultText = filterKeywordSet.stream().map((str) -> str + "\n").reduce(defaultText, String::concat);
        query.setSearchKeyWords(defaultText);
        //searching quant data
        query.setSearchDataType("Quantification Data");

        searchQuantificationProtList = Data_handler.searchQuantificationProtein(query, false);

        String quantNotFound = Data_handler.filterQuantSearchingKeywords(searchQuantificationProtList, query.getSearchKeyWords(), query.getSearchBy());
        Map<String, Integer[]> quantHitsList = Data_handler.getQuantHitsList(searchQuantificationProtList, query.getSearchBy());

        if (quantHitsList != null && searchQuantificationProtList != null) {
            Set<Integer> studiesSet = new HashSet<>();
            searchQuantificationProtList.stream().forEach((qp) -> {
                studiesSet.add(qp.getDsKey());
            });
            initProteinsQuantDataLayout(quantHitsList, query.getSearchBy(), searchQuantificationProtList.size(), query.getSearchKeyWords(), studiesSet.size());
        }
        if (quantNotFound != null && !quantNotFound.trim().equalsIgnoreCase("")) {
            noresultsLabel.setValue(noresultMessage + " for (" + quantNotFound + ")");
            noresultsLabel.setVisible(true);

        }

        query.setSearchDataType("Identification Data");

        //searching id data
        String idSearchIdentificationProtList = Data_handler.searchIdentificationProtein(query);
        if (idSearchIdentificationProtList != null) {
            idDataResult.setVisible(true);
            idDataResult.removeAllComponents();
            Link idSearchingLink = new Link(idSearchIdentificationProtList, new ExternalResource("http://localhost:8084/CSF-PR-ID/searchby:" + query.getSearchBy().replace(" ", "*") + "___searchkey:" + query.getSearchKeyWords().replace("\n", "__").replace(" ", "*")));
            idSearchingLink.setTargetName("_blank");
            idDataResult.addComponent(idSearchingLink);

        } else {
            idDataResult.setVisible(false);
        }

//        quantSearchingErrorLabel.updateErrot(quantNotFound);
    }
    private final String[] items = new String[]{"Alzheimer's", "Multiple Sclerosis", "Parkinson's"};
    private final Color[] itemsColors = new Color[]{Color.decode("#4b7865"), Color.decode("#A52A2A"), Color.decode("#74716E")};

    /**
     * initialize quant searching results layout
     *
     * @param quantHitsList map of hits and main protein title
     * @param searchBy the searching by method
     * @param totalProtNum total number of hits
     * @param keywords the keywords used for the searching
     *
     */
    private void initProteinsQuantDataLayout(Map<String, Integer[]> quantHitsList, String searchBy, int totalProtNum, String keywords, int studiesNum) {

        quantDataResult.removeAllComponents();
        if (quantHitsList == null || quantHitsList.isEmpty()) {
            quantDataResult.setVisible(false);
            noresultsLabel.setVisible(true);
            return;
        }
        int availableWidth = (int) searchingPanel.getWidth()-100;
        quantDataResult.setVisible(true);
        noresultsLabel.setVisible(false);

        int maxColNum = Math.max(availableWidth / 250, 1);

        if (quantHitsList.size() <= maxColNum * 2) {
            quantDataResult.setColumns(maxColNum);
            quantDataResult.setRows(3);
            int col = 0;
            int row = 0;
            for (String proteinName : quantHitsList.keySet()) {
                PieChart chart = new PieChart(250, 200, proteinName);
                chart.initializeFilterData(items, quantHitsList.get(proteinName), itemsColors);
                 chart.redrawChart();
                 
                 quantDataResult.addComponent(chart, col++, row);
               
                if (col == maxColNum) {
                    col = 0;
                    row++;

                }
            }

        } else {
            quantDataResult.setColumns(maxColNum);
            quantDataResult.setRows(1000);
            quantDataResult.setHideEmptyRowsAndColumns(true);
            int col = 0;
            int row = 0;
            for (String proteinName : quantHitsList.keySet()) {

                ProteinSearcingResultLabel chart = new ProteinSearcingResultLabel(proteinName, items, quantHitsList.get(proteinName), itemsColors);         
                quantDataResult.addComponent(chart, col++, row);
//                chart.redrawChart();
                if (col == maxColNum) {
                    col = 0;
                    row++;

                }
            }

//             quantDataResult.setColumns(maxColNum);
//            quantDataResult.setRows(500);
//            int col = 0;
//            int row = 0;
//            for (String proteinName : quantHitsList.keySet()) {
//                //add disease label
//                System.out.println("at proteinName " + proteinName + " " + quantHitsList.get(proteinName));
//
//                PieChart chart = new PieChart(250, 50, proteinName);
//
//                quantDataResult.addComponent(chart, col++, row);
//                if (col == maxColNum) {
//                    col = 0;
//                    row++;
//
//                }
//            }
        }
//        quantResultsOverview.setCaption("Proteins Quantitative Data ( #Proteins " + quantHitsList.size() + "  |  #Datasets " + studiesNum + "  |  #Hits " + totalProtNum + " )");
//        quantResultsOverview.setVisible(true);
//        quantProteinsDataLayout.removeAllComponents();
//        quantProteinsDataLayoutContainer.updateTitleLabel("Proteins Quantitative Data ( #Proteins " + quantHitsList.size() + "  | #Datasets " + studiesNum + "  |  #Hits " + totalProtNum + " )");
//        quantProteinsDataLayoutContainer.setVisible(true);
//        quantProteinsDataLayout.setVisible(true);
//        if (quantHitsList.size() > 1) {
//            HorizontalLayout loadAllProtLabelLayout = generateLabel(searchBy, null, null, "Load All", null, totalProtNum);
//            loadAllProtLabelLayout.addLayoutClickListener(quantLayoutListener);
//            quantProteinsDataLayout.addComponent(loadAllProtLabelLayout);
//        }
//
//        String[] keywordsArr = keywords.split("\n");
//        Map<String, VerticalLayout> layoutMap = new HashMap<String, VerticalLayout>();
//        if (searchBy.equalsIgnoreCase("Peptide Sequence")) {
//            keywordsArr = new String[quantHitsList.size()];
//            int index = 0;
//            for (String keyword : quantHitsList.keySet()) {
//                keywordsArr[index++] = keyword;
//            }
//        }
//
//        for (String keywordsArr1 : keywordsArr) {
//            VerticalLayout spacer = new VerticalLayout();
//            spacer.setStyleName(Reindeer.LAYOUT_WHITE);
//            spacer.setMargin(new MarginInfo(false, true, false, true));
//            spacer.setHeight("20px");
//            Label l = new Label("<h4 style='text-decoration: underline;'><font color=\"gray\">" + keywordsArr1.toUpperCase() + "</font></h4>");
//            spacer.addComponent(l);
//            l.setContentMode(ContentMode.HTML);
//            quantProteinsDataLayout.addComponent(spacer);
//            VerticalLayout vlo = new VerticalLayout();
//            layoutMap.put(keywordsArr1, vlo);
//            quantProteinsDataLayout.addComponent(vlo);
//
//        }
//
//        for (String key : quantHitsList.keySet()) {
//            if (searchBy.equalsIgnoreCase("Protein Name") || searchBy.equalsIgnoreCase("Peptide Sequence")) {
//                HorizontalLayout protLabelLayout = generateLabel(searchBy, null, null, key, null, quantHitsList.get(key));
//                protLabelLayout.addLayoutClickListener(quantLayoutListener);
//                for (String keyWord : layoutMap.keySet()) {
//                    if (key.toUpperCase().contains(keyWord.toUpperCase())) {
//                        layoutMap.get(keyWord).addComponent(protLabelLayout);
//                        break;
//                    }
//                }
//            } else if (searchBy.equalsIgnoreCase("Protein Accession")) {
//
//                HorizontalLayout protLabelLayout = generateLabel(searchBy, key.split("__")[0], null, key.split("__")[1], null, quantHitsList.get(key));
//                protLabelLayout.addLayoutClickListener(quantLayoutListener);
//                for (String keyWord : layoutMap.keySet()) {
//
//                    if (key.toUpperCase().contains(keyWord.toUpperCase())) {
//                        layoutMap.get(keyWord).addComponent(protLabelLayout);
//                        break;
//                    }
//
//                }
//            }
//
//        }
//        int counter = layoutMap.size();
//        for (String keymap : layoutMap.keySet()) {
//            VerticalLayout sectionLayout = layoutMap.get(keymap);
//            if (sectionLayout.getComponentCount() == 0) {
//                int index = quantProteinsDataLayout.getComponentIndex(sectionLayout);
//                quantProteinsDataLayout.getComponent(index - 1).setVisible(false);
//                sectionLayout.setVisible(false);
//                counter--;
//            }
//
//        }
//        if (counter < 2) {
//            int index = quantProteinsDataLayout.getComponentIndex(layoutMap.values().iterator().next());//.setStyleName(Reindeer.LAYOUT_BLACK);//setVisible(false);
//            quantProteinsDataLayout.getComponent(index - 1).setVisible(false);
//        }
    }

    @Override
    public void onClick() {
        searchingPanel.setVisible(true);

    }

}
