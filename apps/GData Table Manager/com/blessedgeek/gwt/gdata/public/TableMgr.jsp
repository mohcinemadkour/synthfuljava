<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="
com.blessedgeek.gwt.gdata.server.MrBean,
org.synthful.gdata.SpreadsheetFeedsSilo,
com.blessedgeek.gwt.gdata.server.SessionSilo"
%>
<jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"/>
<%
SessionSilo.initCache();

SessionSilo.logTableMgr.info(session.getId());
mrBean.setSessionId(session);
SessionSilo.logTableMgr.info(mrBean.sessionId);
SessionSilo.logTableMgr.info("mrBean.FeedsHdlr=" + mrBean.FeedsHdlr);
SessionSilo.logTableMgr.info("mrBean.FeedsHdlr service=" + mrBean.FeedsHdlr.getService());
String token = request.getParameter("token");
if (token!=null&&token.length()>0)
{
    String sessionAuthToken = mrBean.readAuthToken(request);
    System.out.println(sessionAuthToken);
}

if (mrBean.FeedsHdlr.SessionAuthToken==null)
{
  mrBean.FeedsHdlr.SessionAuthToken = request.getParameter("SessionAuthToken");
  if(mrBean.FeedsHdlr.SessionAuthToken!=null)
  {
    mrBean.FeedsHdlr.getService().setAuthSubToken(mrBean.FeedsHdlr.SessionAuthToken);
    SpreadsheetFeedsSilo.initSpreadsheetFeed(true);
  }
}

SessionSilo.logTableMgr.info("SessionAuthToken=" + mrBean.FeedsHdlr.SessionAuthToken);
if (mrBean.FeedsHdlr.SessionAuthToken!=null)
 if (!MrBean.logTokenInfo(mrBean.FeedsHdlr.SessionAuthToken, null))
   mrBean.FeedsHdlr.SessionAuthToken = null;

SessionSilo.logTableMgr.info("AuthToken=" + mrBean.AuthToken);
SessionSilo.logTableMgr.info("SessionAuthToken=" + mrBean.FeedsHdlr.SessionAuthToken);
SessionSilo.logTableMgr.info("mrBean.hash=" + mrBean.hashCode());
SessionSilo.logTableMgr.info("mrBean.FeedsHdlr.hash=" + mrBean.FeedsHdlr.hashCode());
String logIn = mrBean.FeedsHdlr.SessionAuthToken==null?"logIn":"logOut";
%>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="Woowho.css">
    <title>TableMgr</title>
    <script type="text/javascript" language="javascript" src="TableMgr.nocache.js"></script>
  </head>

  <body>
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>

    <center id="header"></center>

    <table align="center">
      <tr>
        <td id="<%=logIn%>"></td>        
      </tr>
      <tr><td><hr/></td></tr>
      <tr>
        <td height="30px" id="currentDoc"></td>
      </tr>
      <tr><td height="20px">&nbsp;</td></tr>
      <tr><td><hr/></td></tr>
      <tr>
        <td id="sendButtonContainer"></td>
      </tr>
      <tr>
        <td id="actions"></td>
      </tr>
    </table>
    <div id="authform"/>
    <p/>
    <hr/>
    <div><a href="https://www.google.com/accounts/IssuedAuthSubTokens" style="font-size:10; ">Revoke Tokens</a></div>
  </body>
</html>