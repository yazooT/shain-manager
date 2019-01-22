package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

// multipart型フォームからのリクエストを扱うときに使うメソッドを持つクラス
public class FileItemExt {

	// FileItemExt.getItemsと併せて使う
	// 属性名がnameと一致する値を返す
	public static final String getParameter(List<FileItem> items, String name) {

		for (FileItem item : items) {
			if (item.getFieldName().equals(name)) {
				// enctype="multipart/form-data"フォームからのリクエストは日本語が文字化けするため、
				// エンコードの変換が必要
				return FileItemExt.encode(item.getString());
			}
		}

		return null;
	}

	// 文字エンコードの変換
	public static String encode(String string) {
		if (string == null) {
			return "";
		}

		try {
			return new String(string.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// ファイルをアップロードするためのenctype="multipart/form-data"の指定が入っているformからは
	// 直接request.getParameter()で値を取り出せないため、一旦List<FileItem>の形にする
	public static final List<FileItem> getItems(HttpServletRequest request) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);

		try {
			List<FileItem> list = sfu.parseRequest(request);
			return list;
		} catch (FileUploadException e) {
			e.getStackTrace();
		}

		return null;
	}
}
