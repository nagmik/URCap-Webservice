package com.nast.webservice.install;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


public class XmlRpcDaemonInterface {
	private final XmlRpcClient client;

	public XmlRpcDaemonInterface(String host, int port) {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setEnabledForExtensions(true);
		try {
			config.setServerURL(new URL("http://" + host + ":" + port + "/RPC2"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		config.setConnectionTimeout(1000);
		client = new XmlRpcClient();
		client.setConfig(config);
	}
	
	public boolean isReachable() {		
		return true;
	}
}