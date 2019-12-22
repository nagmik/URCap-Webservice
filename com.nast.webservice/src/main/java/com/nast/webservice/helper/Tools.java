package com.nast.webservice.helper;

public class Tools {

	
	/****************************
	* 
	*****************************/	
	public static boolean isInList( String item, String[] list ) {				
		boolean found = false;
		for( int i=0; i < list.length; i++) {
			if( list[i].equals( item ) ) {
				found = true;
				break;					
			}
		}		
		return found;
	}
	
}
