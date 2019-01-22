package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataAccessObject.EmployeeDAO;
import dataObject.Employee;

/**
 * 社員データのCSVを出力します。
 *
 */
@WebServlet("/ExportCSV")
public class ExportCSV extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ExportCSV() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/csv;charset=UTF8");
		response.setHeader("Content-Disposition", "attachment; filename=employeeList.csv");

		String[] ids = request.getParameter("employees").split(",");

		List<Employee> employees = new ArrayList<>();
		try (EmployeeDAO dao = new EmployeeDAO()) {
			for (String id : ids) {
				employees.add(dao.selectByID(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (PrintWriter pw = response.getWriter()) {
			pw.println("社員ID,名前,年齢,性別,郵便番号,都道府県,住所,部署ID,入社日,退社日");
			for (Employee employee : employees) {
				pw.println(employee.csv());
			}
			pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
