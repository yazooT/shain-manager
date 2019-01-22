package dataAccessObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.org.apache.xml.internal.utils.URI;
import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;

/**
 * H2データベースのデータアクセスオブジェクト抽象クラス
 *
 * @param URL データベースのディレクトリ
 * @param USER データベースのユーザー名
 * @param PASS データベースのパスワード
 * @param DRIVER データベースドライバ
 *
 * インスタンスを生成したあと、prepareStatement()でSQLを設定してください。
 * その後、検索結果が必要な場合はgetResultSet()、不要な場合はrun()を
 * 使用してください。
 * それぞれStatementのexecuteQuery()とexecute()に相当します。
 * 最後には必ずclose()でデータベースとの接続を切断してください。
 * AutoClosableを実装しているためtry-with-resources文が使えます。
 *
 * setString()やsetBinaryStream()などのPreparedStatementのメソッドを
 * 使う場合は、prepareStatement()はPreparedStatementを返すため、呼び出し側で
 * 適宜行ってください。
 * ただし、呼び出し側で受け取ったPreparedStatementに対してexecute()と
 * executeQuery()を使用するとエラーが起こります。
 * 必ずsuper.run()もしくはsuper.getResultSet()を使用してください。
 *
 * オートコミットをオフにしたい場合は、実行前にdisableAutoCommit()を呼び出し
 * てください。
 * コミットする際はrun()もしくはgetResultSet()を使用後、
 * commit()を呼び出してください。
 *
 * 下記のサイトを参考にさせていただきました。
 * @see <a href="http://www.atmarkit.co.jp/ait/articles/0111/02/news003.html">
 * Tomcatを使う「JSPプログラミング」（10）：データベースと連携する</a>
 */
public abstract class DataAccessObject implements AutoCloseable{
	private static String USER, PASS, URL;
	private Connection conn;
	private PreparedStatement ps;
	private ResultSet rs;

	/*
	 * データベースのURLをセット
	 */
	public static void setURL() {
		URI dbUri = null;
		try {
			dbUri = new URI(System.getenv("DATABASE_URL"));
		} catch (MalformedURIException e) {
			e.printStackTrace();
		}

	    USER = dbUri.getUserinfo().split(":")[0];
	    PASS = dbUri.getUserinfo().split(":")[1];
	    URL = "jdbc:postgresql://" + dbUri.getHost() + ':'
	    		+ dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
	}

	/**
	 * コンストラクタ。
	 * データベース接続処理を行います。
	 * @throws SQLException
	 */
	protected DataAccessObject() throws SQLException {
		this.conn = DriverManager.getConnection(URL, USER, PASS);
	}

	/**
	 * PreparedStatementを作成します。
	 *
	 * @param sql SQL文
	 * @return PreparedStatementを返します。受け取り側でsetString()などの
	 * メソッドを使えますが、execute()などの実行メソッドはエラーを起こします。
	 */
	protected PreparedStatement prepareStatement(String sql) throws Exception {
		this.ps = this.conn.prepareStatement(sql);
		return this.ps;
	}

	/**
	 * INSERT, UPDATE, DELETE SQLを実行します。
	 *
	 * @return SQL実行が成功した場合はtrue、失敗した場合はflaseを返します。
	 */
	protected boolean run() throws Exception {
		return this.ps.execute();
	}

	/**
	 * SELECT SQLを実行します。
	 *
	 * @return ResultSet
	 */
	protected ResultSet getResultSet() throws Exception {
		this.rs = this.ps.executeQuery();
		return this.rs;
	}

// 以下オプション

	/**
	 * オートコミットをオフにします。
	 *
	 * コミットする際はcommit()を呼び出してください。
	 */
	protected void disableAutoCommit() throws Exception {
		this.conn.setAutoCommit(false);
	}

	/**
	 * コミットします。
	 */
	protected boolean commit() {
		try {
			this.conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("コミットに失敗したためロールバックします。");
			this.rollback();
			return false;
		}
	}



	/**
	 * ロールバック処理
	 */
	private void rollback(){
		try {
			this.conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 切断処理
	 *
	 * getResultSet()使用後はResultSetに対する処理前に呼び出すと
	 * ResultSetの接続も切れます。
	 */
	@Override
	public void close() throws Exception {
		if (this.conn != null) { this.conn.close(); }
		if (this.ps != null) { this.ps.close(); }
		if (this.rs != null) { this.rs.close(); }
	}
}
