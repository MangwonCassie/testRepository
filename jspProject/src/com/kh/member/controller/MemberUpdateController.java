package com.kh.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.member.model.service.MemberService;
import com.kh.member.model.vo.Member;

/**
 * Servlet implementation class MemberUpdateController
 */
@WebServlet("/update.me")
public class MemberUpdateController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberUpdateController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("UTF-8");
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		String phone = request.getParameter("phone");
		String email = request.getParameter("email");
		String address = request.getParameter("address");
		String[] interests = request.getParameterValues("interest");//null
		String interest = "";
		
		if(interests !=null) {
			interest = String.join(",", interests);
		}
		
		Member m = new Member(userId,userName,phone,email,address,interest);
		
		
		//insert는 dml구문이니까 결과값이 처리된 행수(int)로 넘어올것 
		Member updateMem = new MemberService().updateMember(m);
		
		//변경된 정보 수정하기
		
		//수정 완료 후 성공시 정보변경 완료되었습니다. 알림 메세지 후 마이페이지로 이동 (변경정보 적용) (재요청)
		
		//실패 시 에러페이지 포워딩 (위임)
		
		//성공시 index로 돌아가서(재요청) alertMsg로 회원가입을 환영합니다. 메세지 띄우기
		if (updateMem != null) { //updateMem이 null이 아니면 (조회결과가 있다면) 성공 
			
			HttpSession session = request.getSession();
			session.setAttribute("alertMsg", "정보 변경 완료되었습니다.");
			session.setAttribute("loginUser", updateMem);
			response.sendRedirect(request.getContextPath()+"/myPage.me"); 
			//루트 뒤에 마이페이지 요청 매핑 주소 넣기 
			//response.sendRedirect(request.getContextPath()); //재요청 나는 여기까지 했는데 마이페이지로 돌아가야함!!! 
			
		} else {// 실패시 errorPage가서(위임) 회원가입에 실패하였습니다 메세지 띄우기
			request.setAttribute("errorMsg", "정보수정에 실패하였습니다.");
			request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
		}

	}

}


