/*
 * Code based on http://stackoverflow.com/questions/4115101/apache-httpclient-on-android-producing-certpathvalidatorexception-issuername
 * 
 * 
 * 
 */
package com.scai.prizesdk;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

import android.content.Context;

public class PeHttpClient extends DefaultHttpClient {
	
	//private static final String TAG = PeHttpClient.class.getCanonicalName();
	final Context context;
	  
    public PeHttpClient(HttpParams httpParams, Context context) {
    	super(httpParams);
        this.context = context;
    }
      
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
        return new SingleClientConnManager(getParams(), registry);
    }
 
}
