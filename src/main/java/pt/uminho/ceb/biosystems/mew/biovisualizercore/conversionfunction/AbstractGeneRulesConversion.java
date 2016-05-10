package pt.uminho.ceb.biosystems.mew.biovisualizercore.conversionfunction;

import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.AbstractSyntaxTreeNode;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.IEnvironment;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DataTypeEnum;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.And;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Or;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.node.Variable;

public abstract class AbstractGeneRulesConversion {
	
	protected IEnvironment<IValue> environment;
	AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRegulationRule;
	
	public AbstractGeneRulesConversion() {}

	public void setEnvironment(IEnvironment<IValue> environment) {
		this.environment = environment;
	}
	
	protected abstract AbstractSyntaxTreeNode<DataTypeEnum, IValue> convertAnd (AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRule);
	
	protected abstract AbstractSyntaxTreeNode<DataTypeEnum, IValue> convertOr (AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRule);
	
	protected abstract AbstractSyntaxTreeNode<DataTypeEnum, IValue> convertVariable (AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRule);
	
	public AbstractSyntaxTreeNode<DataTypeEnum, IValue> convertGeneRule(AbstractSyntaxTreeNode<DataTypeEnum, IValue> geneRule) {
		geneRegulationRule = null;
		
		if (geneRule instanceof And)
			geneRegulationRule = convertAnd(geneRule);
		else if (geneRule instanceof Or)
			geneRegulationRule = convertOr(geneRule);
		else if (geneRule instanceof Variable)
			geneRegulationRule = convertVariable(geneRule);

		return geneRegulationRule;
	}
}