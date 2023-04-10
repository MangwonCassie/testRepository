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
 * Servlet implementation class NoticeDetailController
 */
@WebServlet("/detail.no")
public class NoticeDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    //url매핑이니까 doget
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession session = request.getSession();

		int noticeNo = Integer.parseInt(request.getParameter("nno"));
		
		//해당 글번호로 그 글 정보에 대해 조회수를 증가시키고 난 뒤 조회해오기 (못한 부분)
		//조회수 증가 메소드
		
		int result = new NoticeService().increaseCount(noticeNo);
		
	
	
		
		if(result>0) {
			
			//조회수 증가가 잘되었다면 해당 게시글 정보 조회해오기
			
			Notice notice = new NoticeService().selectNotice(noticeNo);
	
			request.setAttribute("notice", notice);
			
		} else {		//조회수 증가가 실패했다면 에러페이지로 보내버리기
			request.getSession().setAttribute("errorMsg", "조회수 수정 실패");
		}

		
		
		
		
	 // 조회한 공지사항 정보를 request 객체에 추가함
		    
		
		//공지사항 하나의 정보를 조회해와서 request에 담아 위임하기
		//제목//작성자//작성일//내용// 다 가져와야함
		//selectNotice 메소드라고 하자고 함 
		//selectNotice()
		//보내진 view에서 데모데이터(가짜데이터)에서 담아온 notice 정보 출력하기 
		
		//상세보기에..조회해온 데이터가 쫙 떠야함...검은창에!
		
		
		//이게 페이지를 위임시키는거야.
		request.getRequestDispatcher("views/notice/noticeDetailView.jsp").forward(request, response);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		int noticeNo = Integer.parseInt(request.getParameter("nno"));
		
		//noticeForm에..뿌려져있어야함..........
	}

}
