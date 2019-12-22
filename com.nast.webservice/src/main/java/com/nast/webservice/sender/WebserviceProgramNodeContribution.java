package com.nast.webservice.sender;

import java.util.Collection;

import com.nast.webservice.install.WebserviceInstallationNodeContribution;
import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.util.Filter;
import com.ur.urcap.api.domain.variable.Variable;

public class WebserviceProgramNodeContribution implements ProgramNodeContribution {
	
	private final ProgramAPI programAPI;
	private final WebserviceProgramNodeView view;
	private final DataModel model;		
	private final UndoRedoManager undoRedoManager;
	
	// Daemon response
	private static final String SERVER_RESULT_VAR  = "serviceResult";
	
	// Selected Endpoint
	private static final String SELECTED_ENDPOINT_KEY = "selectedEndpoint";
	private static final String SELECTED_ENDPOINT_DEFAULT_VALUE = "simpleServerScript.php";
	
		
	// Selected Var
	public static final String SELECTED_VARIABLE_KEY = "selectedVar";	
	
	/************************************
	 * 
	 ************************************/
	WebserviceProgramNodeContribution(ProgramAPIProvider apiProvider, WebserviceProgramNodeView view, DataModel model) {
		this.programAPI = apiProvider.getProgramAPI();					
		this.view = view;
		this.model = model;
		this.undoRedoManager = apiProvider.getProgramAPI().getUndoRedoManager();

	}
	/************************************
	 * 
	 ************************************/	
	@Override
	public void openView() {		
					
		// update
		view.updateView();
		
	}
	/************************************
	 * 
	 ************************************/
	@Override
	public void closeView() {
	}
	
	/************************************
	 * 
	 ************************************/
	@Override
	public String getTitle() {
		return "Webservice";		
	}
	/************************************
	 * 
	 ************************************/
	@Override
	public boolean isDefined() {	
		
		// cap is activated
		boolean urcapIsActive 	= getInstallation().getUrcapActive();
		// Daemon state
		DaemonContribution.State state = getInstallation().getDaemonState();
		// Selected var		
		Variable selectedVar 	= getSelectedVariable();
		
		// var
		if( selectedVar == null ) {		
			view.setWarningRowVisible( true );
			view.setWarningText( "Warning\r\nSelected Variable: null" );
			return false;
		}else{
			view.setWarningRowVisible( false );
		}
		
		// cap is not activated
		if( !urcapIsActive ) {			
			view.setWarningRowVisible( true );
			view.setWarningText( "Warning\r\nWebservice URCap is not activated" );
			return false;
		}else{
			view.setWarningRowVisible( false );
		}				
		
		// chk daemon running		
		if (state == DaemonContribution.State.RUNNING) {
			view.setWarningRowVisible( false );
			return true;
		}else {
			view.setWarningRowVisible( true );
			view.setWarningText( "Warning\r\nWebservice daemon is not running" );
			return false;			
		}	
		
	}
	/************************************
	 * 
	 ************************************/
	@Override
	public void generateScript(ScriptWriter writer) {
		
		DaemonContribution.State state 	= getInstallation().getDaemonState();
		
		String XMLRPCVariable 			= getInstallation().getXMLRPCVariable();
		String webServer 				= getInstallation().getRestServer();
		String apiKey 					= getInstallation().getApiKey();
		String endpoint 				= getSelectedEndpoint();
		Variable selectedVar 			= getSelectedVariable();					
		
		if (state == DaemonContribution.State.RUNNING) {			
			writer.assign( SERVER_RESULT_VAR, XMLRPCVariable + ".requestWebserver(\"" + webServer + "\",\"" +  apiKey  + "\", \"" +  endpoint + "\"," + selectedVar + ")");
			writer.ifCondition( SERVER_RESULT_VAR + " != 200");
				writer.appendLine("popup( str_cat(\"Err: \"," + SERVER_RESULT_VAR + "), title=\"Error\", error=True, blocking=True)");
			writer.end();
		}		
	}
	/************************************
	 * getInstallation
	 ************************************/
	public WebserviceInstallationNodeContribution getInstallation() {
		return programAPI.getInstallationNode(WebserviceInstallationNodeContribution.class);
	}
	
	/************************************
	 * Endpoint
	 ************************************/
	public void setSelectedEndpoint( final String value ) {		
		programAPI.getUndoRedoManager().recordChanges(
			new UndoableChanges() {
				@Override
				public void executeChanges() {					
					model.set(SELECTED_ENDPOINT_KEY, value);
				}
			}
		);						
	}
	
	public String getSelectedEndpoint() {
		return model.get(SELECTED_ENDPOINT_KEY, SELECTED_ENDPOINT_DEFAULT_VALUE);
	}	
	
	/************************************
	 * Variable
	 * 
	 * Sample URCap: idletimesswing
	 ************************************/
	public Variable getSelectedVariable() {
		return model.get(SELECTED_VARIABLE_KEY, (Variable) null);
	}
	
	public void setVariable(final Variable variable) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.set(SELECTED_VARIABLE_KEY, variable);
			}
		});
	}
	
	public void removeVariable() {
		undoRedoManager.recordChanges(new UndoableChanges() {
			@Override
			public void executeChanges() {
				model.remove(SELECTED_VARIABLE_KEY);
			}
		});
	}	
	
	public Collection<Variable> getGlobalVariables() {
		return programAPI.getVariableModel().get(new Filter<Variable>() {
			@Override
			public boolean accept(Variable element) {
				return element.getType().equals(Variable.Type.GLOBAL) || element.getType().equals(Variable.Type.VALUE_PERSISTED);
			}
		});
	}
	
}