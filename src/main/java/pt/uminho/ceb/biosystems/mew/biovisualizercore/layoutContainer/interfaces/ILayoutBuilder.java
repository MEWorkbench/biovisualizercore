package pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.interfaces;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.layoutContainer.LayoutContainer;

/**
 * Interface that defines the method that
 * all readers must implement, in order to build
 * a layout.
 * @author Alberto Noronha
 *
 */
public interface ILayoutBuilder {

	public LayoutContainer buildLayout();
}
