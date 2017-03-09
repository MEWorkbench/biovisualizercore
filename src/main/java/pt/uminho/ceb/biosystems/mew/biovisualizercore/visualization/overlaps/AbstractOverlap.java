package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prefuse.util.ColorLib;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;
import pt.uminho.ceb.biosystems.mew.utilities.java.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AbstractOverlap implements IOverlapObject, Serializable {
	
	private static final long						serialVersionUID	= 1L;
	
	public static final String						COLUMN_DELIMITER	= "\t";
	public static final String						INNER_DELIMITER		= ";";
	
	public static final String						MET_ID				= "M_ID";
	public static final String						MET_LABEL			= "M_LABEL";
	public static final String						MET_SHAPE			= "M_SHAPE";
	public static final String						MET_SIZES			= "M_SIZES";
	public static final String						MET_COLOR			= "M_COLOR";
	
	public static final String						REACTION_ID			= "R_ID";
	public static final String						REACTION_LABEL		= "R_LABEL";
	public static final String						REACTION_SHAPE		= "R_SHAPE";
	public static final String						REACTION_SIZES		= "R_SIZES";
	public static final String						REACTION_COLORS		= "R_COLORS";
	public static final String						REACTION_THICKNESS	= "R_THICK";
	public static final String						REACTION_DIRECTIONS	= "R_DIR";
	
	public static final Pattern						FILTER_PATTERN		= Pattern.compile("^FILTER\\[(.+)\\]");
	public static final Pattern						RGBA_PATTERN		= Pattern.compile("\\s*\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)\\s*");
	
	protected String								name;
	
	protected Map<String, String>					nodeLabels;
	protected Map<String, Integer>					nodeShapes;
	protected Map<String, Pair<Double, Double>>		nodeSizes;
	protected Map<String, Integer>					nodeColors;
	
	protected Map<String, String>					reaction_labels;
	protected Map<String, Integer>					reactionShapes;
	protected Map<String, Pair<Double, Double>>		reactionSizes;
	protected Map<String, Pair<Integer, Integer>>	reactionColors;
	protected Map<String, Double>					edgeThickness;
	protected Map<String, Boolean>					positiveReactions;
	protected Map<String, Set<String>>				visFilters;
	
	protected Map<String, Double>					edgesOriginalValues;
	protected Map<String, Double>					nodesOriginalValues;
	
	public AbstractOverlap() {
		this(null);
	}
	
	public AbstractOverlap(String name) {
		
		this.name = name;
		reaction_labels = new HashMap<String, String>();
		nodeLabels = new HashMap<String, String>();
		edgeThickness = new HashMap<String, Double>();
		nodeShapes = new HashMap<String, Integer>();
		reactionShapes = new HashMap<String, Integer>();
		nodeColors = new HashMap<String, Integer>();
		nodeSizes = new HashMap<String, Pair<Double, Double>>();
		reactionColors = new HashMap<String, Pair<Integer, Integer>>();
		visFilters = new HashMap<String, Set<String>>();
		positiveReactions = new HashMap<String, Boolean>();
		reactionSizes = new HashMap<String, Pair<Double, Double>>();
	}
	
	public Map<String, Pair<Double, Double>> getNodeSizes() {
		return nodeSizes;
	}
	
	public void setNodeSizes(Map<String, Pair<Double, Double>> nodeSizes) {
		this.nodeSizes = nodeSizes;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Map<String, String> getNewReactionLabels() {
		return reaction_labels;
	}
	
	@Override
	public Map<String, Double> getEdgeThickness() {
		return edgeThickness;
	}
	
	@Override
	public Map<String, Integer> getNodeShapes() {
		return nodeShapes;
	}
	
	@Override
	public Map<String, Integer> getNodeColors() {
		return nodeColors;
	}
	
	@Override
	public Map<String, Pair<Integer, Integer>> getReactionsColors() {
		return reactionColors;
	}
	
	@Override
	public Map<String, Set<String>> getPossibleInvR() {
		return visFilters;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addVisualFilter(String s, Set<String> filtered) {
		
		this.visFilters.put(s, filtered);
	}
	
	@Override
	public Map<String, Boolean> getFluxDirections() {
		return positiveReactions;
	}
	
	public void setReactionLabels(Map<String, String> labels) {
		this.reaction_labels = labels;
	}
	
	public void setEdgeThickness(Map<String, Double> edgeThickness) {
		this.edgeThickness = edgeThickness;
	}
	
	public void setNodeShapes(Map<String, Integer> nodeShapes) {
		this.nodeShapes = nodeShapes;
	}
	
	public void setNodeColors(Map<String, Integer> nodeColors) {
		this.nodeColors = nodeColors;
	}
	
	public void setReactionColors(Map<String, Pair<Integer, Integer>> reactionColors) {
		this.reactionColors = reactionColors;
	}
	
	public void setVisFilters(Map<String, Set<String>> visFilters) {
		this.visFilters = visFilters;
	}
	
	public void setFluxDirections(Map<String, Boolean> positiveReactions) {
		this.positiveReactions = positiveReactions;
	}
	
	public void addNodeShape(String nodeId, Integer shape) {
		this.nodeShapes.put(nodeId, shape);
		
	}
	
	public void addReactionColor(String nodeId, Pair<Integer, Integer> pair) {
		this.reactionColors.put(nodeId, pair);
	}
	
	public void addNodeColor(String nodeId, Integer color) {
		this.nodeColors.put(nodeId, color);
		
	}
	
	public void addNodeSize(String nodeId, Pair<Double, Double> sizes) {
		this.nodeSizes.put(nodeId, sizes);
	}
	
	public void addReactionSize(String nodeId, Pair<Double, Double> sizes) {
		this.reactionSizes.put(nodeId, sizes);
	}
	
	public void addReactionShape(String nodeId, Integer shape) {
		this.reactionShapes.put(nodeId, shape);
	}
	
	public void addReactionThickness(String nodeId, Double thickness){
		this.edgeThickness.put(nodeId, thickness);
	}
	
	
	@Override
	public Map<String, Integer> getReactionShapes() {
		return reactionShapes;
	}
	
	@Override
	public void setReactionShapes(Map<String, Integer> reactionShapes) {
		this.reactionShapes = reactionShapes;
	}
	
	@Override
	public Map<String, Pair<Double, Double>> getReactionSizes() {
		return reactionSizes;
	}
	
	@Override
	public void setReactionSizes(Map<String, Pair<Double, Double>> reactionSizes) {
		this.reactionSizes = reactionSizes;
		
	}
	
	@Override
	public Map<String, String> getNewNodeLabels() {
		return nodeLabels;
	}
	
	@Override
	public void setNodeLabels(Map<String, String> newLabels) {
		this.nodeLabels = newLabels;
		
	}
	
	public static String color2rgba(Color color) {
		return "(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha() + ")";
	}
	
	public void saveToFile(String fileReactions, String fileMetabolites) throws IOException {
		Set<String> metabolites = new TreeSet<String>();
		metabolites.addAll(nodeLabels.keySet());
		metabolites.addAll(nodeShapes.keySet());
		metabolites.addAll(nodeSizes.keySet());
		metabolites.addAll(nodeColors.keySet());
		
		if (!metabolites.isEmpty()) {
			List<String> include = new ArrayList<String>();
			include.add(MET_ID);
			if (!nodeLabels.isEmpty()) include.add(MET_LABEL);
			if (!nodeShapes.isEmpty()) include.add(MET_SHAPE);
			if (!nodeSizes.isEmpty()) include.add(MET_SIZES);
			if (!nodeColors.isEmpty()) include.add(MET_COLOR);
			
			String headerMetabolites = StringUtils.concat(COLUMN_DELIMITER, include);
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileMetabolites));
			bw.append(headerMetabolites);
			for (String m : metabolites) {
				String[] mline = new String[include.size()];
				int i = 0;
				for (String lab : include) {
					switch (lab) {
						case MET_ID:
							mline[i] = m;
							break;
						case MET_LABEL:
							mline[i] = (nodeLabels.containsKey(m) ? nodeLabels.get(m) : "");
							break;
						case MET_SHAPE:
							mline[i] = (nodeShapes.containsKey(m) ? nodeShapes.get(m).toString() : "");
							break;
						case MET_SIZES:
							mline[i] = (nodeSizes.containsKey(m) ? nodeSizes.get(m).toString(INNER_DELIMITER) : "");
							break;
						case MET_COLOR:
							if (nodeColors.containsKey(m)) {
								Integer colorCode = nodeColors.get(m);
								Color color = ColorLib.getColor(colorCode);
								mline[i] = color2rgba(color);
							} else
								mline[i] = "";
							break;
						default:
							break;
					}
					i++;
				}
				bw.newLine();
				bw.append(StringUtils.concat(COLUMN_DELIMITER, mline));
			}
			bw.flush();
			bw.close();
		}
		
		Set<String> reactions = new TreeSet<String>();
		reactions.addAll(reaction_labels.keySet());
		reactions.addAll(reactionShapes.keySet());
		reactions.addAll(reactionSizes.keySet());
		reactions.addAll(reactionColors.keySet());
		reactions.addAll(edgeThickness.keySet());
		reactions.addAll(positiveReactions.keySet());
		
		if (!reactions.isEmpty()) {
			
			List<String> include = new ArrayList<String>();
			include.add(REACTION_ID);
			if (!reaction_labels.isEmpty()) include.add(REACTION_LABEL);
			if (!reactionShapes.isEmpty()) include.add(REACTION_SHAPE);
			if (!reactionSizes.isEmpty()) include.add(REACTION_SIZES);
			if (!reactionColors.isEmpty()) include.add(REACTION_COLORS);
			if (!edgeThickness.isEmpty()) include.add(REACTION_THICKNESS);
			if (!positiveReactions.isEmpty()) include.add(REACTION_DIRECTIONS);
			if (!visFilters.isEmpty()) {
				for (String filter : visFilters.keySet())
					include.add("FILTER[" + filter + "]");
			}
			
			String headerReactions = StringUtils.concat(COLUMN_DELIMITER, include);
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileReactions));
			bw.append(headerReactions);
			for (String r : reactions) {
				String[] rline = new String[include.size()];
				int i = 0;
				for (String lab : include) {
					Matcher m = FILTER_PATTERN.matcher(lab);
					if (lab.equals(REACTION_ID))
						rline[i] = r;
					else if (lab.equals(REACTION_LABEL))
						rline[i] = (reaction_labels.containsKey(r) ? reaction_labels.get(r) : "");
					else if (lab.equals(REACTION_SHAPE))
						rline[i] = (reactionShapes.containsKey(r) ? reactionShapes.get(r).toString() : "");
					else if (lab.equals(REACTION_SIZES))
						rline[i] = (reactionSizes.containsKey(r) ? reactionSizes.get(r).toString(INNER_DELIMITER) : "");
					else if (lab.equals(REACTION_COLORS)) {
						if (reactionColors.containsKey(r)) {
							Integer colorCodeA = reactionColors.get(r).getA();
							Color colorA = ColorLib.getColor(colorCodeA);
							String colorStringA = color2rgba(colorA);
							
							Integer colorCodeB = reactionColors.get(r).getB();
							Color colorB = ColorLib.getColor(colorCodeB);
							String colorStringB = color2rgba(colorB);
							
							rline[i] = colorStringA + INNER_DELIMITER + colorStringB;
						} else
							rline[i] = "";
					} else if (lab.equals(REACTION_THICKNESS))
						rline[i] = (edgeThickness.containsKey(r) ? edgeThickness.get(r).toString() : "");
					else if (lab.equals(REACTION_DIRECTIONS))
						rline[i] = (positiveReactions.containsKey(r) ? positiveReactions.get(r).toString() : "");
					else if (m.matches()) {
						String name = m.group(1);
						rline[i] = (visFilters.get(name).contains(r) ? "true" : "");
					}
					i++;
				}
				bw.newLine();
				bw.append(StringUtils.concat(COLUMN_DELIMITER, rline));
			}
			bw.flush();
			bw.close();
		}
	}
	
	public void loadFromFile(String reactionsFile, String metabolitesFile) throws IOException, GenericOverlapIOException {
		if (metabolitesFile == null && reactionsFile == null)
			throw new GenericOverlapIOException("At least one file [metabolites] or [reactions] must be selected.");
		else {
			loadMetabolitesFile(metabolitesFile);
			loadReactionsFile(reactionsFile);
		}
	}
	
	private void loadReactionsFile(String reactionsFile) throws IOException, GenericOverlapIOException {
		if (reactionsFile != null && new File(reactionsFile).exists()) {
			
			List<String> validHeaders = new ArrayList<String>();
			validHeaders.add(REACTION_ID);
			validHeaders.add(REACTION_LABEL);
			validHeaders.add(REACTION_SHAPE);
			validHeaders.add(REACTION_SIZES);
			validHeaders.add(REACTION_COLORS);
			validHeaders.add(REACTION_THICKNESS);
			validHeaders.add(REACTION_DIRECTIONS);
			validHeaders.add(FILTER_PATTERN.pattern());
			
			BufferedReader br = new BufferedReader(new FileReader(reactionsFile));
			List<String> reactionsHeader = CollectionUtils.convertPrimitiveArrayToList(br.readLine().split(COLUMN_DELIMITER));
			boolean allvalid = true;
			for (String rheader : reactionsHeader) {
				boolean valid = false;
				for (String h : validHeaders) {
					if (rheader.matches(h)) {
						valid = true;
						break;
					}
				}
				if (!valid) {
					br.close();
					allvalid = false;
					throw new GenericOverlapIOException("Invalid header(s) in reactions file. Expected any of " + validHeaders.toString() + " but got " + rheader);
					
				}
			}
			if (allvalid) {
				int index_LABEL = reactionsHeader.indexOf(REACTION_LABEL);
				int index_SHAPE = reactionsHeader.indexOf(REACTION_SHAPE);
				int index_SIZES = reactionsHeader.indexOf(REACTION_SIZES);
				int index_COLOR = reactionsHeader.indexOf(REACTION_COLORS);
				int index_THICK = reactionsHeader.indexOf(REACTION_THICKNESS);
				int index_DIREC = reactionsHeader.indexOf(REACTION_DIRECTIONS);
				List<Integer> indexes_FILTERS = new ArrayList<Integer>();
				for (int i = 0; i < reactionsHeader.size(); i++) {
					String rheader = reactionsHeader.get(i);
					Matcher m = FILTER_PATTERN.matcher(rheader);
					if (m.matches()) indexes_FILTERS.add(i);
				}
				while (br.ready()) {
					String l = br.readLine();
					String[] line = l.split(COLUMN_DELIMITER, reactionsHeader.size());
					int index_ID = reactionsHeader.indexOf(REACTION_ID);
					if (index_ID == -1) {
						br.close();
						throw new GenericOverlapIOException("Invalid reactions file. Must contain a [" + REACTION_ID + "] column relative to the reactions ID's.");
					} else {
						String id = line[index_ID];
						String label = null;
						Integer shape = null;
						Pair<Double, Double> sizes = null;
						Pair<Integer, Integer> colors = null;
						Double thickness = null;
						Boolean direction = null;
						
						if (index_LABEL != -1) label = line[index_LABEL];
						if (index_SHAPE != -1) {
							String shapeString = line[index_SHAPE];
							if (shapeString != null && !shapeString.isEmpty()) {
								shape = Integer.parseInt(shapeString);
								if (shape < 0 || shape > 14) {
									br.close();
									throw new GenericOverlapIOException("Invalid reactions file at line [" + id + "] column [" + REACTION_SHAPE + " ]. Shape values must be in the range [0,14]. Please consult manual for int code to shape mappings.");
								}
							}
						}
						if (index_SIZES != -1) {
							String sizeString = line[index_SIZES];
							if (!sizeString.isEmpty()) {
								String[] sizeTokens = sizeString.split(INNER_DELIMITER);
								if (sizeTokens.length > 0 && sizeTokens.length != 2) {
									br.close();
									throw new GenericOverlapIOException("Invalid reactions file at reaction [" + id + "]. Elements in sizes column [" + REACTION_SIZES + "] must contain two double values separated by [" + INNER_DELIMITER
											+ "]. Element was [" + sizeString + "]");
								} else if (sizeTokens.length == 2) {
									double a = Double.parseDouble(sizeTokens[0]);
									double b = Double.parseDouble(sizeTokens[1]);
									sizes = new Pair<Double, Double>(a, b);
								}
							}
						}
						if (index_COLOR != -1) {
							String colorsString = line[index_COLOR];
							
							if (!colorsString.isEmpty()) {
								
								String[] colorsTokens = colorsString.split(INNER_DELIMITER);
								if (colorsTokens.length > 0 && colorsTokens.length != 2) {
									br.close();
									throw new GenericOverlapIOException("Invalid reactions file at reaction [" + id + "]. Elements in colors column [" + REACTION_COLORS
											+ "] must contain two RGBA values [(RRR,GGG,BBB,AAA),(RRR,GGG,BBB,AAA)] separated by [" + INNER_DELIMITER + "]. Element was [" + Arrays.toString(colorsTokens) + "]");
								} else {
									String colorStringA = colorsTokens[0];
									String colorStringB = colorsTokens[1];
									int colorCodeA = -1;
									int colorCodeB = -1;
									Matcher m = RGBA_PATTERN.matcher(colorStringA);
									if (m.matches()) {
										int[] rgba = new int[4];
										rgba[0] = Integer.parseInt(m.group(1));
										rgba[1] = Integer.parseInt(m.group(2));
										rgba[2] = Integer.parseInt(m.group(3));
										rgba[3] = Integer.parseInt(m.group(4));
										for (int x : rgba)
											if (x > 255 || x < 0) {
												br.close();
												throw new GenericOverlapIOException("Invalid reactions file at reaction [" + id + "], column [" + REACTION_COLORS + "], first color [" + colorStringA + "]. All values bust be in the range [0,255].");
											}
										colorCodeA = ColorLib.rgba(rgba[0], rgba[1], rgba[2], rgba[3]);
									}
									m = RGBA_PATTERN.matcher(colorStringB);
									if (m.matches()) {
										int[] rgba = new int[4];
										rgba[0] = Integer.parseInt(m.group(1));
										rgba[1] = Integer.parseInt(m.group(2));
										rgba[2] = Integer.parseInt(m.group(3));
										rgba[3] = Integer.parseInt(m.group(4));
										for (int x : rgba)
											if (x > 255 || x < 0) {
												br.close();
												throw new GenericOverlapIOException("Invalid reactions file at reaction [" + id + "], column [" + REACTION_COLORS + "], second color [" + colorStringB + "]. All values bust be in the range [0,255].");
											}
										colorCodeB = ColorLib.rgba(rgba[0], rgba[1], rgba[2], rgba[3]);
									}
									
									colors = new Pair<Integer, Integer>(colorCodeA, colorCodeB);
								}
							}
						}
						if (index_THICK != -1) {
							String thickString = line[index_THICK];
							if (thickString != null && !thickString.isEmpty()) {
								thickness = Double.parseDouble(thickString);
							}
						}
						if (index_DIREC != -1) {
							String dirString = line[index_DIREC];
							if (dirString != null && !dirString.isEmpty()) {
								direction = Boolean.parseBoolean(dirString);
							}
						}
						if (!indexes_FILTERS.isEmpty()) {
							for (int index : indexes_FILTERS) {
								String filt_index_string = line[index];
								if (filt_index_string != null && !filt_index_string.isEmpty()) {
									boolean filt_value = Boolean.parseBoolean(filt_index_string.trim());
									if (filt_value) {
										Matcher m = FILTER_PATTERN.matcher(reactionsHeader.get(index));
										if (m.matches()) {
											String filter = m.group(1);
											if (visFilters.containsKey(filter))
												visFilters.get(filter).add(id);
											else {
												HashSet<String> set = new HashSet<String>();
												set.add(id);
												visFilters.put(filter, set);
											}
										}
									}
								}
								
							}
						}
						
						if (label != null) reaction_labels.put(id, label);
						if (shape != null) reactionShapes.put(id, shape);
						if (sizes != null) reactionSizes.put(id, sizes);
						if (colors != null) reactionColors.put(id, colors);
						if (thickness != null) edgeThickness.put(id, thickness);
						if (direction != null) positiveReactions.put(id, direction);
						
					}
				}
			}
			br.close();
		}
	}
	
	private void loadMetabolitesFile(String metabolitesFile) throws IOException, GenericOverlapIOException {
		if (metabolitesFile != null && new File(metabolitesFile).exists()) {
			
			List<String> validHeaders = new ArrayList<String>();
			validHeaders.add(MET_ID);
			validHeaders.add(MET_LABEL);
			validHeaders.add(MET_SHAPE);
			validHeaders.add(MET_SIZES);
			validHeaders.add(MET_COLOR);
			BufferedReader br = new BufferedReader(new FileReader(metabolitesFile));
			List<String> metabolitesHeader = CollectionUtils.convertPrimitiveArrayToList(br.readLine().split(COLUMN_DELIMITER));
			if (!validHeaders.containsAll(metabolitesHeader)) {
				br.close();
				throw new GenericOverlapIOException("Invalid header(s) in metabolites file. Expect any of " + validHeaders.toString() + " but got " + metabolitesHeader.toString());
			} else {
				while (br.ready()) {
					String[] line = br.readLine().split(COLUMN_DELIMITER, metabolitesHeader.size());
					int index_ID = metabolitesHeader.indexOf(MET_ID);
					if (index_ID == -1) {
						br.close();
						throw new GenericOverlapIOException("Invalid metabolites file. Must contain a [" + MET_ID + "] column relative to the metabolite ID's.");
					} else {
						String id = line[index_ID];
						String label = null;
						Integer shape = null;
						Pair<Double, Double> sizes = null;
						Integer color = null;
						int index_LABEL = metabolitesHeader.indexOf(MET_LABEL);
						int index_SHAPE = metabolitesHeader.indexOf(MET_SHAPE);
						int index_SIZES = metabolitesHeader.indexOf(MET_SIZES);
						int index_COLOR = metabolitesHeader.indexOf(MET_COLOR);
						
						if (index_LABEL != -1) label = line[index_LABEL];
						if (index_SHAPE != -1) {
							String shapeString = line[index_SHAPE];
							if (shapeString != null && !shapeString.isEmpty()) {
								shape = Integer.parseInt(shapeString);
								if (shape < 0 || shape > 14) {
									br.close();
									throw new GenericOverlapIOException("Invalid metabolites file at line [" + id + "] column [" + MET_SHAPE + " ]. Shape values must be in the range [0,14]. Please consult manual for int code to shape mappings.");
								}
							}
						}
						if (index_SIZES != -1) {
							String[] sizeString = line[index_SIZES].split(INNER_DELIMITER);
							if (sizeString.length != 2) {
								br.close();
								throw new GenericOverlapIOException("Invalid metabolites file at metabolite [" + id + "]. Elements in sizes column [" + MET_SIZES + "] must contain two double values separated by [" + INNER_DELIMITER + "].");
							} else {
								double a = Double.parseDouble(sizeString[0]);
								double b = Double.parseDouble(sizeString[1]);
								sizes = new Pair<Double, Double>(a, b);
							}
						}
						if (index_COLOR != -1) {
							String colorString = line[index_COLOR];
							if (colorString != null && !colorString.isEmpty()) {
								Matcher m = RGBA_PATTERN.matcher(colorString);
								if (m.matches()) {
									int[] rgba = new int[4];
									rgba[0] = Integer.parseInt(m.group(1));
									rgba[1] = Integer.parseInt(m.group(2));
									rgba[2] = Integer.parseInt(m.group(3));
									rgba[3] = Integer.parseInt(m.group(4));
									for (int x : rgba)
										if (x > 255 || x < 0) {
											br.close();
											throw new GenericOverlapIOException("Invalid metabolites file at metabolite[" + id + "], column [" + MET_COLOR + "] = [" + colorString + "]. All values bust be in the range [0,255].");
										}
									color = ColorLib.rgba(rgba[0], rgba[1], rgba[2], rgba[3]);
								} else {
									br.close();
									throw new GenericOverlapIOException("Invalid metabolites file at metabolite [" + id + "]. Element in colors column [" + MET_COLOR + "] must contain one RGBA value in the format [(RRR,GGG,BBB,AAA)].");
								}
							}
						}
						
						if (label != null) nodeLabels.put(id, label);
						if (shape != null) nodeShapes.put(id, shape);
						if (sizes != null) nodeSizes.put(id, sizes);
						if (color != null) nodeColors.put(id, color);
						
					}
				}
			}
			br.close();
		}
	}
	
	public void saveToXML(String xmlFileReactions, String xmlFileMetabolites) throws IOException {
		
		Map<String, Map<String, ?>> metabolitesMap = new HashMap<String, Map<String, ?>>();
		metabolitesMap.put("nodeLabels", nodeLabels);
		metabolitesMap.put("nodeShapes", nodeShapes);
		metabolitesMap.put("nodeSizes", nodeSizes);
		metabolitesMap.put("nodeColors", nodeColors);
		
		Map<String, Map<String, ?>> reactionsMap = new HashMap<String, Map<String, ?>>();
		reactionsMap.put("reactionLabels", reaction_labels);
		reactionsMap.put("reactionShapes", reactionShapes);
		reactionsMap.put("reactionSizes", reactionSizes);
		reactionsMap.put("reactionColors", reactionColors);
		reactionsMap.put("edgeThickness", edgeThickness);
		reactionsMap.put("positiveReactions", positiveReactions);
		reactionsMap.put("visFilters", visFilters);
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("metabolites", Map.class);
		xstream.toXML(metabolitesMap, new BufferedWriter(new FileWriter(xmlFileMetabolites)));
		
		xstream.alias("reactions", Map.class);
		xstream.toXML(reactionsMap, new BufferedWriter(new FileWriter(xmlFileReactions)));
	}
	
	@SuppressWarnings("unchecked")
	public void loadFromXML(String xmlFileMetabolites, String xmlFileReactions) throws FileNotFoundException {
		
		XStream xstream = new XStream(new DomDriver());
		
		Map<String, Map<String, ?>> mapMetabolites = (Map<String, Map<String, ?>>) xstream.fromXML(new BufferedReader(new FileReader(xmlFileMetabolites)));
		setNodeLabels((Map<String, String>) mapMetabolites.get("nodeLabels"));
		setNodeShapes((Map<String, Integer>) mapMetabolites.get("nodeShapes"));
		setNodeSizes((Map<String, Pair<Double, Double>>) mapMetabolites.get("nodeSizes"));
		setNodeColors((Map<String, Integer>) mapMetabolites.get("nodeColors"));
		
		Map<String, Map<String, ?>> mapReactions = (Map<String, Map<String, ?>>) xstream.fromXML(new BufferedReader(new FileReader(xmlFileReactions)));
		setReactionLabels((Map<String, String>) mapReactions.get("reactionLabels"));
		setReactionShapes((Map<String, Integer>) mapReactions.get("reactionShapes"));
		setReactionSizes((Map<String, Pair<Double, Double>>) mapReactions.get("reactionSizes"));
		setReactionColors((Map<String, Pair<Integer, Integer>>) mapReactions.get("reactionColors"));
		setEdgeThickness((Map<String, Double>) mapReactions.get("edgeThickness"));
		setFluxDirections((Map<String, Boolean>) mapReactions.get("positiveReactions"));
		setVisFilters((Map<String, Set<String>>) mapReactions.get("visFilters"));
	}
	
	public static void main(String... args) throws IOException {
		String color = " (254, 042, 3 , 2) ";
		Matcher m = RGBA_PATTERN.matcher(color);
		
		if (m.matches()) {
			int[] rgba = new int[4];
			rgba[0] = Integer.parseInt(m.group(1));
			rgba[1] = Integer.parseInt(m.group(2));
			rgba[2] = Integer.parseInt(m.group(3));
			rgba[3] = Integer.parseInt(m.group(4));
			for (int x : rgba)
				if (x > 255 || x < 0) {
					throw new IOException("Invalid reactions file at reaction [], column [" + REACTION_COLORS + "],  color [" + color + "]. All values bust be in the range [0,255].");
				}
			int colorCode = ColorLib.rgba(rgba[0], rgba[1], rgba[2], rgba[3]);
			
			System.out.println("MATCHES COLOR CODE = " + colorCode);
		} else
			System.out.println("No match found");
	}

	@Override
	public Map<String, Double> getEdgesOriginalValues() {
		return edgesOriginalValues;
	}

	@Override
	public Map<String, Double> getNodesOriginalValues() {
		return nodesOriginalValues;
	}

	@Override
	public void setEdgesOriginalValues(Map<String, Double> originalValues) {
		edgesOriginalValues = originalValues;
	}

	@Override
	public void setNodesOriginalValues(Map<String, Double> originalValues) {
		nodesOriginalValues = originalValues;
	}
}
