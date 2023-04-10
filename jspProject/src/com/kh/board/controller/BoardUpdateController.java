package com.kh.board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.kh.board.model.service.BoardService;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.MyFileRenamePolicy;
import com.oreilly.servlet.MultipartRequest;


//여울
/**
 * Servlet implementation class BoardUpdateController
 */
@WebServlet("/update.bo")
public class BoardUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		int bno = Integer.parseInt(request.getParameter("bno"));
		//받은 글번호로 해당 게시글 정보 조회해와서 수정페이지 전달 (수정페이지 - 작성페이지 이용하여 해보기)
		
		Board b = new BoardService().selectBoard(bno);
		
		Attachment at = new BoardService().selectAttachment(bno);
		
		ArrayList<Category> clist = new BoardService().categoryList();
		
		request.setAttribute("board", b);
		request.setAttribute("at", at);
		request.setAttribute("clist", clist);
		
		request.getRequestDispatcher("views/board/boardUpdateForm.jsp").forward(request, response);
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//수정처리
		request.setCharacterEncoding("UTF-8");
		
		//enctype이 multipart/form-data형식인지 확인
		if(ServletFileUpload.isMultipartContent(request)) {
			//전송파일용량 제한
			int maxSize = 10 * 1024 * 1024; //10메가바이트 제한
		
		
		//저장시킬 서버 경로 찾기(물리적인 서버 폴더경로)
		//getServletContext() - >까지는 프로젝트
		String savePath  = request.getSession().getServletContext().getRealPath("/resources/board_files"); //index 페이지의 디렉토리 WEB-INF 같은 선상에 있어 VIEW 이건 절대경로
		//파일명 수정 작업객체 추가하기
		
		MultipartRequest multiRequest = new MultipartRequest(request, savePath, maxSize, "UTF-8", new MyFileRenamePolicy());
		
		//수정작업에 필요한 기존 데이터 추출하기
		String boardTitle = multiRequest.getParameter("title");
		String boardContent = multiRequest.getParameter("content");
		String category = multiRequest.getParameter("category");
		int boardNo = Integer.parseInt(multiRequest.getParameter("boardNo"));
		
		Board b = new Board();
		b.setBoardTitle(boardTitle);
		b.setBoardNo(boardNo);
		b.setCategory(category);
		b.setBoardContent(boardContent);
		
		Attachment at = null;
		
		//새롭게전달된 첨부파일이 있다면 처리하기
		if(multiRequest.getOriginalFileName("reUpfile") != null) {
			
			at = new Attachment(); 
			//다시 복습
			at.setOriginName(multiRequest.getOriginalFileName("reUpfile"));
			at.setChangeName(multiRequest.getFilesystemName("reUpfile")); //이부분 못함  복습하기 이게 시스템에 저장된 진짜 네임 부르는 메소드 
			at.setFilePath("/resources//board_files"); //insertController의 at.setFilePath("resources/board_files");  확인하기
			
			//very impo 복습
			//기존에 첨부파일이 있었을 경우 (해당 데이터에서 수정작업을 해야한다)
			//hidden으로 boardUpDateForm에서 보넀잖아 첨부파일 있는지 없는지
			//form에서 hidden으로 파일번호와 변경된 이름 ( 서버에 저장된 이름) 을 전달했기 때문에 
			//해당 정보가 있는지 판별해주면 된다. 
			//식별자 fileNo 이용 
			if(multiRequest.getParameter("fileNo") != null) { 
				//새로운 첨부파일이 있고 기존에도 첨부파일이 있는경우
				//update attachment 
				//기존 파일번호(식별자)를 이용하여 데이터 변경하기 
				at.setFileNo(Integer.parseInt(multiRequest.getParameter("fileNo")));   //해당하는 원본파일번호를 식별자로 사용하라고하시는데
				
				//기존에 첨부파일 삭제하기. 어려운 부분이래
				//savePath 는 실질적으로 등록되어있는 물리적인 찍힌데
				
				//System.out.println(savePath); 안 찍히는데..?
				
				//실제 경로되는 경로 자체를 넣어야해서.. FilePath쓰는데 아니라 전체 경로를 넣어줘야한다... 많이들 신경 못쓰는 부분
				//filePath가 아니다.
				//파일삭제랑 정보수정은 다른거라는데..?화면에게..띄어준건..객체에 담겨져있는거기때문에 파일정보까지 null로 처리해야한다함 하..누르면 오류나게될거라는데..?
				new File(savePath+"/"+multiRequest.getParameter("originFileName")).delete(); //java.io
				
				}else {//원래 첨부파일이 없었고 새롭게 들어온 경우
					
				//현재 게시글 번호를 참조하게 INSERT하기
					at.setRefBno(boardNo);
				
			}
			
		}
			//DML (update)
			int result = new BoardService().updateBoard(b, at);
			System.out.println("boardNo찍히는지 테스트"+result);
			//새로운 첨부파일 없고 기존 첨부파일도 없는 경우 - board-update
			//새로운 첨부파일 있고 기존 첨부파일 없는 경우 - board-update / attachment -insert
			//새로운  첨부파일 있고 기존 첨부파일 있는 경우 - board-update / attachment -update
			
			/*
			 * 이거 찾는데한 1시간 넘게걸림 나는 getSession을 안함
			 * request.setAttribute("alertMsg", "게시글 수정 성공");은 세션에 값을 저장하는 것이 아니라, 해당 request에 값을 설정하는 것입니다. 그래서 request를 통해서는 다른 페이지로 이동하거나, 브라우저를 닫으면서 request가 제거되기 전까지만 이 값을 유지할 수 있습니다. 이와 달리 request.getSession().setAttribute("alertMsg", "게시글 수정 성공");은 세션에 값을 저장하는 것이므로, 세션이 유지되는 한 어느 페이지에서든 값을 유지할 수 있습니다.
			 * 
			 * */
		
		
			//이게 지금  
			//System.out.println("boardNo찍히는지 테스트"+boardNo); 했는데 
			//출력이 안되는거는..RESULT가 0이아니야
			if(result>0) {
				
				request.getSession().setAttribute("alertMsg", "게시글 수정 성공");
				//System.out.println("boardNo찍히는지 테스트"+boardNo);
				response.sendRedirect(request.getContextPath()+"/detail.bo?bno="+boardNo);
			} else {
				request.setAttribute("errorMsg", "게시글 수정 실패");
				request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
				//상대경로니까..views 폴더를 갖고있는 "현재 디렉토리는 WebContent"인거야................와 이게 상대경로니까....
				//나는 기준으로 현재 디렉토리니까...어휴
				//드디어 이해했따....ㅠㅠㅠㅠㅠㅠㅠ 나를 갖고있는 현재 디렉토리 기준! 그래서 마지막 슬래쉬 기준 이라고 뭐시기 하신거구나
			}

		

	}

	// 첨부파일 있고 없고 차이 생각해보기
	// 있는 경우 원래 있던 첨부파일 데이터에서 수정해야함
	// 없는 경우 새롭게 추가해야함(지금 작성되어있는 게시글에 추가되기)
	// 성공시 상세보기페이지 띄워주기
	// 실패시 에러페이지

}
}
