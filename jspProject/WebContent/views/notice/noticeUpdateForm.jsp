<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.kh.notice.model.vo.Notice"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<%
	Notice notice = (Notice)request.getAttribute("notice");

%>


<style>

</style>
</head>
<body>


<%@ include file="../common/menubar.jsp" %>
	<div class="outer">
	<h2 align="center">공지사항 수정</h2>
	
	<form action="<%=contextPath%>/update.no" method="post">
	
	
	<input type="hidden" name="nno" value="<%=notice.getNoticeNo()%>">
	
	<table align="center">
	
		<tr>
		<td width="50" >제목</td>
		<td width="350"><input type="text" name="title" required value="<%=notice.getNoticeTitle()%>"></td>
		</tr>
		
		<tr>
			<td>내용</td>
			<td></td>
		</tr>
	
	
		<tr>
			<td colspan="2"> 
				<textarea name="content" rows="10" cols="20" style="resize:none" required><%=notice.getNoticeContent() %></textarea>
				
			</td>
		</tr>
		
		
	</table>
	
	<br><br>
	
	<div align="center">
		<button type="submit" id=update>수정하기</button>
		
		<input type="hidden" name="nno" value="<%= notice.getNoticeNo() %>">

	</div>
	</form>
	
	
	<!--  
		<script>
		$(document).ready(function() {
		    // Update button click event
		    $("#update").click(function(){
		        var nno = <%= notice.getNoticeNo() %>;

		        location.href = '../update.no?nno=' + nno;
		    });
		});
		</script>
	-->
	
	</div>


</body>
</html>