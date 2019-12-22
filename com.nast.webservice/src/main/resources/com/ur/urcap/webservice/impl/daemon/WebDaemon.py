#!/usr/bin/env python

import xmlrpclib
from SimpleXMLRPCServer import SimpleXMLRPCServer
import string
import json
import urllib2

# *****************************
# request 
# *****************************
def requestWebserver( apiurl, apikey, endpoint, value ):		
	url = apiurl + endpoint
	data = json.dumps( {"apikey":apikey,"value":value} )	
	req = urllib2.Request(url, data, {'Content-Type': 'application/json'})
	try:	
		f = urllib2.urlopen( req )
		response = f.read()
		f.close()
		return 200
	except urllib2.URLError as e:
		print "An error %s " %e	
		return 400	
# *****************************
server = SimpleXMLRPCServer(("", 33025), allow_none=True)
server.register_function(requestWebserver, "requestWebserver")
server.serve_forever()
