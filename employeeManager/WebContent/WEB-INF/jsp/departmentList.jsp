<%@page import="com.sun.xml.internal.ws.resources.HttpserverMessages"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="dataObject.Department, java.util.List, java.util.Map,
        dataAccessObject.*, java.util.List, java.sql.*" %>

<%
// 部署データを全て取得する
DepartmentDAO dao = null;
List<Department> departments = null;
try {
	dao = new DepartmentDAO();
	departments = dao.selectAll();
} finally {
	dao.close();
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部署一覧</title>
</head>
<body>

<p>${ error_message }</p>

<table>
	<tr>
		<td>部署No.</td>
		<td>部署名</td>
	</tr>
<%
for (Department dep : departments) {
%>
	<form action="./Servlet" method="post">
	<input type="hidden" name="department_id" value="<%= dep.getid() %>"/>
    <tr>
        <td><%= dep.getid() %></td>
        <td><%= dep.getName() %></td>
        <td><button type="submit" name="jsp"
                value="departmentEdit.jsp">編集</button></td>
        <td><button type="submit" name="action"
                value="action.DeleteDepartmentLogic">削除</button></td>
    </tr>
	</form>
<%
}
%>
</table>
<input type="button" value="新規追加"
         onclick="location.href='./Servlet?jsp=departmentEdit.jsp'">
<br />
<a href="./Servlet?jsp=employeeList.jsp">社員一覧ページ</a>
</body>
</html>