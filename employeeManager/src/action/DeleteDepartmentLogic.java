package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataAccessObject.DepartmentDAO;
import dataObject.Department;
/**
 * 部署データを削除する
 */
public class DeleteDepartmentLogic implements Logic {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// 削除する部署のIDを取得
		String id = request.getParameter("id");
		request.removeAttribute("id");

		// 部署データを削除
		try (DepartmentDAO dao = new DepartmentDAO()) {
			dao.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error_message", "削除に失敗しました。");
		}

		// 部署データを取得し、リストを更新する
		try (DepartmentDAO dao = new DepartmentDAO()) {
			List<Department> departments = dao.selectAll();
			request.setAttribute("departments", departments);
		} catch (Exception e) {
			e.printStackTrace();
			return "error.jsp";
		}

		return "departmentList.jsp";
	}

}
