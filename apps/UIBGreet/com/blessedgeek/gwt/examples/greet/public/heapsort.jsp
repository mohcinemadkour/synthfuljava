<%@ page
 language="java"
 contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"
%>
<jsp:useBean id="sort" class="com.blessedgeek.gwt.examples.greet.HeapSort"></jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Heap sort</title>
</head>
<body>

<%!

int[] a = {
	545,34,9,87,4,134,98,454,98,234,67,92,9,4760,265,8,544,22,83,395,
	95,342,803,23,-7,357,93,48,992,629,417,96,386,53,73,493,5664,8558,
	764,385,297,231,-346,5,853,333,732,467,521,584,389,345,865,25,556,
	-653,455,874,132,724,862,236,462,233,12,122,65,327,-273,650,3,-21
};
%>
Unsorted<br/><pre><%=sort.prna(a)%></pre><%

sort.buildheap(this.a);

%>Heaped<br/><pre><%=sort.prna(a)%></pre><%

sort.sort();

%>Sorted<br/><pre><%=sort.prna(a)%></pre>

</body>
</html>