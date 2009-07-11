<%@ page
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="
com.google.gdata.client.http.AuthSubUtil"
%>
<%
String callBackBaseHref = request.getParameter("callBackBaseHref");

String authReqUrl =
    AuthSubUtil.getRequestUrl(
        callBackBaseHref+"/LoggedIn.jsp",
        "http://spreadsheets.google.com/feeds",
        false,
        true);
//pageContext.forward(authReqUrl);
%>
<script type="text/javascript">
//alert("<%=authReqUrl%>");
location.replace("<%=authReqUrl%>");
</script>