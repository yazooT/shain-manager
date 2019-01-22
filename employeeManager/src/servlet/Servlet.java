package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Logic;
import dataAccessObject.DataAccessObject;

/**
 * コントローラサーブレット
 *
 * action属性で指定されたモデルを呼び出します。
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Servlet() {
        super();
    }

    /*
     * サーバー起動時に一度だけ呼び出される。
     * データベースのパスを設定する
     *
     * @param path データベースのパス
     */
	public void init() throws ServletException {
//		String path = getServletContext().getRealPath("WEB-INF/database/test");
		DataAccessObject.setURL();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String next = "/WEB-INF/jsp/";

		// jspが指定されていた場合、直接飛ぶ
		String jsp = request.getParameter("jsp");
		if (jsp != null) {
			next += jsp;
			request.getRequestDispatcher(next).forward(request, response);
			return;
		}

		// actionの指定に従い、Logicを呼ぶ
		String action = request.getParameter("action");
		try {
			Class<?> clazz = Class.forName(action);
			Logic logic = (Logic) clazz.newInstance();
			next += logic.execute(request, response);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			next += "error.jsp";
		}
		request.getRequestDispatcher(next).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
