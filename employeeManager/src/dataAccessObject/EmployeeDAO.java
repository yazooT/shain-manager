package dataAccessObject;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.FileItem;
import org.h2.util.StringUtils;

import dataObject.Employee;

/**
 * 社員データベース用データアクセスオブジェクト
 *
 * 写真データベースも扱います。
 */
public class EmployeeDAO extends DataAccessObject {
	public EmployeeDAO() throws Exception {
		super();
	}

	/**
	 * 該当する社員データを取得します。
	 *
	 * @param where SQLのWHEREも含めたWHERE句。空欄、nullを許容します。
	 * @return 社員データのリスト。
	 * 該当するデータがひとつもないとき、空のリストを返します。
	 */
	public List<Employee> select(String where) throws Exception {
		// SQL文の準備
		String sql = "SELECT * FROM EMPLOYEE ";
		sql += where;
		sql += " ORDER BY EMPLOYEE_ID";
		System.out.println(sql);

		// SQL実行
		super.prepareStatement(sql);
		ResultSet rs = super.getResultSet();

		// Employeeリストの作成の返却
		List<Employee> employees = new LinkedList<>();
		while (rs.next()) {
			employees.add(new Employee(rs));
		}
		return employees;
	}

	/**
	 * 全ての社員データを取得します。
	 *
	 * @return 社員データのリスト
	 * データがひとつもないとき、空のリストを返します。
	 */
	public List<Employee> selectAll() throws Exception {
		return this.select("");
	}

	/**
	 * 指定されたIDと一致する社員データを取得します。
	 *
	 * @param id 検索したい社員のID
	 * @return 指定されたIDの社員データ。該当データが存在しないとき、
	 * 値を持たない社員データを返します。
	 */
	public Employee selectByID(String id) throws Exception {
		String where = "WHERE EMPLOYEE_ID=" + id;
		List<Employee> employees = this.select(where);
		return (employees.isEmpty()) ? new Employee() : employees.get(0);
	}

	/**
	 * 部署は完全一致、社員IDと名前は部分一致で全ての条件をみたした
	 * 社員データを取得します。
	 *
	 * @param departmentID 部署ID。空欄、nullを許容します。
	 * @param employeeID 社員ID。空欄、nullを許容します。
	 * @param name 社員の名前。空欄、nullを許容します。
	 * @return 該当する社員データのリスト。見つからない場合は空のリストを返します。
	 */
	public List<Employee> find(String departmentID, String employeeID, String name)
			throws Exception {

		String where = "";

		if (!StringUtils.isNullOrEmpty(departmentID)) {
			where += "DEPARTMENT_ID=" + departmentID + " AND ";
		}

		if (!StringUtils.isNullOrEmpty(employeeID)) {
			where += "EMPLOYEE_ID=" + employeeID + " AND ";
		}

		if (!StringUtils.isNullOrEmpty(name)) {
			where += "NAME LIKE '%" + name + "%'";
		}

		where = where.replaceAll(" AND $", "");

		if (!where.isEmpty()) {
			where = "WHERE " + where;
		}

		return this.select(where);
	}

	/**
	 * 社員とその写真をデータベースに登録します。
	 *
	 * @param employee 社員データ。
	 * @throws java.sql.SQLException employeeが社員IDの値を持っていない場合
	 */
	public void insert(Employee employee) throws Exception {
		// オートコミットをオフにする
		super.disableAutoCommit();

		// 社員データベース用SQL文の準備
		String sql = "INSERT INTO EMPLOYEE (" + employee.columns() + ") "
				+ "VALUES (" + employee.values() + ")";
		System.out.println(sql);

		// 社員データベース用SQL実行
		super.prepareStatement(sql);
		super.run();

		// 挿入した社員のIDを取得
		super.prepareStatement("SELECT EMPLOYEE_ID FROM EMPLOYEE");
		ResultSet rs = super.getResultSet();
		List<Integer> empIds = new ArrayList<>();
		while (rs.next()) {
			empIds.add(rs.getInt(1));
		}
		Collections.sort(empIds);
		Collections.reverse(empIds);
		Integer empId = empIds.get(0);

		/*
		 * 以下写真データベースの処理
		 * nullならIDだけ登録する
		 */
		FileItem photo = employee.getPhoto();
		PreparedStatement ps = null;
		if (photo == null) {
			ps = super.prepareStatement("INSERT INTO PHOTO VALUES (?)");
		} else {
			ps = super.prepareStatement("INSERT INTO PHOTO VALUES (?, ?)");
			ps.setBinaryStream(2, photo.getInputStream());
		}
		ps.setInt(1, empId);

		// 写真データベースのアップデート実行
		super.run();

		// コミット
		super.commit();
	}

	/**
	 * 社員データと写真データを上書きします。
	 *
	 * @param employee 社員データ。
	 */
	public void update(Employee employee) throws Exception {
		// オートコミットをオフにする
		super.disableAutoCommit();

		// 社員データベース用SQL文の準備
		String sql = String.format("UPDATE EMPLOYEE SET %s WHERE EMPLOYEE_ID=%s",
				employee.sqlSet(), employee.getEmployeeID());

		// 社員データベース用SQL実行
		super.prepareStatement(sql);
		super.run();

		/*
		 * 以下写真データベースの処理
		 * もし写真がアップロードされていなければコミット後に早期リターンする
		 */
		FileItem photo = employee.getPhoto();
		if (photo == null) {
			super.commit();
			return;
		}

		// 写真データベース用ステートメントの準備
		PreparedStatement ps = super.prepareStatement("UPDATE PHOTO SET PHOTO=? WHERE ID=?");
		ps.setBinaryStream(1, photo.getInputStream());
		ps.setInt(2, Integer.parseInt(employee.getEmployeeID()));

		// 写真データベースのアップデート実行
		super.run();

		// コミット
		super.commit();
	}

	/**
	 * 指定された社員データを削除します。
	 *
	 * @param id 削除したい社員のID
	 * @throws java.sql.Exception idが空欄、nullの場合
	 */
	public void delete(String id) throws Exception {
		// オートコミットをオフにする
		super.disableAutoCommit();

		// 社員データの削除
		PreparedStatement ps =
				super.prepareStatement("DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID=?");
		ps.setString(1, id);
		super.run();

		// 写真データの削除
		ps = super.prepareStatement("DELETE FROM PHOTO WHERE ID=?");
		ps.setString(1, id);
		super.run();

		// コミット
		super.commit();
	}

	/**
	 * 指定されたIDの写真データを取得します。
	 *
	 * @param id 取得したい写真のID
	 * @return 画像データ。もし該当するデータが存在しないとき、
	 * IDが0番の未登録者用の画像データを返します。
	 */
	public BufferedImage getPhoto(String id) throws Exception {

		// SQL文の準備
		String sql = "SELECT * FROM PHOTO WHERE ID=" + id;

		// 画像データを取得し、返却する
		super.prepareStatement(sql);
		ResultSet rs = super.getResultSet();

		rs.next();
		InputStream is = rs.getBinaryStream("PHOTO");
		if (is == null) {
			return this.getPhoto("0");
		} else {
			BufferedInputStream bs = new BufferedInputStream(is);
			return ImageIO.read(bs);
		}
	}
}
