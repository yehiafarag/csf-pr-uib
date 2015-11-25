/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util.vaadintoimageutil.peptideslayout;

import java.io.Serializable;
import java.util.List;
import probe.com.view.core.jfreeutil.StackedBarPeptideComponent;

/**
 *
 * @author yfa041
 */
public class StudyInfoData implements Serializable{
    
    private String title;
    private String subTitle;
    private int trend;
    private int coverageWidth;
    private List<StackedBarPeptideComponent> peptidesInfoList;

    public int getLevelsNumber() {
        return levelsNumber;
    }

    public void setLevelsNumber(int levelsNumber) {
        this.levelsNumber = levelsNumber;
    }
    private int levelsNumber;

    public String getTitle() {
        return title;
    }

    public int getCoverageWidth() {
        return coverageWidth;
    }

    public void setCoverageWidth(int coverageWidth) {
        this.coverageWidth = coverageWidth;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getTrend() {
        return trend;
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

    public List<StackedBarPeptideComponent> getPeptidesInfoList() {
        return peptidesInfoList;
    }

    public void setPeptidesInfoList(List<StackedBarPeptideComponent> peptidesInfoList) {
        this.peptidesInfoList = peptidesInfoList;
    }
    
}
