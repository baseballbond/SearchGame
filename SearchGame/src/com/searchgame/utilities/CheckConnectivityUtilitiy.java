package com.searchgame.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class CheckConnectivityUtilitiy
{
	 
    /**
     * Check for <code>TYPE_WIFI</code> and <code>TYPE_MOBILE</code> connection using <code>isConnected()</code>
     * Checks for generic Exceptions and writes them to logcat as <code>CheckConnectivity Exception</code>. 
     * Make sure AndroidManifest.xml has appropriate permissions.
     * @param con Application context
     * @return Boolean
     */
    public Boolean checkNow(final Context con)
    {
    	try
    	{
    		final ConnectivityManager connectivityManager = 
    				(ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
    		final NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    		final NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);   
    		
    		if(wifiInfo.isConnected() || mobileInfo.isConnected())
    		{
    			return true;
    		}
    	}	
    	catch(Exception e)
    	{
    		System.out.println("CheckConnectivity Exception: " + e.getMessage());
    	}
    	return false;
    }   
}

