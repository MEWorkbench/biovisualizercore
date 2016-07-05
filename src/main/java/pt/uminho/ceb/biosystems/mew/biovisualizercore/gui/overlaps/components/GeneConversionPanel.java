package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.overlaps.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.ErrorsException;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.io.readers.JSBMLReader;
import pt.uminho.ceb.biosystems.mew.biocomponents.validation.io.JSBMLValidationException;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.conversionfunction.GenericRuleConversion;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.ReactionListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.generule.GeneRuleJPanel;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapsListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants.ConversionFunction;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.omics.OmicsMultipleConditionsOverlap;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTree;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;

public class GeneConversionPanel extends JPanel implements ReactionListener, OverlapsListener {

	private static final long serialVersionUID = 1L;
//	protected GeneRuleJPanel geneRulePanel;
	protected JPanel geneRulePanel;
	protected Container container;
	protected IOverlapObject overlapObject;
	protected boolean canExpand = false;

	public GeneConversionPanel(Container container) {
		super();
		this.container = container;
		initGUI();
	}

	protected void initGUI() {
		setLayout(new BorderLayout());
	}

	@Override
	public void reactionChanged(ReactionEvent event) {
		final Set<String> ids = event.getIds();
		update(ids);
	}

	protected void update(final Set<String> ids) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					populateGeneRuleInfo(ids);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void reset() {
		synchronized (this) {
			removeAll();
		}
	}
	
	public void noInfo() {
		synchronized (this) {
			removeAll();
			JPanel noInfo = new JPanel();
			noInfo.add(new JLabel("No available information"));
			add(noInfo, BorderLayout.CENTER);
		}
	}
	
	@Override
	public void selectedOverlapChanged(OverlapEvent event) {
		overlapObject = event.get_selectedOverlap();
	}

	protected void populateGeneRuleInfo(Set<String> ids) throws IOException {
		if (ids == null || ids.isEmpty()) {
			reset();
		} else {
			reset();
			
			for (final String reactionID : ids) {
				if (overlapObject != null && overlapObject instanceof OmicsMultipleConditionsOverlap) {
		
					String id = ((OmicsMultipleConditionsOverlap) overlapObject).get_selectedCondition();
					Map<String, Double> map = ((OmicsMultipleConditionsOverlap) overlapObject).getGenesInformation().get(id);
					GenericRuleConversion conversion = ((OmicsMultipleConditionsOverlap) overlapObject).getGeneRuleConversions().get(id);
					
					if(map == null || map.isEmpty()){
						geneRulePanel = new JPanel();
						geneRulePanel.add(new JLabel("No gene info was found"));
						canExpand = false;
					}else{
						geneRulePanel = generateGeneRulePanel(reactionID, map, conversion);
					}
		
					JPanel lowerPanel = new JPanel(new BorderLayout());
					JPanel rulePanel = new JPanel(new BorderLayout());
					rulePanel.setPreferredSize(new Dimension(200, 200));
					JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
					split.setResizeWeight(1.0);
					JScrollPane scroller = new JScrollPane(geneRulePanel);
					
					scroller.setViewportView(geneRulePanel);
					scroller.setMaximumSize(new Dimension((int) (500 * 1.6), 500));
					rulePanel.add(scroller, BorderLayout.CENTER);
					
					split.setTopComponent(rulePanel);
		
					JButton infoButton = new JButton("Expand");
					infoButton.setEnabled(canExpand);
					infoButton.addActionListener(new ActionListener() {
		
						@Override
						public void actionPerformed(ActionEvent e) {
							JDialog dialog = new JDialog();
							dialog.setTitle("Gene Rule");
							dialog.setLayout(new BorderLayout());
		
							dialog.setMinimumSize(new Dimension((int) (300 * 1.6), 300));
							
							JScrollPane scroll = new JScrollPane(geneRulePanel) {
								
								private static final long	serialVersionUID	= 1L;
								
								@Override
								public Dimension getPreferredSize() {
									Dimension preferred = super.getPreferredSize();
									Dimension maximum = getMaximumSize();
									
									int width= (int) ((preferred.getWidth() > maximum.getWidth()) ? maximum.getWidth() : preferred.getWidth());
									int height= (int) ((preferred.getHeight() > maximum.getHeight()) ? maximum.getHeight() : preferred.getHeight());
									
									return new Dimension(width+20,height+20);
								}
							};
							scroll.setBackground(Color.WHITE);
							scroll.setViewportView(geneRulePanel);
							scroll.setMaximumSize(new Dimension((int) (500 * 1.6), 500));
							
							dialog.add(scroll, BorderLayout.CENTER);
							dialog.setModal(true);
							dialog.pack();
							dialog.setLocationRelativeTo(null);
							dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
							dialog.setVisible(true);
						}
					});
		
					lowerPanel.add(infoButton, BorderLayout.NORTH);
					split.setBottomComponent(lowerPanel);
					split.updateUI();
					split.revalidate();
					split.repaint();
					add(split, BorderLayout.CENTER);
				}
				else{
					noInfo();
				}
			}

			updateUI();
			revalidate();
			repaint();
		}

	}

