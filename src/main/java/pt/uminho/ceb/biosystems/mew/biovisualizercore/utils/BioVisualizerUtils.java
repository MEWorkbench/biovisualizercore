package pt.uminho.ceb.biosystems.mew.biovisualizercore.utils;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeTypeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;

public class BioVisualizerUtils {

	
	public static void addLayoutToMapMetabolite(String name, ILayout layout, Map<String, Set<String>> metabolitesInPathway){
		Set<String> addedNode = new HashSet<String>();
		for (INodeLay n : layout.getNodes().values()) {
			if (n.getNodeType().equals(NodeTypeLay.METABOLITE)) for (String s : n.getIds()) {
				
				if (!addedNode.contains(s)) {
					
					Set<String> set = metabolitesInPathway.get(s);
					if (set == null) {
						set = new TreeSet<String>();
						metabolitesInPathway.put(s, set);
					}
					set.add(name);
					addedNode.add(s);
				}
			}
		}
	}
	
	public static void remLayoutToMapMetabolite(String name, Map<String, Set<String>> metabolitesInPathway){
		Set<String> metToDel = new HashSet<String>();
		for (String id : metabolitesInPathway.keySet()) {
			Set<String> set = metabolitesInPathway.get(id);
			set.remove(name);
			if (set.isEmpty()) metToDel.add(id);
		}
		metabolitesInPathway.keySet().removeAll(metToDel);
	}
	
	static public Map<String, Set<String>> getMetabolitesByPathway(Map<String, ILayout> info) {
		Map<String, Set<String>> teste = new HashMap<String, Set<String>>();
		
		for (String idLayout : info.keySet()) {
			
			ILayout layout = info.get(idLayout);
			for (INodeLay n : layout.getNodes().values()) {
				for (String metId : n.getIds()) {
					
					Set<String> paths = teste.get(metId);
					if (paths == null) {
						paths = new HashSet<String>();
						teste.put(metId, paths);
					}
					
					paths.add(idLayout);
				}
			}
		}
		return teste;
	}
	
	static public void addOverLap(String type, IOverlapObject simOverlap, Map<String, Map<String, IOverlapObject>> overlapsMap) {
		
		if(overlapsMap.get(type)==null){
			Map<String, IOverlapObject> overlaps = new HashMap<String, IOverlapObject>();
			overlaps.put(simOverlap.getName(), simOverlap);
			overlapsMap.put(type, overlaps);
		}
		else{
			overlapsMap.get(type).put(simOverlap.getName(), simOverlap);
		}
			
		
	}

	public static Point getMaxPoint(ILayout lay) {
		
		double x = -Double.MAX_VALUE;
		double y = -Double.MAX_VALUE;
		
		for(INodeLay node : lay.getNodes().values()){
			Double nx = node.getX();
			if(nx != null && nx > x) x = nx;
			
			Double ny = node.getY();
			if(ny != null && ny > y) y = ny;
			
		}
		
		if(x == -Double.MAX_VALUE) x=0;
		if(y == -Double.MAX_VALUE) y=0;
		
		Point p = new Point();
		p.setLocation(x, y);
		return p;
	}
}
