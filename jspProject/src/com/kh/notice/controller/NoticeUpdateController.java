package com.kh.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeUpdateController
 */
@WebServlet("/update.no")
public class NoticeUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    //a태그니까 doGet
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int noticeNo = Integer.parseInt(request.getParameter("nno")); //request에위임했으니까..? 이전페이지에서 url에서 가져오는게 getParameter라고?
		Notice notice = new NoticeService().selectNotice(noticeNo);
		request.setAttribute("notice", notice);
		request.getRequestDispatcher("views/notice/noticeUpdateForm.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		
		//session으로 안했으니까 request로 해 session.getAttribute("noticeNo");
	
		//getSession
		int noticeNo = Integer.parseInt(request.getParameter("nno")); 
		String noticeTitle = (String) request.getParameter("title");
		String noticeContent = (String) request.getParameter("content");
		Notice n  = new Notice(noticeNo, noticeTitle, noticeContent);
		
		//System.out.println("서블릿에서 왔습니다"+n);
		
		int result = new NoticeService().updateNotice(n);
		
	
		if(result>0) {   //수정한 글번호를이용해서 상세보기 페이지에 보내기 
			request.getSession().setAttribute("alertMsg", "공지사항 수정 완료");
			response.sendRedirect(request.getContextPath()+"/detail.no?nno="+noticeNo);
			
			
		} else {
			request.setAttribute("errorMsg", "공지사항 수정 실패");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}
		
	}

}
