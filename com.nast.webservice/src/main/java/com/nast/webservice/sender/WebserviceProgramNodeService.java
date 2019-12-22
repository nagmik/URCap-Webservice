package com.nast.webservice.sender;

import java.util.Locale;

import com.nast.webservice.sender.WebserviceProgramNodeContribution;
import com.nast.webservice.sender.WebserviceProgramNodeView;
import com.nast.webservice.style.Style;
import com.nast.webservice.style.V3Style;
import com.nast.webservice.style.V5Style;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

public class WebserviceProgramNodeService implements SwingProgramNodeService<WebserviceProgramNodeContribution, WebserviceProgramNodeView> {

	@Override
	public String getId() {
		return "Webservice";
	}

	@Override
	public void configureContribution(ContributionConfiguration contributionConfiguration) {
		
	}

	@Override
	public String getTitle(Locale locale) {		
		return "Webservice";			
	}

	@Override
	public WebserviceProgramNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new WebserviceProgramNodeView(style);
	}

	@Override
	public WebserviceProgramNodeContribution createNode(ProgramAPIProvider apiProvider, WebserviceProgramNodeView view, DataModel model, CreationContext creationContext) {
		return new WebserviceProgramNodeContribution(apiProvider, view,  model );
	}
	
}

