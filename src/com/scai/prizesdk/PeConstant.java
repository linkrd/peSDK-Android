/*
 * Copyright ©2013 SCA interactive
 * 
 * @author Thieu Huynh
 */
package com.scai.prizesdk;


public class PeConstant {
	
	static final String SERVICE_HOST = "contest.linkrd.com";
	static final Boolean IS_SSL = true;
	static final String SERVICE = "rngapi";
	static final String RESPONSE_TYPE = "json";
	/**
	 * PeSDK error code
	 */
	public static enum peErrorCode {
		/** Can't communicate with server */
		REQUEST_ERROR,	
		/** Can't get the configuration */
	    CONFIG_ERROR,	
	    /** Data from server is wrong */
	    DATA_ERROR, 	
	    /** Data doesn't allow play */
	    API_ERROR,		
	    /** Result from server is an error */
	    RESULT_ERROR	
	}
}
