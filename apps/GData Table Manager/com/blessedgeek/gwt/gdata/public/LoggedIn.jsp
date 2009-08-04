<%@ page
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="
com.blessedgeek.gwt.gdata.server.MrBean,
com.blessedgeek.gwt.gdata.server.SessionSilo"
%><%
MrBean mrBean = SessionSilo.initSessionBean(session.getId());
mrBean.readAuthToken(request);
mrBean.setSessionId(session);
SessionSilo.storeSessionBean(mrBean);
SessionSilo.logTableMgr.info(mrBean.sessionId);
SessionSilo.logTableMgr.info("mrBean.authToken=" + mrBean.authToken);
SessionSilo.logTableMgr.info("mrBean.hash=" + mrBean.hashCode());
%>
<script type="text/javascript">
location.replace("TableMgr.jsp");
</script>