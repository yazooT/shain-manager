package action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

// multipart型フォームからのリクエストを受け付ける
public interface MultipartLogic {
	public String execute(List<FileItem> items, HttpServletRequest request);
}
