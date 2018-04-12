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
 * SQL �������� ͨ����,���Ժ����ɵ�ʹ�� JDBC ���������ݿ�
 * 
 * @author ����һ��
 */
public class MYSQL {
	/**
	 * ����
	 */
	public static String driver = "oracle.jdbc.driver.OracleDriver";
	/**
	 * �����ַ���
	 */
	public static String url = "jdbc:oracle:thin:@192.168.9.133:1521:SCHOOL";
	/**
	 * �û���
	 */
	public static String user = "scott";
	/**
	 * ����
	 */
	public static String password = "scott";

	/**
	 * ������ʵ��������
	 */
	private MYSQL() {
	}

	/**
	 * ��ȡһ�����ݿ����� ͨ��������� driver / url / user / password ���ĸ���̬������ �������ݿ���������
	 * 
	 * @return ���ݿ�����
	 */
	public static Connection getConnection() {
		try {
			// ��ȡ����
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
	 * ��ȡһ�� Statement �� Statement �Ѿ��������ݼ� ���Թ���,���Ը���
	 * 
	 * @return �����ȡʧ�ܽ����� null,����ʱ�ǵü�鷵��ֵ
	 */
	public static Statement getStatement() {
		Connection conn = getConnection();
		if (conn == null) {
			return null;
		}
		try {
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// �������ݼ����Թ���,���Ը���
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			close(conn);
		}
		return null;
	}

	/**
	 * ��ȡһ�� Statement �� Statement �Ѿ��������ݼ� ���Թ���,���Ը���
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @return �����ȡʧ�ܽ����� null,����ʱ�ǵü�鷵��ֵ
	 */
	public static Statement getStatement(Connection conn) {
		if (conn == null) {
			return null;
		}
		try {
			return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			// �������ݼ����Թ���,���Ը���
		} catch (SQLException ex) {
			Logger.getLogger(MYSQL.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	/**
	 * ��ȡһ���������� PreparedStatement �� PreparedStatement �Ѿ��������ݼ� ���Թ���,���Ը���
	 * 
	 * @param cmdText
	 *            ��Ҫ ? ������ SQL ���
	 * @param cmdParams
	 *            SQL ���Ĳ�����
	 * @return �����ȡʧ�ܽ����� null,����ʱ�ǵü�鷵��ֵ
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
	 * ��ȡһ���������� PreparedStatement �� PreparedStatement �Ѿ��������ݼ� ���Թ���,���Ը���
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @param cmdText
	 *            ��Ҫ ? ������ SQL ���
	 * @param cmdParams
	 *            SQL ���Ĳ�����
	 * @return �����ȡʧ�ܽ����� null,����ʱ�ǵü�鷵��ֵ
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
	 * ִ�� SQL ���,���ؽ��Ϊ �� �� ��Ҫ����ִ�зǲ�ѯ���
	 * 
	 * @param cmdText
	 *            SQL ���
	 * @return �Ǹ���:����ִ��; -1:ִ�д���; -2:���Ӵ���
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
	 * ִ�� SQL ���,���ؽ��Ϊ���� ��Ҫ����ִ�зǲ�ѯ���
	 * 
	 * @param cmdText
	 *            SQL ���
	 * @return �Ǹ���:����ִ��; -1:ִ�д���; -2:���Ӵ���
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
	 * ִ�� SQL ���,���ؽ��Ϊ���� ��Ҫ����ִ�зǲ�ѯ���
	 * 
	 * @param cmdText
	 *            ��Ҫ ? ������ SQL ���
	 * @param cmdParams
	 *            SQL ���Ĳ�����
	 * @return �Ǹ���:����ִ��; -1:ִ�д���; -2:���Ӵ���
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
	 * ִ�� SQL ���,���ؽ��Ϊ���� ��Ҫ����ִ�зǲ�ѯ���
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @param cmdText
	 *            ��Ҫ ? ������ SQL ���
	 * @param cmdParams
	 *            SQL ���Ĳ�����
	 * @return �Ǹ���:����ִ��; -1:ִ�д���; -2:���Ӵ���
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
	 * ���ؽ�����ĵ�һ�е�һ�е�ֵ,��������
	 * 
	 * @param cmdText
	 *            SQL ���
	 * @return
	 */
	public static Object execScalar(String cmdText) {
		ResultSet rs = getResultSet(cmdText);
		Object obj = buildScalar(rs);
		closeConnection(rs);
		return obj;
	}

	/**
	 * ���ؽ�����ĵ�һ�е�һ�е�ֵ,��������
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @param cmdText
	 *            SQL ���
	 * @return
	 */
	public static Object execScalar(Connection conn, String cmdText) {
		ResultSet rs = getResultSet(conn, cmdText);
		Object obj = buildScalar(rs);
		closeEx(rs);
		return obj;
	}

	/**
	 * ���ؽ�����ĵ�һ�е�һ�е�ֵ,��������
	 * 
	 * @param cmdText
	 *            ��Ҫ ? ������ SQL ���
	 * @param cmdParams
	 *            SQL ���Ĳ�����
	 * @return
	 */
	public static Object execScalar(String cmdText, Object... cmdParams) {
		ResultSet rs = getResultSet(cmdText, cmdParams);
		Object obj = buildScalar(rs);
		closeConnection(rs);
		return obj;
	}

	/**
	 * ���ؽ�����ĵ�һ�е�һ�е�ֵ,��������
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @param cmdText
	 *            ��Ҫ ? ������ SQL ���
	 * @param cmdParams
	 *            SQL ���Ĳ�����
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
	 * ����һ�� ResultSet
	 * 
	 * @param cmdText
	 *            SQL ���
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
	 * ����һ�� ResultSet
	 * 
	 * @param conn
	 * @param cmdText
	 *            SQL ���
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
	 * ����һ�� ResultSet
	 * 
	 * @param cmdText
	 *            ��Ҫ ? ������ SQL ���
	 * @param cmdParams
	 *            SQL ���Ĳ�����
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
	 * ����һ�� ResultSet
	 * 
	 * @param conn
	 *            ���ݿ�����
	 * @param cmdText
	 *            ��Ҫ ? ������ SQL ���
	 * @param cmdParams
	 *            SQL ���Ĳ�����
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

	// ����һ��ArrayList
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

	// ����һ��ArrayList
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

	// ����һ��ArrayList
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

	// ����һ��ArrayList
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

	// ����һ��ArrayList<T>
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
				// ͨ��������ƴ���һ��ʵ��
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = res.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}

					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // ��javabean�ķ���Ȩ��
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

	// ����һ��ArrayList<T>
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
				// ͨ��������ƴ���һ��ʵ��
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = res.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // ��javabean�ķ���Ȩ��
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

	// ����һ��ArrayList<T>
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
				// ͨ��������ƴ���һ��ʵ��
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = res.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // ��javabean�ķ���Ȩ��
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

	// ����һ��ArrayList<T>
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
				// ͨ��������ƴ���һ��ʵ��
				T resultObject = cls.newInstance();
				for (int i = 0; i < cols_len; i++) {
					String cols_name = metaData.getColumnName(i + 1);
					Object cols_value = res.getObject(cols_name);
					if (cols_value == null) {
						cols_value = "";
					}
					Field field = cls.getDeclaredField(cols_name);
					field.setAccessible(true); // ��javabean�ķ���Ȩ��
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

	// ����һ��boolean
	public static boolean exist(String cmdText) {
		ArrayList<?> list = getArrayList(cmdText);
		if (list.size() <= 0)
			return false;
		else
			return true;
	}

	// ����һ��boolean
	public static boolean exist(Connection conn, String cmdText) {
		ArrayList<?> list = getArrayList(conn, cmdText);
		if (list.size() <= 0)
			return false;
		else
			return true;
	}

	// ����һ��boolean
	public static boolean exist(String cmdText, Object... cmdParams) {
		ArrayList<?> list = getArrayList(cmdText, cmdParams);
		if (list.size() <= 0)
			return false;
		else
			return true;
	}

	// ����һ��boolean
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
