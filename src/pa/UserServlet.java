package pa;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public UserServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String method = request.getParameter("method").trim();

		if (method.equals("userexit")) {
			String us = request.getParameter("username").trim();
			String pw = request.getParameter("password").trim();
			booUser(us, pw, out);
		}

		if (method.equals("zhaohui")) {
			String us = request.getParameter("id").trim();
			String tn = request.getParameter("tn").trim();
			booUser2(us, tn, out);
		}

		if (method.equals("queren")) {
			String us = request.getParameter("id").trim();
			String pw = request.getParameter("pwd").trim();
			booUser1(us, pw, out);
		}

		if (method.equals("xiugai")) {
			String us = request.getParameter("id").trim();
			String pw = request.getParameter("pwd").trim();
			int a = UsService.xiugai(us, pw);
			String b = JSON.toJSONString(a);
			out.print(b);
		}

		if (method.equals("zc")) {
			String id = request.getParameter("id").trim();
			String pw = request.getParameter("pwd").trim();
			String name = request.getParameter("name").trim();
			String tn = request.getParameter("truename").trim();
			booUser(id, pw, name, tn, out);
		}

		if (method.equals("getUser")) {
			// String id=(String)request.getSession().getAttribute("user");
			String id = request.getParameter("id2").trim();
			Object name = UsService.getUserByname(id);
			String us = JSON.toJSONString(name);
			out.print(us);
		}
		if (method.equals("UserList")) {
			ArrayList<?> aa = UserList.getUserList();
			Object res = JSON.toJSON(aa);
			out.print(res);
		}
		if (method.equals("sendmsgs")) {
			String str = request.getParameter("send");
			ListMsg.setMsg(str);
		}
		if (method.equals("getmessage")) {
			ArrayList<?> arr = ListMsg.getMsg();
			String aa = JSON.toJSONString(arr);
			out.print(aa);
		}

		if (method.equals("getUsername")) {
			Object n = UsService.getUserByname("id");
			String ustr = JSON.toJSONString(n);
			out.print(ustr);
		}

		out.flush();
		out.close();
	}

	private void booUser(String us, String pw, PrintWriter out) {
		// TODO Auto-generated method stub
		if (us.equals("") || pw.equals("") || us.equals(null)
				|| pw.equals(null)) {
			out.print("用户名或密码不能为空！");
		} else if (UsService.userexist(us, pw)) {
			out.print(us);
		} else {
			out.print("用户名或密码错误！");
		}
		return;
	}

	private void booUser2(String us, String tn, PrintWriter out) {
		// TODO Auto-generated method stub
		if (us.equals("") || tn.equals("") || us.equals(null)
				|| tn.equals(null)) {
			out.print("用户名或真实姓名不能为空！");
		} else if (UsService.zhaohui(us, tn)) {
			out.print(us);
		} else {
			out.print("用户名或真实姓名错误！");
		}
		return;
	}

	private void booUser1(String us, String pw, PrintWriter out) {
		// TODO Auto-generated method stub
		if (us.equals("") || pw.equals("") || us.equals(null)
				|| pw.equals(null)) {
			out.print("用户名或密码不能为空！");
		} else if (UsService.userexist(us, pw)) {
			out.print(us);
		} else {
			out.print("原用户名或密码错误！");
		}
		return;
	}

	private void booUser(String id, String pw, String name, String tn,
			PrintWriter out) {
		// TODO Auto-generated method stub
		if (id.equals("") || pw.equals("") || id.equals(null)
				|| pw.equals(null) || name.equals("") || tn.equals("")
				|| name.equals(null) || tn.equals(null)) {
			out.print("不能为空!");
		} else {
			if (UsService.zhuche(id, pw, name, tn) == 5) {
				out.print("5");// 注册成功
			} else if (UsService.zhuche(id, pw, name, tn) == 0) {
				out.print("0");// 用户已存在
			} else {
				out.print("1");// 注册失败
			}
		}
		return;
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
