<%@page import="org.h2.util.StringUtils"%>
<%@page import="dataObject.Department"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="dataAccessObject.DepartmentDAO"%>
<%
String id = request.getParameter("department_id");

Department department = new Department();
String actionText = "";
String action = "action.";
if (StringUtils.isNullOrEmpty(id)) {
	actionText = "新規登録";
	action += "RegisterDepartmentLogic";
} else {
	actionText = "編集";
	action += "UpdateDepartmentLogic";
	DepartmentDAO dao = new DepartmentDAO();
	try  {
		department = dao.selectByID(id);
	} finally {
	    dao.close();
	}
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部署<%= actionText %></title>
</head>
<body>
<h2>部署を<%= actionText %>します</h2>

<form action="./Servlet" method="post">
<input type="hidden" name="id" id="id"	value="<%= id %>" />
<input type="hidden" name="action" id="action"  value="<%= action %>" />

<p>
	<label for="name">部署名: </label>
	<input type="text" name="name" id="name" value="<%= department.getName() %>" />
</p>

<button type="submit" ><%= actionText %></button>
<button type="button" onclick="history.back()">キャンセル</button>
</form>
</body>
</html>