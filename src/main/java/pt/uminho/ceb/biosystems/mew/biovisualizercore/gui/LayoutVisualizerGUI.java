package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.batik.apps.rasterizer.DestinationType;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.xml.sax.SAXException;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.util.GraphicsLib;
import prefuse.util.display.DisplayLib;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ChangeLayoutListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.INodeLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.IReactionLay;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.NodeClickingListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers.EscherLayoutWriter;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers.SBGNWriter;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.io.writers.XGMMLWriter;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.utils.filefilter.FileTypeFilter;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.LayoutVisualizator;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.controls.ColorShapeManager;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.AbstractOverlap;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.AbstractOverlapFactory;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.VisualizationProperties;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.utils.LayoutUtils;
import de.erichseifert.vectorgraphics2d.SVGGraphics2D;
import de.erichseifert.vectorgraphics2d.VectorGraphics2D.FontRendering;

/**
 * User interface showing the graph, the filters panel and the information
 * panel.
 * Can be used as a standalone version of the visualizer.
 * 
 * @author Alberto Noronha
 */
public class LayoutVisualizerGUI extends javax.swing.JPanel implements ChangeLayoutListener, NodeClickingListener {
	
	public static boolean								fonts				= true;
	private static final long							serialVersionUID	= 1L;
	protected static Proxy								proxy				= null;
	
	protected JPanel									visPanel;
	protected OverlapPanel								overlapsPanel;
	protected AbstractInformationPanel					informationPanel;
	protected LayoutVisualizator						lVis;
	protected Container									container;
	protected Map<String, Map<String, IOverlapObject>>	overlaps;
	protected JSplitPane								splitPane;
	private ZoomPanel									zoomPanel;
	
	private JPanel										sidePanel;
	protected ExpandableInfoPanel						expandableInfoPanel;
	
	protected Map<String, ILayout>						layouts;
	
	protected Map<String, Set<String>>					mapOfMetIds;
	
	private String										last_visited_directory;
	private String										exportFile;
	private JButton										exportButton;
	protected ILayout									selectedLayout;
	public String										selectedLayoutId;
	protected Map<String, Set<String>>					metLayoutLinks;
	
	private AbstractOverlapFactory						overlapConversionFactory;
	
	private VisualizationProperties						vps;
	
	public LayoutVisualizerGUI(
			Container cont, 
			Map<String, ILayout> layouts, 
			Map<String, Set<String>> metLayoutLinks, 
			Map<String, Map<String, IOverlapObject>> overlaps, 
			AbstractInformationPanel infoPanel, 
			AbstractOverlapFactory factory) {
		
		this(cont, layouts, metLayoutLinks, overlaps, infoPanel, factory, null, null);
	}
	
	public LayoutVisualizerGUI(
			Container cont,
			Map<String, ILayout> layouts,
			Map<String, Set<String>> metLayoutLinks,
			Map<String, Map<String, IOverlapObject>> overlaps,
			AbstractInformationPanel infoPanel,
			AbstractOverlapFactory factory,
			VisualizationProperties vps) {
		
		this(cont, layouts, metLayoutLinks, overlaps, infoPanel, factory, vps, null);
	}
	
	public LayoutVisualizerGUI(
			Container cont,
			Map<String, ILayout> layouts,
			Map<String, Set<String>> metLayoutLinks,
			Map<String, Map<String, IOverlapObject>> overlaps,
			AbstractInformationPanel infoPanel,
			AbstractOverlapFactory factory,
			VisualizationProperties vps,
			String selectedLayout) {
		
		this.container = cont;
		this.layouts = layouts;
		this.metLayoutLinks = metLayoutLinks;
		this.overlaps = overlaps;
		this.informationPanel = infoPanel;
		this.overlapConversionFactory = factory;
		this.vps = (vps == null) ? new VisualizationProperties() : vps;
		this.selectedLayoutId = selectedLayout;
		
		initGUI();
	}
	
	
	
