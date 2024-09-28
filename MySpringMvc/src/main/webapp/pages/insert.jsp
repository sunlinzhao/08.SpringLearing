<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Save</title>
</head>
<body>
<form action="/student/insert" method="post">
    <div>姓名：<input type="text" name="name"></div>
    <div>年龄：<input type="number" name="age"></div>
    <div>性别：
        <input type="radio" name="gender" value="男">男
        <input type="radio" name="gender" value="女">女
    </div>
    <div><button type="submit">提交</button></div>
</form>
</body>
</html>
