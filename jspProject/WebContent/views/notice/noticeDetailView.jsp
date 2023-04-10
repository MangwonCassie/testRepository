<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.kh.notice.model.vo.Notice" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<%
	Notice notice = (Notice)request.getAttribute("notice");
%>


<style>
#detail-area{
border: 1px solid white;
}
</style>

</head>
<body>


	
	<%@ include file="../common/menubar.jsp" %>
	
	
	<div class="outer">
		<br><br>
	
		<h2 align="center">공지사항 상세보기</h2>
		
		<table id="detail-area" align="center" >
		
		<tr>
			<th width="70">제목</th>
			<td width="350" colspan="3"><%= notice.getNoticeTitle() %></td>
		</tr>
		
		<tr>
			<th>작성자</th>
			<td><%= notice.getNoticeWriter()%></td>
			<th>작성일</th>
			<td><%= notice.getCreateDate()%></td>
		</tr>
		
		<tr>
			<th>내용</th>
			<td colspan="3"><p style="height:150px"><%= notice.getNoticeContent() %></p></td>
		</tr>
		</table>
		<br><br>
		
		
		<%if(loginUser != null && loginUser.getUserId().equals(notice.getNoticeWriter())){ %>
		<div align="center">
		<!-- get방식은 위임 post 처리해서...하는거? -->
		
		<a href="<%=contextPath%>/update.no?nno=<%=notice.getNoticeNo() %>" class="btn btn-warning">수정하기</a>
		<a href="<%=contextPath%>/delete.no?nno=<%=notice.getNoticeNo() %>" class="btn btn-danger">삭제하기</a>
		
		<!-- 혼자 좀 해 -->
		<!--<a href="<%=contextPath%>/delete.no?nno=<%=notice.getNoticeNo() %>" class="btn btn-danger">삭제하기</a>  -->
		<!-- 매핑 주소 오타 나면 HTTP 상태 404 – 찾을 수 없음 -->
		
		
		</div>
		
		<%} %>
		
		
				<script>
		    //.list-area 클래스 자손 tbody 자손 tr 클릭 됐을 때
		    $(".list-area>tbody>tr").click(function(){
		        //목록에 글을 클릭했을 때 해당 글번호가 있어야
		        //그 글에 대해서 상세조회를 할 수 있으니
		        //글번호 추출하여
		        //서버에 넘기기
		       // console.log($(this).children()); innerText!!!!!!!!!라고!!!
		     var nno = $(this).children().eq(0).text();
		       // var nno = console.log($(this).children().eq(0).text());이게아님!!
		       //맞습니다. console 창에서 출력된 값을 변수에 할당하려면 다른 방법을 사용해야 합니다.
		
		     //요청할 url?키=밸류&키=밸류.....
		     //물음표 뒤에 내용들을 쿼리 스트링이라고 한다. -직접 기술하여 넘기기
		     //''는 문자열이라는데 , 옆에 nno는 변수처리
		     // '/jsp/detail.no?nno='+nno
		     location.href = '<%=contextPath%>/detail.no?nno='+nno;
		    })
		
		</script>
		
		
	</div>

</body>
</html>