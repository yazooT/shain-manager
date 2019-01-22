package servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dataAccessObject.EmployeeDAO;

/**
 * 画像をサイト上に出力します。
 *
 * <img src="/Image?id=123001">のように画像のソースに
 * このサーブレットを指定することでidパラメータの画像を出力します。
 */
@WebServlet("/Image")
public class Image extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Image() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 写真IDを取得する
		String id = request.getParameter("id");

		// データベースから画像ファイルを取得
		// 登録されていなければ未登録用の画像データを取得
		BufferedImage bi = null;
		try (EmployeeDAO dao = new EmployeeDAO()) {
			bi = dao.getPhoto(id);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// 画像の形式に合わせてコンテンツタイプを設定する
		// typeNumが5の場合jpeg、6の場合png
		int typeNum = bi.getType();
		String type = "";
		if (typeNum == 5) {
			type = "jpeg";
		} else if (typeNum == 6) {
			type = "png";
		}
		response.setContentType("image/" + type);

		// 画像を返す
		OutputStream os = response.getOutputStream();
		ImageIO.write(bi, type, os);
		os.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
