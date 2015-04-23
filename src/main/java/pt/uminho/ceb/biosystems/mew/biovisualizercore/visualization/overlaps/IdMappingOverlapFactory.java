package pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps;

import java.util.Set;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces.ILayout;

public class IdMappingOverlapFactory extends AbstractOverlapFactory{

	@Override
	protected Set<String> convertToNodeLayoutId(ILayout lc, String source) {
		return lc.getNodes().get(source).getIds();
	}

	@Override
	protected Set<String> convertToReactionLayoutId(ILayout lc, String source) {
		return lc.getReactions().get(source).getIDs();
	}

	@Override
	protected Set<String> convertToExtraInfoLayoutId(ILayout lc, String source) {
		return lc.getNodes().get(source).getIds();
	}

}
