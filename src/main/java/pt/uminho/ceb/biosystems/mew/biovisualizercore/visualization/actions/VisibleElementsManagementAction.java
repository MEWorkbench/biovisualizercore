package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.actions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import prefuse.Visualization;
import prefuse.action.GroupAction;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.util.PrefuseLib;
import prefuse.visual.VisualItem;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;

public class VisibleElementsManagementAction extends GroupAction{
	
	private Set<String> invisibleGroups;
	private Set<String> invisibleReactions;
	
	
	public VisibleElementsManagementAction(Visualization vis){
		super(vis);
		this.invisibleGroups = new HashSet<String>();
		this.invisibleReactions = new HashSet<String>();
	}
	
	public synchronized Set<String> getInvisibleGroups() {
		return invisibleGroups;
	}

	public void setInvisibleGroups(Set<String> invisibleGroups) {
		this.invisibleGroups = invisibleGroups;
	}

	public synchronized Set<String> getInvisibleReactions() {
		return invisibleReactions;
	}

	public void setInvisibleReactions(Set<String> invisibleReactions) {
		this.invisibleReactions = invisibleReactions;
	}

	public void addGroupVisualRestriction(String group){
		getInvisibleGroups().add(group);
	}
	
	public void removeGroupVisualRestriction(String g){
		getInvisibleGroups().remove(g);
	}
	
	public void removeReactionVisualRestrictions(String id){
		getInvisibleReactions().remove(id);
	}
	
	public void removeAllFluxVisualRestrictions(){
		this.invisibleReactions = new HashSet<String>();
	}
	
	public void removeAllGroupVisualRestrictions(){
		this.invisibleGroups = new HashSet<String>();
	}
	
	

	@Override
	public void run(double frac) {
		Iterator items;

		synchronized (m_vis) {


			items = m_vis.items(LayoutUtils.EDGES);

			while ( items.hasNext() ) {
				VisualItem item = (VisualItem)items.next();

				if(item!=null && item.canGetString(LayoutUtils.ID)){
					if(reactionIsInvisible((item.getString(LayoutUtils.ID))))
						PrefuseLib.updateVisible(item, false);
					else
						PrefuseLib.updateVisible(item, true);
				}
			}

			items = m_vis.items(LayoutUtils.NODES);
			while ( items.hasNext() ) {
				VisualItem item = (VisualItem)items.next();

				if(!(item==null) && item.getString(LayoutUtils.TYPE)!=null && item.canGetString(LayoutUtils.TYPE)){
					if(isInvisibleGroup(item.getString(LayoutUtils.TYPE))){
						PrefuseLib.updateVisible(item, false);
					}
					else{
						Node node = (Node) item.getSourceTuple();
						
						if(node.getString(LayoutUtils.TYPE)!= null && !node.getString(LayoutUtils.TYPE).equals(LayoutUtils.REA_T)){
							boolean visible = false;
							Iterator e = node.edges();
							while(e.hasNext()){
								Edge edge = (Edge) e.next();
								if(!reactionIsInvisible(edge.getString(LayoutUtils.ID)))
									visible = true;
							}
							PrefuseLib.updateVisible(item, visible);
						}
						else{
							if(item.canGetString(LayoutUtils.ID)){
								String rId = item.getString(LayoutUtils.ID);

								if(reactionIsInvisible(rId)){
									PrefuseLib.updateVisible(item,false);
								}
								else
									PrefuseLib.updateVisible(item, true);
							}
						}
					}
				}
			}
			/**
			 * Groups are invisible need to get edges invisible too
			 */

			items = m_vis.items(LayoutUtils.EDGES);
			while ( items.hasNext() ) {
				VisualItem item = (VisualItem)items.next();
				String edgeType = item.getString(LayoutUtils.TYPE);
				if(edgeType!=null)
					if(edgeTypeInvisible(edgeType))
						PrefuseLib.updateVisible(item, false);
			}

		}
	}

	private boolean edgeTypeInvisible(String edgeType) {

		String nodeType;
		if(edgeType.equals(LayoutUtils.LINK_INFO)) nodeType = LayoutUtils.INFO;
		else if(edgeType.equals(LayoutUtils.LINK_HUBS)) nodeType = LayoutUtils.HUBS;
		else nodeType = LayoutUtils.MET_T;
		
		for(String s : getInvisibleGroups())
			if(s.equals(nodeType))
				return true;
		
		return false;
	}


	private boolean isInvisibleGroup(String string) {
		return getInvisibleGroups().contains(string);					
	}


	private boolean reactionIsInvisible(String rId) {
	
		return getInvisibleReactions().contains(rId);
	}

}
