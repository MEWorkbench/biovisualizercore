package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.expandablePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class ExpandablePanel extends javax.swing.JPanel implements ActionListener{

	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	public boolean dfOpen = true;
	
	private ArrayList<JComponent> panels;
	private ArrayList<String> tittles;
	private ArrayList<Boolean> grow;
	private ArrayList<Boolean> openComp;
	private JButton[] buttons;
	GridBagLayout thisLayout;
	
	
	private JPanel mainPanel;
	private Icon showIcon;
	private Icon hideIcon;
	
	public ExpandablePanel(){
		this(false);
	}
	
	private Dimension minDim = new Dimension();
	
	public ExpandablePanel(boolean isAllOpen){
		super();
		dfOpen = isAllOpen;
		panels = new ArrayList<JComponent>();
		tittles = new ArrayList<String>();
		grow = new ArrayList<Boolean>();
		openComp = new ArrayList<Boolean>();
		mainPanel = new JPanel();
	}
	
	
	public void setIcons(Icon toShow, Icon toHide){
		
		if(toHide==null || toShow == null) throw new IllegalArgumentException("The icons must not be null");
		this.showIcon = toShow;
		this.hideIcon = toHide;
	}
	
	
	public void setComponents(ArrayList<JComponent> components, ArrayList<String> tittles, ArrayList<Boolean> grow, ArrayList<Boolean> opened){
		setPanelsWD(components, tittles, grow, opened);
		initGUI();
	}
	
	public void setPanelsWD(ArrayList<JComponent> components, ArrayList<String> tittles, ArrayList<Boolean> grow, ArrayList<Boolean> opened){
		int t =components.size();
		if(t != tittles.size()) throw new IllegalArgumentException("The size of titlles must be equal to the size of components");
		if(t != grow.size()) throw new IllegalArgumentException("The size of grow array must be equal to the size of panels");
		if(opened !=null && t != opened.size()) throw new IllegalArgumentException("The size of opened array must be equal to the size of panels");
		
		for(int i=0; i <  components.size(); i++){
			Boolean isOpen = (opened==null)?true: opened.get(i);
			addComponentWDAt(components.get(i), tittles.get(i), grow.get(i), null, isOpen);
		}
		
	}
	
	public void addComponent( JComponent comp, String name, boolean grow,  Boolean isOpened){
		addComponent(comp, name, grow, isOpened);
		initGUI();
	}
	
	public void addComponentWD( JComponent comp, String name, boolean grow,  Boolean isOpened){
		addComponentWDAt(comp, name, grow, null, isOpened);
	}
	
	public void addComponentWDAt( JComponent comp, String name, boolean grow, Integer position, Boolean isOpened){
		if(comp == null) throw new IllegalArgumentException();
		if(name == null) throw new IllegalArgumentException();
		
		if(position == null || position<=0){
			panels.add(comp);
			tittles.add(name);
			this.grow.add(grow);
			openComp.add((isOpened==null)?dfOpen:isOpened);
		}else{
			panels.add(position,comp);
			tittles.add(position,name);
			this.grow.add(position, grow);
			openComp.add(position,(isOpened==null)?dfOpen:isOpened);
		}
		
		boolean setDim = false;
		double width = minDim.getWidth();
		double height = minDim.getHeight();
		if(comp.getMinimumSize().getWidth() > width){
			width = comp.getMinimumSize().getWidth()+20;
			setDim = true;
		}
		if(comp.getMinimumSize().getHeight() > height){
			height = comp.getMinimumSize().getHeight()+20;
			setDim = true;
		}
		
		if(setDim){
			minDim.setSize(width, height);
		}
	}
	
	protected void removeActionListenersToButtons(){
		if(buttons !=null)
			for(JButton b : buttons) b.removeActionListener(this);
	}
	
	public void constructGUI(){
		initGUI();
	}
	
	private void initGUI() {
		mainPanel.removeAll();
		removeActionListenersToButtons();
		buttons = new JButton[panels.size()];
		try {
			thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[2 * panels.size() + 1];
			thisLayout.rowHeights = new int[2 * panels.size() + 1];
			
			thisLayout.rowWeights[thisLayout.rowWeights.length-1] = Double.MIN_VALUE;
			thisLayout.rowHeights[thisLayout.rowWeights.length-1] = 0;
			
			thisLayout.columnWeights = new double[] {0.1};
			thisLayout.columnWidths = new int[] {0};
			mainPanel.setLayout(thisLayout);
			
			{
				for(int i = 0; i < panels.size(); i++){
					
					buttons[i] = constructButton();
					buttons[i].setActionCommand("" + i);
					buttons[i].addActionListener(this);
					
					mainPanel.add(buttons[i], new GridBagConstraints(0, 2*i, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					mainPanel.add(panels.get(i), new GridBagConstraints(0, 2*i + 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					
					updateButton(i);
					updatePanel(i);
					
//					setButtonProperties(buttons[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setLayout(new GridLayout());
		JScrollPane jScrollPane  = new JScrollPane();
		jScrollPane.setViewportView(mainPanel);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.add(jScrollPane );
	}
	
	


	private JButton constructButton(){
		JButton button = new JButton();
		button.setHorizontalAlignment(SwingConstants.LEFT);
//		button.setBorder(BorderFactory.createRaisedBevelBorder());
		button.setBorderPainted(true);
		button.setContentAreaFilled(true);
		button.setFocusPainted(false);
		return button;
	}
	
	private void updatePanel(int i){
		double d = (grow.get(i) && openComp.get(i))?1.0:0.0;
		if(grow.get(i)) thisLayout.rowWeights[2*i + 1] = d;
		JComponent comp = panels.get(i);
		comp.setVisible(openComp.get(i));
	}
	
	private void updateButton(int i) {
		JButton b = buttons[i];
		String name = null;
		if(openComp.get(i)){
			name = "Hide ";
			if(hideIcon!=null)b.setIcon(hideIcon);
		}else{
			name = "Show ";
			if(showIcon!=null)b.setIcon(showIcon);
		}
		b.setText(name + tittles.get(i));
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		int index = Integer.valueOf(e.getActionCommand());
		System.out.println("INDEX: " + index);
		openComp.set(index, !openComp.get(index));
		printDim();
		updateButton(index);
		updatePanel(index);
		updateUI();
	}

	
	public void addButtonsListener(ActionListener listener){
		for(JButton button : this.buttons) button.addActionListener(listener);
	}
	
	
	public void printDim(){
		int i =1;
		for(JComponent c : panels){
			System.out.println( i + "\tmin: " +c.getMinimumSize().getWidth() + ", " +c.getMinimumSize().getHeight() + "\t" +
					"" +c.getSize().getWidth() + ", " +c.getSize().getHeight() );
		}
		System.out.println(minDim.getWidth() + "," + minDim.getHeight());
	}
	
	@Override
	public Dimension getMinimumSize() {
		return minDim;
	}
	
	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		
		Icon showIcon = new ImageIcon(ExpandablePanel.class.getClassLoader().getResource("biovisualizer/images/closed.png"));
		Icon hideIcon = new ImageIcon(ExpandablePanel.class.getClassLoader().getResource("biovisualizer/images/open.png"));
		
		ExpandablePanel exp = new ExpandablePanel();
		exp.setIcons(showIcon, hideIcon);
		
		
		JFrame frame = new JFrame();
		
		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		JScrollPane panel = new JScrollPane();
		
		
		int lines = 50;
		String[][] data = new String[lines][1];
		for(int i =0 ; i < lines; i++){
			data[i][0] = i+"";
		}
		
		JTable table = new JTable(data, new String[] {"teste"});
		
//		table.setMinimumSize(new Dimension(50,50));
		table.setPreferredScrollableViewportSize(table.getMinimumSize());
		table.setFillsViewportHeight(true);
		panel.setViewportView(table);
		panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//		panel.setv
		p1.add(panel);
		
		JPanel p2 = new JPanel();
		p2.setSize(new Dimension(100, 100));
		p2.setMinimumSize(new Dimension(100, 100));
		p2.setPreferredSize(new Dimension(100, 100));
		JPanel p3 = new JPanel();
		p3.setSize(new Dimension(100, 100));
		p3.setMinimumSize(new Dimension(100, 100));
		p3.setPreferredSize(new Dimension(100, 100));
		
		ArrayList<JComponent> panels = new ArrayList<JComponent>();
		panels.add(p1);
		panels.add(p2);
		panels.add(p3);
		
		ArrayList<String> tittles = new ArrayList<String>();;
		tittles.add( "PANEL 1");
		tittles.add( "PANEL 2");
		tittles.add( "PANEL 3");
		
		ArrayList<Boolean> grow = new ArrayList<Boolean>();;
		grow.add( true);
		grow.add( false);
		grow.add( false);
		
		ArrayList<Boolean> isOpen = new ArrayList<Boolean>();;
		isOpen.add( true);
		isOpen.add( false);
		isOpen.add( false);
		
		
		exp.setComponents(panels, tittles, grow, isOpen);
		
		frame.getContentPane().add(exp);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
