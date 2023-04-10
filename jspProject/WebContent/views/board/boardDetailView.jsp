<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.kh.board.model.vo.Board, com.kh.board.model.vo.Attachment"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<%
	Board board = (Board)request.getAttribute("board");
	Attachment at = (Attachment)request.getAttribute("attachment");
	System.out.println("getBoardNo넘어오니"+board.getBoardNo()); //getBoardNo()이 0 이 넘어옴 무슨 일이야..
	System.out.println("Board 전부다 넘어오니"+board);
	System.out.println("at 전부다 넘어오니"+at);
%>
<!-- 여울 -->
  <style>
        .outer table {
            border-color: white;
        }

    </style>

</head>
<body>

	<%@ include file="../common/menubar.jsp" %>

    <div class="outer">
    	<br>
        <h2 align="center">일반게시판 상세보기</h2>
        <br>
        
        <table id="detail-area" align="center" border="1">
            <tr>
                <th width="70">카테고리</th>
                <td width="70"><%=board.getCategory() %></td>
                <th width="70">제목</th>
                <td width="380"><%= board.getBoardTitle() %></td>
            </tr>
            <tr>
                <th>작성자</th>
                <td><%= board.getBoardWriter() %></td>
                <th>작성일</th>
                <td><%=board.getCreateDate() %></td>
            </tr>
            <tr>
                <th>내용</th>
                <td colspan="3">
                <p style="height:150px"><%=board.getBoardContent() %></p></td>
            </tr>
            <tr>
            
            	<!-- 첨부파일 있을 때 없을 떄 처리 가능 if문으로  -->
                <th>첨부파일</th>
           
                <td colspan="3">
                <!-- td에 들어가야함 -->
                 <%if(at == null) {%>
                	첨부파일이 없습니다.
                <%}  else{ %>
                <a href="<%= request.getContextPath()%>/<%=at.getFilePath()%>/<%=at.getChangeName() %>" download><%=at.getOriginName() %></a>
                <%} %>
          	     </td>
            </tr>
        </table>
        
        <br>
        <br>
        
       <%if(loginUser != null && loginUser.getUserId().equals(board.getBoardWriter())){ %>
         	<div align="center">
         		<button onclick="location.href='<%=contextPath%>/update.bo?bno=<%=board.getBoardNo()%>'" >수정하기</button>
         		<button onclick="location.href='<%=contextPath%>/delete.bo?bno=<%=board.getBoardNo() %>'" >삭제하기</button>
         		
         
         	</div>
         
        <%} %>
        
    </div>

</body>
</html>