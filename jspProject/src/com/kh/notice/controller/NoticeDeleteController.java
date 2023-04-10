package com.kh.notice.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.notice.model.service.NoticeService;

/**
 * Servlet implementation class NoticeDelete
 */
@WebServlet("/delete.no")
public class NoticeDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		int noticeNo = Integer.parseInt(request.getParameter("nno"));
		System.out.println("nno 이름입니다!!!!!! "+noticeNo);
		
		int result = new NoticeService().deleteNotice(noticeNo);
		
		if(result>0) {
			
			
			request.getSession().setAttribute("alertMsg", "공지사항 삭제성공");
			response.sendRedirect(request.getContextPath()+"/list.no");// 클라이언트에게 HTTP 응답을 반환
		} else {
			request.setAttribute("errorMsg", "공지사항 삭제 실패");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
			request.getRequestDispatcher(""); //그럼 시작이 WebContent 인거임? 
			
			//getRequestDispatcher("views/common/errorPage.jsp")에 왜 /WebContent/views/common/errorPage.jsp로 시작안하는거임?
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
