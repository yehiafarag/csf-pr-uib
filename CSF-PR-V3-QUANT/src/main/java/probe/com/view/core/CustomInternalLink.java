package probe.com.view.core;

import java.io.Serializable;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class CustomInternalLink extends VerticalLayout implements Serializable,Comparable<CustomInternalLink> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String expName;
	private Integer key;

    /**
     *
     * @param expName
     * @param key
     */
    public CustomInternalLink(String expName,int key)
	{
		Label l = new Label(expName);
		this.addComponent(l);
		this.expName = expName;
		this.key = key;
	}
	
	public String toString()
	{
		return expName;
	}

    /**
     *
     * @return
     */
    public Integer getKey()
	{
		return key;
	}

    /**
     *
     * @return
     */
    public String getExpName()
	{
		return this.expName;
	}
        @Override
	public int compareTo(CustomInternalLink o) {
		String expNameToCompare = o.expName;
		return this.expName.compareTo(expNameToCompare);
	}
	
	

}
