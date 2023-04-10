<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.kh.board.model.vo.Board" %>
<%@ page import="com.kh.common.model.vo.PageInfo, java.util.ArrayList" %>

<!-- 리스트 여울 -->

<%

	ArrayList<Board> list = (ArrayList<Board>)request.getAttribute("list");	
	PageInfo pi = (PageInfo)request.getAttribute("pi");
	%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.list.area{
	border: 1px solid white;
	text-align: center;
	}
	
	.list-area>tbody>tr:hover{
		background-color:gray;
		cursor: pointer;
	}

</style>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.3/dist/jquery.slim.min.js"></script>
<!-- Popper JS -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

</head>
<body>

<%@ include file = "../common/menubar.jsp" %>

	<div class="outer">
		<br>
		<h1 align="center">일반 게시판 넘어 오나요~~~</h1>
		<br><br>
		<!-- a태그도 버튼 모양 할 수 있으니까...그 이유 button 만들면 onclick function 해야한다 type=button이면 일걸? -->
		
		<!-- 로그인 안하면 버튼 안 보이게끔  -->
		<%if(loginUser != null) {%>
		<div align="center">
			<a href ="<%=contextPath%>/insert.bo" class="btn btn-info">글작성</a>
			<br>
		</div>
		<%} %>
		
		<a href="<%=contextPath%>/test.do/hoho">테스트</a>
		<!-- 쌤이 설명해주신 거 -->
		
		
		<table align="center" class="list-area">
		
		<thead>
			<tr>
				<!-- width가 다 따라간다  -->
				<th width="70">글번호</th>
				<th width="70">카테고리</th>
				<th width="300">제목</th>
				<th width="100">작성자</th>
				<th width="50">조회수</th>
				<th width="100">작성일</th>
			
			</tr>
		
		</thead>
		<tbody>
		
		
		<%if(list.isEmpty()){ %>
		<tr>
			<td colspan="6">조회된 게시글이 없습니다.</td>
		</tr>
		
		<%}  else { %>
			
			<!-- i<pi.getBoardLimit() for문에 하면 안됨!-->
			<!-- 11페이지 예를 들어서 게시판이 10개보다 적으면 오류남 -->
		<%for(Board b: list){ %>
			<tr>
				<td><%= b.getBoardNo() %></td>
				<td ><%= b.getCategory() %></td>	
				<td ><%= b.getBoardTitle() %> </td>
				<td ><%= b.getBoardWriter()%></td>
				<td ><%= b.getCount()%></td>
				<td ><%= b.getCreateDate()%></td>
			</tr>
			<%} %>
		<%} %>	
	
		</tbody>
	
		</table>
		<br><br>

		<!-- 동적으로 버튼 만들기 -->
		
		<!-- 각각요소를 꺼내서 조건처리해줘야함 처음은 시작페이지 끝은 엔드페이지 pageInfo 굳이 들고온 거 시작수를 startPage -->
		<div align="center" class="paging-area">
		
		<!-- 1페이지가 아니면 < 이게 생기게 -->
			<% if(pi.getCurrentPage()!= 1 ){%>
			<button onclick="location.href='<%=contextPath%>/list.bo?currentPage=<%=pi.getCurrentPage()-1%>';">&lt;</button>
			<%} %>
		
		
			<%for(int i =pi.getStartPage(); i<=pi.getEndPage(); i++){ %>
			<!-- 버튼을 누르면 currentPage onclick 해도 상관없고 a태그해도 상관없고 -->
				<%if (i != pi.getCurrentPage()){ %>
					<button onclick="location.href='<%=contextPath%>/list.bo?currentPage=<%=i%>';"><%=i %></button>
					<!--  location.href 속성의 할당문을 세미콜론으로 마무리-->
				<%} else{ %>
				<button disabled><%=i%></button>
				<%} %>
			<%} %>
			<!-- 1페이지인데 이전 버튼이 있으면 안되잖아 끝페이지에 도달하면 다음버튼 없도록 하기  -->
			
			<% if(pi.getCurrentPage()!= pi.getMaxPage() ){%>
			<button onclick="location.href='<%=contextPath%>/list.bo?currentPage=<%=pi.getCurrentPage()+1%>';">&gt;</button>
			<%} %>
			
			
		
		</div>
		
	
	</div>
	
	
	<script>
	 $(".list-area>tbody>tr").click(function(){
		 var bno = $(this).children().eq(0).text();
	
		 location.href ='<%=contextPath%>/detail.bo?bno='+bno;
	 })
	
	</script>

</body>
</html>