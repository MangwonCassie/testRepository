package com.kh.board.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Board;
import com.kh.common.model.vo.PageInfo;

//여울
/**
 * Servlet implementation class BoardListController
 */
@WebServlet("/list.bo")
public class BoardListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//------------------------------------------페이징 처리------------------------------------------ㅍ
		//총게시물이 몇개인지 일단 알아야함
		
		int listCount; //현재 총 게시글의 개수 (그걸 토대로 페이징바를 몇개를 자를건지... 그런 것들을 계산할 수 있다. 제일 먼저 처리해야함)
		int currentPage; //현재 페이지 url에 현재페이지 번호 page=5이런식으로 뜸 
		int pageLimit; //페이지 하단에 보여질 페이징바의 페이지 최대 개수
		int boardLimit; //한페이지에서 보여질 게시글 개수 (최대개수)
		int maxPage; //가장 "마지막" 페이지 (총페이지의 개수)
		int startPage; //페이지 하단에 보여질 페이징바의 시작수 
		int endPage; //페이징 하단에 보여질 페이징바의 끝 수
		
		//listCount: 총 게시글 개수 구하기
		listCount = new BoardService().selectListCount();
		
		//System.out.println(listCount);
		
		//현재페이지
		currentPage = Integer.parseInt(request.getParameter("currentPage"));//메인바에서 옴! 230407 
		//처음에 일반게시판을 눌렀을 때도 from menubar.jsp의 list.bo?currentPage=1 매핑주소에서 currentPage를 담아서 온다. 쿼리스트링 통해서
		//일반게시판을 처음으로 눌렀을 때 무조건 1페이지 가게끔 해야하잖아
		
		
		//	메뉴바에 <a href="<%=contextPath%>/list.bo?currentPage=1">일반게시판</a> 이게 넘어오는거다.!
		//System.out.println("현재페이지: "+currentPage); //확인용
		
		//pageLimit: 페이지 하단에 보여질 페이징바의 페이지 최대개수 (목록 단위)
		pageLimit = 10;
		
		//boardLimit : 한페이지에 보여질 게시글 개수(게시글 단위)
		boardLimit = 10;
		
		
		/*
		 *-공식 찾기
		 *게시글 총 개수  boardLimit       maxPage
		 *100개 		10	  - 10.0		10
		 *101개 		10	 - 10.1			11
		 *111개 		10 	- 11.1				12
		 *
		 *올림처리를 통해 maxPage를 구하자 나눗셈 후 
		 *1) listCount를 "double자료형"으로 바꾸기  - > why? listCount를 double 자료형으로 바꾸는 이유는 나눗셈을 수행할 때, 정수형으로 나누게 되면 "결과값이 소수점 이하를 버리게 됨". 정확한 계산을 위해 나눗셈을 수행하기 전 실수형으로 형변환 
		 *2) listCount/boardLimit 
		 *3) 결과를 올림처리하기 (Math.ceil)
		 */
		
		//ceil:ava에서 Math.ceil 함수의 반환값은 double, 만약 listCount와 boardLimit가 모두 int 형태인 경우에는 double 형으로 자동 형변환
		//그래서 int 형변환
		//maxPage: listCount와 boardLimit의 영향을 받는 수
		
		maxPage =(int)Math.ceil((double)listCount/boardLimit); //총게시글/한페이지에 보여질 게시글 개수
		
		//startPage:페이징바의 시작수
		/*
		 * 공식찾기
		 * startPage: 1, 11, 21,31,41,.......n*pageLimit+1
		 * 10의 배수 + 1 지금은
		 * pageLimit처리하기 나름
		 * 
		 * currentPage StartPage pageLimit: 10(으로 고정) 
		 * 1	-1(을해야함) 0/10 0		1	0*pageLimit+1 -> 1 나오게 하려면 -1이 되야함 -1안하면 페이지리밋단이 됐을때 10의 자리수에서 1이 나옴
		 * 5	-1(을해야함) 4/10 0			1
		 * 10	-1(을해야함) 9/10 0		1
		 * 11	-1(을해야함) 10/10 0		11 1*pageLimit+1 -11
		 * 15			11
		 * 21	20/10/2		21 2*pageLimit+1 -21
		 * 
		 * 20  11?
		 * 
		 * startPage : (currentPage-1)/pageLimit * pageLimit+1
		 * 
		 */
		
		startPage = (currentPage-1)/pageLimit * pageLimit +1;
		
		//1부터 시작하니까
		//endPage: 페이징 바 끝 수  
		//1-10  / 11- 20 // 21-30  /: startPage+pageLimit-1
		
		endPage = startPage + pageLimit-1;
		//만약 총 페이지수가 13페이지라면?
		//startPage: 11  endPage: 20 
		
		//11페이지인데 20페이지까지 있고 다 빈칸이잖아 그걸 처리해야함 if문을 통해
		//끝수가 총 페이지수보다 크다면 해당 수를 총 페이지수로 바꿔주기
		if(endPage>maxPage) {
			endPage = maxPage;
		}
		
		//페이지정보들을 하나의 공간에 담아보내기 (VO이용)
		PageInfo pi = new PageInfo(listCount,currentPage,pageLimit,boardLimit,maxPage,startPage,endPage);
		
		
		//System.out.println(pi); 출력해서 확인
		//현재 사용자가 요청한 페이지에 (currentPage)에 보여질 게시글 리스트 조회
		//조회를 할 떄 해당하는 게시글순번을 매겨서 그 순번에 대해서 조회
		//currentPage가 3번이다 그러면 31번째 게시글부터 나와야함
		//게시글을 다 가져와서 자르는게 아니라 그 부분마다 잘라서 들고올거임
		
		ArrayList<Board> list = new BoardService().selectList(pi);
		
		//조회된 리스트와 페이징정보 request 보내기
		
		/*
		for(Board b : list) {
			System.out.println(b);
		}
		
		*/
		
		request.setAttribute("list", list);
		request.setAttribute("pi", pi);
		request.getRequestDispatcher("views/board/boardListView.jsp").forward(request, response);
	
		
		//현재 사용자가 요청한 페이지에 (currentPage)에 보여질 게시글 리스트 조회
		//ArrayList<Board> list = new BoardService().selectList(pi);
		
		//System.out.println(maxPage);
		
		
		//request로 보내주면 된다!!!! 그 다음 확인 이동 되는지
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
