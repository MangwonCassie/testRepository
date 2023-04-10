<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.kh.notice.model.vo.Notice, java.util.ArrayList"   %>
    
 <%//담은 리스트 꺼내주기
 
 ArrayList<Notice> list = (ArrayList<Notice>)request.getAttribute("noticeList") ;
 
 %>
    
<!DOCTYPE html>
<html>
<head>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.3/dist/jquery.slim.min.js"></script>
<!-- Popper JS -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>


<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    .outer{

        background-color: black;
        color: white;
        width: 1000px;
        height: 500px;
        margin: auto;
        margin-top: 50px;

    }

    .list-area{
        border:1px solid white;
        text-align: center;

    }

    .list-area>tbody>tr:hover{
        background-color: gray;
        cursor: pointer;
    }




</style>


</head>
<body>

	<%@ include file="../common/menubar.jsp" %>
	<!-- views로 ../ 를 통해 위로 올라가서 common/menubar -->
	
<div class="outer">
    <h2 align="center">공지사항</h2>
    <br>
    
    
    <!-- 공지사항 작성은 관리자만 가능하게 조건 부여하기  (관리자여야지만 공지사항 작성이 보임)
    	null인지 확인하는 작업을 먼저 작성하여야한다. 접근전에 확인 후 벗어나기 위해(논리추론)
    	null이 아닌 경우에만 admin인지 확인하기위해 순서 바뀌면 nullpointer Exception뜸 -->
    	<!-- loginUser != null 조건 안 적으면 확인하는 순간 오류남 왜냐 로그인 안했기때문에 로그아웃하고 공지사항 누르면 500 내부사항 오류가남-->
    
    <%if(loginUser != null && loginUser.getUserId().equals("admin")){ %>

    <div align="center">
        <a href="<%=contextPath %>/insert.no" class="btn btn-info">공지사항 작성</a>
        <br><br>
      
    </div>
      <%} %>
      <!-- div 바깥에!!!  -->

    <table class="list-area" align="center">
        <thead></thead>
        <tr>
            <th>글번호</th>
            <th width="400">글제목</th>
            <th width="100">작성자</th>
            <th>조회수</th>
            <th width="100">작성일</th>
        </tr>
        <tbody>
            <!--(tr>td*6)*2-->
            
            <!-- 리시트가 비어있으면 -->
            <%if(list.isEmpty()){%>
            
            	<tr>
            	<td>존재하는 공지사항이 없습니다.</td>
            	</tr>
            	
            <%}else %> <!-- 목록이 "있"으면(반복문으로 처리해주기 -->
            	<%for(Notice n: list){ %> <!-- 처음부터 끝까지 순차적으로 전부 접근할 것이기 때문에 향상된 for문(for-each문) 사용 -->
            	
            	<!-- 담겨있는 데이터를 Notice 타입으로 하나씩 꺼내서 변수 n에 담아주는 것 -->
            	<!-- tr자체가 반복문 돌아가는거 -->
            	
				<tr>
					<td><%=n.getNoticeNo() %></td>
					<td><%=n.getNoticeTitle() %></td>
					<td><%=n.getNoticeWriter() %></td>
					<td><%=n.getCount() %></td>
					<td><%=n.getCreateDate() %></td>
				</tr>

			<%} %>
<!-- 
        	 <tr>
                <td>2</td>
                <td>안녕하세요 등업부탁드립니다.</td>
                <td>김탈퇴</td>
                <td>0</td>
                <td>2001-10-05</td>
            </tr>
       		<tr>
                <td>5</td>
                <td>공지입니다. 꽁지아님</td>
                <td>관리자</td>
                <td>2</td>
                <td>2010-12-10</td>
            </tr>

            <tr>
                <td>7</td>
                <td>사이트 폐쇄합니다. 다 나가주세요</td>
                <td>관리자</td>
                <td>155</td>
                <td>2023-04-01</td>
            </tr>
 -->
 
        </tbody>

    </table>
  
</div>

		<script>
		    //.list-area 클래스 자손 tbody 자손 tr 클릭 됐을 때
		    
		    $(".list-area>tbody>tr").click(function(){
		        //목록에 글을 클릭했을 때 해당 글번호가 있어야
		        //그 글에 대해서 상세조회를 할 수 있으니
		        //글번호 추출하여 서버에 넘기기
		       // console.log($(this).children()); innerText!!!!!!!!!라고!!!
		     var nno = $(this).children().eq(0).text();
		       // var nno = console.log($(this).children().eq(0).text());이게아님!!
		       //맞습니다. console 창에서 출력된 값을 변수에 할당하려면 다른 방법을 사용해야 합니다.
		
		     //요청할 url?키=밸류&키=밸류.....
		     //물음표 뒤에 내용들을 쿼리 스트링이라고 한다. -직접 기술하여 넘기기
		     //''는 문자열이라는데 , 옆에 nno는 변수처리
		     // '/jsp/detail.no?nno='+nno
		    //location은 자바스크립트의 내장 객체라서 이제 디테일로 가는데 nno 가지고 가는거임
		     location.href = '<%=contextPath%>/detail.no?nno='+nno;
		    })

</script>

</body>
</html>