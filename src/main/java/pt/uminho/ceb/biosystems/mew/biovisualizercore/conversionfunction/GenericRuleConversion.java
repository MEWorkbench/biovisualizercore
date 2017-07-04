package pt.uminho.ceb.biosystems.mew.biovisualizercore.conversionfunction;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants.ConversionFunction;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTreeNode;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Maximum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Mean;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Minimum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Variable;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.VariableDouble;

public class GenericRuleConversion extends AbstractGeneRulesConversion {
	
	private ConversionFunction	_andFunction;
	private ConversionFunction	_orFunction;
	
	public GenericRuleConversion(ConversionFunction andFunction, ConversionFunction orFunction) {
		_andFunction = andFunction;
		_orFunction = orFunction;
	}
	
	@Override
	protected AbstractSyntaxTreeNode<DataTypeEnum, IValue> convertAnd(AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRule) {
		return convert(geneRule,_andFunction);
	}
	
	@Override
	protected AbstractSyntaxTreeNode<DataTypeEnum, IValue> convertOr(AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRule) {
		return convert(geneRule,_orFunction);
	}
	
	protected AbstractSyntaxTreeNode<DataTypeEnum, IValue> convert(AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRule, ConversionFunction function) {
		switch (function) {
			case MAXIMUM:
				geneRegulationRule = new Maximum(convertGeneRule(geneRule.getChildAt(0)), convertGeneRule(geneRule.getChildAt(1)));
				break;
			case MINIMUM:
				geneRegulationRule = new Minimum(convertGeneRule(geneRule.getChildAt(0)), convertGeneRule(geneRule.getChildAt(1)));
				break;
			case MEAN:
				geneRegulationRule = new Mean(convertGeneRule(geneRule.getChildAt(0)), convertGeneRule(geneRule.getChildAt(1)));
			default:
				geneRegulationRule = null;
				break;
		}
		return geneRegulationRule;
	}
	
	@Override
	protected AbstractSyntaxTreeNode<DataTypeEnum, IValue> convertVariable(AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRule) {
		String geneID = ((Variable) geneRule).toString();
		double geneExpressionValue = (double) environment.find(geneID).getValue();
		geneRegulationRule = new VariableDouble(geneID, geneExpressionValue);
		return geneRegulationRule;
	}
}
