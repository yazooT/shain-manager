package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataAccessObject.EmployeeDAO;

/**
 * 指定されたIDの社員データと写真データを削除する
 */
public class DeleteEmployeeLogic implements Logic {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// 削除対象のIDを取得
		String id = request.getParameter("employee_id");

		// 削除実行
		try (EmployeeDAO dao = new EmployeeDAO()){
			dao.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error_message", "削除に失敗しました。");
		}

		return "employeeList.jsp";
	}

}
