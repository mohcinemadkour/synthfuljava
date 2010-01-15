package org.synthful.gwt.vaadin;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.syntercourse.StartMenuBar;
import com.syntercourse.login.UserBridge;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.service.ApplicationContext.TransactionListener;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.terminal.URIHandler;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class SynApplication
	extends Application
	implements TransactionListener, Serializable, ParameterHandler
{
	@Override
	public void init()
	{
		this.setTheme(this.theme);
		this.mainWindow.setCaption(caption);
		this.mainWindow.addComponent(this.menuBar);
		this.mainWindow.addComponent(this.bodyLayout);
		this.setMainWindow(this.mainWindow);
		this.mainWindow.addURIHandler(UriDifferentiator);
		this.bodyLayout.setSizeFull();
		this.bodyLayout.setMargin(true, false, true, false);
		
		if (this.label!=null)
			this.mainWindow.addComponent(this.label);

		ApplicationContext ctx = this.getContext();
		this.setWebAppCtx(ctx);
		ctx.addTransactionListener(this);
		
		this.menuBar.refresh();
	}

	@Override
	public void transactionStart(
		Application application, Object transactionData)
	{
		HttpServletRequest request = (HttpServletRequest)transactionData;
		this.requestInfo.setRequestInfo(request);
		this.handleParameters(requestInfo.parameters);
	}

	@Override
	public void transactionEnd(
		Application application, Object transactionData)
	{
	// TODO Auto-generated method stub
	}

	@Override
	public void handleParameters(Map<String, String[]> parameters)
	{
		if (parameterHandlerList != null)
		{
			Object[] handlers;
			synchronized (parameterHandlerList)
			{
				handlers = parameterHandlerList.toArray();
			}
			for (int i = 0; i < handlers.length; i++)
			{
				((ParameterHandler) handlers[i]).handleParameters(parameters);
			}
		}
	}
	
    /**
     * @param handler
     *            the parameter handler to add.
     */
	public void addParameterHandler(ParameterHandler handler)
	{
		if (parameterHandlerList == null)
			parameterHandlerList = new LinkedList<ParameterHandler>();

		synchronized (parameterHandlerList)
		{
			if (!parameterHandlerList.contains(handler))
				parameterHandlerList.addLast(handler);
		}

	}

	public void setWebAppCtx(
		ApplicationContext appCtx)
	{
		if (appCtx instanceof WebApplicationContext)
			this.webAppCtx = (WebApplicationContext) appCtx;
	}

	public WebApplicationContext getWebAppCtx()
	{
		return webAppCtx;
	}

	protected URIHandler UriDifferentiator = new URIHandler()
	{
		@Override
		public DownloadStream handleURI(
			URL context, String relativeUri)
		{

			menuBar.refresh();
			logger.info(context.toString());
			logger.info(relativeUri);
			return null;
		}
	};

	static public void listApps(Application a, AbstractLayout layout)
	{
		Collection<Application>  apps = a.getContext().getApplications();
		
		for (Application app: apps)
		{
			Label t = new Label(app.getMainWindow().getCaption());
			layout.addComponent(t);
		}
	}

	static public SynApplication getSynApp(Application a)
	{
		Collection<Application>  apps = a.getContext().getApplications();
		
		for (Application app: apps)
		{
			if (app instanceof SynApplication)
				return (SynApplication)app;
		}
		
		return null;
	}

	static public class RequestInfo
	implements Serializable
	{
		private void setRequestInfo(HttpServletRequest request)
		{
			this.servletPath = request.getServletPath();
			this.requestUri = request.getRequestURI();
			this.parameters = request.getParameterMap();
		}
		
		private String requestUri;
		private String servletPath;
		private Map<String, String[]> parameters;

		public String getRequestUri()
		{
			return requestUri;
		}
		public String getServletPath()
		{
			return servletPath;
		}
		public Map<String, String[]> getParameters()
		{
			return parameters;
		}
	}
	
	private WebApplicationContext webAppCtx;
	protected String theme = "syntercourse";
	protected String caption = "";
	protected Label label;
	public StartMenuBar menuBar = new StartMenuBar(this);
	
	public UserBridge userBridge = UserBridge.NoUser;
	
    private LinkedList<ParameterHandler> parameterHandlerList = null;
	final public VerticalLayout bodyLayout = new VerticalLayout();
	final public RequestInfo requestInfo = new RequestInfo();
	final protected Window mainWindow = new Window();
	final static private Logger logger = Logger.getLogger(SynApplication.class.getName());

}