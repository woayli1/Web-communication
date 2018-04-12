package pa;

import java.util.ArrayList;

import sql.MYSQL;

public class UserList {
	public static ArrayList<ArrayList<?>> getUserList() {
		ArrayList<ArrayList<?>> aa = new ArrayList<ArrayList<?>>();
		String sql = "select name from chat_user";
		aa.add(MYSQL.getArrayList(sql));
		return aa;
	}
}
