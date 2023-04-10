package com.kh.notice.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.Date;

import com.kh.common.JDBCTemplate;
import com.kh.notice.model.vo.Notice;


public class NoticeDao {
	
	
	private Properties prop = new Properties();
	
	//기본생성자 (classes 폴더에 있는 클래스파일 경로를 알아야함 기준을잡아서 거기서부터 올라가라고하심) 
	//getResource() 는 url "객체" 반환이라 이걸 문자열로 변환해야되서 getPath() 쓴 것  
	public NoticeDao() {
		String filePath = NoticeDao.class.getResource("/sql/notice/notice-mapper.xml").getPath();
		
		try {
			prop.loadFromXML(new FileInputStream(filePath));
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Notice> selectList(Connection conn) {
		
		
	    ArrayList<Notice> list = new ArrayList<>(); //조회된 목록 담아서 반환할 빈 리스트 준비 (제네릭 타입으로)
	    Statement stmt = null;
	    ResultSet rset = null;
	    
	    try {
	       
	        stmt = conn.createStatement();
	        String sql = prop.getProperty("selectList");
	       
	        rset = stmt.executeQuery(sql);
	        
	        
	        while (rset.next()) {
	            list.add(new Notice(rset.getInt("NOTICE_NO"),
	            		rset.getString("NOTICE_TITLE"),
	            		rset.getString("USER_ID"),  
	            		rset.getInt("COUNT"), 
	            		rset.getDate("CREATE_DATE")));
	        } //맞게 생성자 또 만들어줘야지
	        //데이터베이스에 NOTICE_WRITER 1이라고 되어있잖아 작성자 누구인지 모르잖아 작성자에 1이라고 뜨잖아 MAPPER 구문 수정해야함 JOIN 하도록
	            
	          
	       
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	    	JDBCTemplate.close(rset);
	    	JDBCTemplate.close(stmt);
	    }
	    
	    return list;
	}

	public int insertNotice(Connection conn, Notice n) {

		int result = 0; 
		
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("insertNotice");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, n.getNoticeTitle());
			pstmt.setString(2, n.getNoticeContent());
			pstmt.setString(3, n.getNoticeWriter());//내부형변환으로 인해 Number 타입에 들어감
			//pstmt.setInt(3, Integer.parseInt(n.getNoticeWriter())); //정석 sql에서는 number타입이기 때문에 형변환 후 넘기기
			result = pstmt.executeUpdate();
			
			System.out.println("3번 컬럼 : "+Integer.parseInt(n.getNoticeWriter()));
			
			/*
			 * INSERT INTO NOTICE (NOTICE_NO, NOTICE_TITLE, NOTICE_CONTENT, NOTICE_WRITER, COUNT, CREATE_DATE, STATUS) 
				VALUES (3, '세번째 공지사항입니다.', '세번째 공지사항 내용입니다.', 1, 0, SYSDATE, 'Y');
			 * 
			 * */

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					pstmt.close();

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return result;
		}

		public Notice selectNotice(Connection conn, int noticeNo) {

			// NOTICE_TITLE, NOTICE_WRITER, NOTICE_CONTENT, CREATE_DATE 꼭 들어가야하는 구문
			// 총4개

			ResultSet rset = null;

			PreparedStatement pstmt = null;

			Notice notice = null;

			String sql = prop.getProperty("selectNotice");

			
			//이게 5새로 간다니까?
			String noticeTitle = null;
			String noticeWriter = null;
			String noticeContent = null;
			Date createDate = null;

			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, noticeNo);
				rset = pstmt.executeQuery();

				// 생성자 또 만들어줘야함
				//식별자로 가져왔으니까 if 맞음 
				if (rset.next()) { // 결과가 존재하면
					notice = new Notice(rset.getInt("NOTICE_NO"), rset.getString("NOTICE_TITLE"), rset.getString("USER_ID"),
							rset.getString("NOTICE_CONTENT"), rset.getDate("CREATE_DATE"));

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				JDBCTemplate.close(pstmt);
				JDBCTemplate.close(rset);
				
			}

			return notice;
		}
		
		//조회수증가 메소드
		public int increaseCount(Connection conn, int noticeNo) {
			
			int result = 0;
			PreparedStatement pstmt = null;
			String sql = prop.getProperty("increaseCount");
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, noticeNo);
				result = pstmt.executeUpdate();
				
			} catch (SQLException e) {
			
				e.printStackTrace();
			} finally {
				JDBCTemplate.close(pstmt);
			}

			return result;
		}

		public int updateNotice(Connection conn, Notice n) {
			PreparedStatement pstmt = null;
			int result = 0;
			
			System.out.println(n); //그때 그때 마다 확인하기
			
			String sql = prop.getProperty("updateNotice");
			
			try {
				//pstmt에 따로 변수에 안 담아져서 계속 null pointer 오류 남 
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, n.getNoticeTitle());
				pstmt.setString(2, n.getNoticeContent());
				pstmt.setInt(3, n.getNoticeNo());

				result = pstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				JDBCTemplate.close(pstmt);
			}

			return result;
		}

		public int deleteNotice(Connection conn, int noticeNo) {

			int result = 0;
			PreparedStatement pstmt = null;
			
			String sql = prop.getProperty("deleteNotice");
			
			try {
				pstmt = conn.prepareStatement(sql); // pstmt 계속 연결 빼먹은니까 항상 먼저 적기 
				pstmt.setInt(1, noticeNo);
				result=pstmt.executeUpdate();//처리된 행수 반환
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				JDBCTemplate.close(pstmt);
			}
			return result;
		}

	}
