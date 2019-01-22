package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataAccessObject.DepartmentDAO;
import dataObject.Department;

// 部署データを更新する
public class UpdateDepartmentLogic implements Logic {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {

		// 指定された内容に基づき部署データ作成
		String id = request.getParameter("id");
		String name = FileItemExt.encode(request.getParameter("name"));
		Department department = new Department(id, name);

		// 部署データ更新
		try (DepartmentDAO dao = new DepartmentDAO()) {
			dao.update(department);
			request.setAttribute("message", "データの更新に成功しました");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "データの更新に失敗しました");
		}

		request.setAttribute("jsp", "departmentList.jsp");
		return "result.jsp";
	}

}
