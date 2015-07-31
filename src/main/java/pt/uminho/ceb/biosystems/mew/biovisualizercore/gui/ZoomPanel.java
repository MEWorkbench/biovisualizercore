package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import pt.uminho.ceb.biosystems.mew.biovisualizercore.visualization.LayoutVisualizator;


public class ZoomPanel extends javax.swing.JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private JButton addButtoon;
	private JButton fit2ScreenButton;
	private JButton subButton;
	private LayoutVisualizator lViz;
	
	public ZoomPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			this.setBorder(BorderFactory.createTitledBorder("Zoom"));
			thisLayout.rowWeights = new double[] {0.1};
			thisLayout.rowHeights = new int[] {0};
			thisLayout.columnWeights = new double[] {0.1, 0.1, 0.1};
			thisLayout.columnWidths = new int[] {7, 7, 7};
			this.setLayout(thisLayout);
			{
				ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/zoom_in_w45.png"));
				addButtoon = new JButton(icon);
				this.add(addButtoon, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				
			}
			{
				ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/zoom_out_w45.png"));
				subButton = new JButton(icon);
				this.add(subButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				
				
			}
			{
				ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/zoom_fit_w45.png"));
				fit2ScreenButton = new JButton(icon);
				this.add(fit2ScreenButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void setLayoutVisualizator(LayoutVisualizator lvis){
		this.lViz = lvis;
		
		clearlisteners(fit2ScreenButton);
		clearlisteners(subButton);
		clearlisteners(addButtoon);
		
		fit2ScreenButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lViz.fitToScreen();
				
			}
		});
		
		subButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				lViz.zoomIn(0.9);
			}
		});
		
		addButtoon.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lViz.zoomIn(1.1);
				
			}
		});
		
		
	}

	private void clearlisteners(JButton genericButton) {
		
		for(ActionListener al : genericButton.getActionListeners()){
			genericButton.removeActionListener(al);
		}
	}
}
