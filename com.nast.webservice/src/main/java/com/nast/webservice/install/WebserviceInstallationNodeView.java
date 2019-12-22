package com.nast.webservice.install;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.nast.webservice.helper.Tools;
import com.nast.webservice.impl.Constants;
import com.nast.webservice.install.WebserviceInstallationNodeContribution;
import com.nast.webservice.style.Style;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class WebserviceInstallationNodeView implements SwingInstallationNodeView<WebserviceInstallationNodeContribution> {
	

	private final Style style;
			
	// Provider	
	WebserviceInstallationNodeContribution cInstallationNode;	
	
	// LED
	ImageIcon imagesLed[];			
	int totalLedImages = 5;				
	
	// Service / Daemon 
	private JButton btnStartDaemon;
	private JButton btnStopDaemon;
	
	private JLabel 	jLabel_config_service;
	private JLabel 	jLabel_config_service_led;	
		
	// REST // Server
	private JLabel 	jLabel_config_rest_server;
	private JTextField rest_server;
	
	// REST API KEY
	private JLabel 	jLabel_config_api_key;
	private JTextField api_key;
		
	// Endpoint add
	private JLabel 	jLabel_endpoint_add;
	private JTextField endpoint_add;
	private JButton btnAddEndpoint;
	
	// Endpoint rm list 	
	private JLabel 	jLabel_endpoint_rm;
	private JComboBox<String> cb_endpoints;
	private JButton btnRmEndpoint;	
	
	
	// URCAP IS ACTIVE
	public JCheckBox cbUrcapActive;	

	/************************************
	 * 
	 * 
	 ************************************/
	public  WebserviceInstallationNodeView(Style style) {
		this.style = style;
	}
	
	/************************************
	 * 
	 * 
	 ************************************/
	@Override
	public void buildUI(JPanel jPanel, final  WebserviceInstallationNodeContribution installationNode) {
		
		cInstallationNode = installationNode;
		
		Dimension layoutSize = jPanel.getPreferredSize();
		
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));		
		jPanel.add( createRowLogo( layoutSize.width ) );
		jPanel.add( createView( layoutSize ) );	
		
		
	}	
	/************************************
	 * 
	 * 
	 ************************************/
	public void updateView() {
				
		// update checkbox
		updateCheckBoxUrcapActive();		
		
	}
	/************************************
	 * 
	 * 
	 ************************************/
	// private void updateUI() {}
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
	 * logo
	 * 
	 ************************************/
	private Box createRowLogo( int containerWidth ) {
						
		Box logoBox = Box.createHorizontalBox();
		logoBox.setAlignmentX(Component.LEFT_ALIGNMENT);	
		
		ImageIcon logo 	= new ImageIcon( getClass().getResource( Constants.NAST_INSTALL_LOGO ) );
		JLabel logoLabel = new JLabel( logo );
		
		int spacerWidth = (int) (containerWidth - logoLabel.getPreferredSize().getWidth() - 10);		
		Dimension dimLabel = new Dimension( spacerWidth, 40);
				
		JLabel blank = new JLabel("");		
		blank.setSize( dimLabel );
		blank.setMaximumSize( dimLabel );
		blank.setPreferredSize( dimLabel );
				
		logoBox.add( blank );				
		logoBox.add( logoLabel );					
			
		return logoBox;
	}
	/************************************
	 * create View
	 ************************************/
	private JPanel createView( Dimension layoutSize ) {
						
		// LEDs 
		imagesLed = new ImageIcon[totalLedImages];					
		imagesLed[0] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_LED_OFF ) );
		imagesLed[1] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_LED_GREEN ) );
		imagesLed[2] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_LED_YELLOW ) );
		imagesLed[3] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_LED_RED ) );
		imagesLed[4] = new ImageIcon( getClass().getResource( Constants.ICON_PATH + Constants.RES_SIGN_WARNING ) );
							
		JPanel jPanel1 = new JPanel();		
		jPanel1.setLayout( new BoxLayout( jPanel1, BoxLayout.Y_AXIS ) );					
								
		jPanel1.add( createCheckBoxUrcapActive() );
		jPanel1.add( createVerticalSpacing() );
		
		jPanel1.add( createRowWebService() );
		jPanel1.add( createVerticalSpacing() );			
		
		jPanel1.add( createRestServerInput() );
		jPanel1.add( createVerticalSpacing() );		                 
		
		jPanel1.add( createApiKeyInput() );
		jPanel1.add( createVerticalSpacing() );
				
		jPanel1.add( createEndpointInput() );
		jPanel1.add( createVerticalSpacing() );		
		
		jPanel1.add( createComboBoxRmEndpoints() );
		jPanel1.add( createVerticalSpacing() );		
		
			
		return jPanel1;		
	}
	
	/************************************
	 * URCap Active
	 ************************************/
	private Box createCheckBoxUrcapActive() {
		
		Box cbBox = Box.createHorizontalBox();
		cbBox.setAlignmentX(Component.LEFT_ALIGNMENT);	
		
		cbUrcapActive = new JCheckBox("URCAP is activated");
		this.cbUrcapActive.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {												
				boolean isSelected = cbUrcapActive.isSelected();
				cInstallationNode.setUrcapActive( isSelected );				
				updateCheckBoxUrcapActive();				
			}
		});		
		cbBox.add(cbUrcapActive);					
		return cbBox;
	}
	/************************************
	 * update check box urcap active
	 ************************************/
	private void updateCheckBoxUrcapActive() {				
		boolean isSelected = cInstallationNode.getUrcapActive();	
		cbUrcapActive.setSelected( isSelected );	
		if( !isSelected ) {
			cInstallationNode.onStopClick();
		}
	}
	
	/************************************
	 * 
	 ************************************/	
	private Box createRowWebService() {
		
		Box row = Box.createHorizontalBox();
		row.setAlignmentX(Component.LEFT_ALIGNMENT);	
				
		jLabel_config_service_led = new JLabel( imagesLed[0] );
		row.add( jLabel_config_service_led );
		
		row.add( createHorizontalSpacing() );
						
		jLabel_config_service = new JLabel("WebService");
		jLabel_config_service.setSize( style.getLabelSizeLong_1() );
		jLabel_config_service.setMaximumSize( style.getLabelSizeLong_1() );		
		row.add(jLabel_config_service);
		
		row.add( createHorizontalSpacing() );
		btnStartDaemon = new JButton("Start");
		btnStartDaemon.setSize(style.getButtonSize());
		btnStartDaemon.setMaximumSize(style.getButtonSize());
		btnStartDaemon.setMinimumSize(style.getButtonSize());
		btnStartDaemon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cInstallationNode.onStartClick();
			}
		});
		row.add(btnStartDaemon);
		
		row.add( createHorizontalSpacing() );
		
		btnStopDaemon = new JButton("Stop");
		btnStopDaemon.setSize(style.getButtonSize());
		btnStopDaemon.setMaximumSize(style.getButtonSize());
		btnStopDaemon.setMinimumSize(style.getButtonSize());
		btnStopDaemon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cInstallationNode.onStopClick();
			}
		});
		row.add(btnStopDaemon);					
				
		return row;
	}	
	/************************************
	 * activate/deactivate Button
	 ************************************/	
	public void setStartButtonEnabled(boolean enabled) {		
		btnStartDaemon.setEnabled(enabled);		
	}
	/************************************
	 * activate/deactivate Button
	 ************************************/	
	public void setStopButtonEnabled(boolean enabled) {		
		btnStopDaemon.setEnabled(enabled);		
	}		
	/************************************
	 * Daemon State LED
	 ************************************/	
	public void setDaemonState(boolean running) {
		if(running){
			jLabel_config_service_led.setIcon( imagesLed[1] );
		}else {
			jLabel_config_service_led.setIcon( imagesLed[0] );		
		}
	}		
	/************************************
	 * REST Server Input
	 ************************************/
	private Box createRestServerInput() {
		
		Box row = Box.createHorizontalBox();
		row.setAlignmentX(Component.LEFT_ALIGNMENT);	
				
		jLabel_config_rest_server = new JLabel("Server");		
		jLabel_config_rest_server.setSize( style.getLabelSizeLong() );
		jLabel_config_rest_server.setMaximumSize( style.getLabelSizeLong() );	
		jLabel_config_rest_server.setMinimumSize( style.getLabelSizeLong() );		
		row.add(jLabel_config_rest_server);
		
		row.add( createHorizontalSpacing() );
		
		rest_server = new JTextField(); 
		rest_server.setFocusable(false);
		rest_server.setPreferredSize(style.getInputfieldSizeLong());
		rest_server.setMaximumSize(style.getInputfieldSizeLong());
				
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = cInstallationNode.getKeyboardForRestServer();
				keyboardInput.show(rest_server, cInstallationNode.getCallbackForSmtpServer());
			}
		};						
		rest_server.addMouseListener(mouseAdapter);
		row.add(rest_server);
		
		return row;	
	}
	
	public void setRestServer(String value) {
		rest_server.setText(value);
	}
	
	/************************************
	 * API Key Input
	 ************************************/
	private Box createApiKeyInput() {
		
		Box row = Box.createHorizontalBox();
		row.setAlignmentX(Component.LEFT_ALIGNMENT);	
				
		jLabel_config_api_key = new JLabel("API-Key");
		jLabel_config_api_key.setSize(style.getLabelSizeLong());
		jLabel_config_api_key.setMaximumSize(style.getLabelSizeLong());	
		jLabel_config_api_key.setMinimumSize(style.getLabelSizeLong());		
		row.add(jLabel_config_api_key);
		
		row.add( createHorizontalSpacing() );
		
		api_key = new JTextField(); 
		api_key.setFocusable(false);
		api_key.setPreferredSize(style.getInputfieldSizeLong());
		api_key.setMaximumSize(style.getInputfieldSizeLong());
				
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = cInstallationNode.getKeyboardForApiKey();
				keyboardInput.show(api_key, cInstallationNode.getCallbackForApiKey());
			}
		};						
		api_key.addMouseListener(mouseAdapter);
		row.add(api_key);
		
		return row;	
	}
	public void setApiKey(String value) {
		api_key.setText( value );
	}
	
	/************************************
	 * Endpoint Input
	 ************************************/
	private Box createEndpointInput() {
		
		Box row = Box.createHorizontalBox();
		row.setAlignmentX(Component.LEFT_ALIGNMENT);	
				
		jLabel_endpoint_add = new JLabel("Add Endpoint");		
		jLabel_endpoint_add.setSize( style.getLabelSizeLong() );
		jLabel_endpoint_add.setMaximumSize( style.getLabelSizeLong() );	
		jLabel_endpoint_add.setMinimumSize( style.getLabelSizeLong() );		
		row.add(jLabel_endpoint_add);
		
		row.add( createHorizontalSpacing() );
		
		endpoint_add = new JTextField(); 
		endpoint_add.setFocusable(false);
		endpoint_add.setPreferredSize(style.getInputfieldSizeLong());
		endpoint_add.setMaximumSize(style.getInputfieldSizeLong());
				
		MouseAdapter mouseAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				KeyboardTextInput keyboardInput = cInstallationNode.getKeyboardForEndpoint();
				keyboardInput.show(endpoint_add, cInstallationNode.getCallbackForEndpoint());
			}
		};						
		endpoint_add.addMouseListener(mouseAdapter);
		row.add(endpoint_add);
		
		row.add( createHorizontalSpacing() );
		btnAddEndpoint = new JButton("add");
		btnAddEndpoint.setSize(style.getButtonSizeShort());
		btnAddEndpoint.setMaximumSize(style.getButtonSizeShort());
		btnAddEndpoint.setMinimumSize(style.getButtonSizeShort());
		btnAddEndpoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				if ( !Tools.isInList( endpoint_add.getText(), cInstallationNode.getEndpoints() ) ){
					cInstallationNode.addEndpointByName( endpoint_add.getText() );
					endpoint_add.setText( "" );
					updateEndpointEntries();
				}else{
					System.out.println("already in list");
				}								
			}
		});
		row.add(btnAddEndpoint);
		
		return row;	
	}
	public void setEndpoint(String value) {
		endpoint_add.setText( value );
	}
	public String getEndpoint() {
		return endpoint_add.getText();
	}
	/************************************
	 * ComboBox rm endpoint
 	 ************************************/
	private Box createComboBoxRmEndpoints() {
		Box row = Box.createHorizontalBox();
		row.setAlignmentX(Component.LEFT_ALIGNMENT);		
		
		jLabel_endpoint_rm = new JLabel("Remove Endpoint");		
		jLabel_endpoint_rm.setSize( style.getLabelSizeLong() );
		jLabel_endpoint_rm.setMaximumSize( style.getLabelSizeLong() );	
		jLabel_endpoint_rm.setMinimumSize( style.getLabelSizeLong() );		
		row.add(jLabel_endpoint_rm);
		
		row.add( createHorizontalSpacing() );		
		
		String[] currentEndpoints = cInstallationNode.getEndpoints();
		cb_endpoints = new JComboBox<String>( currentEndpoints );		
		cb_endpoints.setSize(style.getComboBoxSize());
		cb_endpoints.setMaximumSize(style.getComboBoxSize());		
		/*
		cb_endpoints.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//@SuppressWarnings("unchecked")
				//JComboBox<String> comboBox = (JComboBox<String>) e.getSource();				
				//String item = (String) comboBox.getSelectedItem();
				// ..
			}
		});	
		*/		
		row.add( cb_endpoints );		
		
		row.add( createHorizontalSpacing() );		
		btnRmEndpoint = new JButton("rm");
		btnRmEndpoint.setSize(style.getButtonSizeShort());
		btnRmEndpoint.setMaximumSize(style.getButtonSizeShort());
		btnRmEndpoint.setMinimumSize(style.getButtonSizeShort());
		btnRmEndpoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				String item = (String) cb_endpoints.getSelectedItem();
				cInstallationNode.rmEndpointByName( item );
				updateEndpointEntries();
			}
		});
		row.add(btnRmEndpoint);
		
		return row;
	}
	/************************************
	 *
 	 ************************************/
	private void updateEndpointEntries() {		
		String[] currentEndpoints = cInstallationNode.getEndpoints();	
		Arrays.sort( currentEndpoints );
		cb_endpoints.setModel( new DefaultComboBoxModel<String>( currentEndpoints ) );		
	}
	
	
}
