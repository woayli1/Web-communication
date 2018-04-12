package sql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SQL 基本操作 通过它,可以很轻松的使用 JDBC 来操纵数据库
 * 
 * @author 月亮一族
 */
public class MYSQL {
	/**
	 * 驱动
	 */
	public static String driver = "oracle.jdbc.driver.OracleDriver";
	/**
	 * 连接字符串
	 */
	public static String url = "jdbc:oracle:thin:@192.168.9.133:1521:SCHOOL";
	/**
	 * 用户名
	 */
	public static String user = "scott";
	/**
	 * 密码
	 */
	public static String password = "scott";

	/**
	 * 不允许实例化该类
	 */
	private MYSQL() {
	}

	/**
	 * 获取一个数据库连接 通过设置类的 driver / url / user / password 这四个静态变量来 设置数据库连接属性
	 * 
	 * @return 数据库连接
	 */
	public static Connection getConnection() {
		try {
			// 获取驱动
			Class.forName(driver);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			return DriverManager.getConnection(url, user, password);
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	/**
	 * 获取一个 Statement 该 Statement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static Statement getStatement() {
		Connection conn = getConnection();
		if (conn == null) {
			return null;
		}
		try {
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// 设置数据集可以滚动,可以更新
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			close(conn);
		}
		return null;
	}

	/**
	 * 获取一个 Statement 该 Statement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param conn
	 *            数据库连接
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static Statement getStatement(Connection conn) {
		if (conn == null) {
			return null;
		}
		try {
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// 设置数据集可以滚动,可以更新
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	/**
	 * 获取一个带参数的 PreparedStatement 该 PreparedStatement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static PreparedStatement getPreparedStatement(String cmdText,
			Object... cmdParams) {
		Connection conn = getConnection();
		if (conn == null) {
			return null;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn
					.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			int i = 1;
			for (Object item : cmdParams) {
				pstmt.setObject(i, item);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			close(conn);
		}
		return pstmt;
	}

	/**
	 * 获取一个带参数的 PreparedStatement 该 PreparedStatement 已经设置数据集 可以滚动,可以更新
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 如果获取失败将返回 null,调用时记得检查返回值
	 */
	public static PreparedStatement getPreparedStatement(Connection conn,
			String cmdText, Object... cmdParams) {
		if (conn == null) {
			return null;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn
					.prepareStatement(cmdText, ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			int i = 1;
			for (Object item : cmdParams) {
				pstmt.setObject(i, item);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			close(pstmt);
		}
		return pstmt;
	}

	/**
	 * 执行 SQL 语句,返回结果为 整 型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int execSql(String cmdText) {
		Statement stmt = getStatement();
		if (stmt == null) {
			return -2;
		}
		int i;
		try {
			i = stmt.executeUpdate(cmdText);
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			i = -1;
		}
		closeConnection(stmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int execSql(Connection conn, String cmdText) {
		Statement stmt = getStatement(conn);
		if (stmt == null) {
			return -2;
		}
		int i;
		try {
			i = stmt.executeUpdate(cmdText);
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			i = -1;
		}
		close(stmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int execSql(String cmdText, Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
		if (pstmt == null) {
			return -2;
		}
		int i;
		try {
			i = pstmt.executeUpdate();
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			i = -1;
		}
		closeConnection(pstmt);
		return i;
	}

	/**
	 * 执行 SQL 语句,返回结果为整型 主要用于执行非查询语句
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return 非负数:正常执行; -1:执行错误; -2:连接错误
	 */
	public static int execSql(Connection conn, String cmdText,
			Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
		if (pstmt == null) {
			return -2;
		}
		int i;
		try {
			i = pstmt.executeUpdate();
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			i = -1;
		}
		close(pstmt);
		return i;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static Object execScalar(String cmdText) {
		ResultSet rs = getResultSet(cmdText);
		Object obj = buildScalar(rs);
		closeConnection(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static Object execScalar(Connection conn, String cmdText) {
		ResultSet rs = getResultSet(conn, cmdText);
		Object obj = buildScalar(rs);
		closeEx(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static Object execScalar(String cmdText, Object... cmdParams) {
		ResultSet rs = getResultSet(cmdText, cmdParams);
		Object obj = buildScalar(rs);
		closeConnection(rs);
		return obj;
	}

	/**
	 * 返回结果集的第一行的一列的值,其他忽略
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static Object execScalar(Connection conn, String cmdText,
			Object... cmdParams) {
		ResultSet rs = getResultSet(conn, cmdText, cmdParams);
		Object obj = buildScalar(rs);
		closeEx(rs);
		return obj;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static ResultSet getResultSet(String cmdText) {
		Statement stmt = getStatement();
		if (stmt == null) {
			return null;
		}
		try {
			return stmt.executeQuery(cmdText);
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			closeConnection(stmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param conn
	 * @param cmdText
	 *            SQL 语句
	 * @return
	 */
	public static ResultSet getResultSet(Connection conn, String cmdText) {
		Statement stmt = getStatement(conn);
		if (stmt == null) {
			return null;
		}
		try {
			return stmt.executeQuery(cmdText);
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			close(stmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static ResultSet getResultSet(String cmdText, Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
		if (pstmt == null) {
			return null;
		}
		try {
			return pstmt.executeQuery();
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			closeConnection(pstmt);
		}
		return null;
	}

	/**
	 * 返回一个 ResultSet
	 * 
	 * @param conn
	 *            数据库连接
	 * @param cmdText
	 *            需要 ? 参数的 SQL 语句
	 * @param cmdParams
	 *            SQL 语句的参数表
	 * @return
	 */
	public static ResultSet getResultSet(Connection conn, String cmdText,
			Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
		if (pstmt == null) {
			return null;
		}
		try {
			return pstmt.executeQuery();
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			close(pstmt);
		}
		return null;
	}

	// 返回一个ArrayList
	public static ArrayList<Object[]> getArrayList(String cmdText) {
		Statement stmt = getStatement();
		if (stmt == null) {
			return null;
		}
		try {
			ResultSet res = stmt.executeQuery(cmdText);
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			ResultSetMetaData rsmd = res.getMetaData();
			int column = rsmd.getColumnCount();

			while (res.next()) {
				Object[] ob = new Object[column];
				for (int i = 1; i <= column; i++) {
					ob[i - 1] = res.getObject(i);
				}
				list.add(ob);
			}
			res.close();
			return list;
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			closeConnection(stmt);
		}
		return null;
	}

	// 返回一个ArrayList
	public static ArrayList<Object[]> getArrayList(String cmdText,
			Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(cmdText, cmdParams);
		if (pstmt == null) {
			return null;
		}
		try {
			ResultSet res = pstmt.executeQuery();
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			ResultSetMetaData rsmd = res.getMetaData();
			int column = rsmd.getColumnCount();

			while (res.next()) {
				Object[] ob = new Object[column];
				for (int i = 1; i <= column; i++) {
					ob[i - 1] = res.getObject(i);
				}
				list.add(ob);
			}
			res.close();
			return list;
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			closeConnection(pstmt);
		}
		return null;
	}

	// 返回一个ArrayList
	public static ArrayList<Object[]> getArrayList(Connection conn,
			String cmdText) {
		Statement stmt = getStatement(conn);
		if (stmt == null) {
			return null;
		}
		try {
			ResultSet res = stmt.executeQuery(cmdText);
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			ResultSetMetaData rsmd = res.getMetaData();
			int column = rsmd.getColumnCount();

			while (res.next()) {
				Object[] ob = new Object[column];
				for (int i = 1; i <= column; i++) {
					ob[i - 1] = res.getObject(i);
				}
				list.add(ob);
			}
			res.close();
			return list;
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			close(stmt);
		}
		return null;
	}

	// 返回一个ArrayList
	public static ArrayList<Object[]> getArrayList(Connection conn,
			String cmdText, Object... cmdParams) {
		PreparedStatement pstmt = getPreparedStatement(conn, cmdText, cmdParams);
		if (pstmt == null) {
			return null;
		}
		try {
			ResultSet res = pstmt.executeQuery();
			ArrayList<Object[]> list = new ArrayList<Object[]>();
			ResultSetMetaData rsmd = res.getMetaData();
			int column = rsmd.getColumnCount();

			while (res.next()) {
				Object[] ob = new Object[column];
				for (int i = 1; i <= column; i++) {
					ob[i - 1] = res.getObject(i);
				}
				list.add(ob);
			}
			res.close();
			return list;
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			close(pstmt);
		}
		return null;

	}

	// 返回一个ArrayList<T>
	public static <T> ArrayList<T> getArrayList(Class<T> cls, String cmdText) {
		Statement stmt = getStatement();
		if (stmt == null) {
			return null;
		}
		try {
			ResultSet res = stmt.executeQuery(cmdText);
			ArrayList<T> list = new ArrayList<T>();
			ResultSetMetaData metaData = res.getMetaData();
			int cols_len = metaData.getColumnCount();

			while (res.next()) {
				// 通过反射机制创建一个实例
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = res.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}

					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // 打开javabean的访问权限
					field.set(resultObject, cols_value);

				}
				list.add(resultObject);
			}
			res.close();
			return list;
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			closeConnection(stmt);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 返回一个ArrayList<T>
	public static <T> ArrayList<T> getArrayList(Class<T> cls, String cmdText,
			Object... cmdParams) {
		PreparedStatement stmt = getPreparedStatement(cmdText, cmdParams);
		if (stmt == null) {
			return null;
		}
		try {
			ResultSet res = stmt.executeQuery(cmdText);
			ArrayList<T> list = new ArrayList<T>();
			ResultSetMetaData metaData = res.getMetaData();
			int cols_len = metaData.getColumnCount();

			while (res.next()) {
				// 通过反射机制创建一个实例
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = res.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // 打开javabean的访问权限
					field.set(resultObject, cols_value);
				}
				list.add(resultObject);
			}
			res.close();
			return list;
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			closeConnection(stmt);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 返回一个ArrayList<T>
	public static <T> ArrayList<T> getArrayList(Connection conn, Class<T> cls,
			String cmdText) {
		Statement stmt = getStatement(conn);
		if (stmt == null) {
			return null;
		}
		try {
			ResultSet res = stmt.executeQuery(cmdText);
			ArrayList<T> list = new ArrayList<T>();
			ResultSetMetaData metaData = res.getMetaData();
			int cols_len = metaData.getColumnCount();

			while (res.next()) {
				// 通过反射机制创建一个实例
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = res.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // 打开javabean的访问权限
					field.set(resultObject, cols_value);
				}
				list.add(resultObject);
			}
			res.close();
			return list;
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			close(stmt);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 返回一个ArrayList<T>
	public static <T> ArrayList<T> getArrayList(Connection conn, Class<T> cls,
			String cmdText, Object... cmdParams) {
		PreparedStatement stmt = getPreparedStatement(conn, cmdText, cmdParams);
		if (stmt == null) {
			return null;
		}
		try {
			ResultSet res = stmt.executeQuery(cmdText);
			ArrayList<T> list = new ArrayList<T>();
			ResultSetMetaData metaData = res.getMetaData();
			int cols_len = metaData.getColumnCount();

			while (res.next()) {
				// 通过反射机制创建一个实例
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = res.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // 打开javabean的访问权限
					field.set(resultObject, cols_value);
				}
				list.add(resultObject);
			}
			res.close();
			return list;
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			close(stmt);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// 返回一个boolean
	public static boolean exist(String cmdText) {
		ArrayList<?> list = getArrayList(cmdText);
		if (list.size() <= 0)
			return false;
		else
			return true;
	}

	// 返回一个boolean
	public static boolean exist(Connection conn, String cmdText) {
		ArrayList<?> list = getArrayList(conn, cmdText);
		if (list.size() <= 0)
			return false;
		else
			return true;
	}

	// 返回一个boolean
	public static boolean exist(String cmdText, Object... cmdParams) {
		ArrayList<?> list = getArrayList(cmdText, cmdParams);
		if (list.size() <= 0)
			return false;
		else
			return true;
	}

	// 返回一个boolean
	public static boolean exist(Connection conn, String cmdText,
			Object... cmdParams) {
		ArrayList<?> list = getArrayList(conn, cmdText, cmdParams);
		if (list.size() <= 0)
			return false;
		else
			return true;
	}

	public static Object buildScalar(ResultSet rs) {
		if (rs == null) {
			return null;
		}
		Object obj = null;
		try {
			if (rs.next()) {
				obj = rs.getObject(1);
			}
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
		}
		return obj;
	}

	private static void close(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void closeEx(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).getStatement().close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void closeConnection(Object obj) {
		if (obj == null) {
			return;
		}
		try {
			if (obj instanceof Statement) {
				((Statement) obj).getConnection().close();
			} else if (obj instanceof PreparedStatement) {
				((PreparedStatement) obj).getConnection().close();
			} else if (obj instanceof ResultSet) {
				((ResultSet) obj).getStatement().getConnection().close();
			} else if (obj instanceof Connection) {
				((Connection) obj).close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
