package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import pt.uminho.ceb.biosystems.mew.biocomponents.container.Container;
import pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.listeners.ReactionEvent;

public class KeggReactionImagePanel2 extends JPanel implements ComponentListener, ReactionListener {
	
	private static final long		serialVersionUID	= 1L;
	public static final String		KEGG_REACTION_ID	= "KEGG_REACTION";
	
	private JScrollPane				_scrollPanel		= null;
	protected JPanel				_containerPanel		= null;
	protected Container				_container			= null;
	protected Map<String, String>	_reac2keggIDs		= null;
	
	public KeggReactionImagePanel2(Container container) {
		super();
		_container = container;
		if (_container != null && container.getReactionsExtraInfo().containsKey(KEGG_REACTION_ID)) {
			_reac2keggIDs = _container.getReactionsExtraInfo().get(KEGG_REACTION_ID);
		}
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		_containerPanel = getContainerPanel();
		_containerPanel.setLayout(new GridBagLayout());
		
		_scrollPanel = new JScrollPane();
		_scrollPanel.setViewportView(_containerPanel);
		add(_scrollPanel, BorderLayout.CENTER);
	}
	
	public void reset() {
		synchronized (_containerPanel) {
			_containerPanel.removeAll();
		}
	}
	
	public JPanel getContainerPanel() {
		if (_containerPanel == null) _containerPanel = new JPanel(true);
		return _containerPanel;
	}
	
	public KeggReactionImagePanel2 getThis() {
		return this;
	}
	
	public void populateKeggImages(Set<String> metaboliteIDs) {
		final Set<String> keggids;
		if (!_reac2keggIDs.isEmpty()) {
			keggids = new HashSet<String>();
			for (String id : metaboliteIDs) {
				if (_reac2keggIDs.containsKey(id)) keggids.add(_reac2keggIDs.get(id));
			}
			
			if (keggids.isEmpty()) {
				reset();
			} else {
				reset();
				
				for (final String id : keggids) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							synchronized (_containerPanel) {
								KeggLabel2 label = new KeggLabel2(id, getThis());
								GridBagConstraints cons = new GridBagConstraints();
								cons.weightx = 1;
								cons.gridx = 0;
								cons.gridheight = GridBagConstraints.REMAINDER;
								cons.gridwidth = GridBagConstraints.REMAINDER;
								getContainerPanel().add(label, cons);
								getContainerPanel().addComponentListener(label);
								//								SwingUtilities.updateComponentTreeUI(getContainerPanel());
							}
						}
					});
				}
			}
		}
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		if (_containerPanel != null) {			
			_containerPanel.revalidate();
			_containerPanel.repaint();								
		}
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
		Set<String> ids = event.getIds();
		populateKeggImages(ids);
	}
	
}

class KeggLabel2 extends JPanel implements ComponentListener {
	
	private static final long	serialVersionUID	= 1L;
	private BufferedImage		_currentImage		= null;
//	private JLabel				_iconLabel			= null;
	private ScalablePane		_iconLabel			= null;
	private JEditorPane			_textArea			= null;
	private Component			_parent				= null;
	private String				_keggID				= null;
	
	public KeggLabel2(String keggID, JComponent parent) {
		super();
		_parent = parent;
		setPreferredSize(new Dimension(parent.getWidth(), parent.getHeight()));
		setLayout(new BorderLayout());
		setOpaque(true);
		setBackground(Color.WHITE);
//		_iconLabel = new ScaledImageLabel();
//		_iconLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/spinning.gif")));
//		add(_iconLabel, BorderLayout.CENTER);
		
		_textArea = new JEditorPane();
		_textArea.setContentType("text/html");
		_textArea.setEditable(false);
		_textArea.setText("<html>Fetching...</html>");
		add(_textArea, BorderLayout.PAGE_END);
		_keggID = keggID;
		loadKegg(_keggID);
	}
	
	public KeggLabel2 getThis() {
		return this;
	}
	
	private void loadKegg(final String keggID) {
		try {
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					String[] names = null;
					try {
						_currentImage = KeggInfoProviderReactions.getImageForKEGGID(keggID);
						names = KeggInfoProviderReactions.getNamesForKEGGID(keggID);
					} catch (IOException e) {
						_textArea.setText("<html>An error occurred.</html>");
						e.printStackTrace();
					}
					if (_currentImage != null) {						
						_iconLabel = new ScalablePane(_currentImage,getThis(),true);
						add(_iconLabel,BorderLayout.CENTER);
//						_iconLabel.revalidate();
//						_iconLabel.repaint();
					} else {
						
						_textArea.setText("<html>Kegg info not available.</html>");
					}
					if (names != null) {
						final String mltext = multiLineListLabelText(names);
						if(mltext!=null)
							SwingUtilities.invokeLater(new Runnable() {
								
								@Override
								public void run() {
									_textArea.setText(mltext);
									
								}
							});
					}
					_parent.revalidate();
					_parent.repaint();							
					
				}
			});
		} catch (Exception e) {
			_textArea.setText("<html>An error occured.</html>");
			e.printStackTrace();
		}
	}
	

	
	private String multiLineListLabelText(String[] lines) {
		String toret = "<html><ul>";
		for (String l : lines) {
			toret += "\n";
			toret += "<li>" + l + "</li>";
		}
		toret += "\n</ul></html>";
		return toret;
	}
	
	public void finalize() throws Throwable {
		super.finalize();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		_parent = e.getComponent();
		//		rescaleImage(_currentImage);
		loadKegg(_keggID);
		_parent.revalidate();
		_parent.repaint();
	}
	
	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}