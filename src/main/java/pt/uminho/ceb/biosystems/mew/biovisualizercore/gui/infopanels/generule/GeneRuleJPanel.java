package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.generule;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.conversionfunction.GenericRuleConversion;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTree;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTreeNode;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.Environment;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DoubleValue;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;

public class GeneRuleJPanel extends JPanel implements ActionListener {
	
	private static final long							serialVersionUID				= 1L;
	private static final String							EXPAND_BUTTON_ACTION_COMMAND	= "expandButtonActionCommand";
	private static final String							EXPAND_TEXT						= "Expand All nodes";
	private static final String							COLLAPSE_TEXT					= "Collapse All nodes";
	private AbstractSyntaxTree<DataTypeEnum, IValue>	ast;
	private GenericRuleConversion						conversion;
	private Map<String, Double>							geneValues;
	private Environment<IValue>							environment;
	private Container									container;
	private JTree										tree;
	private JButton										expandButton;
	private boolean										expanded						= false;
	
	public GeneRuleJPanel(AbstractSyntaxTree<DataTypeEnum, IValue> ast, GenericRuleConversion conversion, Map<String, Double> geneValues, Container container) {
		super();
		this.ast = ast;
		this.conversion = conversion;
		this.geneValues = geneValues;
		this.container = container;
		buildEnvironment();
		init();
	}
	
	private void buildEnvironment() {
		environment = new Environment<IValue>();
		
		for (String gene : container.getGenes().keySet()) {
			if (geneValues.containsKey(gene)) {
				environment.associate(gene, new DoubleValue(geneValues.get(gene)));
			} else {
				environment.associate(gene, new DoubleValue(Double.NaN));
			}
		}
		
		conversion.setEnvironment(environment);
	}
	
	private void init() {
		setLayout(new BorderLayout());
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		NodeUserObject root = new NodeUserObject(ast.getRootNode(), conversion, environment);
		
		rootNode.setUserObject(root);
		for (int i = 0; i < root.getNode().getNumberOfChildren(); i++) {
			addNode(root.getNode().getChildAt(i), rootNode);
		}
		tree = new JTree(rootNode);
		((DefaultTreeCellRenderer) tree.getCellRenderer()).setLeafIcon(null);
		((DefaultTreeCellRenderer) tree.getCellRenderer()).setClosedIcon(null);
		((DefaultTreeCellRenderer) tree.getCellRenderer()).setOpenIcon(null);
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
		
		add(tree, BorderLayout.CENTER);
		
		//		expandButton = new JButton(EXPAND_TEXT);
		//		expandButton.setPreferredSize(new Dimension(60,30));
		//		expandButton.setActionCommand(EXPAND_BUTTON_ACTION_COMMAND);
		//		expandButton.addActionListener(this);
		//		add(expandButton,BorderLayout.SOUTH);
		
	}
	
	private void addNode(AbstractSyntaxTreeNode<DataTypeEnum, IValue> node, DefaultMutableTreeNode parent) {
		DefaultMutableTreeNode child = new DefaultMutableTreeNode();
		NodeUserObject childNode = new NodeUserObject(node, conversion, environment);
		child.setUserObject(childNode);
		parent.add(child);
		for (int i = 0; i < node.getNumberOfChildren(); i++) {
			addNode(node.getChildAt(i), child);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		switch (actionCommand) {
			case EXPAND_BUTTON_ACTION_COMMAND:
				expandCollapse(!expanded);
				break;
			
			default:
				break;
		}
		
	}
	
	private void expandCollapse(boolean expand) {
		for (int i = 0; i < tree.getRowCount(); i++) {
			if (expand) {
				tree.expandRow(i);
			} else {
				tree.collapseRow(i);
			}
		}
		
		expandButton.setText(expand ? COLLAPSE_TEXT : EXPAND_TEXT);
		expanded = expand;
	}
	
	//	public static void main(String[] args) throws ParseException {
	//		String rs = "(G1 or G2) and G3";
	//		
	//		AbstractSyntaxTreeNode<DataTypeEnum, IValue> node = ParserSingleton.boolleanParserString(rs);
	//		
	//		AbstractSyntaxTree<DataTypeEnum, IValue> ast = new AbstractSyntaxTree<DataTypeEnum, IValue>(node);
	//		
	//		GenericRuleConversion conversion = new GenericRuleConversion(ConversionFunction.MINIMUM, ConversionFunction.MAXIMUM);
	//		
	//		Map<String, Double> geneValues = new HashMap<String, Double>();
	//		geneValues.put("G1", 1.0);
	//		geneValues.put("G2", 1.5);
	//		geneValues.put("G3", 2.0);
	//		
	//		JDialog dialog = new JDialog();
	//		dialog.setTitle("Test AST viewer");
	//		dialog.setLayout(new BorderLayout());
	////		dialog.setPreferredSize(new Dimension((int) (500 * 1.6), 500));
	//		GeneRuleJPanel panel = new GeneRuleJPanel(ast, conversion, geneValues);
	//		dialog.add(panel, BorderLayout.CENTER);
	//		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	//		dialog.pack();
	//		dialog.setVisible(true);
	//	}
}

class NodeUserObject {
	
	private AbstractSyntaxTreeNode<DataTypeEnum, IValue>	node;
	private GenericRuleConversion							conversion;
	private Environment<IValue>								environment;
	
	public NodeUserObject(AbstractSyntaxTreeNode<DataTypeEnum, IValue> ast, GenericRuleConversion conversion, Environment<IValue> environment) {
		this.node = ast;
		this.conversion = conversion;
		this.environment = environment;
	}
	
	public double evaluateNode() {
		return (double) conversion.convertGeneRule(node).evaluate(environment).getValue();
	}
	
	public String toString() {
		double value = evaluateNode();
		return node.toString() + " = " + value;
	}
	
	public AbstractSyntaxTreeNode<DataTypeEnum, IValue> getNode() {
		return node;
	}
	
	public GenericRuleConversion getConversion() {
		return conversion;
	}
	
	public Environment<IValue> getEnvironment() {
		return environment;
	}
	
	public void setNode(AbstractSyntaxTreeNode<DataTypeEnum, IValue> node) {
		this.node = node;
	}
	
	public void setConversion(GenericRuleConversion conversion) {
		this.conversion = conversion;
	}
	
	public void setEnvironment(Environment<IValue> environment) {
		this.environment = environment;
	}
}