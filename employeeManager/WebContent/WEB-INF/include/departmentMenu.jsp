<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="dataAccessObject.*,
            dataObject.Department,
            java.util.List,
            java.util.ArrayList,
            java.sql.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
/**
 * 部署のプルダウンメニュー。インクルード用。
 */
DepartmentDAO dao = null;
List<Department> departments = null;
try {
	dao = new DepartmentDAO();
	departments = dao.selectAll();
} finally {
    dao.close();
}

String selectedID = request.getParameter("selected_id");
%>

<select name="department_id">
<option value="">未選択</option>

<%
for (Department dep : departments) {
    String selected = (dep.getid().equals(selectedID)) ? "selected" : "";
%>
    <option value="<%= dep.getid() %>" <%= selected %>>
    <%= dep.getName() %></option>
<%
}
%>

</select>

