package dataAccessObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dataObject.Department;

/**
 * 部署データを扱うデータアクセスオブジェクト
 */
public class DepartmentDAO extends DataAccessObject {

	public DepartmentDAO() throws Exception {
		super();
	}

	/**
	 * 指定された部署データを取得します。
	 *
	 * @param where SQL文のWHEREを含めたWHERE句を与えてください。
	 * ""とnullを許容します。
	 * @return 該当データがない場合、空のリストを返します。
	 */
	private List<Department> select(String where) throws Exception {

		// SQL文の準備
		String sql = "SELECT * FROM DEPARTMENT " + where;

		// SQL実行
		super.prepareStatement(sql);
		ResultSet rs = super.getResultSet();

		// 部署データリスト取得
		List<Department> departments = new ArrayList<>();
		while (rs.next()) {
			departments.add(new Department(rs));
		}
		return departments;
	}


	/**
	 * 指定されたIDの部署データを取得します。
	 *
	 * @param id 検索したい部署のID番号
	 * @return 該当するデータがないとき、データを持たないDepartmentインスタンス
	 * を返します。
	 */
	public Department selectByID(String id) throws Exception {

		// WHERE句の準備
		String where = "WHERE ID=" + id;

		List<Department> departments = this.select(where);
		return (departments.isEmpty()) ?
				new Department() : departments.get(0);
	}

	/**
	 * 部署データを全て取得します。
	 *
	 * @return データがひとつもないとき、空のリストを返します。
	 */
	public List<Department> selectAll() throws Exception {
		return this.select("");
	}

	/**
	 * 部署データを新規登録します。
	 *
	 * @param name 追加したい部署の名前。空欄、nullを許容します。
	 *
	 * IDは追加された順で割り振られます。
	 */
	public void insert(String name) throws Exception {
		PreparedStatement ps =
				super.prepareStatement("INSERT INTO DEPARTMENT (NAME) VALUES (?)");
		ps.setString(1, name);
		super.run();
	}

	/**
	 * 部署データを上書きします。
	 *
	 * @department 部署データ
	 * @throws java.sql.SQLException departmentが空、nullの場合
	 */
	public void update(Department department) throws Exception {
		PreparedStatement ps =
				super.prepareStatement("UPDATE DEPARTMENT SET NAME=? WHERE ID=?");
		ps.setString(1, department.getName());
		ps.setInt(2, Integer.parseInt(department.getid()));
		super.run();
	}

	/**
	 * 部署データを削除します。
	 *
	 * @param id 削除したい部署のID番号。
	 * @throws java.sql.SQLException idが空欄、nullの場合
	 */
	public void delete(String id) throws Exception {
		PreparedStatement ps =
				super.prepareStatement("DELETE FROM DEPARTMENT WHERE ID=?");
		ps.setInt(1, Integer.parseInt(id));
		super.run();
	}
}
