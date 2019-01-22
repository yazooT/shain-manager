package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 指定されたjspへとフォワードします。
 */
@WebServlet("/Move")
public class Move extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Move() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String next = "/WEB-INF/jsp/" + request.getParameter("jsp");
		request.getRequestDispatcher(next).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
