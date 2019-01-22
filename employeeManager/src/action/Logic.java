package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Logic {
  public String execute(HttpServletRequest request, HttpServletResponse response);
}