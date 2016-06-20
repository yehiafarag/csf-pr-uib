package no.uib.probe.csf.pr.touch.view.bigscreen.searchinglayoutcontainer.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
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
import no.uib.probe.csf.pr.touch.view.core.CloseButton;
import no.uib.probe.csf.pr.touch.view.core.PieChart;

/**
 *
 * @author Yehia Farag
 *
 * this class represents searching components
 */
public class SearchingComponent extends BigBtn {

    private final PopupView searchingPanel;
    private List<QuantProtein> searchQuantificationProtList;
    private final GridLayout quantDataResult;
    private final VerticalLayout idDataResult;
    private final Label noresultsLabel;
    private final Data_Handler Data_handler;

    public SearchingComponent(final Data_Handler Data_handler) {
        super("Search", "Search quantitative and  identification proteins.", "img/search.png");
        this.Data_handler = Data_handler;
        VerticalLayout popupbodyLayout = new VerticalLayout();
        popupbodyLayout.setSpacing(true);
        popupbodyLayout.setWidth(1000, Unit.PIXELS);
        popupbodyLayout.setMargin(new MarginInfo(false, false, false, true));
        popupbodyLayout.addStyleName("border");
        searchingPanel = new PopupView(null, popupbodyLayout) {

            @Override
            public void setPopupVisible(boolean visible) {
                this.setVisible(visible);
                super.setPopupVisible(visible); //To change body of generated methods, choose Tools | Templates.
            }
        };
        searchingPanel.setCaptionAsHtml(true);
        searchingPanel.setHideOnMouseOut(false);
        HorizontalLayout topLayoutWrapper = new HorizontalLayout();
        topLayoutWrapper.setWidth(100, Unit.PERCENTAGE);
        topLayoutWrapper.setHeight(30, Unit.PIXELS);
        popupbodyLayout.addComponent(topLayoutWrapper);
        Label searchLabel = new Label("Search");
        searchLabel.setStyleName(ValoTheme.LABEL_BOLD);
        topLayoutWrapper.addComponent(searchLabel);

        CloseButton closePopup = new CloseButton();
        closePopup.setWidth(10, Unit.PIXELS);
        closePopup.setHeight(10, Unit.PIXELS);
        topLayoutWrapper.addComponent(closePopup);
        topLayoutWrapper.setComponentAlignment(closePopup, Alignment.TOP_RIGHT);
        closePopup.addStyleName("translateleft10");
        closePopup.addLayoutClickListener((LayoutEvents.LayoutClickEvent event) -> {
            searchingPanel.setPopupVisible(false);
        });

        SearchingUnitComponent searchingUnit = new SearchingUnitComponent() {

            @Override
            public void search(Query query) {
                searchProteins(query);
                System.out.println("search for quant");
            }

        };

        this.searchingPanel.setPopupVisible(false);
        this.addComponent(searchingPanel);

        VerticalLayout resultsLayout = new VerticalLayout();
        resultsLayout.setWidth(100, Unit.PERCENTAGE);
        Panel searchingResults = new Panel(resultsLayout);
        searchingResults.setStyleName(ValoTheme.PANEL_BORDERLESS);
        searchingResults.setWidth(100, Unit.PERCENTAGE);

        quantDataResult = new GridLayout();
        resultsLayout.addComponent(quantDataResult);

        idDataResult = new VerticalLayout();
        resultsLayout.addComponent(idDataResult);

        noresultsLabel = new Label("No results found");
        noresultsLabel.setStyleName(ValoTheme.LABEL_BOLD);
        noresultsLabel.setVisible(false);
        resultsLayout.addComponent(noresultsLabel);
        resultsLayout.setComponentAlignment(noresultsLabel, Alignment.MIDDLE_CENTER);

        popupbodyLayout.addComponent(searchingUnit);
        popupbodyLayout.addStyleName("searchpopup");
        popupbodyLayout.addComponent(searchingResults);

    }

    private void searchProteins(Query query) {
        query.setValidatedProteins(false);
        query.setSearchDataset("");

        //searching id data
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
        Map<String, Integer> quantHitsList = Data_handler.getQuantHitsList(searchQuantificationProtList, query.getSearchBy());
        if (quantHitsList != null && searchQuantificationProtList != null) {
            Set<Integer> studiesSet = new HashSet<>();
            searchQuantificationProtList.stream().forEach((qp) -> {
                studiesSet.add(qp.getDsKey());
            });
            initProteinsQuantDataLayout(quantHitsList, query.getSearchBy(), searchQuantificationProtList.size(), query.getSearchKeyWords(), studiesSet.size());
        }
//        quantSearchingErrorLabel.updateErrot(quantNotFound);

    }

    /**
     * initialize quant searching results layout
     *
     * @param quantHitsList map of hits and main protein title
     * @param searchBy the searching by method
     * @param totalProtNum total number of hits
     * @param keywords the keywords used for the searching
     *
     */
    private void initProteinsQuantDataLayout(Map<String, Integer> quantHitsList, String searchBy, int totalProtNum, String keywords, int studiesNum) {

        quantDataResult.removeAllComponents();
        if (quantHitsList == null || quantHitsList.isEmpty()) {
            quantDataResult.setVisible(false);
            noresultsLabel.setVisible(true);
            System.out.println("at no results found");
//            quantResultsOverview.setVisible(false);
//            quantResultsOverview.setCaption("Proteins Quantitative Data ");
            return;
        }
          quantDataResult.setVisible(true);
        noresultsLabel.setVisible(false);
        if (quantHitsList.size() <= 12) {
            quantDataResult.setColumns(4);
            quantDataResult.setRows((quantHitsList.size() / 4)+1);
            int col = 0;
            int row = 0;
            for (String proteinName : quantHitsList.keySet()) {
                System.out.println("at proteinName "+proteinName+" "+ quantHitsList.get(proteinName));
                
                PieChart chart = new PieChart(250,200,proteinName);
                
                
                quantDataResult.addComponent(chart, col++, row);
                if (col == 4) {
                    col = 0;
                    row++;

                }
            }              

        }else{
        
        
        
        
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
        searchingPanel.setPopupVisible(true);

    }

}
