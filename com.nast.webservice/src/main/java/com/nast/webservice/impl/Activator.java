package com.nast.webservice.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.nast.webservice.install.ComWeb;
import com.nast.webservice.install.WebserviceInstallationNodeService;
import com.nast.webservice.sender.WebserviceProgramNodeService;
import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("NAST Init Webservice");
				
		// Install		
		ComWeb comWeb = new ComWeb();	
		bundleContext.registerService(SwingInstallationNodeService.class, new WebserviceInstallationNodeService( comWeb ), null);
		
		// Daemon
		bundleContext.registerService(DaemonService.class, comWeb, null);
				
		// Program Node
		bundleContext.registerService(SwingProgramNodeService.class, new WebserviceProgramNodeService(), null);
		
		
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Activator says Goodbye World!");
	}
}

