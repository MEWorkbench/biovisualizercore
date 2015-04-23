package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import javax.swing.JPanel;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapsListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;

public abstract class AbstractInformationPanel extends JPanel implements OverlapsListener{

	private static final long serialVersionUID = 1L;	
	
	public abstract void showNodeInformation(INodeLay node);
	public abstract void showReactionInformation(IReactionLay reaction);
	public abstract void clear();
	public abstract void updateInfo();
}
