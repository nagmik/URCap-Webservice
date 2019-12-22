<?PHP
	// API-KEY
	// $apiKey = "your-api-key";
	
	//receive raw post data
	$jsonContent = trim( file_get_contents( "php://input" ) );		 
	
	//decode the incoming RAW post data from JSON
	//$decoded = json_decode( $jsonContent, true );
	
	// message 4 log
	$logMessage = 'json: ' . $jsonContent;	
	
	// time
	$timer = date("d.m.Y H:i:s");
	
	// append
	$mode = "a";	
	
	// logfile
	$filename = 'log/log.txt';
	
	// file handle
	$fp = fopen($filename, $mode );
	fwrite($fp, $timer . ' - ' . $logMessage . "\r\n"  );
	fclose($fp);	
	
	// return
	return 201;	
?>
