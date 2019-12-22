package com.nast.webservice.install;

import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

import com.nast.webservice.install.ComWeb;
import com.nast.webservice.install.WebserviceInstallationNodeView;
import com.nast.webservice.install.XmlRpcDaemonInterface;
import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.InstallationAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.inputvalidation.InputValidationFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;

import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class WebserviceInstallationNodeContribution implements InstallationNodeContribution {
	
	private final InstallationAPI installationAPI;	
	
	private final WebserviceInstallationNodeView view;
	private final KeyboardInputFactory keyboardFactory;	
	private final InputValidationFactory validatorFactory;	
	private DataModel model;	
	
	private final ComWeb comWeb;
		
	// DAEMON
	private static final String XMLRPC_VARIABLE = "webDaemon";
	private XmlRpcDaemonInterface xmlRpcDaemonInterface;
	
	private static final String DAEMON_ENABLED_KEY = "webserviceDaemonEnabled";
	private static final boolean DAEMON_ENABLED_DEFAULT_VALUE = true;
	
	private static final String XMLRPC_HOST = "127.0.0.1";
	private static final int XMLRPC_PORT = 33025;
	
	private Timer uiTimer;
	private boolean pauseTimer = false;
	
	// REST	Server
	private static final String REST_SERVER_KEY = "restServer";
	private static final String REST_SERVER_DEFAULT_VALUE = "https://api.example.de/";
	
	// REST API KEY
	private static final String REST_API_KEY = "restApiKey";
	private static final String REST_API_KEY_DEFAULT_VALUE = "API-KEY-1234";
	
	// REST ENDPOINT
	
	private static final String ENDPOINTS_KEY = "restEndpoints";
	private static final String ENDPOINT_DEFAULT = "simpleServerScript.php";
	private static final String[] ENDPOINTS_DEFAULT_VALUE = new String[] { ENDPOINT_DEFAULT };		
		
	// URCAP Active
	private static final String WS_URCAP_ACTIVE_KEY = "urcapWebserviceActive";
	private static final boolean WS_URCAP_ACTIVE_KEY_DEFAULT_VALUE = true;
	
	/************************************
	 * 
	 * 
	 ************************************/
	public WebserviceInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, WebserviceInstallationNodeView view, ComWeb comWeb) {
		
		this.keyboardFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getKeyboardInputFactory();
		this.validatorFactory = apiProvider.getUserInterfaceAPI().getUserInteraction().getInputValidationFactory();
		
		this.model = model;
		this.view = view;
						
		this.installationAPI = apiProvider.getInstallationAPI();
		this.comWeb = comWeb;
				
		applyDesiredDaemonStatus();
	}
	/************************************
	 * 
	 * 
	 ************************************/
	@Override
	public void openView() {
		
		// Timer 
		initTimer();					
		
		// set rest server
		view.setRestServer( getRestServer() );
		
		// set api key 
		view.setApiKey( getApiKey() );
						
		// update view
		view.updateView();
	}	
	/************************************
	 * 
	 * 
	 ************************************/
	@Override
	public void closeView() {
		
	}
	/************************************
	 * 
	 * 
	 ************************************/
	public boolean isDefined() {
		return true;
	}	
	/************************************
	 * 
	 * 
	 ************************************/
	@Override
	public void generateScript(ScriptWriter writer) {
		
		// URCap isActivated	
		boolean urcapIsActivated 		= getUrcapActive();		
		
		// DAEMON STATE
		DaemonContribution.State state 	= getDaemonState();
		
		if( urcapIsActivated ) {
			if (state == DaemonContribution.State.RUNNING) {					
				String rpcString = "rpc_factory(\"xmlrpc\",\"http://" + XMLRPC_HOST + ":" + String.valueOf( XMLRPC_PORT ) + "/RPC2\")";				
				writer.assign(XMLRPC_VARIABLE, rpcString );
			}else {
				writer.appendLine("# WEBSERVICE SERVICE NOT RUNNING");										
			}
		}else {
			writer.appendLine("# WEBSERVICE URCAP NOT ACTIVATED");			
		}
	}
	/************************************
	 * 
	 * Sample URCap: MyDaemonSwing
	 ************************************/
	private void initTimer() {
		
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!pauseTimer) {
							updateUI();
						}
					}
				});
			}
		}, 0, 1000);
	}
	/************************************
	 * 
	 * 
	 ************************************/	
	private void updateUI() {
		// Daemon state
		DaemonContribution.State state = getDaemonState();

		// Button enable true/false
		if (state == DaemonContribution.State.RUNNING) {
			view.setStartButtonEnabled(false);
			view.setStopButtonEnabled(true);
			view.setDaemonState(true);
		} else {
			view.setStartButtonEnabled(true);
			view.setStopButtonEnabled(false);
			view.setDaemonState(false);
		}
	}
	/********************************************
	 * 
	 * 
	 *******************************************/	
	// UI Button start daemon
	public void onStartClick() {
		model.set(DAEMON_ENABLED_KEY, true);
		applyDesiredDaemonStatus();
	}
	
	/********************************************
	 * 
	 * 
	 *******************************************/
	// UI Button stop daemon
	public void onStopClick() {
		model.set(DAEMON_ENABLED_KEY, false);
		applyDesiredDaemonStatus();
	}
	
	/********************************************
	 * 
	 * 
	 *******************************************/	
	private void applyDesiredDaemonStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if ( isDaemonEnabled() && getUrcapActive() ) {					
					try {
						pauseTimer = true;
						awaitDaemonRunning(5000);						
					} catch(Exception e){
						System.out.println(e.getMessage());						
					} finally {
						pauseTimer = false;
					}
				} else {
					comWeb.getDaemonContribution().stop();
				}
			}
		}).start();
	}
	/********************************************
	 * 
	 * 
	 *******************************************/	
	private void awaitDaemonRunning(long timeOutMilliSeconds) throws InterruptedException {
		comWeb.getDaemonContribution().start();
		long endTime = System.nanoTime() + timeOutMilliSeconds * 1000L * 1000L;
		while(System.nanoTime() < endTime && (comWeb.getDaemonContribution().getState() != DaemonContribution.State.RUNNING || !xmlRpcDaemonInterface.isReachable())) {
			Thread.sleep(100);
		}
	}	
	
	/********************************************
	 * 
	 * 
	 *******************************************/
	public DaemonContribution.State getDaemonState() {
		return comWeb.getDaemonContribution().getState();
	}

	public Boolean isDaemonEnabled() {
		return model.get(DAEMON_ENABLED_KEY, DAEMON_ENABLED_DEFAULT_VALUE);
	}

	public String getXMLRPCVariable(){
		return XMLRPC_VARIABLE;
	}

	public XmlRpcDaemonInterface getXmlRpcDaemonInterface() {
		return xmlRpcDaemonInterface;
	}
	
	/****************************
	* URCAP ACTIVE
	*****************************/
	public void setUrcapActive( final boolean val ) {			
		model.set(WS_URCAP_ACTIVE_KEY, val);								
	}
	
	public boolean getUrcapActive() {
		return model.get(WS_URCAP_ACTIVE_KEY, WS_URCAP_ACTIVE_KEY_DEFAULT_VALUE);
	}
	
	/****************************
	* Server Input
	*****************************/
	public KeyboardTextInput getKeyboardForRestServer() {
		KeyboardTextInput keyboardInput = keyboardFactory.createStringKeyboardInput();
		keyboardInput.setInitialValue( getRestServer() );
		keyboardInput.setErrorValidator(validatorFactory.createStringLengthValidator(1, 50));
		return keyboardInput;
	}

	public KeyboardInputCallback<String> getCallbackForSmtpServer() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setRestServer(value);
				view.setRestServer(value);
			}
		};
	}
	
	public String getRestServer() {
		return model.get(REST_SERVER_KEY, REST_SERVER_DEFAULT_VALUE);
	}

	public void setRestServer(String message) {
		if ("".equals(message)) {
			resetToDefaultRestServer();
		} else {
			model.set( REST_SERVER_KEY, message );
		}
	}

	private void resetToDefaultRestServer() {
		view.setRestServer(REST_SERVER_DEFAULT_VALUE);
		model.set(REST_SERVER_KEY, REST_SERVER_DEFAULT_VALUE);
	}
			
	/****************************
	* API KEY Input
	*****************************/
	public KeyboardTextInput getKeyboardForApiKey() {
		KeyboardTextInput keyboardInput = keyboardFactory.createStringKeyboardInput();
		keyboardInput.setInitialValue( getApiKey() );
		keyboardInput.setErrorValidator(validatorFactory.createStringLengthValidator(1, 50));
		return keyboardInput;
	}

	public KeyboardInputCallback<String> getCallbackForApiKey() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {
				setApiKey(value);
				view.setApiKey(value);
			}
		};
	}
	
	public String getApiKey() {
		return model.get(REST_API_KEY, REST_API_KEY_DEFAULT_VALUE);
	}

	public void setApiKey(String message) {
		if ("".equals(message)) {
			resetToDefaultApiKey();
		} else {
			model.set( REST_API_KEY, message );
		}
	}

	private void resetToDefaultApiKey() {
		view.setApiKey(REST_API_KEY_DEFAULT_VALUE);
		model.set(REST_API_KEY, REST_API_KEY_DEFAULT_VALUE);
	}
	
	/****************************
	* Endpoint Input
	*****************************/
	public KeyboardTextInput getKeyboardForEndpoint() {
		KeyboardTextInput keyboardInput = keyboardFactory.createStringKeyboardInput();
		keyboardInput.setInitialValue( view.getEndpoint() );
		keyboardInput.setErrorValidator(validatorFactory.createStringLengthValidator(1, 50));
		return keyboardInput;
	}

	public KeyboardInputCallback<String> getCallbackForEndpoint() {
		return new KeyboardInputCallback<String>() {
			@Override
			public void onOk(String value) {				
				view.setEndpoint(value);
			}
		};
	}	
	/****************************
	* get/set endpoints
	*****************************/
	public String[] getEndpoints() {
		return model.get( ENDPOINTS_KEY, ENDPOINTS_DEFAULT_VALUE );
	}

	public void addEndpointByName(String endpoint) {	
		if( !endpoint.equals("") ) {		
			String[] currentEndpoints = getEndpoints();
			String[] newEndpoints = new String [currentEndpoints.length + 1];
			for( int i=0; i < currentEndpoints.length; i++) {
				newEndpoints[i] = currentEndpoints[i];
			}
			newEndpoints[currentEndpoints.length] = endpoint;
			model.set( ENDPOINTS_KEY, newEndpoints );
		}			
	}
	
	public void rmEndpointByName(String endpoint) {		
		if( !endpoint.equals("") && !endpoint.equals( ENDPOINT_DEFAULT ) ) {			
			String[] currentEndpoints = getEndpoints();		
			String[] newEndpoints = new String [currentEndpoints.length -1];			
			int cnt = 0;
			for( int i=0; i < currentEndpoints.length; i++) {
				if( !currentEndpoints[i].equals( endpoint ) ) {					
					newEndpoints[cnt] = currentEndpoints[i];
					cnt++;					
				}				
			}			
			model.set( ENDPOINTS_KEY, newEndpoints );
		}
	}
	
}
