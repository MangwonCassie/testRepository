package com.kh.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.vo.Member;
import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeEnrollController
 */
@WebServlet("/insert.no")
public class NoticeinsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeinsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		request.getRequestDispatcher("views/notice/noticeEnrollForm.jsp").forward(request, response);
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//데이터베이스에 작성한 내용 등록시키고  아니...자바로 하라는 말인가..?
		//성공시 알림메세지로 공지 등록 완료 띄우고 공지사항 목록으로 이동 (재요청)
		//실패시 에러페이지로 이동 (공지사항 작성 실패) 메세지(위임)
		
		request.setCharacterEncoding("UTF-8");
		
		//관리자로 (여러명일 수 있음) 로그인된 상태에서만 공지사항을 적을 수 있으니까
		HttpSession session = request.getSession();
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String userNo = request.getParameter("userNo"); //이거는 input hidden으로 한거임 
		
		System.out.println(title+" "+content+" "+userNo);
		
	
		Notice n = new Notice();
		
		n.setNoticeTitle(title);
		n.setNoticeContent(content);
		n.setNoticeWriter(userNo);
		
		int result = new NoticeService().insertNotice(n);
		
		if(result>0) {
			request.getSession().setAttribute("alertMsg", "공지 등록 완료했습니다.");
			response.sendRedirect(request.getContextPath()+"/list.no");
			
		} else {
			request.setAttribute("errorMsg", "공지 등록완료에 실패하였습니다.");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
		
		
	}

}
