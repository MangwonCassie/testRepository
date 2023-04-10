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
 * Servlet implementation class MemberPassWordController
 */
@WebServlet("/updatePwd.me")
public class MemberUpdatePwdController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MemberUpdatePwdController() {
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
		 
		    //String userId = request.getParameter("userId");
		    //userId는 null....
		    //session으로 이용하기 
		    
		    HttpSession session = request.getSession();
		    //((Member) session.getAttribute("loginUser")).getUserId();      전체를() 묶으면 getUserId()바로 할 수 있다
		    
		    Member loginUser = (Member) session.getAttribute("loginUser");
		    String userId = loginUser.getUserId();
		    
		    
		    //session에서 다시 갱신........ 뭘 해야된다는데...
		    
		    String updatePwd = request.getParameter("updatePwd");
		   
		    Member updateMem = new MemberService().updatePwd(userId, updatePwd);
		    
			
			//성공시 index로 돌아가서(재요청) alertMsg로 회원가입을 환영합니다. 메세지 띄우기
			if(updateMem!=null) {
				request.getSession().setAttribute("alertMsg", "비밀번호 수정완료, 변경된 비밀번호로 재로그인해주세요.");
				//loginUser 갱신
				// 굳이 set할 필요없다고 함 request.setAttribute("loginUser", updateMem);
				response.sendRedirect(request.getContextPath()); //인덱스로 보내기
			}else {//실패시 errorPage가서(위임) 회원가입에 실패하였습니다 메세지 띄우기
				request.getSession().setAttribute("errorMsg", "비밀번호 수정 실패");
				request.getRequestDispatcher("views/common/errorPage.jsp").forward(request, response);
				
			}
		    
		 
		
	}

}
