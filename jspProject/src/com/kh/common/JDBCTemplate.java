package com.kh.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	
	public static Connection getConnection() {
		
		Properties prop = new Properties(); 
		
		//읽어들이고자하는 driver.properties 파일의 경로를 알아내서 대입해야한다.
		String filePath = JDBCTemplate.class.getResource("/sql/driver/driver.properties").getPath();
		//기준이 JDBCTemplate.class  (D:\server-workspace2_t\jspProject\WebContent\WEB-INF\classes\com\kh\common\JDBCTemplate.class 이 경로에 있는 )
		//"/sql/driver/driver.properties 여기서 제일 앞 '/' 이거는 classes 폴더다. D:\server-workspace2_t\jspProject\WebContent\WEB-INF\classes
		//왜냐면 우리가 그렇게 설정함 default output folder에 classes라고 설정함
		//쌤이  JDBCTemplate.class.getResource("/").getPath(); 이렇게 찍어주시니까
		//D:/server-workspace2/jspProject/WebContent/WEB-INF/classes/    이렇게 까지 나옴! classes/   
		//제발 그 WebContent아니다 헷갈려하지마  default output folder 설정했기 때문에 classes로 최상위 폴더가 classes가 되는거임
		//그냥 존재하는 거 찍어놓고 올라가는거임 아무개클래스이름.class.getResource("/sql/driver/driver.properties").getPath();
		
		
		Connection conn = null;
		try {
			prop.load(new FileInputStream(filePath));
		
			
			
			//1.jdbc driver 등록
			Class.forName(prop.getProperty("driver"));
			
			//2.Connection 객체 생성
			
			conn = DriverManager.getConnection(prop.getProperty("url")
											  ,prop.getProperty("username")
											  ,prop.getProperty("password"));
			
			
			//connection 객체 얻어놓은 다음에 트랜잭션을 여러개 묶어서 할거면 false를 해야한다함
			//쌤이 230410
			conn.setAutoCommit(false);
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	//2.전달받은 Connection 객체를 가지고 commit 해주는 메소드
	public static void commit(Connection conn) {
		
		
		try {
			if(conn != null && !conn.isClosed()) {
				conn.commit();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//3.전달받은 Connection 객체를 가지고 rollback해주는 메소드
	public static void rollback(Connection conn) {
		
		try {
			if(conn!=null && !conn.isClosed()) {
				conn.rollback();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//4.전달받은 Connection 객체를 반납시켜주는 메소드
	public static void close(Connection conn) {
		
		try {
			if(conn!=null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//5.전달받은 Statement 객체를 반납시켜주는 메소드
	public static void close(Statement stmt) {
		try {
			if(stmt!=null && !stmt.isClosed()) {
				stmt.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//6.전달받은 ResultSet 객체를 반납시켜주는 메소드
	public static void close(ResultSet rset) {
		try {
			if(rset != null && !rset.isClosed()) {
				rset.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
