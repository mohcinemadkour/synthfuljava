package org.synthful.gwt.http.client;


import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

abstract public class JsonHttpResponseHandler
extends HttpResponseHandler
{
    public JsonHttpResponseHandler(String url)
    {
        super(url);
    }
    
    public void onResponse(
        Request request, Response response)
    {
            try
        {
            // parse the response text into JSON
            JSONValue jsonValue = JSONParser.parse(response.getText());
            JSONObject jsonObj = jsonValue.isObject();

            if (jsonObj != null)
            {
                JsonData = jsonObj;
                this.Status = HttpResponseStatus.Normal;
                this.onJsonResponse(this);
            }
            else
            {
                this.Status = HttpResponseStatus.EmptyData;
                throw new JSONException();
            }
        }
        catch (JSONException e)
        {
            this.Status = HttpResponseStatus.DataParseFailure;
        }
    }
    
    public JSONObject JsonData;
    abstract protected void onJsonResponse(
        JsonHttpResponseHandler jsonHttpResponseHandler);
}
