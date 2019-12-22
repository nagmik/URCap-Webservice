package com.nast.webservice.sender;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.nast.webservice.impl.Constants;
import com.nast.webservice.sender.WebserviceProgramNodeContribution;
import com.nast.webservice.style.Style;
import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.variable.Variable;


public class WebserviceProgramNodeView implements SwingProgramNodeView<WebserviceProgramNodeContribution> {
			
	// Provider	
	ContributionProvider<WebserviceProgramNodeContribution> cProvider;	 	
	private final Style style;
	
	/*************************************
	 * view elements
	 ************************************/
	// LED	
	ImageIcon imagesLed[];	
	int totalLedImages = 5;	
		
	private JPanel configView;
	
	// Warnings
	private Box warningRow;
	ImageIcon iconWarning;	
	private JLabel jLabel_warning_led;
	// Warning Message
	private JTextArea textAreaWarning = new JTextArea("");
	
	// Combobox Endpoint	
	private JLabel jLabel_endpoints;
	private JComboBox<String> cb_endpoints  = new JComboBox<String>();
	
	// Combobox Variables
	private JLabel jLabel_variables;
	private JComboBox<Variable> cb_variables = new JComboBox<Variable>();
	
	// Placeholder
	private static final String PLACEHOLDER = "<none>";
	
	/***************************************
	/* Style
	****************************************/
	WebserviceProgramNodeView(Style style) {
		this.style = style;
	}
	/***************************************
	/* UI
	****************************************/
	@Override
	public void buildUI(JPanel jPanel, final ContributionProvider<WebserviceProgramNodeContribution> provider) {
		cProvider = provider;
		
		Dimension layoutSize = jPanel.getPreferredSize();		
		Dimension dimPanelView 	= new Dimension( layoutSize.width-20, 400);
		
		// LEDs 
		imagesLed = new ImageIcon[totalLedImages];					
		imagesLed[0] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_LED_OFF ) );
		imagesLed[1] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_LED_GREEN ) );
		imagesLed[2] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_LED_YELLOW ) );
		imagesLed[3] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_LED_RED ) );
		imagesLed[4] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_SIGN_WARNING ) );
		
		configView = new JPanel();
		configView.setLayout( new BoxLayout( configView, BoxLayout.Y_AXIS ) );
		configView.setPreferredSize(dimPanelView);
		configView.setMinimumSize(dimPanelView);
		configView.setMaximumSize(dimPanelView);
		
		configView.add( this.createRowWarning() );	
		configView.add( this.createVerticalSpacing() );
		
		configView.add( this.createComboboxEndpoints() );	
		configView.add( this.createVerticalSpacing() );
		
		configView.add( this.createComboboxVariables() );	
		configView.add( this.createVerticalSpacing() );	
				
		
		jPanel.add( configView );
		
	}
	/***************************************
	/* 
	****************************************/
	public void updateView() {
		
		// endpoints
		updateComboboxEndpoints();
		
		// vars
		updateComboboxVariables();
	}
	/***************************************
	/* Spacing
	****************************************/	
	private Component createVerticalSpacing() {
		return Box.createRigidArea(new Dimension(  0, style.getVerticalSpacing() ));
	}	
	
	private Component createHorizontalSpacing() {
		return Box.createRigidArea(new Dimension( style.getHorizontalSpacing(), 0 ));
	}	
	/************************************
	 * Message
	 * 
	 ************************************/
	private Box createRowWarning() {
						
		warningRow = Box.createHorizontalBox();
		warningRow.setAlignmentX(Component.LEFT_ALIGNMENT);	
		
		jLabel_warning_led = new JLabel( imagesLed[2] );
		warningRow.add( jLabel_warning_led );
		
		warningRow.add( createHorizontalSpacing() );		
		
		textAreaWarning.setOpaque(false);
		textAreaWarning.setSize(style.getTextAreaSize1());
		textAreaWarning.setMaximumSize(style.getTextAreaSize1());
		textAreaWarning.setLineWrap(true);
		textAreaWarning.setWrapStyleWord(true);	
		textAreaWarning.setEditable(false);
		warningRow.add(textAreaWarning);			
			
		return warningRow;
	}
	/***************************************
	/* set warning visible
	****************************************/	
	public void setWarningRowVisible( boolean value) {		
		warningRow.setVisible( value );				
	}
	/***************************************
	/* set warning text
	****************************************/		
	public void setWarningText ( String text) {
		textAreaWarning.setText( text );
	}
	/************************************
	 * 
	 ************************************/
	private Box createComboboxEndpoints() {
		
		Box row = Box.createHorizontalBox();
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		jLabel_endpoints = new JLabel("Endpoint");		
		jLabel_endpoints.setSize( style.getLabelSizeLong() );
		jLabel_endpoints.setMaximumSize( style.getLabelSize() );	
		jLabel_endpoints.setMinimumSize( style.getLabelSize() );		
		row.add(jLabel_endpoints);
		
		row.add( createHorizontalSpacing() );				
		
		cb_endpoints.setSize(style.getComboBoxSizeShort());
		cb_endpoints.setMaximumSize(style.getComboBoxSizeShort());		
		cb_endpoints.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<String> comboBox = (JComboBox<String>) e.getSource();						
				String endpoint = (String) comboBox.getSelectedItem();
				cProvider.get().setSelectedEndpoint( endpoint );
				updateComboboxEndpoints();					
			}
		});			
				
		row.add( cb_endpoints );		
		
		return row;
	}
	
	private void updateComboboxEndpoints() {
				
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		String[] endpoints = cProvider.get().getInstallation().getEndpoints();
		String selectedEndpoint = cProvider.get().getSelectedEndpoint();				
				
		model.setSelectedItem( selectedEndpoint );
		for ( String ep : endpoints ) {			
			model.addElement( ep );			
		}
		
		cb_endpoints.setModel(model);		
	}
	
	/************************************
	 * Variable	 	 
	 ************************************/
	private Box createComboboxVariables() {
		
		Box row = Box.createHorizontalBox();
		row.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		jLabel_variables = new JLabel("Log Variable");		
		jLabel_variables.setSize( style.getLabelSizeLong() );
		jLabel_variables.setMaximumSize( style.getLabelSize() );	
		jLabel_variables.setMinimumSize( style.getLabelSize() );		
		row.add(jLabel_variables);
		
		row.add( createHorizontalSpacing() );					
				
		cb_variables.setSize(style.getComboBoxSizeShort());
		cb_variables.setMaximumSize(style.getComboBoxSizeShort());		
		cb_variables.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unchecked")
				JComboBox<Variable> comboBox = (JComboBox<Variable>) e.getSource();						
				Variable endpoint = (Variable) comboBox.getSelectedItem();
				cProvider.get().setVariable( endpoint );
				updateComboboxEndpoints();					
			}
		});		
			
		row.add( cb_variables );		
		
		return row;
	}
	/************************************
	 * 
	 * Sample URCap: idletimesswing
	 ************************************/
	private void updateComboboxVariables() {
				
		List<Object> items = new ArrayList<Object>();
		items.addAll( cProvider.get().getGlobalVariables() );

		Collections.sort(items, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				if (o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase()) == 0) {					
					return o1.toString().compareTo(o2.toString());
				} else {
					return o1.toString().toLowerCase().compareTo(o2.toString().toLowerCase());
				}
			}
		});
		
		cb_variables.setModel( new DefaultComboBoxModel( items.toArray() ) );

		Variable selectedVar = cProvider.get().getSelectedVariable();
		if (selectedVar != null) {
			cb_variables.setSelectedItem(selectedVar);
		}
	}
	
	
}
