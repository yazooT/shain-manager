package dataObject;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.fileupload.FileItem;

import action.FileItemExt;

/**
 * 社員のデータオブジェクト
 *
 * @param photo 写真
 * @param data  社員ID、名前、性別、年齢、写真ID、郵便番号、
 * 都道府県、住所、部署ID、入社日、退社日
 */
public class Employee implements Serializable, Comparable<Employee>{
	private static final long serialVersionUID = 1L;

	private FileItem photo;

	private Map<String, String> data = new LinkedHashMap<>();

	public Employee() {}

	/**
	 * 社員用データベースからインスタンスを作ります。
	 *
	 * @param rs ResultSet
	 */
	public Employee(ResultSet rs) {
		try {

			ResultSetMetaData metaData = rs.getMetaData();
			for (int columnNum = 1; columnNum <= metaData.getColumnCount(); columnNum++) {
				String column = metaData.getColumnName(columnNum);
				this.data.put(column.toLowerCase(), rs.getString(column));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ユーザーが入力した値をデータとして取り込みます
	 *
	 * @param items requestから生成されたFileItemのリスト
	 */
	public Employee(List<FileItem> items) {
		for (FileItem item : items) {
			String fieldName = item.getFieldName();

			if (fieldName.equals("action")) { continue; }

			// 写真ファイルをセットする
			if (fieldName.equals("photo")) {
				String type = item.getContentType();
				if (type.equals("image/jpeg") || type.equals("image/png")) {
					this.photo = item;
				}
				continue;
			}

			String value = FileItemExt.encode(item.getString());
			if (value.isEmpty()) { continue; }

			this.data.put(fieldName, value);
		}
	}

	/**
	 * 値を持ったデータのカラム名をSQLの書式に則って返します
	 * @return employee_id, name, age, gender, ....
	 */
	public String columns() {
		List<String> keys = new ArrayList<>();
		for (String key : this.data.keySet()) {
			keys.add(key);
		}
		return this.join(keys, ", ");
	}

	/**
	 * SQL登録用に値を出力します。
	 *
	 * @return '123001', '田中太郎', '22', '男性', ....
	 */
	public String values() {
		List<String> values = new ArrayList<>();
		for (String value : this.data.values()) {
			values.add("'" + value + "'");
		}

		return this.join(values, ", ");
	}

	/**
	 * SQL用にカラム名と値のセットを返します
	 * @return employee_id='123001', name='田中太郎', ...
	 */
	public String sqlSet() {
		String sql = "";
		for (Entry<String, String> entry : this.data.entrySet()) {
			if (entry.getValue().isEmpty()) { continue; }

			sql += String.format("%s='%s', ", entry.getKey(), entry.getValue());
		}

		// 末尾の [, ] を除去
		return sql.replaceAll(",\\s$", "");
	}

	/**
	 *
	 * @return データのcsv
	 */
	public String csv() {
		String csv = this.getEmployeeID() +","+ this.getName() +","+ this.getAge()
				+","+ this.getGender() +","+ this.getPostalCode()
				+","+ this.getPrefName() +","+ this.getAddress() +","+ this.getDepartmentID()
				+","+ this.getHireDate() +","+ this.getLeavingDate();

		return csv;
	}

	/**
	 * 郵便番号の書式を確認します
	 *
	 * @return 郵便番号が空欄の場合trueを返します。
	 */
	public boolean isValid() {
		if (this.getPostalCode().isEmpty()) { return true; }
		if (!this.getPostalCode().matches("\\d{3}-\\d{4}")) { return false; }

		return true;
	}

	@Override
	public int compareTo(Employee that) {
		int thisID = Integer.parseInt(this.getEmployeeID());
		int thatID = Integer.parseInt(that.getEmployeeID());
		if (thisID > thatID) return 1;
		if (thisID < thatID) return -1;
		return 0;
	}

	/**
	 * 文字列を連結させます
	 * @param words 連結させたい文字列のリスト
	 * @param delimiter 区切り文字
	 * @return {"hoge", "fuga", "foo"} -> ", " -> "hoge, fuga, foo"
	 */
	private String join(List<String> words, String delimiter) {
		if (words.isEmpty()) { return ""; }
		String str = words.get(0);
		words.remove(0);

		if (words.isEmpty()) { return str; }
		for (String word : words) {
			str += delimiter + word;
		}
		return str;
	}

	private String join(String[] words, String delimiter) {
		return this.join(Arrays.asList(words), delimiter);
	}

	// 以下getter
	public FileItem getPhoto() {
		return this.photo;
	}
	public String getEmployeeID() {
		String property = this.data.get("employee_id");
		return (property == null) ? "" : property;
	}
	public String getName() {
		String property = this.data.get("name");
		return (property == null) ? "" : property;
	}
	public String getAge() {
		String property = this.data.get("age");
		return (property == null) ? "" : property;
	}
	public String getGender() {
		String property = this.data.get("gender");
		return (property == null) ? "" : property;
	}
	public String getPostalCode() {
		String property = this.data.get("postal_code");
		return (property == null) ? "" : property;
	}
	public String getPrefName() {
		String property = this.data.get("pref_name");
		return (property == null) ? "" : property;
	}
	public String getAddress() {
		String property = this.data.get("address");
		return (property == null) ? "" : property;
	}
	public String getDepartmentID() {
		String property = this.data.get("department_id");
		return (property == null) ? "" : property;
	}
	public String getHireDate() {
		String property = this.data.get("hire_date");
		return (property == null) ? "" : property;
	}
	public String getLeavingDate() {
		String property = this.data.get("leaving_date");
		return (property == null) ? "" : property;
	}
}
