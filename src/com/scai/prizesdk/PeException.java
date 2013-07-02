/*
 * Copyright ©2013 SCA interactive
 * 
 * @author Thieu Huynh
 */
package com.scai.prizesdk;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.scai.prizesdk.PeConstant.peErrorCode;

import android.util.Log;

/**
 * PeSDK Exception Class
 * 
 * @author thieu
 *
 */
public class PeException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String nlsKey;
	private peErrorCode nlsErrorCode;
	
	public PeException() {
		super();		
	}
	
	/**
	 * 
	 * @param nlsString NLS string of error
	 * @param errorCode error code
	 */
	public PeException(String nlsString, peErrorCode errorCode) {
		super(nlsString);
		nlsKey = nlsString;
		nlsErrorCode = errorCode;
		
	}
	/**
	 * 
	 * @param nlsString
	 */
	public PeException(String nlsString) {
		this(nlsString, peErrorCode.REQUEST_ERROR);		
	}
	
	/**
	 * 
	 * @return localized message for the NLS string
	 */
	
	public String getNLSMessage() {
						
		ResourceBundle rb = ResourceBundle.getBundle("NLS");
	    
		try {
			return rb.getString(nlsKey);
		} catch (MissingResourceException e) {
			return nlsKey;
		} catch (Exception e) {
			Log.e("peSDK PeException", "Caught exception:" + e);
		} 
			
		return nlsKey;		
	}
	public peErrorCode getNLSErrorCode() {	       
		return nlsErrorCode;
	}
	
	public String toString() {
		return "nlsKey:" + nlsKey + " nlsErrorCode:" + nlsErrorCode;
	}
}
