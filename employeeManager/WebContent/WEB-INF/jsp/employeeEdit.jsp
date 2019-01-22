<%@page import="dataAccessObject.DepartmentDAO"%>
<%@page import="dataAccessObject.EmployeeDAO"%>
<%@page import="org.h2.util.StringUtils"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.Collections, dataObject.Employee,
    dataObject.Prefecture, dataObject.Gender, java.util.List, java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
Prefecture[] prefs = Prefecture.values();
Gender[] gens = Gender.values();

String employeeID = request.getParameter("employee_id");

String actionText = "";
String action = "action.";
Employee emp = null;
if (StringUtils.isNullOrEmpty(employeeID)) {
	actionText = "新規登録";
	action += "RegisterEmployeeLogic";
	emp = new Employee();
} else {
	actionText = "編集";
	action += "UpdateEmployeeLogic";

	EmployeeDAO dao = new EmployeeDAO();
	try {
		emp = dao.selectByID(employeeID);
	} finally {
		dao.close();
	}
}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>社員データ<%= actionText %></title>
</head>
<body>
<h2>社員データを<%= actionText %>します</h2>
<form action="./MultipartServlet" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="<%= action %>" />

    <p>
        <label>社員ID: </label>
        <input type="text" value="<%= emp.getEmployeeID() %>" disabled="disabled"/>
        <input type="hidden" name="employee_id" id="employee_id" value="<%= emp.getEmployeeID() %>"/>
    </p>

    <p>
        <label for="name">氏名: </label>
        <input type="text" name="name" id="name" value="<%= emp.getName() %>"/>
    </p>

    <p>
       <label for="age">年齢(半角数字のみ): </label>
       <input type="text" name="age" id="age" value="<%= emp.getAge() %>"/>
    </p>

    <p>
        <label>性別: </label>
        <%
            for (Gender gen : gens) {
            	String checked = (gen.getText().equals(emp.getGender()))
            			? "checked" : "" ;
        %>
            <input type="radio" name="gender" id="<%= gen.getText() %>"
             value="<%= gen.getText() %>" <%= checked %>/>
            <label for="<%= gen.getText() %>"><%= gen.getText() %></label>
        <%  } %>
    </p>

    <p>
        <label>写真(JPGもしくはPNGのみ): </label>
        <%
        if (actionText.equals("編集")) {
        %>
            <p><img style="width: 200px" src="Image?id=<%= emp.getEmployeeID() %>"><br>
        <%
        }
        %>
        <input type="file" name="photo" accept="image/jpeg,image/png">
    </p>

    <p>
       <label for="postal_code">郵便番号(ハイフンをつけて123-4567の形式で): </label>
       <input type="text" name="postal_code" id="postal_code" value="<%= emp.getPostalCode() %>"/>
    </p>

    <p>
        <label>都道府県: </label>
        <select name="pref_name">
            <option value="未選択">未選択</option>
        <%
           for (Prefecture pref : prefs) {
           String selected = (pref.getFullText().equals(emp.getPrefName()))
        		   ? "selected" : "" ;
        %>
           <option value="<%= pref.getFullText() %>"  <%= selected %>>
           <%= pref.getFullText() %>
           </option>
        <% } %>
        </select>
    </p>

    <p>
        <label  for="address">住所: </label>
        <input type="text" name="address"  id="address" value="<%= emp.getAddress() %>" />
    </p>

    <p>
        <label for="department_id">所属部署: </label>
        <jsp:include page="../include/departmentMenu.jsp" flush="true">
            <jsp:param name="selected_id" value="<%= emp.getDepartmentID() %>" />
        </jsp:include>
    </p>

    <p>
        <label for="hire_date">入社日(2010-01-20の形式で): </label>
        <input  type="text" name="hire_date" id="hire_date" value="<%= emp.getHireDate() %>"/>
    </p>

    <p>
        <label  for="leaving_date">退社日(2010-01-20の形式で): </label>
        <input type="text"  name="leaving_date" id="leaving_date" value="<%= emp.getLeavingDate() %>" />
    </p>

    <p>
        <input type="submit" value="<%= actionText %>" /><br>
        <button type="button" onclick="history.back()">キャンセル</button>
    </p>
</form>
</body>
</html>