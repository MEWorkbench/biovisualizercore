package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.generule;

import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.conversionfunction.GenericRuleConversion;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTree;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;

public class GeneCellObject {
	
	private String										id;
	private Double										originalValue;
	private AbstractSyntaxTree<DataTypeEnum, IValue>	ast;
	private GenericRuleConversion						conversion;
	private Map<String, Double>							geneValues;
	
	public GeneCellObject(String id, AbstractSyntaxTree<DataTypeEnum, IValue> ast, GenericRuleConversion conversion, Map<String, Double> geneValues,Double originalValue) {
		this.id = id;
		this.ast = ast;
		this.conversion = conversion;
		this.geneValues = geneValues;
		this.originalValue = originalValue;
	}
	
	public AbstractSyntaxTree<DataTypeEnum, IValue> getAst() {
		return ast;
	}
	
	public GenericRuleConversion getConversion() {
		return conversion;
	}
	
	public Map<String, Double> getGeneValues() {
		return geneValues;
	}
	
	public void setAst(AbstractSyntaxTree<DataTypeEnum, IValue> ast) {
		this.ast = ast;
	}
	
	public void setConversion(GenericRuleConversion conversion) {
		this.conversion = conversion;
	}
	
	public void setGeneValues(Map<String, Double> geneValues) {
		this.geneValues = geneValues;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString(){
		return originalValue.toString();
	}
	
}
