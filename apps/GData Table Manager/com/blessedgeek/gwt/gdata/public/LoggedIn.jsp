<%@ page
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="
com.blessedgeek.gwt.gdata.server.MrBean,
com.blessedgeek.gwt.gdata.server.SessionSilo"
%>
<jsp:useBean id="mrBean" class="com.blessedgeek.gwt.gdata.server.MrBean" scope="session"/>
<%
mrBean.readAuthToken(request);
SessionSilo.logTableMgr.info(session.getId());
mrBean.setSessionId(session);
SessionSilo.logTableMgr.info(mrBean.sessionId);
SessionSilo.logTableMgr.info("mrBean.hash=" + mrBean.hashCode());
SessionSilo.logTableMgr.info("mrBean.FeedsHdlr.hash=" + mrBean.FeedsHdlr.hashCode());
%>
<script type="text/javascript">
location.replace("TableMgr.jsp");
</script>