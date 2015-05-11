package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.expandablePanel.ExpandablePanel;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ExpandableInfoPanel extends ExpandablePanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Icon showIcon = new ImageIcon(ExpandablePanel.class.getClassLoader().getResource("biovisualizer/images/closed.png"));
	static Icon hideIcon = new ImageIcon(ExpandablePanel.class.getClassLoader().getResource("biovisualizer/images/open.png"));

	public ExpandableInfoPanel(OverlapPanel filtersPanel, AbstractInformationPanel information){
		super();
		setIcons(showIcon, hideIcon);

		addComponentWD(filtersPanel.getDefaultFiltersPanel(), "Default Filters", false, true);
		addComponentWD(filtersPanel.getOverlapPanel(), "Overlap Options", false, true);
		addComponentWD(information, "Node Information", true, false);
		this.constructGUI();
	}
}
