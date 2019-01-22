<%@page import="java.util.Iterator"%>
<%@page import="org.h2.util.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sun.xml.internal.ws.api.ha.StickyFeature"%>

<%@ page import="dataObject.Employee, java.util.List, java.util.Map,
        dataAccessObject.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
String action = request.getParameter("action");

String employeeID = (action == null) ?
		request.getParameter("employee_id") : "";
String departmentID = request.getParameter("department_id");
String name = request.getParameter("name");
if (name != null) {
    name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
}
System.out.println(name);

// 検索
EmployeeDAO dao = null;
List<Employee> employees = null;
try {
	dao = new EmployeeDAO();
	employees = dao.find(departmentID, employeeID, name);
} finally {
	dao.close();
}

// CSV出力用の社員ID
String empIds = "";
Iterator<Employee> empIterator = employees.iterator();
if (empIterator.hasNext()) {
	empIds += empIterator.next().getEmployeeID();
}
while (empIterator.hasNext()) {
	empIds += "," + empIterator.next().getEmployeeID();
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>社員情報一覧</title>
</head>
<body>
<p>社員一覧:</p>
<p>${ error_message }</p>

<c:choose>
<c:when test="${ employees.isEmpty() }">
<p>該当する社員が見つかりません</p>
</c:when>
<c:otherwise>
<table>
  <tr>
    <td>ID</td>
    <td>名前</td>
  </tr>
<%
for (Employee emp : employees) {
%>
    <form action="./Servlet" method="post">
    <input type="hidden" name="employee_id" value="<%= emp.getEmployeeID() %>"/>
    <tr>
        <td><%= emp.getEmployeeID() %></td>
        <td><%= emp.getName() %></td>
        <td><button type="submit" name="jsp"
                value="employeeEdit.jsp">編集</button></td>
        <td><button type="submit" name="action"
                value="action.DeleteEmployeeLogic">削除</button></td>
    </tr>
    </form>
<%
}
%>
</table>
</c:otherwise>
</c:choose>

<form action="./Servlet" method="post">
<button type="submit" name="jsp" value="employeeEdit.jsp">新規登録</button><br />
<button type="submit" name="jsp" value="lookUpEmployee.jsp">社員検索</button><br />
</form>
<form action="./ExportCSV">
<input type="hidden" name="employees" value="<%= empIds %>">
<button type="submit"/>CSVファイルに出力</button><br /><br />
</form>
<a href="./Servlet?jsp=departmentList.jsp">部署一覧ページ</a>
</body>
</html>