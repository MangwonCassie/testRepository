package com.kh.board.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.board.model.dao.BoardDao;
import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.PageInfo;
import com.kh.notice.model.dao.NoticeDao;

public class BoardService {
	
	//총 게시글 개수 구하기
	//여울


	public int selectListCount() {
		
		Connection conn = JDBCTemplate.getConnection();
		int listCount = new BoardDao().selectListCount(conn);
		
		//처리된 행수가 아닌 총 게시글의 개수를 조회해온 것 그래서 트랜잭션 처리 필요 x
		JDBCTemplate.close(conn);
		
		return listCount;
	}

	public ArrayList<Board> selectList(PageInfo pi) {
	
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Board> list = new BoardDao().selectList(conn,pi);
		System.out.println("서비스입니다. : "+list);
		JDBCTemplate.close(conn);
		
		return list;
	}
	
	//카테고리 리스트
	
	public ArrayList<Category> categoryList(){
		Connection conn = JDBCTemplate.getConnection();
		
		ArrayList<Category> list = new BoardDao().categoryList(conn);
		
		JDBCTemplate.close(conn);
		
		return list; 
	}
	
	

	public int insertBoard(Board b, Attachment at) {
		
		//연결객체
		Connection conn = JDBCTemplate.getConnection();
		
		//게시글이 작성될 떄 첨부파일이 있거나 없는 경우도 생각하여 작업하기  board안에 들어가야하는..작성자..카테고리....첨부파일 attachment 작성자가
		//첨부파일을 넣으면 들어가야하고 안 넣으면 첨부파일이 있을경우에만 객체 생성해서 넣는거다 그 전에 세팅하는 작업한거임 
		int result = new BoardDao().insertBoard(conn, b);
		int result2 = 1; // 그니까............
		//첨부파일이 없어도 게시글 커밋은 해야하니까 해당 조건에 부합하게 result2는 1로 초기화해놓기 
		
		//첨부파일이 있는 경우에 Attachment 테이블에 insert 작업하기
		if(at != null) {
			result2 = new BoardDao().insertAttachment(conn, at);
		}
		
		//result result2 둘다 부합되야 맞게 됨 commit
		//result2지역변수 만들어주기
		
		if(result>0 && result2>0 ) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		//두개를 곱해서 반환하라고 함....하나라도 0이면 0이 되게끔 
		return result*result2; //둘중하나라도 0이 나오면 0를 반환하게 작성하기 
	}

	public int increaseCount(int boardNo) {
	
		Connection conn = JDBCTemplate.getConnection();
		int result = new BoardDao().increaseCount(conn, boardNo);
		
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		// System.out.println("서비스단 넘어오나요:"+result);

		return result;

	}

	public Board selectBoard(int boardNo) {
		
		Board board = null; 
		
		Connection conn = JDBCTemplate.getConnection();
		
		board = new BoardDao().selectBoard(conn, boardNo);
		
		JDBCTemplate.close(conn);
		
		return board;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Attachment selectAttachment(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		
		Attachment at = new BoardDao().selectAttachment(conn, boardNo);
		
		return at;
	}

	public int deleteBoard(int boardNo) {
	
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().deleteBoard(conn, boardNo);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		return result;
	}

	public int deleteAttachment(int boardNo) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new BoardDao().deleteAttachment(conn, boardNo);
		
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		
		return result;
	}

	//정보수정메소드
	public int updateBoard(Board b, Attachment at) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		//새로운 첨부파일 없고 기존 첨부파일도 없는 경우 - board-update
		//새로운 첨부파일 있고 기존 첨부파일 없는 경우 - board-update / attachment -insert
		//새로운  첨부파일 있고 기존 첨부파일 있는 경우 - board-update / attachment -update
		
		int result = new BoardDao().updateBoard(conn, b);
		
		int result2 = 1; //1로 먼저 초기화
		
		if (at != null) {// 새롭게 추가된 첨부파일이 있는 경우 (너무 복잡해 ㅠㅠ)
			if (at.getFileNo() != 0) { // 기존의 첨부파일이 있었을 경우(변경) -> int라서 기본값 0
				result2 = new BoardDao().updateAttachment(conn, at);
			} else {
				// 기존의 첨부파일이 없었을 경우(추가)
				result2 = new BoardDao().newInsertAttachment(conn, at);

			}
		}

			if (result > 0 && result2 > 0) {
				JDBCTemplate.commit(conn);
			} else {
				JDBCTemplate.rollback(conn);
			}
		

		return result * result2;
	}

	
	
}
