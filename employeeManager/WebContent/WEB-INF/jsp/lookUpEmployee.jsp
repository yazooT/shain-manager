<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>社員検索</title>
</head>
<body>
<form action ="./Servlet" method="post">
    <input type="hidden" name="jsp" value="employeeList.jsp" />

    <p>
	    <label for="department_id">所属部署: </label>
	    <jsp:include page="../include/departmentMenu.jsp" flush="true" />
    </p>

    <p>
	    <label for="employee_id">社員ID: </label>
	    <input type="text" name="employee_id" id="employee_id" />
    </p>

    <p>
	    <label for="name">名前に含まれる文字: </label>
	    <input type="text" name="name" id="name" />
    </p>

    <p>
        <input type="submit" value="検索" />
    </p>

    <p>
        <a href="" onclick="window.history.back();">戻る</a>
    </p>
</form>
</body>
</html>