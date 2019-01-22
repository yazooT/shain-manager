package dataObject;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 部署のデータオブジェクト
 *
 * @param id 部署ナンバー、部署ID
 * @param name 部署の名前
 */
public class Department implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id, name;

	public Department() {}

	public Department(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * データベースからインスタンスを作ります。
	 *
	 * @param rs 部署用データベースからのResultSet
	 */
	public Department(ResultSet rs) {
		try {
			this.id = rs.getString("ID");
			this.name = rs.getString("NAME");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	// 以下getter
	public String getid() {
		return this.id;
	}

	public String getName() {
		return (this.name == null) ? "" : this.name;
	}

}
