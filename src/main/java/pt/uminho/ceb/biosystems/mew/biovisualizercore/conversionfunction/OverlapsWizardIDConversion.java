package pt.uminho.ceb.biosystems.mew.biovisualizercore.conversionfunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biocomponents.container.components.MetaboliteCI;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.overlaps.constants.HeadersIDs;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.constants.OverlapsWizardTypeOfData;

public class OverlapsWizardIDConversion {
	
	private Map<String, List<String>>	_ids		= null;
	private OverlapsWizardTypeOfData	_type		= null;
	private Container					_container	= null;
	
	public OverlapsWizardIDConversion(Container container, Map<String, List<String>> ids, OverlapsWizardTypeOfData type) {
		_container = container;
		_ids = ids;
		_type = type;
	}
	
	public Map<String,Map<String, Double>> getIDValueMappings() throws IDConversionException {
		Map<String, Map<String,Double>> idMappings = null;
		switch (_type) {
			case GENES:
				idMappings = getGeneIDMappings();
				break;
			case REACTIONS:
				idMappings = getReactionIDMappings();
				break;
			case METABOLITES:
				idMappings = getMetaboliteIDMappings();
				break;
			default:
				break;
		}
		return idMappings;
	}
	
	private Map<String, Map<String,Double>> getMetaboliteIDMappings() throws IDConversionException {
		Map<String, Map<String,Double>> toret = new HashMap<String, Map<String,Double>>();
		Set<String> metIDs = _ids.keySet();
		
		if (metIDs.contains(HeadersIDs.MET_ID)) {
			toret = getMetabolitesFromIDs();
		} else if (metIDs.contains(HeadersIDs.MET_FORMULA)) {
			toret = getMetabolitesFromFormulas();
		} else {
			toret = getMetabolitesFromExtraInfo();
		}
		
		return toret;
		
	}
	
	/**
	 * DEAL WITH METABOLITES
	 * 
	 * @return
	 * @throws IDConversionException
	 */
	private Map<String, Map<String,Double>> getMetabolitesFromIDs() throws IDConversionException {
		List<String> ids = _ids.get(HeadersIDs.MET_ID);
		return mapGenericIDsToValues(ids, _container.getMetabolites().keySet());
	}
	
	private Map<String, Map<String,Double>> getMetabolitesFromFormulas() throws IDConversionException {

		Map<String,Map<String,Double>> toret = new HashMap<String,Map<String,Double>>();
		List<String> ids = _ids.get(HeadersIDs.MET_FORMULA);
				
		//FIND ALL IDS MATCHING THE VALUE PATTERN
		Map<String, List<String>> valuesMap = new HashMap<String, List<String>>();
		for (String id : _ids.keySet()) {
			Matcher m = HeadersIDs.VALUE_PATTERN.matcher(id);
			if (m.matches()) {
				List<String> values = _ids.get(id);
				String condition = m.group(1);
				valuesMap.put(condition, values);
			}
		}
		
		if(valuesMap.isEmpty())
			throw new IDConversionException("Could not find metabolite mappings using formulas in the suggested columns.\n"
					+ "Please make sure everything is correctly mapped.");
		
				
		for (String cond : valuesMap.keySet()) {
			/** very inefficient cycle (indexOf) */
			List<String> values = _ids.get(cond);
			Map<String, Double> met2idsMap = new HashMap<String, Double>();
			for (String metID : _container.getMetabolites().keySet()) {
				MetaboliteCI metCI = _container.getMetabolite(metID);
				String formula = metCI.getFormula();
				if (formula != null && !formula.isEmpty()) {
					int index = ids.indexOf(formula); //HERE!
					if (index > -1) {
						String valString = values.get(index).trim();
						Double val = null;
						try {
							val = Double.parseDouble(valString);
						} catch (Exception e) {
							throw new IDConversionException("Cannot convert  [" + valString + "] to Double.");
						}
						met2idsMap.put(metID, val);
					}
				}
			}
		}
		return toret;
	}
	
	private Map<String, Map<String,Double>> getMetabolitesFromExtraInfo() throws IDConversionException {
		return mapGenericExtraInfoToValues(_container.getMetabolitesExtraInfo());
	}
	
	/**
	 * DEAL WITH REACTIONS
	 * 
	 * @return
	 * @throws IDConversionException
	 */
	private Map<String, Map<String,Double>> getReactionIDMappings() throws IDConversionException {
		Map<String, Map<String,Double>> toret = new HashMap<String, Map<String,Double>>();
		Set<String> reactionIDs = _ids.keySet();
		
		if (reactionIDs.contains(HeadersIDs.REACTION_ID)) {
			toret = getReactionsFromIDs();
		} else {
			toret = getReactionsFromExtraInfo();
		}
		return toret;
	}
	
	private Map<String, Map<String, Double>> getReactionsFromIDs() throws IDConversionException {
		List<String> ids = _ids.get(HeadersIDs.REACTION_ID);
		return mapGenericIDsToValues(ids,_container.getReactions().keySet());
	}
	
