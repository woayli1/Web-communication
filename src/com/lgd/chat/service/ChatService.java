package com.lgd.chat.service;

import java.util.List;

import pa.User;
import sql.MYSQL;

public class ChatService {
    public static List <User> getListUser(){
    	String sql= "select*from chat_user;";
    	return MYSQL.getArrayList(User.class,sql);
    }
}
