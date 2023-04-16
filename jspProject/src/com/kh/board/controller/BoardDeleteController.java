package com.kh.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;

/**
 * Servlet implementation class BoardDeleteController
 */
@WebServlet("/delete.bo")
public class BoardDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardDeleteController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//hi artur and yeooul again
		//button은 get방식 
		
		int boardNo = Integer.parseInt(request.getParameter("bno"));
		
		int result = new BoardService().deleteBoard(boardNo);
		
		int result2 = new BoardService().deleteAttachment(boardNo);
		
		if(result*result2 > 0) {
			//alert 띄어주고
			//redirect - >이거 메소드 이름 헷갈려함
			
			request.getSession().setAttribute("alertMsg", "삭제성공");//Session 얻은 뒤
			response.sendRedirect(request.getContextPath()+"/list.bo?currentPage=1"); //페이지1번으로 보냄
			
			//request   그 다음 또 get request request 가 또 들어감 
			
		} else {
			request.getSession().setAttribute("alertMsg", "삭제실패");
			request.getRequestDispatcher("views/common/errorPage");
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
