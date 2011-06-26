package org.synthful.gwt.http.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;

abstract public class HttpResponseHandler implements RequestCallback {
	public HttpResponseHandler(String url) {
		url = URL.encode(url);
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

		try {
			Request request = builder.sendRequest(null, this);
		}
		catch (RequestException e) {
			this.Status = HttpResponseStatus.Failure;
		}
	}

	public void onError(Request request, Throwable exception) {
		this.Status = HttpResponseStatus.Failure;
	}

	public void onResponseReceived(Request request, Response response) {
		if (200 == response.getStatusCode()) {
			this.ResponseText = response.getText();
			this.onResponse(request, response);
		}
		else {
			this.Status = HttpResponseStatus.Unreachable;
		}
	}

	// Data must be handled within onResponseReceived
	// due to async conditions of the response wrt the rootpanel.
	abstract protected void onResponse(Request request, Response response);

	public String ResponseText;
	public HttpResponseStatus Status = HttpResponseStatus.Wait;

	static public enum HttpResponseStatus {
		Wait, Normal, Unreachable, Failure, EmptyData, DataParseFailure
	}

}
