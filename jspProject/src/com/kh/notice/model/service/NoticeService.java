package com.kh.notice.model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.notice.model.dao.NoticeDao;
import com.kh.notice.model.vo.Notice;

public class NoticeService {
	
	//공지사항 리스트 조회 메소드
	public ArrayList<Notice> selectList() {
		Connection conn = JDBCTemplate.getConnection();
		
		//조회구문이기 떄문에 트랜잭션 처리할 필요 없음 
		
		ArrayList<Notice> list = new NoticeDao().selectList(conn);
		
		JDBCTemplate.close(conn);
		
		return list;
	}

//	public int insertNotice(String title, String content) {
		
//		Connection conn = JDBCTemplate.getConnection();
//		
//		//insert 구문임 
//		
//		int result = new NoticeDao().insertNotice(conn, title, content);
//		
//		if(result>0) {
//			try {
//				conn.commit();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		} else {
//			
//			JDBCTemplate.rollback(conn); // insert 작업 실패 시 rollback
//			JDBCTemplate.close(conn);
//			
//		}
//		
//		JDBCTemplate.close(conn);
//		
//		
//		return result;
//	}

	public int insertNotice(Notice n) {
	
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().insertNotice(conn, n);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		return result;
	}
	
	
	//게시글 하나 조회하는 메소드
	public Notice selectNotice(int noticeNo) {
		
		Notice notice = null;
		
		Connection conn = JDBCTemplate.getConnection();
		
		notice = new NoticeDao().selectNotice(conn, noticeNo);
		
		return notice;
		
	}

	//조회수 증가용 메소드
	public int increaseCount(int noticeNo) {
		Connection conn = JDBCTemplate.getConnection();
		int result = new NoticeDao().increaseCount(conn, noticeNo);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		JDBCTemplate.close(conn);
		
		return result;
		
	}

	public int updateNotice(Notice n) {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().updateNotice(conn, n);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
			
		} else {
			JDBCTemplate.rollback(conn);
		}
		return result;
	}

	public int deleteNotice(int noticeNo) {
	
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new NoticeDao().deleteNotice(conn, noticeNo);
		
		if(result>0) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
		
		return result;
	}

}
