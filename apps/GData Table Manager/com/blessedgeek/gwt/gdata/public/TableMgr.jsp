<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="
com.blessedgeek.gwt.gdata.server.MrBean"
%>
<jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"/>
<%
//mrBean.readAuthToken(request);
if (mrBean.SessionAuthToken==null)
{
    mrBean.SessionAuthToken = request.getParameter("SessionAuthToken");
    if (!MrBean.logTokenInfo(mrBean.SessionAuthToken, null))
        mrBean.SessionAuthToken = null;
}

System.out.println("AuthToken=" + mrBean.AuthToken);
System.out.println("SessionAuthToken=" + mrBean.SessionAuthToken);
String logIn = mrBean.SessionAuthToken==null?"logIn":"logOut";
MrBean.logTokenInfo(mrBean.SessionAuthToken, null);
%>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="../Woowho.css">
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
      <tr>
        <td colspan="2" style="font-weight:bold;">Enter item:</td>        
      </tr>
      <tr>
        <td id="itemInput"></td>
        <td id="sendButtonContainer"></td>
      </tr>
      <tr>
        <td id="action"></td>
      </tr>
    </table>
    <div id="authform"/>
  </body>
</html>