	protected JPanel generateGeneRulePanel(String reactionID, Map<String, Double> originalValues, GenericRuleConversion conversion) {
		ReactionCI reaction = container.getReaction(reactionID);
		
		JPanel toRet = new JPanel();
		if(reaction == null){
			toRet.add(new JLabel("Unknown reaction"));
			canExpand = false;
			return toRet;
		}
		
		AbstractSyntaxTree<DataTypeEnum, IValue> ast = reaction.getGeneRule();
		if(ast != null && ast.size() != 0){
			canExpand = true;
			return new GeneRuleJPanel(reaction.getGeneRule(), conversion, originalValues, container);
		}
		
		canExpand = false;
		toRet.add(new JLabel("No gene rule was found"));
		return toRet;
	}

	
	public static void main(String[] args) throws FileNotFoundException, XMLStreamException, ErrorsException, IOException, ParserConfigurationException, SAXException, JSBMLValidationException {
		
		String file = "../mewcore/src/test/resources/models/ecoli_core_model.xml";
		
		JSBMLReader reader = new JSBMLReader(file, "1", false);
		final Container container = new Container(reader);
		Set<String> met = container.identifyMetabolitesIdByPattern(Pattern.compile(".*_b"));
		Set<String> rec = container.identifyReactionsIdByPatter(Pattern.compile("R_EX_.*"));
		container.removeMetabolites(met);
		container.removeReactions(rec);
		
		final JComboBox<String> reactionsCombo = new JComboBox<String>();
		for (String reac : container.getReactions().keySet()) {
			reactionsCombo.addItem(reac);
		}
		
		final Map<String, Double> originalValues = new HashMap<>();
		Random r = new Random();
		for (String gene : container.getGenes().keySet()) {
			double randomValue = 0 + (15 - 0) * r.nextDouble();
			originalValues.put(gene, randomValue);
		}
		
		JButton button = new JButton("Get gene rule");
		
		final JDialog dialog = new JDialog();
		
		dialog.setTitle("Gene Rule");
		dialog.setLayout(new BorderLayout());
		final GenericRuleConversion conversion = new GenericRuleConversion(ConversionFunction.MINIMUM, ConversionFunction.MAXIMUM);
		
		
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel toAdd = new JPanel();
				toAdd.setLayout(new BorderLayout());
				toAdd.setPreferredSize(new Dimension(200, 200));
				
				String reactionID = reactionsCombo.getSelectedItem().toString();
				ReactionCI reaction = container.getReaction(reactionID);
				JDialog dialogRule = new JDialog();
				dialogRule.setLayout(new BorderLayout());
				
				AbstractSyntaxTree<DataTypeEnum, IValue> ast = reaction.getGeneRule();
				if(ast != null){
					GeneRuleJPanel geneRulePanel = new GeneRuleJPanel(reaction.getGeneRule(), conversion, originalValues, container);
					JScrollPane scroll = new JScrollPane(geneRulePanel)/*{
						
						private static final long	serialVersionUID	= 1L;
						
						@Override
						public Dimension getPreferredSize() {
							Dimension preferred = super.getPreferredSize();
							Dimension maximum = getMaximumSize();
							
							int width= (int) ((preferred.getWidth() > maximum.getWidth()) ? maximum.getWidth() : preferred.getWidth());
							int height= (int) ((preferred.getHeight() > maximum.getHeight()) ? maximum.getHeight() : preferred.getHeight());
							
							return new Dimension(width+20,height+20);
						}
					}*/;
					scroll.setViewportView(geneRulePanel);
					scroll.setMaximumSize(new Dimension(500, 500));
					
					
					
					toAdd.add(scroll, BorderLayout.CENTER);
				}else{
					toAdd.add(new JLabel("No gene rule found"), BorderLayout.CENTER);
				}
				
				dialogRule.add(toAdd, BorderLayout.CENTER);
				dialogRule.setPreferredSize(new Dimension(800, 1000));
				dialogRule.pack();
				dialogRule.setLocationRelativeTo(null);
				dialogRule.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialogRule.setVisible(true);
			}
		});
		
		
		dialog.add(reactionsCombo, BorderLayout.NORTH);
		dialog.add(button, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}
}
