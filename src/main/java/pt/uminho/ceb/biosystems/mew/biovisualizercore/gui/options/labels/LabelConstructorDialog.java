package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.options.labels;

import java.awt.Component;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.JTableUtils;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.celleditor.ComboEditor;
import pt.uminho.ceb.biosystems.mew.guiutilities.gui.table.cellrender.ComboRenderer;
import pt.uminho.ceb.biosystems.mew.utilities.datastructures.pair.Pair;

public class LabelConstructorDialog extends JComponent implements TableModelListener{
	
	@SuppressWarnings("unchecked")
	public static <T> AbstractLabelsConstructor<T> createDialogLabelConstructor(Component ownerComponent, AbstractLabelsConstructor<T> initialConstructor){
		
		LabelConstructorDialog labelConstructor = new LabelConstructorDialog();
		labelConstructor.setLabelConstructor(initialConstructor);
		
		AbstractLabelsConstructor ret = constructFrame(labelConstructor, ownerComponent);
		return ret;
	}
	
	protected static AbstractLabelsConstructor constructFrame(final LabelConstructorDialog constructorDialog, Component ownerComponent) {
		
		final ConstructorBox box = constructorDialog.new ConstructorBox();
		final JDialog mainDialog = new JDialog();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0f, 0.0f};
		gridBagLayout.rowWeights = new double[]{1.0f, 0.0f, 1.0f};
		mainDialog.getContentPane().setLayout(gridBagLayout);
		
