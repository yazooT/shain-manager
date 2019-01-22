package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import action.FileItemExt;
import action.MultipartLogic;

/**
 * enctype="multipart/form-data"形式のformタグからの送信
 * を処理するコントローラ
 *
 * action属性で指定されたモデルを呼び出します。
 *
 * 画像をアップロードするためにはformタグは
 * enctype="multipart/form-data"の属性を持たないといけません。
 * しかしそうすると他の文字列等のデータをリクエストから取得できません。
 * そのためorg.apache.commons.fileupload.FileItemを用いた特殊な処理が
 * 必要で、このサーブレットはそれを受け持っています。
 */
@WebServlet("/MultipartServlet")
public class MultipartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public MultipartServlet() {
        super();
    }

    // actionの指定に従い、リクエストを送る
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String next = "/WEB-INF/jsp/";

		// 送信されたデータリスト
		List<FileItem> items = FileItemExt.getItems(request);

		// 次に実行するLogicクラスを表す文字列
		String action = FileItemExt.getParameter(items, "action");

		// 次に実行するLogicクラス
		MultipartLogic logic = null;
		try {
			Class<?> clazz = Class.forName(action);
			logic = (MultipartLogic) clazz.newInstance();
			next += logic.execute(items, request);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			next += "error.jsp";
		}
		request.getRequestDispatcher(next).forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
