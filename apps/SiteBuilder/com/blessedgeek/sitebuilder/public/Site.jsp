<%
String sitemap = request.getParameter("SiteMap");
String title = request.getParameter("Title");
if (sitemap==null || sitemap.length()==0)
    sitemap = "DefaultSiteMap.js";
%>
<html>
<head>
	<title><%=title%></title>
	<meta name='gwt:module' content='com.blessedgeek.sitebuilder.SiteBuilder'/>
	<link type="text/css" rel='stylesheet' href='BlessedGeek.css'/>
</head>
<body>
	<script language="javascript">
	var SiteMap = "<%=sitemap%>";
	</script>
	<script language="javascript" src="SiteBuilder.nocache.js"></script>

	<!-- OPTIONAL: include this if you want history support -->
	<iframe id="__gwt_historyFrame" style="width:0;height:0;border:0"></iframe>

</body>
</html>