	public LayoutVisualizator getLayoutVisualizator() { return lVis;}

	public void changeLayout(String pathId) {
		changeLayout(pathId, null);
		informationPanel.clear();
	}
	
	public void updateProperties(VisualizationProperties vps) {
		this.vps = vps;
		vps.updateLabels(container);
		overlapConversionFactory.converProperties(vps, selectedLayout);
		lVis.runNewProperties(vps);
		overlapsPanel.rerunOverlap();
	}
	
	public void changeLayout(String pathId, Set<String> mets) {
		
		ILayout lay = layouts.get(pathId);
		if (selectedLayout == null || !selectedLayout.equals(lay)) {
			selectedLayout = lay;
			selectedLayoutId = pathId;
			LayoutVisualizator oldVis = lVis;
			if (oldVis != null) {
				oldVis.stopAndclear();
				oldVis.removeAllListeners();
				visPanel.setVisible(false);
				splitPane.setLeftComponent(null);
				visPanel.removeAll();
				
			}
			
			ColorShapeManager csm;
			if (vps != null) {
				vps.updateLabels(container);
				overlapConversionFactory.converProperties(vps, selectedLayout);
				csm = new ColorShapeManager(vps);
				lVis = new LayoutVisualizator(selectedLayout, csm);
				visPanel = lVis.generateVisualization();
				csm.setDefaultLabels(vps);
			} else {
				csm = new ColorShapeManager();
				lVis = new LayoutVisualizator(selectedLayout, csm);
				visPanel = lVis.generateVisualization();
			}
			
			lVis.addNodeClickListener(this);
			visPanel.setVisible(true);
			
			this.mapOfMetIds = getMapOfMetabolicIds(selectedLayout.getReactions(), selectedLayout.getNodes());
			
			lVis.addChangeLayoutListener(this);
			lVis.addNodeClickListener(this);
			
			zoomPanel.setLayoutVisualizator(lVis);
			overlapsPanel.setLayoutVisualizator(lVis);
			overlapsPanel.selectedPathways(false);
			overlapsPanel.setVisibleInfoFilter(lay.hasInformationNodes());
			
			if (mets != null) lVis.setHighlightMetabolites(mets);
			
			visPanel.setBorder(BorderFactory.createTitledBorder(pathId));
			splitPane.setLeftComponent(visPanel);
			splitPane.setOneTouchExpandable(true);
			splitPane.setResizeWeight(1);
			this.updateUI();
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				lVis.tryToPanToDimensions();
			}
		});

	}
	
	public void addOverlap(String type, AbstractOverlap overlap) {
		this.overlapsPanel.addOverLap(type, overlap);
	}
	
	public void addOverlap(String type, IOverlapObject overlap) {
		this.overlapsPanel.addOverLap(type, overlap);
	}
	
	protected void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
			thisLayout.rowHeights = new int[] { 7, 7, 7, 4 };
			thisLayout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
			thisLayout.columnWidths = new int[] { 7, 7, 7, 7 };
			this.setLayout(thisLayout);
			{
				
				sidePanel = new JPanel();
				GridBagLayout sidePanelLayout = new GridBagLayout();
				sidePanelLayout.rowWeights = new double[] { 0.1, 0.0, 0.0 };
				sidePanelLayout.rowHeights = new int[] { 0, 0, 0 };
				sidePanelLayout.columnWeights = new double[] { 0.1 };
				sidePanelLayout.columnWidths = new int[] { 7 };
				sidePanel.setLayout(sidePanelLayout);
				
			}
			{
				overlapsPanel = new OverlapPanel(container, overlaps, overlapConversionFactory);
				overlapsPanel.addListnerInPathway(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if (((JCheckBox) e.getSource()).isSelected()) {
							higlightPatwaySelected();
						} else {
							clearPathwaySelected();
						}
						
					}
				});
				overlapsPanel.addOverlapListener(informationPanel);
			}
			{
				
				this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, visPanel, sidePanel);
				splitPane.setOneTouchExpandable(true);
				splitPane.setResizeWeight(1);
				
				this.add(splitPane, new GridBagConstraints(0, 0, 4, 4, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
				expandableInfoPanel = new ExpandableInfoPanel(overlapsPanel, informationPanel);
				sidePanel.add(expandableInfoPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
				
			}
			{
				exportButton = new JButton();
				sidePanel.add(exportButton, new GridBagConstraints(0, 2, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
				exportButton.setText("Export Current Layout");
				exportButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						JFileChooser fc = new JFileChooser(last_visited_directory);
						fc.setAcceptAllFileFilterUsed(false);
						FileFilter xgmmlFilter = new FileTypeFilter(".xgmml", "XGMML Files");
						FileFilter escherFilter = new FileTypeFilter(".json", "Escher json Map");
						FileFilter svgFilter = new FileTypeFilter(".svg", "SVG Map");
						FileFilter pdfFiler = new FileTypeFilter(".pdf", "PDF document");
						FileFilter sbgn = new FileTypeFilter(".sbgn", "SBGN Map (Beta)");
						
						fc.addChoosableFileFilter(escherFilter);
						fc.addChoosableFileFilter(sbgn);
						fc.addChoosableFileFilter(pdfFiler);
						fc.addChoosableFileFilter(svgFilter);
						fc.setFileFilter(xgmmlFilter);
						
						int returnVal = fc.showDialog(/*
													 * Workbench.getInstance().
													 * getMainFrame()
													 */null, "Export");
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File selected = fc.getSelectedFile();
							
							String desc = fc.getFileFilter().getDescription();
							System.out.println("Description: " + desc);
							String ext;
							if (desc.equals("XGMML Files (*.xgmml)"))
								ext = ".xgmml";
							else if (desc.equals("SVG Map (*.svg)"))
								ext = ".svg";
							else if (desc.equals("PDF document (*.pdf)"))
								ext = ".pdf";
							else if (desc.equals("Escher json Map (*.json)"))
								ext = ".json";
							else
								ext = ".sbgn";
							
							if (!selected.getName().endsWith(ext)) {
								exportFile = selected.getAbsolutePath() + ext;
							} else {
								exportFile = selected.getAbsolutePath();
								
							}
							
							last_visited_directory = fc.getSelectedFile().getAbsolutePath();
							
							exportLayoutToDir(ext, exportFile, 0.25);
							
						}
					}
				});
			}
			{
				zoomPanel = new ZoomPanel();
				sidePanel.add(zoomPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Exportation of the layout to xgmml.
	 */
	private void exportLayoutToDir(String ext, String file, double scale) {
		
		if (ext.equals(".xgmml")) {
			exportToXGMML(file);
		} else if (ext.equals(".svg")) {
			exportToSVG(file, scale);
		} else if (ext.equals(".pdf"))
			exportToPDF(file, scale);
		else if(ext.equals(".json"))
			exportToEscher(file, scale);
		else
			exportToSBGN(file);
	}
	
	private void exportToSBGN(String file) {
		
		try {
			SBGNWriter.writeSBGNtoFile(file, getNewLayout());
			JOptionPane.showMessageDialog(this, "Layout exported to SBGN!");
		} catch (JAXBException e) {
			JOptionPane.showMessageDialog(this, "Error exporting the layout! " + e.getMessage());
		} catch (SAXException e) {
			JOptionPane.showMessageDialog(this, "Error exporting the layout! " + e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error exporting the layout! " + e.getMessage());
		}
		
	}
	
	private void exportToPDF(final String pdffile, final double scale) {
		
		final LayoutVisualizerGUI currentPanel = this;
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					Rectangle2D bounds = lVis.getVis().getBounds(Visualization.ALL_ITEMS);
					
					Display display = new Display(lVis.getVis());
					SVGGraphics2D g = new SVGGraphics2D(0, 0, display.getWidth(), display.getHeight());
					
					if (fonts) g.setFontRendering(FontRendering.VECTORS);
					
					GraphicsLib.expand(bounds, 50 + (int) (1 / display.getScale()));
					DisplayLib.fitViewToBounds(display, bounds, 0);
					
//					System.out.println("antes");
					display.paintDisplay(g, new Dimension(display.getWidth(), display.getHeight()));
//					System.out.println("end");
					
					// Save this SVG into a file (required by SVG -> PDF transformation process)
					File svgFile = File.createTempFile("graphic-", ".svg");
					FileOutputStream file = new FileOutputStream(svgFile);
					try {
						file.write(g.getBytes());
					} finally {
						file.close();
						
					}
					
					// Convert the SVG into PDF
					File outputFile = new File(pdffile);
					SVGConverter converter = new SVGConverter();
					converter.setDestinationType(DestinationType.PDF);
					converter.setSources(new String[] { svgFile.toString() });
					converter.setDst(outputFile);
					converter.execute();
					
					JOptionPane.showMessageDialog(currentPanel, "Layout exported to PDF!");
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(currentPanel, "Error exporting the layout! " + ex.getMessage());
					ex.printStackTrace();
				}
				
			}
		});
		
		t.start();
	}
	
	private void exportToSVG(final String svgpath, final double scale) {
		
		final LayoutVisualizerGUI currentPanel = this;
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					Rectangle2D bounds = lVis.getVis().getBounds(LayoutUtils.GRAPH);
					Point2D p2d = new Point();
					p2d.setLocation(bounds.getX(), bounds.getY());
					
					Display display = new Display(lVis.getVis());
					display.pan(-bounds.getX() + (50 * scale), -bounds.getY() + (50 * scale));
					display.setBounds(0, 0, (int) ((bounds.getWidth() + 100) * scale), (int) ((bounds.getHeight() + 100) * scale));
					
					Point2D p = new Point2D.Double(0, 0);
					
					display.zoom(p, scale);
					display.setHighQuality(true);
					SVGGraphics2D g = new SVGGraphics2D(0, 0, display.getWidth(), display.getHeight());
					
					if (fonts) g.setFontRendering(FontRendering.VECTORS);
					
					display.paintDisplay(g, new Dimension(display.getWidth(), display.getHeight()));
					
					FileOutputStream file = new FileOutputStream(svgpath);
					try {
						file.write(g.getBytes());
						JOptionPane.showMessageDialog(currentPanel, "Layout exported to SVG!");
					} finally {
						file.close();
						
					}
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(currentPanel, "Failed SVG exportation. " + ex.getMessage());
					ex.printStackTrace();
				}
				
			}
			
		});
		
		t.start();
		
	}
	
	private void exportToEscher(String filedir, Double scale){
		try {
			FileOutputStream out = new FileOutputStream(filedir);
			BufferedOutputStream bout = new BufferedOutputStream(out);
			
			EscherLayoutWriter writer = new EscherLayoutWriter(out);
			writer.write(getNewLayout());
			bout.flush();
			bout.close();
			out.close();
			JOptionPane.showMessageDialog(this, "Layout exported!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Failed to export layout. " + e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	private void exportToXGMML(String filedir) {
		
		File file = new File(filedir);
		
		if (!file.exists()) try {
			file.createNewFile();
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, "Failed to create the file!\n" + e1.getMessage());
			e1.printStackTrace();
		}
		
		if (file.canWrite()) {
			
			try {
				XGMMLWriter writer = new XGMMLWriter();
				Map<String, Double> fD = new HashMap<String, Double>();
				writer.createDocumentWithFluxes(fD, getNewLayout(), filedir);
				JOptionPane.showMessageDialog(this, "Layout exported to XGMML!");
			} catch (TransformerException e) {
				JOptionPane.showMessageDialog(this, "Failed to export layout.\n" + e.getMessage());
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				JOptionPane.showMessageDialog(this, "Failed to export layout. Can't Write in file!\n" + e.getMessage());
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Failed to export layout. Can't find file!\n" + e.getMessage());
				e.printStackTrace();
				
			}
			
		} else {
			
			JOptionPane.showMessageDialog(this, "Failed to export layout. Can't Write in file!");
		}
		//		
		
	}
	
	private ILayout getNewLayout() {
		return LayoutUtils.cleanInconsistencies(lVis.getNewLayoutWithCoords());
	}
	
	/**
	 * Builds a map of metabolic ids.
	 * 
	 * @param reactions
	 * @param nodes
	 * @return map of metabolic ids.
	 */
	protected Map<String, Set<String>> getMapOfMetabolicIds(Map<String, IReactionLay> reactions, Map<String, INodeLay> nodes) {
		
		Map<String, Set<String>> idsToMetIds = new HashMap<String, Set<String>>();
		for (String rId : reactions.keySet()) {
			
			Set<String> ids = reactions.get(rId).getIDs();
			
			idsToMetIds.put(rId, ids);
		}
		
		for (String mId : nodes.keySet()) {
			if (nodes.get(mId).getIds() != null) {
				
				Set<String> ids = nodes.get(mId).getIds();
				idsToMetIds.put(mId, ids);
			}
		}
		
		return idsToMetIds;
	}
	
	public void addReaction(IReactionLay reaction) {
		lVis.addReaction(reaction);
	}
	
	@Override
	public void layoutChanged() {
		
		mapOfMetIds = getMapOfMetabolicIds(selectedLayout.getReactions(), selectedLayout.getNodes());
		this.overlapsPanel.rerunOverlap();
	}
	
	public void setInformationPanel(AbstractInformationPanel panel) {
		
		sidePanel.remove(informationPanel);
		int divider = splitPane.getDividerLocation();
		this.informationPanel = panel;
		sidePanel.add(informationPanel, new GridBagConstraints(0, 3, 1, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		splitPane.setRightComponent(sidePanel);
		splitPane.setDividerLocation(divider);
		
	}
	
	@Override
	public void nodeClicked(String id) {
		
		INodeLay node = this.selectedLayout.getNodes().get(id);
		this.informationPanel.showNodeInformation(node);
	}
	
	@Override
	public void reactionClicked(String id) {
		
		IReactionLay reaction = this.selectedLayout.getReactions().get(id);
		this.informationPanel.showReactionInformation(reaction);
	}
	
	private void higlightPatwaySelected() {
		
		Set<String> metIds = new HashSet<String>();
		
		for (String id : metLayoutLinks.keySet()) {
			Set<String> path = metLayoutLinks.get(id);
			if (path.contains(selectedLayoutId) && path.size() > 1) {
				metIds.add(id);
			}
		}
		
		lVis.setHighlightMetabolites(metIds);
	}
	
	private void clearPathwaySelected() {
		lVis.clearMetabolitesHighlight();
	}
	
	public void updateInfo() {
		informationPanel.updateInfo();
	}
	
	public void stopRendering() {
		lVis.stopAndclear();
	}
	
	public void centerOnScreen() {
		lVis.fitToScreen();
	}
	
	public Set<String> getSelectedReactions() {
		return lVis.getSelectedReactions();
	}
	
	public void removeReaction(Set<String> uuids) {
		lVis.removeReactions(uuids);
	}

	public Set<String> getReactionIds(){
		return new TreeSet<>(lVis.getLayoutContainer().getReactions().keySet());
	}
	
	public static Proxy getProxy() {
		return proxy;
	}
	
	public static void setProxy(Proxy proxy) {
		LayoutVisualizerGUI.proxy = proxy;
	}

	public void removeReactionById(Collection<String> reactions) {
		throw new RuntimeException("Chama o liu!!!");
	}
	
	public OverlapPanel getOverlapsPanel() {
		return overlapsPanel;
	}
}
