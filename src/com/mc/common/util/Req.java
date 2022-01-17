package com.mc.common.util;
 
import javax.servlet.http.HttpServletRequest;

public class Req {

    public static String getValue(HttpServletRequest request, String paramName)
    {
        try{
            if (request.getParameter(paramName) != null){
            	paramName = 	Util.isNull(request.getParameter(paramName));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return paramName;
    }
    
    public static String getAttValue(HttpServletRequest request, String attName)
    {
        try{
            if (request.getAttribute(attName) != null){
            	return Util.isNull((String)request.getAttribute(attName));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return getValue(request, attName, "");
    }
    
    public static String getValueHsc(HttpServletRequest request, String paramName)
    {
        try{
            if (request.getParameter(paramName) != null){
            	paramName = Util.htmlspecialchars(Util.isNull(request.getParameter(paramName)));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return paramName;
    }
	
    public static String [] getValues(HttpServletRequest request, String paramName)
    {
    	try{
    		
	    	String Parameters[]=request.getParameterValues(paramName);
	
	    	for( int i=0 ; i < Parameters.length ; i++ ){
	    		Parameters[i] = Util.isNull(Parameters[i]);
	    	}
	    	
	    	if( Parameters.length == 0 ) return null;
	    	
	    	return Parameters;
	    	
    	}catch(Exception e){
    		return null;
    	}
    }
    
    public static String [] getValuesHsc(HttpServletRequest request, String paramName)
    {
    	try{
    		
	    	String Parameters[]=request.getParameterValues(paramName);
	
	    	for( int i=0 ; i < Parameters.length ; i++ ){
	    		Parameters[i] = Util.htmlspecialchars(Util.isNull(Parameters[i]));
	    	}
	    	
	    	if( Parameters.length == 0 ) return null;
	    	
	    	return Parameters;
	    	
    	}catch(Exception e){
    		return null;
    	}
    }
    
    public static String getValue(HttpServletRequest request, String paramName, String defaultValue)
    {
        String paramValue = defaultValue;

        try{
            if (request.getParameter(paramName) == null || request.getParameter(paramName).equals("")){
                paramValue = defaultValue;
            } else{
                paramValue = Util.isNull(request.getParameter(paramName));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return paramValue;
    }
    
    public static String getValueHsc(HttpServletRequest request, String paramName, String defaultValue)
    {
        String paramValue = defaultValue;

        try{
            if (request.getParameter(paramName) == null || request.getParameter(paramName).equals("")){
                paramValue = defaultValue;
            } else{
                paramValue = Util.htmlspecialchars(Util.isNull(request.getParameter(paramName)));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return paramValue;
    }

    public static String getValues(HttpServletRequest request, String paramName, String defaultValue)
    {    	
    	String paramValue = defaultValue;
    	
    	try{    		
    		String Parameters[]=request.getParameterValues(paramName);
	    	
	    	if( Parameters.length > 0 ){
		    	for( int i=0 ; i < Parameters.length; i++ ){
		    		
		    		paramValue += Util.isNull((Parameters[i]));
		    		
			    	if(i > 0 && i < (Parameters.length-1)){
			    		paramValue += ";";
			    	}
		    	}
	    	}
	    
    	}catch(Exception e){
    		 e.printStackTrace();
    	}   	
    	return paramValue;
    }
    
    public static String getValuesHsc(HttpServletRequest request, String paramName, String defaultValue)
    {    	
    	String paramValue = defaultValue;
    	
    	try{    		
    		String Parameters[]=request.getParameterValues(paramName);
	    	
	    	if( Parameters.length > 0 ){
		    	for( int i=0 ; i < Parameters.length; i++ ){
		    		
		    		paramValue += Util.htmlspecialchars(Util.isNull((Parameters[i])));
		    		
			    	if(i > 0 && i < (Parameters.length-1)){
			    		paramValue += ";";
			    	}
		    	}
	    	}
	    
    	}catch(Exception e){
    		 e.printStackTrace();
    	}   	
    	return paramValue;
    }
    
    public static String getValue(HttpServletRequest request, String paramName, String defaultValue, String thisCharSet, String changeCharSet)
    {
        String paramValue = defaultValue;

        try{
            if (request.getParameter(paramName) == null){
                paramValue = defaultValue;
            } else{
                paramValue = (String) request.getParameter(paramName);
                if (!(thisCharSet == null || thisCharSet.equals(""))){
                    paramValue = new String(paramValue.getBytes(thisCharSet), changeCharSet);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return paramValue;
    }
    
}
