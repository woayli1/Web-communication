package pa;

import sql.MYSQL;

public class UsService {
	public static boolean userexist(String us, String pw) {
		String sql = "select * from chat_user where id= '" + us + "'and pwd='"
				+ pw + "'";
		return MYSQL.exist(sql);
	}

	public static Object getUserByname(String id) {
		String sql = "select name from chat_user where id='" + id + "'";
		return MYSQL.execScalar(sql);
	}

	public static int zhuche(String id1, String pwd1, String name1, String tn1) {
		String sql = "insert into chat_user(id,name,pwd,truename) values('"
				+ id1 + "','" + name1 + "','" + pwd1 + "','" + tn1 + "')  ";
		String sql2 = "select id from chat_user where id='" + id1 + "'";
		if (MYSQL.exist(sql2) == true) {
			return 0;// �û��Ѵ���
		} else {
			if (MYSQL.execSql(sql) >= 0) {
				return 5;// ע��ɹ���
			}
			return 1;// δ֪�쳣
		}
	}

	public static int xiugai(String id, String newpwd) {
		String sql = "update chat_user set pwd='" + newpwd + "' where id='"
				+ id + "'";
		if (MYSQL.execSql(sql) >= 0) {
			return 1;// �޸ĳɹ���
		}
		return 0;// δ֪�쳣
	}
	
	public static boolean zhaohui(String us, String tn) {
		String sql = "select * from chat_user where id= '" + us + "'and truename='"
				+ tn + "'";
		return MYSQL.exist(sql);
	}
}
