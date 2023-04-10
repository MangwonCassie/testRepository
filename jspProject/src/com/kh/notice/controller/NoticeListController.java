package com.kh.notice.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.notice.model.service.NoticeService;
import com.kh.notice.model.vo.Notice;

/**
 * Servlet implementation class NoticeListController
 */
@WebServlet("/list.no")
public class NoticeListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoticeListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    //메뉴바 공지사항 a태그랑 연결
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
		ArrayList<Notice> list = new NoticeService().selectList();
		
		//request.setAttribute()를 사용하여 request 객체에 데이터를 담으면, 해당 데이터를 jsp 파일에서 꺼내서 사용할 수 있습니다.
		request.setAttribute("noticeList", list);
		//공지사항 전체 리스트를 request에 담아 위임한다( 포워딩)
		request.getRequestDispatcher("views/notice/noticeListView.jsp").forward(request, response);
		//메뉴바에서 공지사항 a태그에서 href list.no매핑주소로 연결하면 그 다음 noticeListView로 위임
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		
	
	}

}
