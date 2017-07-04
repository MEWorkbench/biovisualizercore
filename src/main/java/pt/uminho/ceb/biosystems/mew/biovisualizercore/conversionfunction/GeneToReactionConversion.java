package pt.uminho.ceb.biosystems.mew.biovisualizercore.conversionfunction;

import java.util.HashMap;
import java.util.Map;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.ReactionCI;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.Environment;
import pt.uminho.ceb.biosystems.mew.utilities.grammar.syntaxtree.IEnvironment;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.DoubleValue;
import pt.uminho.ceb.biosystems.mew.utilities.math.language.mathboolean.IValue;

public class GeneToReactionConversion {
	
	private Container			_container;
	private Map<String, Double>	_geneValues;
	
	public GeneToReactionConversion(Container container, Map<String, Double> geneValues) {
		_container = container;
		_geneValues = geneValues;
	}
	
	public Map<String, Double> convert(AbstractGeneRulesConversion geneRulesConversion) throws Exception {
		// calculate the reaction value considering the gene rule
		Map<String, Double> geneValues = _geneValues;
		IEnvironment<IValue> environment = new Environment<IValue>();
		geneRulesConversion.setEnvironment(environment);
		
		for (String gene : _container.getGenes().keySet()) {
			// when gene don't have weight put NAN
			if (geneValues.containsKey(gene)){
				environment.associate(gene, new DoubleValue(geneValues.get(gene)));
			} else {
				environment.associate(gene, new DoubleValue(Double.NaN));
			}
		}
		
		Map<String, Double> reacValues = new HashMap<String, Double>();
		try {
			for (Map.Entry<String, ReactionCI> e : _container.getReactions().entrySet()) {
				
				if (e.getValue().getGeneRule() != null && !e.getValue().getGeneRuleString().isEmpty()) {
					double value = (double) geneRulesConversion.convertGeneRule(e.getValue().getGeneRule().getRootNode()).evaluate(environment).getValue();
					
					if(!Double.isNaN(value) && !Double.isInfinite(value)){
						reacValues.put(e.getKey(), value);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Conversion problems: " + e.getMessage());
		}
		
		return reacValues;
	}
}
