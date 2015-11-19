package probe.com.view.body;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import probe.com.dal.Query;
import probe.com.handlers.CSFPRHandler;
import probe.com.model.beans.identification.IdentificationProteinBean;
import probe.com.model.beans.quant.QuantProtein;
import probe.com.view.body.searching.SearchingUnitLayout;
import probe.com.view.body.searching.id.IdDataSearchingTabLayout;
import probe.com.view.body.searching.quant.QuantDataSearchingTabLayout;
import probe.com.view.core.HideOnClickLayout;

/**
 * @author Yehia Farag
 *
 * main searching tab layout
 */
public class ProteinsSearchingLayout extends VerticalLayout implements Serializable, Button.ClickListener {

    private final SearchingUnitLayout searchingUnitLayout;
    private final CSFPRHandler handler;
    private final VerticalLayout idProteinsDataLayout, quantProteinsDataLayout;
    private final HideOnClickLayout idProteinsDataLayoutContainer, quantProteinsDataLayoutContainer;
    private final LayoutEvents.LayoutClickListener idLayoutListener, quantLayoutListener;
    private List<QuantProtein> searchQuantificationProtList;
    private final Button quantResultsOverview, idResultsOverview;

    /**
     *
     * @param handler
     * @param mainTabSheet
     */
    public ProteinsSearchingLayout(final CSFPRHandler handler, final TabSheet mainTabSheet) {
        this.handler = handler;
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setSpacing(true);
       
        searchingUnitLayout = new SearchingUnitLayout(ProteinsSearchingLayout.this);
        idLayoutListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                if (event.getClickedComponent() == null || ((HorizontalLayout) event.getClickedComponent().getParent()) == null) {
                    return;
                }
                String protName = ((HorizontalLayout) event.getClickedComponent().getParent()).getData().toString();
                if (protName.equalsIgnoreCase("Load All")) {
                    IdDataSearchingTabLayout idDatasearchingLayout = new IdDataSearchingTabLayout(searchIdentificationProtList, handler);

                    Tab tab = mainTabSheet.addTab(idDatasearchingLayout, "All Proteins Identification Data Results", null);
                    tab.setClosable(true);
                    mainTabSheet.setSelectedTab(tab);

                } else {
                    Map<Integer, IdentificationProteinBean> subSearchIdentificationProtList = new HashMap<Integer, IdentificationProteinBean>();
                    for (int key : searchIdentificationProtList.keySet()) {
                        IdentificationProteinBean idProtBean = searchIdentificationProtList.get(key);
                        if (idProtBean.getDescription().equalsIgnoreCase(protName)) {
                            subSearchIdentificationProtList.put(key, idProtBean);
                        }
                    }
                    IdDataSearchingTabLayout idDatasearchingLayout = new IdDataSearchingTabLayout(subSearchIdentificationProtList, handler);
                    Tab tab = mainTabSheet.addTab(idDatasearchingLayout, protName, null);
                    tab.setClosable(true);
                    mainTabSheet.setSelectedTab(tab);

                }
            }

        };

        quantLayoutListener = new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                if (event == null || event.getClickedComponent() == null || ((HorizontalLayout) event.getClickedComponent().getParent()) == null) {
                    return;
                }
                String protName = ((HorizontalLayout) event.getClickedComponent().getParent()).getData().toString();
                if (protName.equalsIgnoreCase("Load All")) {
                    QuantDataSearchingTabLayout quantDatasearchingLayout = new QuantDataSearchingTabLayout(searchQuantificationProtList, handler);

                    Tab tab = mainTabSheet.addTab(quantDatasearchingLayout, "All Proteins Quantitative Data Results", null);
                    tab.setClosable(true);
                    mainTabSheet.setSelectedTab(tab);

                } else {
                    List<QuantProtein> subSearchQuantitativeProtList = new ArrayList<QuantProtein>();
                    for (QuantProtein quantProt : searchQuantificationProtList) {
                        if (quantProt.getUniprotProteinName().trim().toLowerCase().equalsIgnoreCase(protName.trim().toLowerCase())) {
                            subSearchQuantitativeProtList.add(quantProt);
                        }
                    }
                    QuantDataSearchingTabLayout quantDatasearchingLayout = new QuantDataSearchingTabLayout(subSearchQuantitativeProtList, handler);
                    Tab tab = mainTabSheet.addTab(quantDatasearchingLayout, protName, null);
                    tab.setClosable(true);
                    mainTabSheet.setSelectedTab(tab);

                }
            }

        };
 String infoText="Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.";

        HideOnClickLayout searchingLayoutContainer = new HideOnClickLayout("Searching Filters", searchingUnitLayout, null,infoText);
        this.addComponent(searchingLayoutContainer);
        searchingLayoutContainer.setVisability(true);

        VerticalLayout searchingResultsOverview = new VerticalLayout();
        searchingResultsOverview.setHeightUndefined();
        searchingResultsOverview.setSpacing(true);
        searchingResultsOverview.setMargin(new MarginInfo(true, false, true, false));
        this.addComponent(searchingResultsOverview);

        Label searchingResultsTitle = new Label("<h4 style='font-family:verdana;color:black;'> Search Results </h4>");
        searchingResultsTitle.setContentMode(ContentMode.HTML);
        searchingResultsOverview.addComponent(searchingResultsTitle);

        idResultsOverview = new Button();
        idResultsOverview.setStyleName(Reindeer.BUTTON_LINK);
        idResultsOverview.setHeight("20px");
        searchingResultsOverview.addComponent(idResultsOverview);

        idResultsOverview.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                quantProteinsDataLayoutContainer.setVisability(false);
                idProteinsDataLayoutContainer.setVisability(true);
            }
        });

        quantResultsOverview = new Button();
        quantResultsOverview.setStyleName(Reindeer.BUTTON_LINK);
        quantResultsOverview.setHeight("20px");
        searchingResultsOverview.addComponent(quantResultsOverview);

        quantResultsOverview.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                quantProteinsDataLayoutContainer.setVisability(true);
                idProteinsDataLayoutContainer.setVisability(false);
            }
        });

        VerticalLayout searchingResultsLayout = new VerticalLayout();
        searchingResultsLayout.setMargin(new MarginInfo(true, false, false, false));
        searchingResultsLayout.setSpacing(true);
        this.addComponent(searchingResultsLayout);

        idProteinsDataLayout = new VerticalLayout();
        idProteinsDataLayout.setSpacing(true);
        idProteinsDataLayout.setWidthUndefined();
       
        idProteinsDataLayoutContainer = new HideOnClickLayout("Proteins Identification Data", idProteinsDataLayout, null, Alignment.MIDDLE_LEFT,infoText);
        searchingResultsLayout.addComponent(idProteinsDataLayoutContainer);
        idProteinsDataLayoutContainer.setVisability(true);
        idProteinsDataLayoutContainer.setVisible(false);

        VerticalLayout line = new VerticalLayout();
        line.setWidth("100%");
        line.setHeight("2px");
        line.setStyleName(Reindeer.LAYOUT_WHITE);
        line.setMargin(new MarginInfo(true, false, false, false));
        searchingResultsLayout.addComponent(line);

        quantProteinsDataLayout = new VerticalLayout();
        quantProteinsDataLayout.setSpacing(true);
        quantProteinsDataLayout.setWidthUndefined();
        quantProteinsDataLayoutContainer = new HideOnClickLayout("Proteins Quantitative Data", quantProteinsDataLayout, null, Alignment.MIDDLE_LEFT,infoText);
        searchingResultsLayout.addComponent(quantProteinsDataLayoutContainer);
        quantProteinsDataLayoutContainer.setVisability(true);
        quantProteinsDataLayoutContainer.setVisible(false);
        
        
         String requestSearching = VaadinService.getCurrentRequest().getPathInfo();
        if (!requestSearching.trim().endsWith("/")) {
            requestSearching = requestSearching.replace("/", "");
            searchingUnitLayout.searchingProcess(requestSearching);
        } 


    }
    private Map<Integer, IdentificationProteinBean> searchIdentificationProtList;

    @Override
    public void buttonClick(Button.ClickEvent event) {

        boolean validQuery = searchingUnitLayout.isValidQuery();
        if (searchingUnitLayout.getSearchingByValue() == null || searchingUnitLayout.getSearchingByValue().equalsIgnoreCase("")) {
            return;
        }
        if (validQuery) {
            Query query = new Query();
            query.setValidatedProteins(false);
            query.setSearchDataset("");
            query.setSearchKeyWords(searchingUnitLayout.getSearchingKeywords());
            query.setSearchBy(searchingUnitLayout.getSearchingByValue());
            query.setSearchDataType("Identification Data");

            //searching id data
            String idNotFound;
            String defaultText = query.getSearchKeyWords();
            defaultText = defaultText.replace(",", "\n").replace(" ", "").trim();
            searchingUnitLayout.setSearchingFieldValue(defaultText);
            searchIdentificationProtList = handler.searchIdentificationProtein(query);
            idNotFound = handler.filterIdSearchingKeywords(searchIdentificationProtList, query.getSearchKeyWords(), query.getSearchBy());
            Map<String, Integer> idHitsList = handler.getIdHitsList(searchIdentificationProtList, query.getSearchBy(),query.getSearchKeyWords());
            initProteinsIdDataLayout(idHitsList, query.getSearchBy(), searchIdentificationProtList.size(), query.getSearchKeyWords());

            //searching quant data
            query.setSearchDataType("Quantification Data");

            searchQuantificationProtList = handler.searchQuantificationProtein(query);
            String quantNotFound = handler.filterQuantSearchingKeywords(searchQuantificationProtList, query.getSearchKeyWords(), query.getSearchBy());
            Map<String, Integer> quantHitsList = handler.getQuantHitsList(searchQuantificationProtList, query.getSearchBy());
            if (quantHitsList != null && searchQuantificationProtList != null) {
                initProteinsQuantDataLayout(quantHitsList, query.getSearchBy(), searchQuantificationProtList.size(), query.getSearchKeyWords());
            }
            //not found error message
            String notFound = idNotFound.toUpperCase() + "," + quantNotFound.toUpperCase();
            Set<String> notFoundSet = new HashSet<String>();
            for (String str : notFound.split(",")) {
                if (idNotFound.toUpperCase().contains(str.trim()) && quantNotFound.toUpperCase().contains(str.trim())) {
                    notFoundSet.add(str);
                }
            }

            initializeNotFoundMessage(notFoundSet.toString().replace("[", "").replace("]", ""));
        }
    }

    /**
     * initialize identification searching results layout
     *
     * @param idHitsList map of hits and main protein title
     * @param searchBy the searching by method
     * @param totalProtNum total number of hits
     * @param keywords the keywords used for the searching
     *
     */
    private void initProteinsIdDataLayout(Map<String, Integer> idHitsList, String searchBy, int totalProtNum, String keywords) {
        if (idHitsList == null || idHitsList.isEmpty()) {
            idProteinsDataLayout.setVisible(false);
            idProteinsDataLayoutContainer.setVisible(false);
            idResultsOverview.setVisible(false);
            return;
        }
        idResultsOverview.setCaption("Proteins Identification Data ( #Protein Groups " + idHitsList.size() + " | #Hits " + totalProtNum + " )");
        idResultsOverview.setVisible(true);
        idProteinsDataLayout.removeAllComponents();
        idProteinsDataLayoutContainer.updateTitleLabel("Proteins Identification Data ( #Protein Groups " + idHitsList.size() + " | #Hits " + totalProtNum + " )");
        idProteinsDataLayoutContainer.setVisible(true);
        idProteinsDataLayout.setVisible(true);
        if (idHitsList.size() > 1) {
            HorizontalLayout loadAllProtLabelLayout = generateLabel(searchBy, null, null, "Load All", null, totalProtNum);
            loadAllProtLabelLayout.addLayoutClickListener(idLayoutListener);
            idProteinsDataLayout.addComponent(loadAllProtLabelLayout);
        }

        String[] keywordsArr = keywords.split("\n");
        Map<String, VerticalLayout> layoutMap = new HashMap<String, VerticalLayout>();

        if (searchBy.equalsIgnoreCase("Peptide Sequence")) {
            keywordsArr = new String[idHitsList.size()];
            int index = 0;
            for (String keyword : idHitsList.keySet()) {
                keywordsArr[index++] = keyword;
            }
        }
        for (String keywordsArr1 : keywordsArr) {
            VerticalLayout spacer = new VerticalLayout();
            spacer.setStyleName(Reindeer.LAYOUT_WHITE);
            spacer.setMargin(new MarginInfo(false, true, false, true));
            spacer.setHeight("20px");
            Label l = new Label("<h4 style='text-decoration: underline;'><font color=\"gray\">" + keywordsArr1.toUpperCase() + "</font></h4>");
            spacer.addComponent(l);
            l.setContentMode(ContentMode.HTML);
            idProteinsDataLayout.addComponent(spacer);
            VerticalLayout vlo = new VerticalLayout();
            layoutMap.put(keywordsArr1, vlo);
            idProteinsDataLayout.addComponent(vlo);

        }

        for (String key : idHitsList.keySet()) {
            if (searchBy.equalsIgnoreCase("Protein Name") || searchBy.equalsIgnoreCase("Peptide Sequence")) {
                HorizontalLayout protLabelLayout = generateLabel(searchBy, null, null, key, null, idHitsList.get(key));
                protLabelLayout.addLayoutClickListener(idLayoutListener);
                for (String keyWord : layoutMap.keySet()) {
                    if (key.toUpperCase().contains(keyWord.toUpperCase())) {
                        layoutMap.get(keyWord).addComponent(protLabelLayout);
                        break;
                    }

                }
            } else if (searchBy.equalsIgnoreCase("Protein Accession")) {
                HorizontalLayout protLabelLayout = generateLabel(searchBy, key.split("__")[0], key.split("__")[1], key.split("__")[2], null, idHitsList.get(key));
                protLabelLayout.addLayoutClickListener(idLayoutListener);
                for (String keyWord : layoutMap.keySet()) {

                    if (key.toUpperCase().contains(keyWord.toUpperCase())) {
                        layoutMap.get(keyWord).addComponent(protLabelLayout);
                        break;
                    }

                }
            }

        }
        int counter = layoutMap.size();
        for (String keymap : layoutMap.keySet()) {
            VerticalLayout sectionLayout = layoutMap.get(keymap);
            if (sectionLayout.getComponentCount() == 0) {
                int index = idProteinsDataLayout.getComponentIndex(sectionLayout);
                idProteinsDataLayout.getComponent(index - 1).setVisible(false);
                sectionLayout.setVisible(false);
                counter--;
            }

        }
        if (counter < 2) {
            int index = idProteinsDataLayout.getComponentIndex(layoutMap.values().iterator().next());//.setStyleName(Reindeer.LAYOUT_BLACK);//setVisible(false);
            idProteinsDataLayout.getComponent(index - 1).setVisible(false);
        }
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
    private void initProteinsQuantDataLayout(Map<String, Integer> quantHitsList, String searchBy, int totalProtNum, String keywords) {
        if (quantHitsList == null || quantHitsList.isEmpty()) {
            quantProteinsDataLayout.setVisible(false);
            quantProteinsDataLayoutContainer.setVisible(false);
            quantResultsOverview.setVisible(false);
            return;
        }
        quantResultsOverview.setCaption("Proteins Quantitative Data ( #Protein Groups " + quantHitsList.size() + " | #Hits " + totalProtNum + " )");
        quantResultsOverview.setVisible(true);
        quantProteinsDataLayout.removeAllComponents();
        quantProteinsDataLayoutContainer.updateTitleLabel("Proteins Quantitative Data ( #Protein Groups " + quantHitsList.size() + " | #Hits " + totalProtNum + " )");
        quantProteinsDataLayoutContainer.setVisible(true);
        quantProteinsDataLayout.setVisible(true);
        if (quantHitsList.size() > 1) {
            HorizontalLayout loadAllProtLabelLayout = generateLabel(searchBy, null, null, "Load All", null, totalProtNum);
            loadAllProtLabelLayout.addLayoutClickListener(quantLayoutListener);
            quantProteinsDataLayout.addComponent(loadAllProtLabelLayout);
        }

        String[] keywordsArr = keywords.split("\n");
        Map<String, VerticalLayout> layoutMap = new HashMap<String, VerticalLayout>();
        if (searchBy.equalsIgnoreCase("Peptide Sequence")) {
            keywordsArr = new String[quantHitsList.size()];
            int index = 0;
            for (String keyword : quantHitsList.keySet()) {
                keywordsArr[index++] = keyword;
            }
        }

        for (String keywordsArr1 : keywordsArr) {
            VerticalLayout spacer = new VerticalLayout();
            spacer.setStyleName(Reindeer.LAYOUT_WHITE);
            spacer.setMargin(new MarginInfo(false, true, false, true));
            spacer.setHeight("20px");
            Label l = new Label("<h4 style='text-decoration: underline;'><font color=\"gray\">" + keywordsArr1.toUpperCase() + "</font></h4>");
            spacer.addComponent(l);
            l.setContentMode(ContentMode.HTML);
            quantProteinsDataLayout.addComponent(spacer);
            VerticalLayout vlo = new VerticalLayout();
            layoutMap.put(keywordsArr1, vlo);
            quantProteinsDataLayout.addComponent(vlo);

        }

        for (String key : quantHitsList.keySet()) {
            if (searchBy.equalsIgnoreCase("Protein Name") || searchBy.equalsIgnoreCase("Peptide Sequence")) {
                HorizontalLayout protLabelLayout = generateLabel(searchBy, null, null, key, null, quantHitsList.get(key));
                protLabelLayout.addLayoutClickListener(quantLayoutListener);
                for (String keyWord : layoutMap.keySet()) {
                    if (key.toUpperCase().contains(keyWord.toUpperCase())) {
                        layoutMap.get(keyWord).addComponent(protLabelLayout);
                        break;
                    }
                }
            } else if (searchBy.equalsIgnoreCase("Protein Accession")) {
                HorizontalLayout protLabelLayout = generateLabel(searchBy, key.split("__")[0], null, key.split("__")[1], null, quantHitsList.get(key));
                protLabelLayout.addLayoutClickListener(quantLayoutListener);
                for (String keyWord : layoutMap.keySet()) {

                    if (key.toUpperCase().contains(keyWord.toUpperCase())) {
                        layoutMap.get(keyWord).addComponent(protLabelLayout);
                        break;
                    }

                }
            }

        }
        int counter = layoutMap.size();
        for (String keymap : layoutMap.keySet()) {
            VerticalLayout sectionLayout = layoutMap.get(keymap);
            if (sectionLayout.getComponentCount() == 0) {
                int index = quantProteinsDataLayout.getComponentIndex(sectionLayout);
                quantProteinsDataLayout.getComponent(index - 1).setVisible(false);
                sectionLayout.setVisible(false);
                counter--;
            }

        }
        if (counter < 2) {
            int index = quantProteinsDataLayout.getComponentIndex(layoutMap.values().iterator().next());//.setStyleName(Reindeer.LAYOUT_BLACK);//setVisible(false);
            quantProteinsDataLayout.getComponent(index - 1).setVisible(false);
        }
    }

    /**
     * no searching results found
     *
     * @param notFound
     */
    private void initializeNotFoundMessage(String notFound) {
        searchingUnitLayout.updateErrorLabelIIValue(notFound);
    }

    private HorizontalLayout generateLabel(String type, String acc, String otherProt, String name, String peptidesSeq, int number) {
        HorizontalLayout labelLayout = new HorizontalLayout();
        labelLayout.setData(name);
        labelLayout.setStyleName("proteinsLinkLabel");
        if (name.equalsIgnoreCase("Load All")) {
            name = "<b>Load All</b>";
        }
        labelLayout.setDescription("Click to Load Proteins");
        labelLayout.setMargin(new MarginInfo(true, false, false, true));
        labelLayout.setSpacing(true);
        if (type.equalsIgnoreCase("Protein Accession") && !name.equalsIgnoreCase("<b>Load All</b>")) {
            String str = acc;
            if (otherProt != null && !otherProt.equalsIgnoreCase("")) {
                str += " ( " + otherProt + " )";
            }
            str += " - ";
            Label accLable = new Label(str);
            accLable.setContentMode(ContentMode.HTML);
            labelLayout.addComponent(accLable);
        }
        Label nameLable = new Label("" + name + "");
        Label numberLabel = new Label("<font color=\"blue\">(" + number + ")</font>");
        nameLable.setContentMode(ContentMode.HTML);
        labelLayout.addComponent(nameLable);
        numberLabel.setContentMode(ContentMode.HTML);
        labelLayout.addComponent(numberLabel);
        return labelLayout;
    }

}
