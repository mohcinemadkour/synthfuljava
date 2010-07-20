package org.synthful.gwt.javascript.client;

import org.synthful.gwt.javascript.client.DomUtils;

import com.google.gwt.core.client.JavaScriptObject;
/**
 * 
 * @author Icecream
 * JSON Remote Script Call inserts a <script src='datasrc'> tag
 * in the client document.<br>
 * 
 * It defines a callback function on the client document.<br>
 * 
 * The server datasrc has to be in proper javascript format, with the data
 * enclosed within the callback function, so that on loading into the client
 * document, the callback function would execute with the enclosed json data
 * as its argument. 
 * 
 * Example javascript data: 
 * <pre>
 * hello( {json data});
 * </pre>
 *
 * alternatively: 
 * <pre>
 * var x = {json data};
 * hello(x);
 * </pre>
 * 
 * Which means a call need be about json but executing a complete javascript.
 *  
 */
abstract public class JsonRemoteScriptCall
{
    public JsonRemoteScriptCall() {}
    
    // use this call if dynamic remote data source and therefore able to
    // dynamically code the callback at the end of the json stream
    public void callByCallBackParameter( String url, String callBackParameter )
    {
        String uniqCallbackName = "jsonRSC"+this.hashCode();
        url = url +'?'+ callBackParameter +'='+ uniqCallbackName;
        call( url, uniqCallbackName );
    }   

    // use this call if remote data source is static file and the callback
    // has be hard-coded at the end of the json file
    public void call( String url, String callbackName )
    {
        bridgeCallbackNames( this, callbackName );
        DomUtils.jsAddScript(url);
    }
    
    private native static void bridgeCallbackNames(
        JsonRemoteScriptCall hdlr, String callbackName )/*-{
        jsonRSCcallback = function( j ){
            hdlr.@org.synthful.gwt.javascript.client.JsonRemoteScriptCall::onJsonRSCResponse(Lcom/google/gwt/core/client/JavaScriptObject;)( j );
        };
        eval( "window."+callbackName+"=jsonRSCcallback" );
    }-*/;
    
    /*
	    $wnd[callbackName] = function(j) {
	    	hdlr.@org.synthful.gwt.javascript.client.JsonRemoteScriptCall::onJsonRSCResponse(Lcom/google/gwt/core/client/JavaScriptObject;)( j );
	    }
    */

    abstract public void onJsonRSCResponse( JavaScriptObject json );
}
