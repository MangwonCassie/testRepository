package com.kh.board.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.notice.model.service.NoticeService;

/**
 * Servlet implementation class BoardSelectController
 */
@WebServlet("/detail.bo")
public class BoardDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public  BoardDetailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		//쌤: 조회수 증가 / 게시글 조회 /첨부파일 조회 
		
		BoardService service = new BoardService();
		
		int boardNo = Integer.parseInt(request.getParameter("bno"));
	
		int result = service.increaseCount(boardNo);
		
		Attachment at = service.selectAttachment(boardNo);
		

	
		//Board 객체에 정보를 담아와야하니까!  ArrayList는 RNUM이 여러개일 때
		if(result>0) {
			Board board = new BoardService().selectBoard(boardNo);
			request.setAttribute("board", board);
			request.setAttribute("attachment", at);
			
			//쌤이 request.getRequestDispatcher("views/board/boardDetailView.jsp").forward(request, response); 
			//이 구문 IF 안에 넣으라고 하심 
			request.getRequestDispatcher("views/board/boardDetailView.jsp").forward(request, response);
		} else {
			request.setAttribute("errorMsg", "게시글 조회 실패");
			request.getRequestDispatcher("views/common/errorPage.jsp");
			
			//쌤한테 물어보기
			//이게 getRequestDispatcher()는 Context Root에서 시작하는거 아님? (아닌가봄..확인하기)
			//상대경로(작업 디렉토리 기준???)인 것 같은데 잘 모르겠음..개념 
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
