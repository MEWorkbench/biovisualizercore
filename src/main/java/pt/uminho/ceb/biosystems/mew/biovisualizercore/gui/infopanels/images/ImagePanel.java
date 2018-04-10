package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.infopanels.images;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import pt.uminho.ceb.biosystems.mew.utilities.graphics.GraphicsUtils;

public class ImagePanel extends JPanel implements ComponentListener {
	
	private static final long		serialVersionUID	= 1L;

	private IImageProvider provider;
	
	public ImagePanel(IImageProvider provider) {
		super();
		this.provider = provider;
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new GridBagLayout());
	}
	
	public synchronized void reset() {

		removeAll();
	}

	public void populateImages(Set<String> metaboliteIDs) {
		reset();
		
		for (final String id : metaboliteIDs) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
						ImageLabel label = new ImageLabel(id, provider, getThis());
						GridBagConstraints cons = new GridBagConstraints();
						cons.weightx = 0;
						cons.gridx = 0;
						cons.gridheight = GridBagConstraints.REMAINDER;
						cons.gridwidth = GridBagConstraints.REMAINDER;
						add(label, cons);
						addComponentListener(label);
				}

				
			});
		}
		
	}
	
	private JComponent getThis() {
		return this;
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

	
}

class ImageLabel extends JPanel implements ComponentListener {
	
	private static final long	serialVersionUID	= 1L;
	private BufferedImage		_currentImage		= null;
	private String[] names = null;
	private JLabel				_iconLabel			= null;
	private JEditorPane			_textArea			= null;
	private Component			_parent				= null;
	private String				_keggID				= null;
	private IImageProvider provider;
	
	public ImageLabel(String keggID, IImageProvider provider,JComponent parent) {
		super();
		this.provider = provider;
		_parent = parent;
		setPreferredSize(new Dimension(parent.getWidth(), parent.getHeight()));
		setLayout(new BorderLayout());
		setOpaque(true);
		setBackground(Color.WHITE);
		_iconLabel = new ScaledImageLabel();
		_iconLabel.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/spinning.gif")));
		add(_iconLabel, BorderLayout.CENTER);
		_textArea = new JEditorPane();
		_textArea.setContentType("text/html");
		_textArea.setEditable(false);
		_textArea.setText("<html>Fetching...</html>");
		add(_textArea, BorderLayout.PAGE_END);
		_keggID = keggID;
		load(_keggID);
	}
	

	
	private void load(final String keggID) {
		try {
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					names = null;
					try {
						_currentImage = provider.getImage(keggID);
						names = provider.getNames(keggID);
					} catch (IOException e) {
						_textArea.setText("<html>An error occurred.</html>");
						e.printStackTrace();
					}
					if (_currentImage != null) {
						rescaleImage(_currentImage);
					} else {
						_iconLabel.setIcon(null);
						_textArea.setText("<html>Kegg info not available.</html>");
					}
					if (names != null) {
						final String mltext = multiLineListLabelText(names);
						if(mltext!=null)
							setToolTipText(mltext);
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
	
	
	public void updateRescaling(){
		rescaleImage(_currentImage);
	}
	
	private void rescaleImage(final BufferedImage image) {
		
		
		
		if (image != null) {
		
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					BufferedImage rescaledImage = image;
					int imageWidth = image.getWidth();
					int imageHeight = image.getHeight();
					int parentHeight = Math.max(_parent.getHeight() - 10, 0);
					int parentWidth = Math.max(_parent.getWidth() - 10, 0);
					if (imageWidth > parentWidth && parentWidth > 0) {
						double factor = ((double) parentWidth) / (double) imageWidth;
						int targetHeight = (int) (((double) imageHeight) * factor);
						rescaledImage = GraphicsUtils.getScaledInstance(image, parentWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
					} else if (imageHeight > parentHeight & parentHeight > 0) {
						double factor = (double) parentHeight / (double) imageHeight;
						int targetWidth = (int) (((double) imageWidth) * factor);
						rescaledImage = GraphicsUtils.getScaledInstance(image, targetWidth, parentHeight, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
					}
					if (rescaledImage != null) {
						ImageIcon icon = new ImageIcon(rescaledImage);
						_iconLabel.setIcon(icon);
					}
					setPreferredSize(new Dimension(parentWidth, parentHeight));
					setMaximumSize(new Dimension(parentWidth, parentHeight));
					revalidate();
					repaint();
					
				}
			});
			
			
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
				rescaleImage(_currentImage);
//		load(_keggID);
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

class ScaledImageLabel extends JLabel {
	
	private static final long	serialVersionUID	= 1L;
	
	protected void paintComponent(Graphics g) {
		ImageIcon icon = (ImageIcon) getIcon();
		if (icon != null) {
			GraphicsUtils.drawScaledImage(icon.getImage(), this, g);
		}
	}
}
