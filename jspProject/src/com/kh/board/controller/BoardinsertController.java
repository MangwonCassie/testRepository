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
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

//여울
/**
 * Servlet implementation class BoardinsertController
 */
@WebServlet("/insert.bo")
public class BoardinsertController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardinsertController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//카테고리 목록 조회해오기
		
		ArrayList<Category> list = new BoardService().categoryList();
		
		request.setAttribute("clist", list);
	
		request.getRequestDispatcher("views/board/boardEnrollForm.jsp").forward(request, response);
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("UTF-8");
		
		/*
		 * form에서 multipart/form-data 형식으로 전송했기 때문에 기존 request 에 parameter 영역에서
		 * 꺼낼 수 없다. 특정 라이브러리를 사용하여 전달받아야함
		String category = request.getParameter("category");
		String title = request.getParameter("title");
		String content = request.getParameter("content");
		String file = request.getParameter("upfile");
		int userNo = Integer.parseInt(request.getParameter("userNo"));
		
		System.out.println(category);
		System.out.println(title);
		System.out.println(content);
		System.out.println(file);
		System.out.println(userNo);
		*/
		
		//cos.jar 라이브러리 추가 후 작업하기 
		//form 전송방식이 일반 방식이 아니라 multipart/form-data 방식이라면 
		//request를 multipart 객체로 이관시켜서 다뤄야함
		
		//enctype이 multipiart로 작성되어 넘어왔을 경우에만 수정이되도록 
		if(ServletFileUpload.isMultipartContent(request)) {
			//확인작업 System.out.println("멀티 파트 맞아요!!!!!!!!!!");
			
			//파일업로드를 위한 라이브러리 cos.jar
			//1. 전송되는 파일을 처리할 작업내용(용량제한, 전달된 파일을 전달할 경로 설정)
			//1_1. 용량 제한하기 (10메가바이트  Mbyte) -> 메가바이트라서 1024 두번 곱해줌 
			//byte - kbyte - mbyte- gbyte- tbyte
			
			int maxSize = 10 * 1024 * 1024; 
			
			//1-2. 전송된 파일을 저장할 서버의 폴더 경로 알아내기
			/*
			 * 세션 객체에서 제공하는 getRealPath메소드를 이용
			 * WebContent에 board_files 폴더 경로까지는 잡아주어야한다. 해당 폴더에 저장될 것이기 때문에
			 * */
			
			//getServletContext(); : 우리 프로젝트의 경로를 알 수 있다. -> 실질적인 서버 경로를 알아낼 수 있다.
			
			//		String savePath = request.getSession().getServletContext().getRealPath("/"); 이렇게 하면 
			//D:\server-workspace2_t\jspProject\WebContent\     :  "WebContent" 까지 출력됨

			//경로마지막에 / 를 또 붙여주기 왜냐면 그 폴더안에 저장할 것이기 때문에
			//("/resources/board_files/"); 
			//getRealPath() 실질적인 경로
		
			String savePath = request.getSession().getServletContext().getRealPath("/resources/board_files/"); //경로저장
			//System.out.println(savePath);
			
			/*
			 * 2. 전달된 파일명 수정 및 서버에 업로드 작업
			 * -HttpServletRequest request -> MultipartRequest multiRequest 로 변환하기(왜냐? MultiPart 폼데이터 형식으로 보냈기때문 그냥 request로 못 읽어옴)
			 * 
			 * 매개변수 생성자로 생성("cos.jar 라이브러리" 사용하기 때문에 거기에 있는 형식을 맞춰줘야함) 일단 ()안에 기존의 요청객체를 바꿔줘야하는거니까 매개변수에  들어가야함
			 * MultipartRequest multiRequest = new MultipartRequest(request객체, 저장할 폴더 경로, 용량제한, 인코딩값, 파일명 수정객체);
			 * 
			 * 위 구문 한줄이 실행되는 순간(생성자가 되는 순간) 지정한 경로에 파일이 업로드 된다.
			 * 사용자가 올린 파일명은 그대로 해당 폴더에 업로드 하지 않는게 일반적이다. 같은 파일명이 있을 경우 덮어씌어질 경우도 있고
			 * 한글/특수문자가 포함된 파일명 경우는 업로드되는 서버에따라 오류를 발생시킬 여지가 있기 때문에
			 * 
			 * 기본적으로 파일명을 변경해주는 객체를 제공한다.
			 * DefaultFileRenamePolicy 객체 (얘는 카운트만 함)
			 * 내부적으로 rename()이라는 메소드가 실행되며 파일명 수정을 해준다.
			 * 기본적으로 제공된 객체는 파일명 수정을 파일명 뒤에 숫자를 카운팅하는 형식으로만 진행해준다.
			 * ex)hello.jpg, hello1.jpg, hello2.jpg...
			 * 
			 * 해당 rename 작업을 따로 정의하여 사용해볼 것
			 * rename 메소드를 정의하면 해당 작업을 할 수 있다. (내 방식 대로)
			 * */
			
			//(파일명의 숫자) MultipartRequest multiRequest = new MultipartRequest(request, savePath, maxSize, "UTF-8", new DefaultFileRenamePolicy());
			//이 구문이 실행되는 순간 파일이 저장된다니까 (renamePolicy()는 우리형식으로) 클래스 직접 만들어야함 인터페이스 구현해서 재정의함
			MultipartRequest multiRequest = new MultipartRequest(request, savePath, maxSize, "UTF-8", new MyFileRenamePolicy());

			//3.DB에 저장할 정보들 추출하기
			//-카테고리 번호, 제목, 내용, 작성자번호(회원번호)를 추출하여 board 테이블에 insert하기 (그리고 넘어온 첨부파일이 있다면 그 정보 담아서 Attachment테이블에 따로 넣어주고) 별도로 작성해야한다.
			
			//이건 먼저 보드테이블애 넣을 데이터를 추출하는 것 ()안에 키값
			String category = multiRequest.getParameter("category");			
			String title = multiRequest.getParameter("title");
			String content = multiRequest.getParameter("content");
			String boardWriter = multiRequest.getParameter("userNo"); //String 객체 타입이니 굳이 형변환x
			
			//생성자 만들거나 getter 메소드
			Board b =  new Board(); 
			
			//매개변수에 채워넣어도 되는데 생성자 만들어야하니까 그냥 setter 메소드사용함
			b.setCategory(category);
			b.setBoardTitle(title);
			b.setBoardContent(content);
			b.setBoardWriter(boardWriter);
			
			//첨부파일에 대해서도 데이터 담아와야함 처음엔 null로 초기화 첨부파일이 있을지 없을지 모르니까
			
			Attachment at = null; // 처음에는 null로 초기화, 첨부파일이 있다면 객체 생성
			//multiRequest.getOriginalFileName("키") 이라는 "메소드가 있음" (넘어온 파일의 이름을 조회하는 메소드임) -> "키" name값 똑같음
			//첨부파일이 있을 경우 원본명을 가지고 옴 없는 경우 null을 반환
			
			//null이 아닐 때만 첨부파일 만들어주면 됨 
			if(multiRequest.getOriginalFileName("upfile") != null) {
			
				//우리가 세팅을 해야하는 부분
				at = new Attachment(); 	//조회가 된 경우 (첨부파일이 있다)
				at.setOriginName(multiRequest.getOriginalFileName("upfile")); //원본명   |  있으면 원본명을 가져오니까 그걸 set 하는거임
				//at.setChangeName(changeName);
				at.setChangeName(multiRequest.getFilesystemName("upfile"));  //수정명( 실제서버에 업로드된 파일명)
				//둘다 필요하다는데 이용자한테는 원본명을 보여주고...
				at.setFilePath("resources/board_files"); //뷰페이지에 보여주는 용도 | 그 아까 뒤에 path에 한거랑 연관 / 할지 안할지 여부코딩스타일에 따라 다름
				//뒤에 / 하고 안하냐 굉장히 중요함.
				
				//WebContent 다음에 했던거 하면 되고
			
			}
			
			//new File(savePath+"토끼.png").delete(); 프로젝트 refresh > 로그인 > 게시글 등록> 파일 삭제됐는지 확인 
			
				//서비스에게 준비된 객체들 전달하며 서비스 요청하기
				
				int result = new BoardService().insertBoard(b,at);
			
				if(result>0) {
					//session에 올려줌!
					request.getSession().setAttribute("alert", "게시글 작성 성공");
					//send샌드!! redirect
					//보낼 때는 "재요청" 방식으로 위임이 아니라 작업이 끝났으니까
					response.sendRedirect(request.getContextPath()+"/list.bo?currentPage=1");//목록 페이지 첫페이지로 돌아감 (가장 위에 뜰거니까 게시글 등록하면)
					
				} else {
						
					//실패시에는 업로드된 파일을 지워주는 작업이 필요하다. (게시글을 "없"는데 업로드파일이 자원만 낭비하니까)
					if(at != null) {//넘어온 파일이 있어서 객체가 생성됐다면 
						
						//**IO때 사용하던 파일객체 사용 경로를 잡아주면 경로에 있는 파일을 객체로 사용가능 **
						//해당경로 파일 경로 잡아서 File 객체 생성 후 delete 메소드로 파일 삭제작업
						//서버에 변경된 네임 들어가니까....ChangeName() -> OriginName 아니다.
						new File(savePath+at.getChangeName()).delete();
						//java.io.file 이고, savePath 저장해놓은거에 우리가 서버에 저장해놓은 getChangeName 가져와 바로 delete
						
					}
					request.setAttribute("errorMsg", "게시글 작성 실패");
					request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);

				}

				// System.out.println(b);

			}

		}

	}
