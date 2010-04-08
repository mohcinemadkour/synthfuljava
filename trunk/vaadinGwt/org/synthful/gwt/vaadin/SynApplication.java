package org.synthful.gwt.vaadin;

import java.io.Serializable;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.message.AuthRequest;

import com.syntercourse.StartMenuBar;
import com.syntercourse.userInfo.UserBridge;
import com.vaadin.Application;
import com.vaadin.Application.UserChangeListener;
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
/**
 * Instantiates a com.vaadin.ui.Window as mainWindow.
 * Sets mainWindow as the main window of this Application.
 * <br/>
 * mainWindow provides
 * <ul>
 * <li> a menuBar
 * <li> a bodyLayout
 * </ul>
 * 
 * All activities should take place at this.bodyLayout.
 * Providing a body allows bodyLayout to be cleared without
 * clearing the menu (and other standard presentation) off the Application.
 * 
 * @author icecream
 *
 */
@SuppressWarnings("unchecked")
public abstract class SynApplication
<T extends SynApplication<?,?>, W extends Window>
	extends Application
	implements Serializable, UserChangeListener
{
	private static final long serialVersionUID = 1L;
	
	public void init(W mainWindow, String theme)
	{
		this.setMainWindow(mainWindow);
		this.setTheme(theme);
		this.setSynUser(UserBridge.NoUser);
		this.addListener(this);
		
		ApplicationContext ctx = this.getContext();
		this.setWebAppCtx(ctx);
		ctx.addTransactionListener(this.getHttpRequest);
	}
	
	public void setSynUser(UserBridge u)
	{
		super.setUser(u);
	}
	
	public UserBridge getSynUser()
	{
		Object u = super.getUser();
		UserBridge ub
		= (u == null || !(u instanceof UserBridge))
		? UserBridge.NoUser 
		: (UserBridge)u;
		
		return ub;
	}
	
	public W getMainWindow()
	{
		return (W) super.getMainWindow();
	}
	
	private TransactionListener getHttpRequest =
		new TransactionListener()
	{
		private static final long serialVersionUID = 1L;

		@Override
		public void transactionStart(
			Application application,
			Object transactionData)
		{
			HttpServletRequest request = (HttpServletRequest)transactionData;
			requestInfo.setRequestInfo(request);
			handleParameters(requestInfo.parameters);
			logger.info("#attribs:"+requestInfo.attributes.size());
		}

		@Override
		public void transactionEnd(
			Application application, Object transactionData){
		// TODO Auto-generated method stub
		}
		
		public void handleParameters(Map<String, String[]> parameters){
			if (parameterHandlerList != null){
				Object[] handlers;
				synchronized (parameterHandlerList)
				{
					handlers = parameterHandlerList.toArray();
				}
				for (int i = 0; i < handlers.length; i++){
					((ParameterHandler) handlers[i]).handleParameters(parameters);
				}
			}
		}
	};
	
	
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
	
	//@Override
	//public void applicationUserChanged(UserChangeEvent event){}

	static public void listApps(Application a, AbstractLayout layout)
	{
		Collection<Application>  apps = a.getContext().getApplications();
		
		for (Application app: apps)
		{
			Label t = new Label(app.getMainWindow().getCaption());
			layout.addComponent(t);
		}
	}

	static public SynApplication getApp(
		ApplicationContext ctx, Class<? extends SynApplication> c)
	{
		Collection<Application>  apps = ctx.getApplications();
		
		for (Application app: apps)
		{
			if (app.getClass() == c)
				return (SynApplication)app;
		}
		
		return null;
	}

	static public void copyAttributes(
		HttpServletRequest request,
		Map<String, Object> destinationMap)
	{
		Enumeration attribsen = request.getAttributeNames();
		while (attribsen.hasMoreElements())
		{
			String attrib = (String) attribsen.nextElement();
			Object o = request.getAttribute(attrib);
			if (o instanceof Serializable)
				destinationMap.put(attrib, o);
		}
	}

	static public class RequestInfo
	implements Serializable
	{
		public void setRequestInfo(HttpServletRequest request)
		{
			this.scheme = request.getScheme();
			this.serverPort = request.getServerPort();
			this.serverName = request.getServerName();
			this.servletPath = request.getServletPath();
			this.requestUri = request.getRequestURI();
			this.parameters = request.getParameterMap();
			 
			copyAttributes(request, this.attributes);
			
			this.done = true;
		}
		
		public boolean done;
		private String scheme;
		private String serverName;
		private int serverPort;
		private String requestUri;
		private String servletPath;
		final private Map<String, Object> attributes = new HashMap<String, Object>();
		
		private Map<String, String[]> parameters;

		public String getScheme()
		{
			return scheme;
		}
		public String getServerName()
		{
			return serverName;
		}
		public int getServerPort()
		{
			return serverPort;
		}
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
		public Map<String, Object> getAttributes()
		{
			return attributes;
		}
	}
	
	
    private LinkedList<ParameterHandler> parameterHandlerList = null;
	private WebApplicationContext webAppCtx;
	//protected Window mainWindow;
	//protected Label label;
		
	public final SynApplication<T,W> synApp = this;
	final public RequestInfo requestInfo = new RequestInfo();
	final static private Logger logger = Logger.getLogger(SynApplication.class.getName());
}
