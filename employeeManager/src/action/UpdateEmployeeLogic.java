package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import dataAccessObject.EmployeeDAO;
import dataObject.Employee;

/*
 * 社員データベースを更新する
 */
public class UpdateEmployeeLogic implements MultipartLogic {

	@Override
	public String execute(List<FileItem> items, HttpServletRequest request) {
		Employee employee = new Employee(items);

		// 郵便番号のチェック
		if (!employee.isValid()) {
			request.setAttribute("message", "郵便番号の書式に誤りがあります");
			return "result.jsp";
		}

		// データベースの更新
		try (EmployeeDAO dao = new EmployeeDAO()) {

			dao.update(employee);
			request.setAttribute("message", "データの更新に成功しました");

		} catch (Exception e) {
			request.setAttribute("message", "社員データの更新に失敗しました");
		}

		request.setAttribute("jsp", "employeeList.jsp");
		return "result.jsp";
	}
}
