<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        List<String> list =  (List<String>) request.getAttribute("list");
        for (String str : list) {
            out.print(str + "<br>");
        }
    %>
</body>
</html>