		mainDialog.getContentPane().add(constructorDialog, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		JPanel okCancelPanel = new JPanel();
		okCancelPanel.setLayout(new GridLayout(1, 0, 0, 0));
		mainDialog.getContentPane().add(okCancelPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		final JButton okButton = new JButton("OK");
		okCancelPanel.add(okButton);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				box.setConstructor(constructorDialog.getConfiguredConstructor());
				mainDialog.setVisible(false);
				mainDialog.setModal(false);
				mainDialog.dispose();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		okCancelPanel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainDialog.setVisible(false);
				mainDialog.setModal(false);
				mainDialog.dispose();
			}
		});
		
		mainDialog.setPreferredSize(new Dimension(500, 400));
		mainDialog.pack();
		mainDialog.setModal(true);
		mainDialog.setLocationRelativeTo(ownerComponent); // This line has to be after invoking the pack() method, and before the setVisible() method
		
		mainDialog.setModalExclusionType(ModalExclusionType.NO_EXCLUDE);
		mainDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		f.pack();
		mainDialog.setVisible(true);
		
		return box.c;
	}
	
	
	protected JScrollPane selectedAttsScrollPane;
	protected JTable selectedAttsTable;
	protected JButton addButton;
	protected JButton removeButton;
	protected JScrollPane previewScrollPane;
	protected JTextArea previewTextArea;
	protected JScrollPane availableAttsPanel;
	protected JList availableAttsList;
	
	protected IFactoryLabel tempFactory;
	
	
	private LabelConstructorDialog(){
		super();
		initComponent();
	}
	
	
	protected void initComponent(){
		GridBagLayout thisLayout = new GridBagLayout();
		thisLayout.rowWeights = new double[] {0.0f, 0.0f, 1.0f, 1.0f};
		thisLayout.columnWeights = new double[] {1.0f, 0.0f, 1.0f};
		setLayout(thisLayout);
		
		buildSelectedAttsPanel();
		add(selectedAttsScrollPane, new GridBagConstraints(2, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
		buildAddButton();
		add(addButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(15, 2, 0, 2), 0, 0));
		buildRemoveButton();
		add(removeButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
		buildAvailableAttsPanel();
		add(availableAttsPanel, new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
		buildPreviewPanel();
		add(previewScrollPane, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 0, 2), 0, 0));
	}
	
	protected void buildSelectedAttsPanel(){
		selectedAttsTable = new JTable(){
			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				return (arg1==0) ? false : super.isCellEditable(arg0, arg1);
			}
		};
		selectedAttsScrollPane = new JScrollPane();
		selectedAttsScrollPane.setViewportView(selectedAttsTable);
		selectedAttsScrollPane.setBorder(new TitledBorder("Selected attributes"));
	}
	
	protected void buildAddButton(){
		addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addToSelectedAtts();
				if(((DefaultListModel) availableAttsList.getModel()).getSize()==0)
					addButton.setEnabled(false);
				if(!removeButton.isEnabled())
					removeButton.setEnabled(true);
			}
		});
	}
	
	protected void buildRemoveButton(){
		removeButton = new JButton("x");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] indexes = selectedAttsTable.getSelectedRows();
				if(indexes.length>0)
				{
					int counter = 0;
					for(int i : indexes)
					{
						int row = i-counter;
						Object methodId = ((DefaultTableModel) selectedAttsTable.getModel()).getValueAt(row, 0);
						((DefaultTableModel) selectedAttsTable.getModel()).removeRow(row);
						((DefaultListModel) availableAttsList.getModel()).addElement(methodId);
						counter++;
					}
					if(!addButton.isEnabled())
						addButton.setEnabled(true);
					if(selectedAttsTable.getRowCount()==0)
						removeButton.setEnabled(false);
				}
			}
		});
	}
	
	protected void buildPreviewPanel(){
		previewTextArea = new JTextArea();
		previewTextArea.setEditable(false);
		previewScrollPane = new JScrollPane();
		previewScrollPane.setViewportView(previewTextArea);
		previewScrollPane.setBorder(new TitledBorder("Preview"));
	}
	
	protected void buildAvailableAttsPanel(){
		availableAttsList = new JList(new DefaultListModel());
		availableAttsPanel = new JScrollPane();
		availableAttsPanel.setViewportView(availableAttsList);
		availableAttsPanel.setBorder(new TitledBorder("Available attributes"));
	}
	
	protected void addToSelectedAtts(){
		List<Object> selectedAtts = availableAttsList.getSelectedValuesList();
		if(selectedAtts!=null)
		{
			for(Object at : selectedAtts)
			{
				String[] row = new String[2];
				row[0] = at.toString();
				row[1] = SeparatorsUtils.convertSepToName("");
				((DefaultTableModel) selectedAttsTable.getModel()).addRow(row);
				((DefaultListModel) availableAttsList.getModel()).removeElement(at);
			}
		}
	}
	
	public void setLabelConstructor(AbstractLabelsConstructor alc){
		
		tempFactory = alc.getConstructInfo();
		DefaultTableModel sAttsTableModel = (DefaultTableModel) selectedAttsTable.getModel();
		DefaultListModel aAttslistModel = getEmptyListModel(availableAttsList);
		
		if(sAttsTableModel!=null)
			sAttsTableModel.removeTableModelListener(this);
		
		sAttsTableModel = new DefaultTableModel(new String[]{"Method", "Separator"}, 0);
		
		List<String> addedToTable = new ArrayList<String>();
		for(int i =0; i < alc.countMethods(); i++)
		{
			String[] row = new String[2];
			Pair<String, String> p = alc.getMethodAndSep(i);
			row[0] = p.getA();
			row[1] = SeparatorsUtils.convertSepToName(p.getB());
			sAttsTableModel.addRow(row);
			addedToTable.add(p.getA());
		}
		
		Set<String> all =  alc.getAllMethodIds();
		for(String id :all)
			if(!addedToTable.contains(id))
				aAttslistModel.addElement(id);
		
		selectedAttsTable.setModel(sAttsTableModel);
		availableAttsList.setModel(aAttslistModel);
		JTableUtils.setCellEditor(selectedAttsTable, 1, new ComboEditor(SeparatorsUtils.names));
		JTableUtils.setCellRender(selectedAttsTable, 1, new ComboRenderer(SeparatorsUtils.names));
		
		selectedAttsTable.setRowHeight(25);
		updateUI();
		sAttsTableModel.addTableModelListener(this);
		previewTextArea.setText(getConfiguredConstructor().getLabelMasK());
	}
	
	private DefaultListModel getEmptyListModel(JList list){
		
		DefaultListModel lm = (DefaultListModel) list.getModel();
		
		if(lm==null)
			lm = new DefaultListModel();
		else
			lm.removeAllElements();
		return lm;
	}
	
	public AbstractLabelsConstructor getConfiguredConstructor(){
		
		AbstractLabelsConstructor alc = new AbstractLabelsConstructor(tempFactory);
		
		DefaultTableModel model = (DefaultTableModel) selectedAttsTable.getModel();
		for(int i =0; i < model.getRowCount() ;i++){
			String methodId = (String) model.getValueAt(i, 0);
			String separator = SeparatorsUtils.convertNameToSep((String) model.getValueAt(i, 1));
			alc.addLabelInfo(methodId, separator);
		}
		return alc;
	}
	
	
	
	@Override
	public void tableChanged(TableModelEvent arg0) {
		previewTextArea.setText(getConfiguredConstructor().getLabelMasK());
		
	}
	
	
	private class ConstructorBox{
		public ConstructorBox(){}
		public AbstractLabelsConstructor c;
		public void setConstructor(AbstractLabelsConstructor c){this.c=c;}
	}
	
	
	public static void main(String[] args) {
		AbstractLabelsConstructor<Object> metInfo = new AbstractLabelsConstructor<Object>(new FakeIMethods());
		metInfo.addLabelInfo("id", ";");
		metInfo.addLabelInfo("name", "");
//		
//		JFrame frame = new JFrame();
//		DialogLabelConstructor dlc = new DialogLabelConstructor();
//		frame.getContentPane().add(dlc);
//		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//		frame.pack();
//		frame.setVisible(true);
//		
//		dlc.setLableConstructor(metInfo);
		
		metInfo = LabelConstructorDialog.createDialogLabelConstructor(null, metInfo);
		System.out.println((metInfo==null) ? "NULL" : metInfo.getLabelMasK());
		System.out.println("end");
	}

}
