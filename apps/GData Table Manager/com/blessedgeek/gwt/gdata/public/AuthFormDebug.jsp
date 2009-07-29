<%@ page
language="java"
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="
java.util.Enumeration
"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Auth Form Debug</title>
<%
Enumeration parame = request.getParameterNames();
while (parame.hasMoreElements())
{
    String param = ""+parame.nextElement();
    String paramv = request.getParameter(param);
    System.out.println(param + " = " + paramv);
}
%>
</head>
<body>

</body>
</html>