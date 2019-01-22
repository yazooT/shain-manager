package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataObject.Prefecture;

/**
 * Servlet implementation class PrefectureList
 */
@WebServlet("/PrefectureList")
public class PrefectureList extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PrefectureList() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Prefecture[] prefs = Prefecture.values();
		String selectedPref = request.getParameter("selected");

		PrintWriter writer = response.getWriter();

		response.setContentType("text/html; charset=UTF-8");
		writer.println("<select name=\"pref_name\">");
		writer.println("<label>都道府県: </label>");
		writer.println("<option value=\"未選択\">未選択</option>");
        for (Prefecture pref : prefs) {
        	String selected = (pref.getFullText().equals(selectedPref)) ? "selected" : "" ;
        	writer.println(String.format("<option value=\"%s\" %s>%s</option>",
        			selected, pref.getFullText(), pref.getFullText()));
		}
        writer.println("</select>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
