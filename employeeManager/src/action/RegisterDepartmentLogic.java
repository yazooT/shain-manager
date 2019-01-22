package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataAccessObject.DepartmentDAO;

// 部署データを新規登録する
public class RegisterDepartmentLogic implements Logic {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// 登録する部署名を取得する
		String name = FileItemExt.encode(request.getParameter("name"));

		// 部署を登録する
		try (DepartmentDAO dao = new DepartmentDAO()) {
			dao.insert(name);
			request.setAttribute("message", "登録に成功しました");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "登録に失敗しました");
		}

		request.setAttribute("jsp", "departmentList.jsp");
		return "result.jsp";
	}

}
