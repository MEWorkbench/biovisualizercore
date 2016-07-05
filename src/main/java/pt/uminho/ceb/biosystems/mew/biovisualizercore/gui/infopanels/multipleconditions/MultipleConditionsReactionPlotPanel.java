package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.multipleconditions;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.StyleManager.ChartTheme;
import com.xeiam.xchart.StyleManager.ChartType;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.ReactionListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.OverlapsListener;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.overlaps.components.ReactionEvent;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.IOverlapObject;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.overlaps.MultipleConditionsOverlap;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.collection.CollectionUtils;

public class MultipleConditionsReactionPlotPanel extends JPanel implements ReactionListener, OverlapsListener, ComponentListener {
	
	private static final long		serialVersionUID		= 1L;
	private static final boolean	_debug					= false;
	private static final String		DEFAULT_RECTION_LABEL	= "IDs: ";
	
	private IOverlapObject			_overlapObject			= null;
	private JLabel					_reactionIDPanel		= null;
	private JPanel					_plotPanel				= null;
	private Set<String>				_reactionIDs			= null;
	
	public MultipleConditionsReactionPlotPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		_reactionIDPanel = new JLabel(DEFAULT_RECTION_LABEL);
		add(_reactionIDPanel, BorderLayout.PAGE_START);
	}
	
	@Override
	public void selectedOverlapChanged(OverlapEvent event) {
		IOverlapObject newOverlap = event.get_selectedOverlap();
		if (newOverlap == null) {
			if (_debug) System.out.println("[selectedOverlapChanged]: new overlap is null!");
			_overlapObject = newOverlap;
			clearPlot();
		} else {
			if (_debug) System.out.println("[selectedOverlapChanged]: new overlap is NOT null! Proceed to plot!");
			_overlapObject = newOverlap;
			clearPlot();
			buildDataPlot();
		}
	}
	
	private void clearPlot() {
		if (_reactionIDs == null) {
			if (_debug) System.out.println("[clearPlot]: MetaboliteIDs are null, clear text!");
			_reactionIDPanel.setText(DEFAULT_RECTION_LABEL);
		}
		
		if (_plotPanel != null) {
			if (_debug) System.out.println("[clearPlot]: PlotPanel not null, removeAll");
			_plotPanel.removeAll();
			remove(_plotPanel);
			_plotPanel = null;
			
		}
	}
	
	private void buildDataPlot() {
		if (_debug) System.out.println("[buildDataPlot]: begin");
		
		if (_overlapObject != null && (_reactionIDs != null && !_reactionIDs.isEmpty())) {
			if (_overlapObject instanceof MultipleConditionsOverlap) {
				
				Map<String, IOverlapObject> conditions = ((MultipleConditionsOverlap) _overlapObject).get_conditions();
				String name = ((MultipleConditionsOverlap) _overlapObject).get_id();
				Chart chart = new ChartBuilder().chartType(ChartType.Bar).width(800).height(600).title(name).xAxisTitle("Conditions").yAxisTitle("Values").theme(ChartTheme.GGPlot2).build();
				
				for (String rID : _reactionIDs) {
					
					ArrayList<String> conds = new ArrayList<String>();
					ArrayList<Number> val = new ArrayList<Number>();
					for (String cond : conditions.keySet()) {
						IOverlapObject condOverlap = conditions.get(cond);
						Double v = condOverlap.getEdgesOriginalValues().get(rID);
						if (v != null) {
							conds.add(cond);
							val.add(v);
						}
					}
					if (!conds.isEmpty() && !val.isEmpty()) chart.addSeries(rID, conds, val);
					chart.getStyleManager().setLegendVisible(false);
					chart.getStyleManager().setXAxisTitleVisible(false);
					chart.getStyleManager().setYAxisTitleVisible(false);
				}
				if (chart.getSeriesMap().size() > 0) {
					_plotPanel = new ChartPanel(chart);
					add(_plotPanel, BorderLayout.CENTER);
				}
				revalidate();
				repaint();
			}
		}
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		this.revalidate();
		this.repaint();
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
	}
	
	@Override
	public void reactionChanged(ReactionEvent event) {
		Set<String> newIds = event.getIds();
		
		if (_reactionIDs == null && (newIds != null && !newIds.isEmpty())) {
			if (_debug) System.out.println("[reactionChanged]: IDs are null, new IDs are neither null nor empty");
			_reactionIDs = newIds;
			clearPlot();
			buildDataPlot();
			System.out.println("NEW IDS: "+_reactionIDs.toString());
		} else if (newIds == null || newIds.isEmpty()) {
			if (_debug) System.out.println("[reactionChanged]: New IDs are null or empty");
			_reactionIDs = null;
			clearPlot();
		} else if (_reactionIDs != null && newIds != null) {
			Set<String> diff = CollectionUtils.getSetDiferenceValues(_reactionIDs, newIds);
			if (_debug) System.out.print("[reactionChanged]: Both new IDs and old IDs are not null... ");
			if (!diff.isEmpty()) {
				if (_debug) System.out.println("and there ARE differences!");
				_reactionIDs = newIds;
				_reactionIDPanel.setText(DEFAULT_RECTION_LABEL + " " + _reactionIDs.toString());
				clearPlot();
				buildDataPlot();
			} else if (_debug) System.out.println("but there are NO differences!");
		}
		
	}
	
}
