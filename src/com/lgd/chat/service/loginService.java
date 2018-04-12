package com.lgd.chat.service;

import java.util.ArrayList;

import pa.User;
import sql.MYSQL;

public class loginService {
	public static boolean userexist(String us,String pw){
		String sql="select * from chat_user where name='"+ us +"' and pwd='"+pw+"';";
		return MYSQL.exist(sql);
 	}
     public static ArrayList<User> getUserByName(String us){
    	 String sql= "select * from chat_user where name='"+us;
     return MYSQL.getArrayList(User.class,sql);
     }
}
