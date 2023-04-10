package com.kh.board.model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.kh.board.model.vo.Attachment;
import com.kh.board.model.vo.Board;
import com.kh.board.model.vo.Category;
import com.kh.common.JDBCTemplate;
import com.kh.common.model.vo.PageInfo;
import com.kh.notice.model.dao.NoticeDao;



public class BoardDao {
	
	
	private Properties prop = new Properties();
	
	
	//기본생성자 (classes 폴더에 있는 클래스파일 경로를 알아야함 기준을잡아서 거기서부터 올라가라고하심) 
	//getResource() 는 url "객체" 반환이라 이걸 문자열로 변환해야되서 getPath() 쓴 것  
	//dao에서 properties 불러오는 생성자 만들기 
	public BoardDao() {
		String filePath  = BoardDao.class.getResource("/sql/board/board-mapper.xml").getPath();
		
		//매우 중요!!!!!!!!!500번 에러 getResource("sql/board/board-mapper.xml") 앞에 / 안 적으면 BoardDao 에서 sql 폴더 찾는다!
		//콘솔창으로 오류 잡을 것!!!!!!!!!!!!!!!!!!!!!!!!!인터넷창에서 오류 보지말고!!!
		
		try {
			prop.loadFromXML(new FileInputStream(filePath));
			
			//제일 상위 하나만 해도 된다.
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		
	}

	//총 게시글 개수 구하는 메소드 
	public int selectListCount(Connection conn) {
		int listCount = 0;
		ResultSet rset = null;
		Statement stmt = null;

		String sql = prop.getProperty("selectListCount");

		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			// 바로 listCount 안 담아짐. 무조건 쿼리문은 rset으로 값 반환됨

			if (rset.next()) {
				listCount = rset.getInt("COUNT"); // columnLabel 가능

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(stmt);
		}

		return listCount;
	}

	public ArrayList<Board> selectList(Connection conn, PageInfo pi) {
	
		ArrayList<Board>list = new ArrayList<>();
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("selectList");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			/*
			 * boardLimit (한페이지에 보여져야할 게시글개수) 가  10일 때
			 * currentPage: 1 - 시작값 : 1 - 끝값: 10 
			 * currentpage: 2 - 시작값: 11 - 끝값: 20
			 * 
			 * 시작값 =  (currentPage-1) * boardLimit + 1 
			 * 끝값 = (시작값+boardLimit) - 1 
			 */
			
			//계산해서 담아주는거임
			int startRow = (pi.getCurrentPage()-1) * pi.getBoardLimit() + 1;
			int endRow = (startRow+pi.getBoardLimit()) - 1; 
			
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				list.add(new Board(rset.getInt("BOARD_NO"), rset.getString("CATEGORY_NAME"),
						rset.getString("BOARD_TITLE"), rset.getString("USER_ID"), rset.getInt("COUNT"),
						rset.getDate("CREATE_DATE")));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
			JDBCTemplate.close(rset);
		}
//		System.out.println("dao list : "+ list);
		return list;
	}
	
	
	public ArrayList<Category> categoryList(Connection conn){
		ArrayList<Category>list = new ArrayList<>();
		
		ResultSet rset = null;
		Statement stmt = null;
		
		String sql = prop.getProperty("categorylist");
		
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery(sql);
			
			while(rset.next()) {
				list.add(new Category(rset.getInt("CATEGORY_NO")
						,rset.getString("CATEGORY_NAME")));
			
			}
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(stmt);
			JDBCTemplate.close(rset);

		}
		return list;

	}

	public int insertBoard(Connection conn, Board b) {
		
		int result = 0;
		
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("insertBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(b.getCategory()));
			pstmt.setString(2, b.getBoardTitle());
			pstmt.setString(3, b.getBoardContent());
			pstmt.setInt(4, Integer.parseInt(b.getBoardWriter()));
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
	}

	//Service 단에서 insertBoard()랑 연결
	public int insertAttachment(Connection conn, Attachment at) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertAttachment");
		//sql 물음표 뚫은거를 세팅해줘야지.. 까먹더라
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			
			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	public int increaseCount(Connection conn, int boardNo) {

		
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("increaseCount");
		
		try {
			pstmt = conn.prepareStatement(sql);
			//SQL에 물음표 채워넣는 과정
			pstmt.setInt(1, boardNo);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;

	}

	public Board selectBoard(Connection conn, int boardNo) {

		
		Board board = null;
		ResultSet rset = null;
		PreparedStatement pstmt = null;

		String sql = prop.getProperty("selectBoard");
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
	
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				board = new Board(
						rset.getInt("BOARD_NO")
						,rset.getString("CATEGORY_NAME")
						,rset.getString("BOARD_TITLE")
						,rset.getString("BOARD_CONTENT")
						,rset.getString("USER_ID")
						,rset.getDate("CREATE_DATE"));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			//rset pstmt만들고 rset pstmt 닫아
			//내꺼 이거 실수했는데? 
			//사용된 객체부터 먼저 닫아서 데이터베이스 리소스를 최대한 빨리 반환
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return board;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Attachment selectAttachment(Connection conn, int boardNo) {
	
		Attachment at = null;
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("selectAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, boardNo);
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				
				//쌤이 식별자는 가지고 다니는게 좋다고 FILE_NO은 가지고 다니라고 하심 나는 ORIGIN_NAME, CHANGE_NAME, FILE_PATH만 가져옴
				 at = new Attachment(
						 rset.getInt("FILE_NO")
						,rset.getString("ORIGIN_NAME")
						,rset.getString("CHANGE_NAME")
						,rset.getString("FILE_PATH"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {

			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}

		return at;
	}

	public int deleteBoard(Connection conn, int boardNo) {
		
		
		PreparedStatement pstmt = null;
		int result = 0;
		
		String sql = prop.getProperty("deleteBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			 pstmt.setInt(1, boardNo); // pstmt 에 set를 이미 했으니까 그대로 쓰면 됨
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		 
		return result;
	}

	public int deleteAttachment(Connection conn, int boardNo) {
		
	
		PreparedStatement pstmt = null;
		int result = 0;
		String sql  =prop.getProperty("deleteAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	
	
	//게시글 수정 메소드 
	public int updateBoard(Connection conn, Board b) {
		int result = 0;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("updateBoard");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(b.getCategory()));
			pstmt.setString(2, b.getBoardTitle());
			pstmt.setString(3, b.getBoardContent());
			pstmt.setInt(4, b.getBoardNo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

	//기존파일 정보 수정하기
	public int updateAttachment(Connection conn, Attachment at) {
		int result = 0;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("updateAttachment");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, at.getOriginName());
			pstmt.setString(2, at.getChangeName());
			pstmt.setString(3, at.getFilePath());
			pstmt.setInt(4, at.getFileNo());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}

	//새로운 파일정보 넣기 
	public int newInsertAttachment(Connection conn, Attachment at) {
		int result = 0;
		PreparedStatement pstmt = null;
		
		String sql = prop.getProperty("newInsertAttachment");
	
		try {
			pstmt = conn.prepareStatement(sql);
			
			//물음표 채워주는 거
			pstmt.setInt(1, at.getRefBno());
			pstmt.setString(2, at.getOriginName());
			pstmt.setString(3, at.getChangeName());
			pstmt.setString(4, at.getFilePath());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCTemplate.close(pstmt);
		}

		return result;
	}

}
