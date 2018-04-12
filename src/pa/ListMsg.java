package pa;

import java.util.ArrayList;


public class ListMsg {
	   static ArrayList<String> bb= new ArrayList<String>();
	   public static void setMsg(String s){
		      bb.add(s);
	   }
       public static ArrayList<String> getMsg(){
    	   return bb;
       }
}
