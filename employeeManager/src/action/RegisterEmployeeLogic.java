package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import dataAccessObject.EmployeeDAO;
import dataObject.Employee;

// 社員情報をデータベースに登録する
public class RegisterEmployeeLogic implements MultipartLogic {

	@Override
	public String execute(List<FileItem> items, HttpServletRequest request) {

		// 送信された内容に基づき社員データ作成
		Employee employee = new Employee(items);

		// 郵便番号のチェック
		if (!employee.isValid()) {
			request.setAttribute("message", "登録に失敗しました。\n郵便番号の書式が誤っています。");
			return "result.jsp";
		}

		// 社員情報を登録
		try (EmployeeDAO dao = new EmployeeDAO()) {
			dao.insert(employee);
			request.setAttribute("message", "データの登録に成功しました。");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "社員データの登録に失敗しました。");
		}

		request.setAttribute("jsp", "employeeList.jsp");
		return "result.jsp";
	}
}
