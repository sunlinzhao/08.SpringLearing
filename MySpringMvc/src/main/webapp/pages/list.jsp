<%@ page import="com.slz.springmvc.entity.Student" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List</title>
    <style>
        table, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
        table {
            margin: 0 auto;
            text-align: center;
            width: 700px;
        }
    </style>
</head>
<body>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>性别</td>
    </tr>
    <%
        List<Student> list = (List<Student>) request.getAttribute("list");
        for (int i = 0; i < list.size(); i++) {%>
    <tr>
        <td><%=i + 1%>
        </td>
        <td><%=list.get(i).getName()%>
        </td>
        <td><%=list.get(i).getAge()%>
        </td>
        <td><%=list.get(i).getGender()%>
        </td>
    </tr>
    <%}%>
</table>
</body>
</html>
