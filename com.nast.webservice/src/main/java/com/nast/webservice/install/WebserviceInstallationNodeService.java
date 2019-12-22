package com.nast.webservice.install;

import java.util.Locale;

import com.nast.webservice.install.ComWeb;
import com.nast.webservice.install.WebserviceInstallationNodeContribution;
import com.nast.webservice.install.WebserviceInstallationNodeView;
import com.nast.webservice.style.Style;
import com.nast.webservice.style.V3Style;
import com.nast.webservice.style.V5Style;
import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

public class WebserviceInstallationNodeService implements SwingInstallationNodeService<WebserviceInstallationNodeContribution, WebserviceInstallationNodeView> {

	private ComWeb comWeb;
	
	/************************************
	 * 
	 * 
	 ************************************/
	public WebserviceInstallationNodeService(ComWeb comWeb) {
		this.comWeb = comWeb;		
	}
	/************************************
	 * 
	 * 
	 ************************************/
	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}
	/************************************
	 * 
	 * 
	 ************************************/
	@Override
	public String getTitle(Locale locale) {			
		return "NAST Webservice";
	}		
	/************************************
	 * 
	 * 
	 ************************************/
	@Override
	public WebserviceInstallationNodeView createView(ViewAPIProvider apiProvider) {
		SystemAPI systemAPI = apiProvider.getSystemAPI();
		Style style = systemAPI.getSoftwareVersion().getMajorVersion() >= 5 ? new V5Style() : new V3Style();
		return new WebserviceInstallationNodeView(style);
	}
	/************************************
	 * 
	 * 
	 ************************************/
	@Override
	public WebserviceInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, WebserviceInstallationNodeView view, DataModel model, CreationContext context) {
		return new WebserviceInstallationNodeContribution(apiProvider, model, view, comWeb );
	}
	
}