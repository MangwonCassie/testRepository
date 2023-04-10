package com.kh.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oreilly.servlet.multipart.FileRenamePolicy;

public class MyFileRenamePolicy implements FileRenamePolicy {

	//파일명 변경메소드(rename메소드)  재정의하기
	//기존의 파일명을 전달받아 수정작업 후 해당 파일명을 반환해주는메소드
	@Override
	public File rename(File originFile) {
	
		//원본 파일명("hello.jpg")
		
		String originName = originFile.getName(); //파일객체에 있는 메소드 
		
		//수정파일명: 파일업로드된 시간(년월일시분초) + 5자리 랜덤값 
		//특수문자 한글 포함되지않도록 
		//확장자: 그대로 
		//hello.jpg - >20230406154630012443.jpg
		
		//1.파일업로드 시간(년월일시분초) - String currentTime 
		
		//util의 Date다. 
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		
		//2.랜덤 숫자 5자리
		int ranNum = (int)(Math.random()*90000)+10000;
		
		//indexOf는 앞에서 찾고 lastIndexOf는 뒤에서 찾고  마지막 점을 기준으로 
		
		//3.원본파일 확장자명 추출
		//원본 파일명에서 가장 마지막 . 기준으로 확장자추출 
		String ext = originName.substring(originName.lastIndexOf("."));
		
		String changeName = currentTime + ranNum + ext;
		
		
		
		//반환타입 file이라서 객체 만들어서 담음 
		//getParent() 상위 요소 , 하위요소
		
		//원본 파일을 파일명 변경하여 전달  (파일의 형태로 되돌려줄 것)
		//originFile은 내부 객체다.
		/*부모 디렉토리에 changeName이라는 파일 이름을 가진 새로운 File 객체를 생성하는 것입니다.*/
	
		return new File(originFile.getParent(), changeName);
	}
	
	

}
