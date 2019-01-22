package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataAccessObject.EmployeeDAO;
import dataObject.Employee;

/**
 * Servlet implementation class EmployeeEdit
 */
@WebServlet("/emp-edit")
public class EmployeeEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public EmployeeEdit() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		try (EmployeeDAO dao = new EmployeeDAO()) {
			Employee employee = dao.selectByID(id);
			request.setAttribute("emp", employee);
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("actionText", "編集");
		request.setAttribute("action", "action.UpdateEmployeeLogic");

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
