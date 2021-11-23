package co.micol.bootstrap.comm;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.micol.bootstrap.comm.main.Buttons;
import co.micol.bootstrap.main.Login;
import co.micol.bootstrap.main.MainCommand;
import co.micol.bootstrap.member.command.LoginCheck;
import co.micol.bootstrap.member.command.Logout;
import co.micol.bootstrap.notice.command.NoticeList;

@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Command> map = new HashMap<String, Command>();

	public FrontController() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		map.put("/main.do", new MainCommand()); // 시작하는곳.
		map.put("/noticeList.do", new NoticeList()); // 공지사항
		map.put("/buttons.do", new Buttons());	//buttons.html 호출
		map.put("/login.do", new Login());
		map.put("/loginCheck.do", new LoginCheck());
		map.put("/logout.do", new Logout());
	}

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String uri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String page = uri.substring(contextPath.length());
		
		Command command = map.get(page);
		String viewPage = command.run(request, response);

		if (!viewPage.endsWith(".do")) {
			if (viewPage.startsWith("ajax:")) {
				// ajax 처리
				return;
			}
			if (viewPage.endsWith(".jsp")) {
				viewPage = "WEB-INF/views/" + viewPage;		//타일즈를 안탈때
			} else {
				viewPage = viewPage + ".tiles"; // 타일즈 레이아웃 사용하기 위해서 만듬.
			}
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
		dispatcher.forward(request, response);
	}

}
