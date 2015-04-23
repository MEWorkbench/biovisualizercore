/*
 * Copyright 2010
 * IBB-CEB - Institute for Biotechnology and Bioengineering - Centre of Biological Engineering
 * CCTC - Computer Science and Technology Center
 *
 * University of Minho 
 * 
 * This is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This code is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Public License for more details. 
 * 
 * You should have received a copy of the GNU Public License 
 * along with this code. If not, see http://www.gnu.org/licenses/ 
 * 
 * Created inside the SysBioPseg Research Group (http://sysbio.di.uminho.pt)
 */
package pt.uminho.ceb.biosystems.mew.biovisualizercore.gui.io;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OkCancelMiniPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static final String OK_BUTTON_ACTION_COMMAND = "okButtonActionCommand";
	public static final String CANCEL_BUTTON_ACTION_COMMAND = "cancelButtonActionCommand";
	protected JButton okButton;
	protected JButton cancelButton;
	
	public OkCancelMiniPanel(){
		initGUI();
	}


	protected void initGUI(){
		setLayout(new FlowLayout());
		okButton = new JButton("Ok");
		okButton.setActionCommand(OK_BUTTON_ACTION_COMMAND);
		cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);
		add(okButton);
		add(cancelButton);
		
	}
	
	public void addButtonsActionListener(ActionListener actionListener){
		okButton.addActionListener(actionListener);
		cancelButton.addActionListener(actionListener);
	}
	
	public void addOkButtonActionListener(ActionListener actionListener){
		okButton.addActionListener(actionListener);
	}
	
	public void addCancelButtonActionListener(ActionListener actionListener){
		cancelButton.addActionListener(actionListener);
	}
	
	public void setEnabledOkButton(Boolean b){
		okButton.setEnabled(b);
	}
	
	public void removeListeners(){
		for(ActionListener l : okButton.getActionListeners())
			okButton.removeActionListener(l);
		for(ActionListener l : cancelButton.getActionListeners())
			cancelButton.removeActionListener(l);
	}


	public JButton getOkButton() {
		return okButton;
	}


	public void setOkButton(JButton okButton) {
		this.okButton = okButton;
	}


	public JButton getCancelButton() {
		return cancelButton;
	}


	public void setCancelButton(JButton cancelButton) {
		this.cancelButton = cancelButton;
	}
	
	

}
