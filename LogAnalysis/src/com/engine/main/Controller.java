package com.engine.main;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class Controller
 */
@MultipartConfig
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Controller() {

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String url = "Login.html";
		String action = request.getParameter("action");
		String userName = null;
		String password = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("userName")) {
					userName = cookie.getValue();
				}
				if (cookie.getName().equals("password")) {
					password = cookie.getValue();
				}
			}
		}

		if (action != null) {

			if (action.equals("login")) {
				System.out.println("Login");
				url="Welcome.jsp";

			} else if (action.equals("signup")) {

			} else if (action.equals("success")) {

			} else if (action.equals("updatePassword")) {

			} else if (action.equals("logout")) {

			} else if (action.equals("fail")) {

			}
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (ServletFileUpload.isMultipartContent(request)) {
			Part filepart = request.getPart("profile");
			InputStream fin = filepart.getInputStream();
		} else {
			doGet(request, response);
		}
	}

	private void setCookie(String userName, String password,
			HttpServletResponse response) {
		Cookie cookie1 = new Cookie("userName", userName);
		cookie1.setMaxAge(0);
		Cookie cookie2 = new Cookie("password", password);
		cookie2.setMaxAge(0);
		response.addCookie(cookie1);
		response.addCookie(cookie2);
	}

	private void deleCookie(HttpServletResponse response) {
		Cookie cookie1 = new Cookie("userName", "default");
		cookie1.setMaxAge(0);
		Cookie cookie2 = new Cookie("password", "default");
		cookie2.setMaxAge(0);
		response.addCookie(cookie1);
		response.addCookie(cookie2);
	}

}