	private Map<String, Map<String,Double>> getReactionsFromExtraInfo() throws IDConversionException {
		return mapGenericExtraInfoToValues(_container.getReactionsExtraInfo());
	}
	
	/**
	 * DEAL WITH GENES
	 * 
	 * @return
	 * @throws IDConversionException
	 */
	private Map<String, Map<String,Double>> getGeneIDMappings() throws IDConversionException {
		Map<String, Map<String,Double>> toret = new HashMap<String, Map<String,Double>>();
		Set<String> reactionIDs = _ids.keySet();
		
		if (reactionIDs.contains(HeadersIDs.GENE_ID)) {
			toret = getGenesFromIDs();
		} else
			throw new IDConversionException("A valid identifier for GENES could not be found.");
		
		return toret;
	}
	
	
	private Map<String, Map<String, Double>> getGenesFromIDs() throws IDConversionException {
		List<String> ids = _ids.get(HeadersIDs.GENE_ID);
		return mapGenericIDsToValues(ids,_container.getGenes().keySet());
	}
	
	
	
	/*****************************
	 * GENERIC MAPPING FUNCTIONS *
	 *****************************/
	
	private Map<String,Map<String,Double>> mapGenericIDsToValues(List<String> ids,Set<String> modelIDs) throws IDConversionException{
		Map<String, Map<String, Double>> toret = new HashMap<String, Map<String, Double>>();
		
		//FIND ALL IDS MATCHING THE VALUE PATTERN
		Map<String, List<String>> valuesMap = new HashMap<String, List<String>>();
		for (String id : _ids.keySet()) {
			Matcher m = HeadersIDs.VALUE_PATTERN.matcher(id);
			if (m.matches()) {
				List<String> values = _ids.get(id);
				String condition = m.group(1);			
				valuesMap.put(condition, values);
			}
		}
		
		if(valuesMap.isEmpty())
			throw new IDConversionException("Could not find mappings using the suggested columns.\n"
					+ "Please make sure everything is correctly mapped.");
		
		for (String cond : valuesMap.keySet()) {
			List<String> values = valuesMap.get(cond);
			Map<String,Double> ids2valueMap = new HashMap<String,Double>();
			for (int i = 0; i < ids.size(); i++) {
				String id = ids.get(i);
				if (modelIDs.contains(id)) {
					String valString = values.get(i).trim();
					Double val = null;
					try {
						val = Double.parseDouble(valString);
					} catch (Exception e) {
						throw new IDConversionException("Cannot convert " + valString + " to Double.");
					}
					ids2valueMap.put(id, val);
				}
			}
			if(ids2valueMap.isEmpty())
				throw new IDConversionException("Could not find mappings using the suggested columns.\n"
						+ "Please make sure everything is correctly mapped.");
			toret.put(cond, ids2valueMap);
		}
		return toret;
	}
	
	private Map<String,Map<String,Double>> mapGenericExtraInfoToValues(Map<String, Map<String, String>> extraInfo) throws IDConversionException{
		Map<String, Map<String, Double>> toret = null;
		
		String selectedID = null;
		List<String> ids = null;
		
		/**
		 * In this case, the first detected ID in the extra information will be
		 * used
		 */
		for (String id : extraInfo.keySet()) {
			if (_ids.containsKey(id)) {
				selectedID = id;
				ids = _ids.get(id);
				break;
			}
		}
		
		if (ids != null) {
			toret = new HashMap<String, Map<String, Double>>();
			
			//FIND ALL IDS MATCHING THE VALUE PATTERN
			Map<String, List<String>> valuesMap = new HashMap<String, List<String>>();
			for (String id : _ids.keySet()) {
				Matcher m = HeadersIDs.VALUE_PATTERN.matcher(id);
				if (m.matches()) {
					List<String> values = _ids.get(id);
					String condition = m.group(1);
					valuesMap.put(condition, values);
				}
			}
			
			if(valuesMap.isEmpty())
				throw new IDConversionException("Could not find mappings using the suggested columns.\n"
						+ "Please make sure everything is correctly mapped.");
			
			for (String cond : valuesMap.keySet()) {
				List<String> values = valuesMap.get(cond);
				Map<String,Double> ids2valMap = new HashMap<String,Double>();
				for (String id : extraInfo.get(selectedID).keySet()) {
					String extraID = extraInfo.get(selectedID).get(id);
					int index = ids.indexOf(extraID);
					if (index > -1) {
						String valString = values.get(index).trim();
						Double val = null;
						try {
							val = Double.parseDouble(valString);
						} catch (Exception e) {
							throw new IDConversionException("Cannot convert  [" + valString + "] to Double.");
						}
						ids2valMap.put(id, val);
					}
				}
				if(ids2valMap.isEmpty())
					throw new IDConversionException("Could not find mappings using the suggested columns.\n"
							+ "Please make sure everything is correctly mapped.");
				toret.put(cond, ids2valMap);
			}
			
			return toret;
		} else
			throw new IDConversionException("None of the IDS could be detected in the model's extra information.");
		
	}
	
}
