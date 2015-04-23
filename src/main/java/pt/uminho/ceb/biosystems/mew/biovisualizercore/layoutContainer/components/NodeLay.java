package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.components;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;

/**
 * 
 * Implementation of the interface {@link INodeLay}.
 * @author Alberto Noronha
 *
 */
public class NodeLay implements INodeLay, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String id;
	protected String label;
	protected Set<String> ids;
	protected NodeTypeLay nodeTYPE;
	protected Double x,y;
	protected Double width, height;
	
	public NodeLay(String id, String label, Set<String> ids, NodeTypeLay nodeType, Double x, Double y){
		this.id = id;
		this.label = label;
		this.ids = ids;
		this.nodeTYPE = nodeType;
		this.x = x;
		this.y = y;
	}
	
	public NodeLay clone(){
		return new NodeLay(this.id, this.label, new HashSet<>(this.ids), this.nodeTYPE, this.x, this.y);
	}

	@Override
	public String getUniqueId() {
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public Set<String> getIds() {
		return ids;
	}

	@Override
	public NodeTypeLay getNodeType() {
		return nodeTYPE;
	}

	@Override
	public Double getX() {
		return x;
	}

	@Override
	public Double getY() {
		return y;
	}

	@Override
	public void setX(Double x) {
		this.x = x;
		
	}

	@Override
	public void setY(Double y) {
		this.y = y;
		
	}

	@Override
	public void setNodeType(NodeTypeLay nodeType) {
		this.nodeTYPE = nodeType;
	}

	@Override
	public void setLabel(String string) {
		label = string;
		
	}
}
